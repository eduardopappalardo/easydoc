package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.GerenciadorConexao;
import davidsolutions.easydoc.servico.constante.Configuracao;

public class GerenciadorConexaoProducao implements GerenciadorConexao {

	public Connection obterConexao() throws DaoException {
		try {
			DataSource dataSource = (DataSource) new InitialContext().lookup(Configuracao.JNDI_CONEXAO_BANCO.obterValor());
			return dataSource.getConnection();
		}
		catch (Exception excecao) {
			throw new DaoException(excecao);
		}
	}
}