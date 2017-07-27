package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.QueryUtil;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.easydoc.servico.entidade.Grupo;
import davidsolutions.easydoc.servico.entidade.Usuario;

public class GrupoDao extends Dao {

	public GrupoDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(Grupo grupo) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		grupo.setCodigoGrupo(super.gerarSequencial("Grupo"));
		super.executarInstrucaoSql(instrucaoSql, grupo);
	}

	public void alterar(Grupo grupo) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterar");

		if (super.executarInstrucaoSql(instrucaoSql, grupo) == 0) {
			throw new RegistroInexistenteException();
		}
	}

	public Grupo consultarPorCodigo(Integer codigoGrupo) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("consultarPorCodigo");
		return super.executarConsultaRegistroUnico(instrucaoSql, new Grupo(codigoGrupo), Grupo.class);
	}

	public List<Grupo> consultar(Grupo grupoFiltro, Paginacao paginacao) throws DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("consultar"), grupoFiltro);
		queryUtil.setPaginacao(paginacao);
		return super.executarConsulta(queryUtil, Grupo.class);
	}

	public boolean existeCodigo(Grupo grupo) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("existeCodigo");
		return super.existeRegistro(instrucaoSql, grupo);
	}

	public List<Grupo> listarAtivosPorCodigoCliente(Integer codigoCliente) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("listarAtivosPorCodigoCliente");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoCliente", codigoCliente);
		mapaParametros.put("ativo", true);
		return super.executarConsulta(instrucaoSql, mapaParametros, null, Grupo.class);
	}

	public List<Grupo> listarAtivosPorCodigoUsuario(Integer codigoUsuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("listarAtivosPorCodigoUsuario");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoUsuario", codigoUsuario);
		mapaParametros.put("ativo", true);
		return super.executarConsulta(instrucaoSql, mapaParametros, null, Grupo.class);
	}

	public void adicionarGrupoUsuario(Integer codigoGrupo, Integer codigoUsuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionarGrupoUsuario");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoGrupo", codigoGrupo);
		mapaParametros.put("codigoUsuario", codigoUsuario);
		super.executarInstrucaoSql(instrucaoSql, mapaParametros);
	}

	public void excluirGrupoUsuarioPorCodigoUsuario(Integer codigoUsuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("excluirGrupoUsuarioPorCodigoUsuario");
		super.executarInstrucaoSql(instrucaoSql, new Usuario(codigoUsuario));
	}

	public void excluirGrupoUsuarioPorCodigoGrupo(Integer codigoGrupo) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("excluirGrupoUsuarioPorCodigoGrupo");
		super.executarInstrucaoSql(instrucaoSql, new Grupo(codigoGrupo));
	}
}