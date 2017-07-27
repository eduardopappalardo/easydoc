package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import davidsolutions.caixaferramentas.dao.DaoException;

public abstract class DaoTeste {

	protected Connection conexao;

	@Before
	public void configurarConexao() throws DaoException, SQLException {
		this.conexao = new GerenciadorConexaoTeste().obterConexao();
		this.conexao.setAutoCommit(false);
	}

	@After
	public void fecharConexao() throws SQLException {
		this.conexao.rollback();
		this.conexao.close();
	}
}