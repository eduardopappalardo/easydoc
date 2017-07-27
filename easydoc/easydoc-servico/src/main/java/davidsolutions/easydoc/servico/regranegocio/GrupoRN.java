package davidsolutions.easydoc.servico.regranegocio;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.ExecutorConsulta;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.constante.Dicionario;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.dao.GrupoDao;
import davidsolutions.easydoc.servico.entidade.Grupo;
import davidsolutions.easydoc.servico.entidade.Usuario;

public class GrupoRN extends Servico<GrupoDao> {

	public GrupoRN() {
		super(new GerenciadorConexaoProducao());
	}

	public GrupoRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	public RespostaServico<Void> adicionar(Grupo grupo) throws ValidacaoException, FalhaExecucaoException {
		this.validar(grupo, false);
		try {
			GrupoDao grupoDao = this.obterDao();

			super.iniciarTransacao();
			grupoDao.adicionar(grupo);

			if (!ValidacaoUtil.estaVazio(grupo.getUsuarios())) {
				for (Usuario usuario : grupo.getUsuarios()) {
					grupoDao.adicionarGrupoUsuario(grupo.getCodigoGrupo(), usuario.getCodigoUsuario());
				}
			}
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> alterar(Grupo grupo) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		this.validar(grupo, true);
		try {
			GrupoDao grupoDao = this.obterDao();

			super.iniciarTransacao();
			grupoDao.alterar(grupo);
			grupoDao.excluirGrupoUsuarioPorCodigoGrupo(grupo.getCodigoGrupo());

			if (!ValidacaoUtil.estaVazio(grupo.getUsuarios())) {
				for (Usuario usuario : grupo.getUsuarios()) {
					grupoDao.adicionarGrupoUsuario(grupo.getCodigoGrupo(), usuario.getCodigoUsuario());
				}
			}
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (RegistroInexistenteException excecao) {
			super.cancelarTransacao();
			throw excecao;
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Grupo> consultarPorCodigo(Integer codigoGrupo) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_GRUPO.obterValor(), codigoGrupo);
		try {
			Grupo grupo = this.obterDao().consultarPorCodigo(codigoGrupo);
			grupo.setUsuarios(new UsuarioRN(super.obterConexao()).listarAtivosPorCodigoGrupo(codigoGrupo).getDados());
			return new RespostaServico<Grupo>(grupo);
		}
		catch (DaoException excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Grupo>> consultar(Grupo grupoFiltro, Paginacao paginacao) throws ValidacaoException, FalhaExecucaoException {

		if (grupoFiltro != null) {
			Validador validador = new Validador();
			validador.adicionarValidacao("nome", "Nome", grupoFiltro.getNome()).validarTamanho(1, 100);
			validador.validar();
		}
		try {
			List<Grupo> lista = this.obterDao().consultar(grupoFiltro, paginacao);
			return new RespostaServico<List<Grupo>>(lista);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Boolean> existeCodigo(Integer codigoGrupo) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_GRUPO.obterValor(), codigoGrupo);
		Grupo grupo = new Grupo(codigoGrupo);
		grupo.setAtivo(true);
		try {
			return new RespostaServico<Boolean>(this.obterDao().existeCodigo(grupo));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Grupo>> listarAtivosPorCodigoCliente(Integer codigoCliente) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_CLIENTE.obterValor(), codigoCliente);
		try {
			return new RespostaServico<List<Grupo>>(this.obterDao().listarAtivosPorCodigoCliente(codigoCliente));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Grupo>> listarAtivosPorCodigoUsuario(Integer codigoUsuario) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_USUARIO.obterValor(), codigoUsuario);
		try {
			return new RespostaServico<List<Grupo>>(this.obterDao().listarAtivosPorCodigoUsuario(codigoUsuario));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	private void validar(final Grupo grupo, boolean alteracao) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.validarNulo("grupo", grupo);

		if (alteracao) {
			validador.adicionarValidacao(null, Dicionario.CODIGO_GRUPO.obterValor(), grupo.getCodigoGrupo()).validarPreenchimento();
		}
		else {
			validador.adicionarValidacao("cliente", "Cliente", (grupo.getCliente() != null ? grupo.getCliente().getCodigoCliente() : null)).validarPreenchimento().validarCadastrado(new ExecutorConsulta() {

				public boolean estaCadastrado() throws Exception {
					return new ClienteRN(obterConexao()).existeCodigo(grupo.getCliente().getCodigoCliente()).getDados();
				}
			}).dependeValidacaoAnterior();
		}
		validador.adicionarValidacao("nome", "Nome", grupo.getNome()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao("ativo", "Ativo", grupo.getAtivo()).validarPreenchimento();

		if (!ValidacaoUtil.estaVazio(grupo.getUsuarios())) {

			for (final Usuario usuario : grupo.getUsuarios()) {
				validador.adicionarValidacao(null, Dicionario.CODIGO_USUARIO.obterValor(), usuario.getCodigoUsuario()).validarPreenchimento().validarCadastrado(new ExecutorConsulta() {

					public boolean estaCadastrado() throws Exception {
						return new UsuarioRN(obterConexao()).existeCodigo(usuario.getCodigoUsuario()).getDados();
					}
				}).dependeValidacaoAnterior();
			}
		}
		validador.validar();
	}
}