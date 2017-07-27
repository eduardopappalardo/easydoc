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
import davidsolutions.easydoc.servico.entidade.Usuario;

public class UsuarioDao extends Dao {

	public UsuarioDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(Usuario usuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		usuario.setCodigoUsuario(super.gerarSequencial("Usuario"));
		super.executarInstrucaoSql(instrucaoSql, usuario);
	}

	public void alterar(Usuario usuario) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterar");

		if (super.executarInstrucaoSql(instrucaoSql, usuario) == 0) {
			throw new RegistroInexistenteException();
		}
	}

	public Usuario consultarPorCodigo(Integer codigoUsuario) throws RegistroInexistenteException, DaoException {
		String instrucaoSql = super.obterInstrucaoSql("consultarPorCodigo");
		return super.executarConsultaRegistroUnico(instrucaoSql, new Usuario(codigoUsuario), Usuario.class);
	}

	public List<Usuario> consultar(Usuario usuarioFiltro, Paginacao paginacao) throws DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("consultar"), usuarioFiltro);
		queryUtil.setPaginacao(paginacao);
		return super.executarConsulta(queryUtil, Usuario.class);
	}

	public boolean existeLogin(Usuario usuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("existeLogin");
		return super.existeRegistro(instrucaoSql, usuario);
	}

	public void alterarSenha(Usuario usuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("alterarSenha");
		super.executarInstrucaoSql(instrucaoSql, usuario);
	}

	public boolean existeSenha(Usuario usuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("existeSenha");
		return super.existeRegistro(instrucaoSql, usuario);
	}

	public void adicionarHistoricoSenha(Integer codigoUsuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionarHistoricoSenha");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoSenha", super.gerarSequencial("HistoricoSenha"));
		mapaParametros.put("codigoUsuario", codigoUsuario);
		super.executarInstrucaoSql(instrucaoSql, mapaParametros);
	}

	public Usuario validarAcesso(Usuario usuario) throws RegistroInexistenteException, DaoException {
		QueryUtil queryUtil = new QueryUtil(super.obterInstrucaoSql("validarAcesso"), usuario, false);
		return super.executarConsultaRegistroUnico(queryUtil, Usuario.class);
	}

	public boolean existeCodigo(Usuario usuario) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("existeCodigo");
		return super.existeRegistro(instrucaoSql, usuario);
	}

	public List<Usuario> listarAtivosPorCodigoCliente(Integer codigoCliente) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("listarAtivosPorCodigoCliente");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoCliente", codigoCliente);
		mapaParametros.put("ativo", true);
		return super.executarConsulta(instrucaoSql, mapaParametros, null, Usuario.class);
	}

	public List<Usuario> listarAtivosPorCodigoGrupo(Integer codigoGrupo) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("listarAtivosPorCodigoGrupo");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoGrupo", codigoGrupo);
		mapaParametros.put("ativo", true);
		return super.executarConsulta(instrucaoSql, mapaParametros, null, Usuario.class);
	}
}