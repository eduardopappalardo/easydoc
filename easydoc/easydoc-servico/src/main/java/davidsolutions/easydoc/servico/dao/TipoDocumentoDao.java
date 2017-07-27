package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.QueryUtil;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.easydoc.servico.entidade.Cliente;
import davidsolutions.easydoc.servico.entidade.TipoDocumento;

public class TipoDocumentoDao extends Dao {

	public TipoDocumentoDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(TipoDocumento tipoDocumento) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		tipoDocumento.setCodigoTipoDocumento(super.gerarSequencial("TipoDocumento"));
		super.executarInstrucaoSql(instrucaoSql, tipoDocumento);
	}

	public void alterar(TipoDocumento tipoDocumento) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterar");

		if (super.executarInstrucaoSql(instrucaoSql, tipoDocumento) == 0) {
			throw new RegistroInexistenteException();
		}
	}

	public void excluir(Integer codigoTipoDocumento) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("excluir");
		super.executarInstrucaoSql(instrucaoSql, new TipoDocumento(codigoTipoDocumento));
	}

	public TipoDocumento consultarPorCodigo(Integer codigoTipoDocumento) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("consultarPorCodigo");
		return super.executarConsultaRegistroUnico(instrucaoSql, new TipoDocumento(codigoTipoDocumento), TipoDocumento.class);
	}

	public List<TipoDocumento> consultar(TipoDocumento tipoDocumentoFiltro, Paginacao paginacao) throws DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("consultar"), tipoDocumentoFiltro);
		queryUtil.setPaginacao(paginacao);
		return super.executarConsulta(queryUtil, TipoDocumento.class);
	}

	public List<TipoDocumento> listarPorCodigoCliente(Integer codigoCliente) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("listarPorCodigoCliente");
		return super.executarConsulta(instrucaoSql, new Cliente(codigoCliente), null, TipoDocumento.class);
	}
}