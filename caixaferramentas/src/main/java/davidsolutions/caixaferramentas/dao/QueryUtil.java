package davidsolutions.caixaferramentas.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;

public class QueryUtil {

	private String instrucaoSql;
	private Map<String, Object> mapaParametros = new HashMap<String, Object>();
	private Object objetoParametros;
	private Paginacao paginacao;
	private String concatedorFiltro = WHERE;
	private boolean controlaJuncaoFiltro;
	private boolean adicionaLimiteRegistro = false;
	private static final String REGEX_FILTRO_OPCIONAL = "\\{.+?\\}";
	private static final String REGEX_NOME_PARAMETRO = "\\?%?[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*";
	private static final String REGEX_LIMPEZA_FILTRO_OPCIONAL = "\\{|\\}|%";
	private static final String REGEX_LIMPEZA_NOME_PARAMETRO = "\\?%?";
	private static final String WHERE = " WHERE ";
	private static final String AND = " AND ";
	private static final String CARACTERE_APROXIMACAO = "%";
	private static final String SQL_LIMITE_REGISTRO = " LIMIT 1, 100";

	public QueryUtil(String instrucaoSql, Object objetoParametros) {
		this(instrucaoSql, objetoParametros, true);
	}

	public QueryUtil(String instrucaoSql, Object objetoParametros, boolean controlaJuncaoFiltro) {
		this.instrucaoSql = instrucaoSql;
		this.objetoParametros = objetoParametros;
		this.controlaJuncaoFiltro = controlaJuncaoFiltro;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public void adicionarParametro(String nomeParametro, Object valorParametro) {
		this.mapaParametros.put(nomeParametro, valorParametro);
	}

	public void adicionarParametroAproximado(String nomeParametro, String valorParametro) {
		this.mapaParametros.put(nomeParametro, CARACTERE_APROXIMACAO + valorParametro.replaceAll(CARACTERE_APROXIMACAO, "") + CARACTERE_APROXIMACAO);
	}

	public void adicionarLimiteRegistro() {
		this.adicionaLimiteRegistro = true;
	}

	String obterInstrucaoSql() throws DaoException, ReflectionException {
		Pattern patternFiltro = Pattern.compile(REGEX_FILTRO_OPCIONAL);
		Matcher matcherFiltro = patternFiltro.matcher(this.instrucaoSql);
		Pattern patternParametro = Pattern.compile(REGEX_NOME_PARAMETRO);
		Matcher matcherParametro = null;
		String nomeParametro = null;
		Object valorParametro = null;
		StringBuffer instrucaoSqlTemp = new StringBuffer("");
		String filtro = null;

		while (matcherFiltro.find()) {
			matcherParametro = patternParametro.matcher(matcherFiltro.group());
			boolean adicionaFiltro = true;

			while (matcherParametro.find()) {
				nomeParametro = matcherParametro.group().replaceAll(REGEX_LIMPEZA_NOME_PARAMETRO, "");
				valorParametro = DaoUtil.obterParametro(nomeParametro, this.mapaParametros, this.objetoParametros, false);

				if (ValidacaoUtil.estaVazio(valorParametro)) {
					adicionaFiltro = false;
					break;
				}
				else {
					if (matcherParametro.group().contains(CARACTERE_APROXIMACAO)) {
						this.adicionarParametroAproximado(nomeParametro, valorParametro.toString());
					}
				}
			}
			if (adicionaFiltro) {
				filtro = matcherFiltro.group().replaceAll(REGEX_LIMPEZA_FILTRO_OPCIONAL, "");

				if (this.controlaJuncaoFiltro) {
					matcherFiltro.appendReplacement(instrucaoSqlTemp, this.concatedorFiltro + filtro);
					this.concatedorFiltro = AND;
				}
				else {
					matcherFiltro.appendReplacement(instrucaoSqlTemp, filtro);
				}
			}
			else {
				matcherFiltro.appendReplacement(instrucaoSqlTemp, "");
			}
		}
		matcherFiltro.appendTail(instrucaoSqlTemp);

		if (this.adicionaLimiteRegistro) {
			instrucaoSqlTemp.append(SQL_LIMITE_REGISTRO);
		}
		return instrucaoSqlTemp.toString();
	}

	Map<String, Object> getMapaParametros() {
		return this.mapaParametros;
	}

	Object getObjetoParametros() {
		return this.objetoParametros;
	}

	Paginacao getPaginacao() {
		return this.paginacao;
	}
}