package davidsolutions.caixaferramentas.dao;

import java.sql.Connection;

public interface GerenciadorConexao {

	public Connection obterConexao() throws DaoException;

}