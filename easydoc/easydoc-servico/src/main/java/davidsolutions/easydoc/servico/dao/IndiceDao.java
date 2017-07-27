package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.easydoc.servico.entidade.Indice;
import davidsolutions.easydoc.servico.entidade.TipoDocumento;

public class IndiceDao extends Dao {

	public IndiceDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(Indice indice) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		indice.setCodigoIndice(super.gerarSequencial("Indice"));
		super.executarInstrucaoSql(instrucaoSql, indice);
	}

	public void alterar(Indice indice) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterar");
		super.executarInstrucaoSql(instrucaoSql, indice);
	}

	public void excluir(Integer codigoIndice) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("excluir");
		super.executarInstrucaoSql(instrucaoSql, new Indice(codigoIndice));
	}

	public void excluirPorCodigoTipoDocumento(Integer codigoTipoDocumento) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("excluirPorCodigoTipoDocumento");
		super.executarInstrucaoSql(instrucaoSql, new TipoDocumento(codigoTipoDocumento));
	}

	public List<Indice> consultarPorCodigoTipoDocumento(Integer codigoTipoDocumento) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("consultarPorCodigoTipoDocumento");
		return super.executarConsulta(instrucaoSql, new TipoDocumento(codigoTipoDocumento), null, Indice.class);
	}
}