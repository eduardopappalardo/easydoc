package davidsolutions.caixaferramentas.servico;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.GerenciadorConexao;

public abstract class Servico<T extends Dao> {

	private GerenciadorConexao gerenciadorConexao;
	private Connection conexao;
	private boolean transacaoAberta = false;
	private T dao;

	public Servico(GerenciadorConexao gerenciadorConexao) {
		this.gerenciadorConexao = gerenciadorConexao;
	}

	public Servico(Connection conexao) throws DaoException {
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
		this.transacaoAberta = true;
	}

	protected Connection obterConexao() throws DaoException {

		if (this.conexao == null) {
			this.conexao = this.gerenciadorConexao.obterConexao();
		}
		return this.conexao;
	}

	protected void iniciarTransacao() throws DaoException, SQLException {

		if (!this.transacaoAberta) {
			this.obterConexao();
			this.conexao.setAutoCommit(false);
		}
	}

	protected void confirmarTransacao() throws SQLException {

		if (!this.transacaoAberta) {
			this.conexao.commit();
			this.conexao.setAutoCommit(true);
		}
	}

	protected void cancelarTransacao() {

		if (!this.transacaoAberta) {
			try {
				if (!this.conexao.getAutoCommit()) {
					this.conexao.rollback();
					this.conexao.setAutoCommit(true);
				}
			}
			catch (Exception excecao) {
			}
		}
	}

	protected T obterDao() throws DaoException {

		if (this.dao == null) {
			try {
				Class<T> classe = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
				this.dao = (T) classe.getConstructor(Connection.class).newInstance(this.obterConexao());
			}
			catch (DaoException excecao) {
				throw excecao;
			}
			catch (Exception excecao) {
				throw new DaoException(excecao);
			}
		}
		return this.dao;
	}
}