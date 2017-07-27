package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;

public class ArquivoDao extends Dao {

	public ArquivoDao(Connection conexao) throws DaoException {
		super(conexao);
	}
}