package davidsolutions.caixaferramentas.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import davidsolutions.caixaferramentas.conversao.Conversor;
import davidsolutions.caixaferramentas.conversao.ConversorException;
import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;

class DaoUtil {

	private static final String REGEX_NOME_PARAMETRO = "\\?[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*";
	private static final String SEPARADOR_NOME_ATRIBUTO = "_";

	private DaoUtil() {
	}

	public static PreparedStatement prepararStatement(String instrucaoSql, Map<String, Object> mapaParametros, Object objetoParametros, Connection conexao) throws DaoException, SQLException, ReflectionException, ConversorException {
		Pattern pattern = Pattern.compile(REGEX_NOME_PARAMETRO);
		Matcher matcher = pattern.matcher(instrucaoSql);
		StringBuffer instrucaoSqlTemp = new StringBuffer("");
		Map<Integer, Object> parametrosPreparedStatement = new HashMap<Integer, Object>();
		Object valorParametro = null;

		while (matcher.find()) {
			valorParametro = obterParametro(matcher.group().substring(1), mapaParametros, objetoParametros, true);

			if (valorParametro instanceof List<?>) {
				String separadorInterrogacao = "";
				StringBuilder interrogacoes = new StringBuilder();

				for (Object valorParametroLista : (List<?>) valorParametro) {
					parametrosPreparedStatement.put((parametrosPreparedStatement.size() + 1), valorParametroLista);
					interrogacoes.append(separadorInterrogacao + "?");
					separadorInterrogacao = ", ";
				}
				matcher.appendReplacement(instrucaoSqlTemp, interrogacoes.toString());
			}
			else {
				parametrosPreparedStatement.put((parametrosPreparedStatement.size() + 1), valorParametro);
				matcher.appendReplacement(instrucaoSqlTemp, "?");
			}
		}
		matcher.appendTail(instrucaoSqlTemp);
		return prepararStatement(instrucaoSqlTemp.toString(), parametrosPreparedStatement, conexao);
	}

	public static <T> List<T> consultar(ResultSet resultSet, Paginacao paginacao, Class<T> tipoObjetoRetorno) throws DaoException, SQLException, ReflectionException, ConversorException {
		List<T> objetos = new ArrayList<T>();
		int totalRegistros = 0;

		if (tipoObjetoRetorno.isPrimitive()) {
			throw new DaoException("Tipo primitivo não suportado. Utilize seu respectivo objeto.");
		}
		if (resultSet.next()) {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			T objeto = null;
			List<String> rotulosColunas = null;
			String rotuloColuna = null;
			Conversor conversor = null;
			try {
				conversor = Conversor.obterConversor(tipoObjetoRetorno);
			}
			catch (ConversorException excecao) {
			}
			if (conversor == null) {
				rotulosColunas = new ArrayList<String>();

				for (int posicao = 1; posicao <= resultSetMetaData.getColumnCount(); posicao++) {
					rotulosColunas.add(resultSetMetaData.getColumnLabel(posicao));
				}
			}
			else {
				rotuloColuna = resultSetMetaData.getColumnLabel(1);
			}
			do {
				totalRegistros++;

				if (paginacao != null && (totalRegistros < paginacao.getPosicaoInicialPagina() || totalRegistros > paginacao.getPosicaoFinalPagina())) {
					continue;
				}
				if (conversor == null) {
					objeto = (T) ReflectionUtil.instanciarObjeto(tipoObjetoRetorno);

					for (String rotuloColunaTemp : rotulosColunas) {
						atribuirValorPropriedade(rotuloColunaTemp, resultSet, objeto);
					}
				}
				else {
					objeto = (T) conversor.converterDeResultSet(resultSet, rotuloColuna);
				}
				objetos.add(objeto);
			}
			while (resultSet.next());
		}
		if (paginacao != null) {
			paginacao.setTotalRegistros(totalRegistros);
		}
		return objetos;
	}

	static Object obterParametro(String nomeParametro, Map<String, Object> mapaParametros, Object objetoParametros, boolean validaParametroNaoDefinido) throws DaoException, ReflectionException {

		if (mapaParametros != null && mapaParametros.containsKey(nomeParametro)) {
			return mapaParametros.get(nomeParametro);
		}
		else if (objetoParametros != null) {
			return ReflectionUtil.obterValorPropriedade(objetoParametros, nomeParametro);
		}
		else if (validaParametroNaoDefinido) {
			throw new DaoException("Parâmetro '" + nomeParametro + "' não definido.");
		}
		else {
			return null;
		}
	}

	private static PreparedStatement prepararStatement(String instrucaoSql, Map<Integer, Object> parametrosPreparedStatement, Connection conexao) throws SQLException, ConversorException {
		PreparedStatement preparedStatement = conexao.prepareStatement(instrucaoSql);

		for (Entry<Integer, Object> chaveValor : parametrosPreparedStatement.entrySet()) {
			Conversor.obterConversor(chaveValor.getValue() != null ? chaveValor.getValue().getClass() : null).converterParaPreparedStatement(preparedStatement, chaveValor.getKey(), chaveValor.getValue());
		}
		return preparedStatement;
	}

	private static void atribuirValorPropriedade(String sequenciaNomesPropriedades, ResultSet resultSet, Object objeto) throws SQLException, ReflectionException, ConversorException {
		String[] nomesPropriedades = sequenciaNomesPropriedades.split(SEPARADOR_NOME_ATRIBUTO);
		Method metodoGet = null;
		Class<?> classePropriedade = null;
		Object objetoAtual = null;
		Object objetoAnterior = objeto;
		Object valorPropriedade = null;

		for (int cont = 0; cont <= (nomesPropriedades.length - 2); cont++) {
			metodoGet = ReflectionUtil.obterMetodoGet(objetoAnterior.getClass(), nomesPropriedades[cont]);
			classePropriedade = metodoGet.getReturnType();
			objetoAtual = ReflectionUtil.executarMetodo(metodoGet, objetoAnterior);

			if (objetoAtual == null) {
				objetoAtual = ReflectionUtil.instanciarObjeto(classePropriedade);
				ReflectionUtil.atribuirValorPropriedade(objetoAnterior, nomesPropriedades[cont], classePropriedade, objetoAtual);
			}
			objetoAnterior = objetoAtual;
		}
		classePropriedade = ReflectionUtil.obterClassePropriedade(objetoAnterior.getClass(), nomesPropriedades[(nomesPropriedades.length - 1)]);
		valorPropriedade = Conversor.obterConversor(classePropriedade).converterDeResultSet(resultSet, sequenciaNomesPropriedades);
		ReflectionUtil.atribuirValorPropriedade(objetoAnterior, nomesPropriedades[(nomesPropriedades.length - 1)], classePropriedade, valorPropriedade);
	}
}