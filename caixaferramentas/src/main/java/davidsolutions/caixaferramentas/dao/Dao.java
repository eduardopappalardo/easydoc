package davidsolutions.caixaferramentas.dao;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;

public abstract class Dao {

	protected Connection conexao;
	private static Map<String, Map<String, String>> instrucoesSql = new HashMap<String, Map<String, String>>();
	private static final String SQL_GRAVA_SEQUENCIAL = "INSERT INTO Sequencial(nome, ultimoValorUsado, valorIncremento) VALUES (?nome, 1, 1)";
	private static final String SQL_ATUALIZA_SEQUENCIAL = "UPDATE Sequencial SET ultimoValorUsado = (ultimoValorUsado + valorIncremento) WHERE nome = ?nome";
	private static final String SQL_CONSULTA_SEQUENCIAL = "SELECT ultimoValorUsado FROM Sequencial WHERE nome = ?nome";

	protected Dao(Connection conexao) throws DaoException {
		boolean conexaoValida;
		try {
			conexaoValida = conexao != null && conexao.isValid(0);
		}
		catch (SQLException excecao) {
			conexaoValida = false;
		}
		if (!conexaoValida) {
			throw new DaoException("Conexão inválida");
		}
		this.conexao = conexao;

		if (!instrucoesSql.containsKey(this.getClass().getName())) {
			this.carregarInstrucoesSql();
		}
	}

	protected int executarInstrucaoSql(String instrucaoSql, Object objetoParametros) throws DaoException {
		return this.executarInstrucaoSql(instrucaoSql, null, objetoParametros);
	}

	protected int executarInstrucaoSql(String instrucaoSql, Map<String, Object> mapaParametros) throws DaoException {
		return this.executarInstrucaoSql(instrucaoSql, mapaParametros, null);
	}

	protected int executarInstrucaoSql(String instrucaoSql, Map<String, Object> mapaParametros, Object objetoParametros) throws DaoException {
		try {
			PreparedStatement preparedStatement = DaoUtil.prepararStatement(instrucaoSql, mapaParametros, objetoParametros, this.conexao);
			int quantidadeRegistrosAfetados = preparedStatement.executeUpdate();
			preparedStatement.close();
			return quantidadeRegistrosAfetados;
		}
		catch (DaoException excecao) {
			throw excecao;
		}
		catch (Exception excecao) {
			throw new DaoException(excecao);
		}
	}

	protected <T> T executarConsultaRegistroUnico(QueryUtil queryUtil, Class<T> tipoObjetoRetorno) throws RegistroInexistenteException, DaoException {
		List<T> lista = this.executarConsulta(queryUtil, tipoObjetoRetorno);

		if (lista.isEmpty()) {
			throw new RegistroInexistenteException();
		}
		return lista.get(0);
	}

	protected <T> T executarConsultaRegistroUnico(String instrucaoSql, Object objetoParametros, Class<T> tipoObjetoRetorno) throws RegistroInexistenteException, DaoException {
		List<T> lista = this.executarConsulta(instrucaoSql, objetoParametros, null, tipoObjetoRetorno);

		if (lista.isEmpty()) {
			throw new RegistroInexistenteException();
		}
		return lista.get(0);
	}

	protected <T> T executarConsultaRegistroUnico(String instrucaoSql, Map<String, Object> mapaParametros, Class<T> tipoObjetoRetorno) throws RegistroInexistenteException, DaoException {
		List<T> lista = this.executarConsulta(instrucaoSql, mapaParametros, null, tipoObjetoRetorno);

		if (lista.isEmpty()) {
			throw new RegistroInexistenteException();
		}
		return lista.get(0);
	}

	protected <T> List<T> executarConsulta(QueryUtil queryUtil, Class<T> tipoObjetoRetorno) throws DaoException {
		try {
			return this.executarConsulta(queryUtil.obterInstrucaoSql(), queryUtil.getMapaParametros(), queryUtil.getObjetoParametros(), queryUtil.getPaginacao(), tipoObjetoRetorno);
		}
		catch (DaoException excecao) {
			throw excecao;
		}
		catch (Exception excecao) {
			throw new DaoException(excecao);
		}
	}

	protected <T> List<T> executarConsulta(String instrucaoSql, Class<T> tipoObjetoRetorno) throws DaoException {
		return this.executarConsulta(instrucaoSql, null, null, null, tipoObjetoRetorno);
	}

	protected <T> List<T> executarConsulta(String instrucaoSql, Object objetoParametros, Paginacao paginacao, Class<T> tipoObjetoRetorno) throws DaoException {
		return this.executarConsulta(instrucaoSql, null, objetoParametros, paginacao, tipoObjetoRetorno);
	}

	protected <T> List<T> executarConsulta(String instrucaoSql, Map<String, Object> mapaParametros, Paginacao paginacao, Class<T> tipoObjetoRetorno) throws DaoException {
		return this.executarConsulta(instrucaoSql, mapaParametros, null, paginacao, tipoObjetoRetorno);
	}

	protected <T> List<T> executarConsulta(String instrucaoSql, Map<String, Object> mapaParametros, Object objetoParametros, Paginacao paginacao, Class<T> tipoObjetoRetorno) throws DaoException {
		try {
			PreparedStatement preparedStatement = DaoUtil.prepararStatement(instrucaoSql, mapaParametros, objetoParametros, this.conexao);
			ResultSet resultSet = preparedStatement.executeQuery();
			List<T> retornoConsulta = DaoUtil.consultar(resultSet, paginacao, tipoObjetoRetorno);
			resultSet.close();
			preparedStatement.close();
			return retornoConsulta;
		}
		catch (DaoException excecao) {
			throw excecao;
		}
		catch (Exception excecao) {
			throw new DaoException(excecao);
		}
	}

	protected boolean existeRegistro(String instrucaoSql, Object objetoParametros) throws DaoException {
		return this.existeRegistro(instrucaoSql, null, objetoParametros);
	}

	protected boolean existeRegistro(String instrucaoSql, Map<String, Object> mapaParametros) throws DaoException {
		return this.existeRegistro(instrucaoSql, mapaParametros, null);
	}

	protected boolean existeRegistro(String instrucaoSql, Map<String, Object> mapaParametros, Object objetoParametros) throws DaoException {
		try {
			PreparedStatement preparedStatement = DaoUtil.prepararStatement(instrucaoSql, mapaParametros, objetoParametros, this.conexao);
			ResultSet resultSet = preparedStatement.executeQuery();
			boolean existeRegistro = resultSet.next();
			resultSet.close();
			preparedStatement.close();
			return existeRegistro;
		}
		catch (DaoException excecao) {
			throw excecao;
		}
		catch (Exception excecao) {
			throw new DaoException(excecao);
		}
	}

	protected Integer gerarSequencial(String nome) throws DaoException {
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("nome", nome);

		if (this.executarInstrucaoSql(SQL_ATUALIZA_SEQUENCIAL, mapaParametros) == 1) {
			return this.executarConsulta(SQL_CONSULTA_SEQUENCIAL, mapaParametros, null, Integer.class).get(0);
		}
		else {
			this.executarInstrucaoSql(SQL_GRAVA_SEQUENCIAL, mapaParametros);
			return 1;
		}
	}

	protected String obterInstrucaoSql(String nome) throws DaoException {
		String instrucaoSql = instrucoesSql.get(this.getClass().getName()).get(nome);

		if (instrucaoSql == null) {
			throw new DaoException("Instrução SQL '" + nome + "' não definida.");
		}
		return instrucaoSql;
	}

	private String obterCaminhoArquivo() {
		return this.getClass().getSimpleName() + ".sql";
	}

	private void carregarInstrucoesSql() {
		String caminhoArquivo = this.obterCaminhoArquivo();
		Map<String, String> mapa = null;
		try {
			mapa = (Map<String, String>) new JSONParser().parse(new InputStreamReader(this.getClass().getResourceAsStream(caminhoArquivo)));
		}
		catch (Exception excecao) {
			mapa = new HashMap<String, String>();
		}
		instrucoesSql.put(this.getClass().getName(), mapa);
	}
}