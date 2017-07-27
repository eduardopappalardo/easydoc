package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.QueryUtil;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.easydoc.servico.entidade.Cliente;

public class ClienteDao extends Dao {

	public ClienteDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(Cliente cliente) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		cliente.setCodigoCliente(super.gerarSequencial("Cliente"));
		super.executarInstrucaoSql(instrucaoSql, cliente);
	}

	public void alterar(Cliente cliente) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterar");

		if (super.executarInstrucaoSql(instrucaoSql, cliente) == 0) {
			throw new RegistroInexistenteException();
		}
	}

	public Cliente consultarPorCodigo(Integer codigoCliente) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("consultarPorCodigo");
		return super.executarConsultaRegistroUnico(instrucaoSql, new Cliente(codigoCliente), Cliente.class);
	}

	public List<Cliente> consultar(Cliente clienteFiltro, Paginacao paginacao) throws DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("consultar"), clienteFiltro);
		queryUtil.setPaginacao(paginacao);
		return super.executarConsulta(queryUtil, Cliente.class);
	}

	public boolean existeCodigo(Cliente cliente) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("existeCodigo");
		return super.existeRegistro(instrucaoSql, cliente);
	}

	public List<Cliente> listar(Cliente cliente) throws DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("listar"), cliente, false);
		return super.executarConsulta(queryUtil, Cliente.class);
	}
}