package davidsolutions.easydoc.servico.regranegocio;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.utilidade.CriptografiaUtil;
import davidsolutions.caixaferramentas.utilidade.DataUtil;
import davidsolutions.caixaferramentas.validacao.ExecutorConsulta;
import davidsolutions.caixaferramentas.validacao.Mensagem;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.constante.Configuracao;
import davidsolutions.easydoc.servico.constante.Dicionario;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.dao.GrupoDao;
import davidsolutions.easydoc.servico.dao.UsuarioDao;
import davidsolutions.easydoc.servico.entidade.Cliente;
import davidsolutions.easydoc.servico.entidade.Grupo;
import davidsolutions.easydoc.servico.entidade.Usuario;

public class UsuarioRN extends Servico<UsuarioDao> {

	private static final char[] CARACTERES_SENHA_ALEATORIA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	public UsuarioRN() {
		super(new GerenciadorConexaoProducao());
	}

	public UsuarioRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	public RespostaServico<Void> adicionar(Usuario usuario) throws ValidacaoException, FalhaExecucaoException {
		this.validar(usuario, false);
		usuario.setDataCadastro(new Date());
		try {
			RespostaServico<Void> respostaServico = new RespostaServico<Void>();

			super.iniciarTransacao();
			this.obterDao().adicionar(usuario);

			if (!ValidacaoUtil.estaVazio(usuario.getGrupos())) {
				GrupoDao grupoDao = new GrupoDao(super.obterConexao());

				for (Grupo grupo : usuario.getGrupos()) {
					grupoDao.adicionarGrupoUsuario(grupo.getCodigoGrupo(), usuario.getCodigoUsuario());
				}
			}
			if (usuario.getAtivo()) {
				this.gerarSenhaAleatoria(usuario);
				respostaServico.getMensagensAlerta().add(new Mensagem(Dicionario.USUARIO_SENHA_GERADA.obterValor()));
			}
			super.confirmarTransacao();

			return respostaServico;
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> alterar(Usuario usuario) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		this.validar(usuario, true);
		try {
			GrupoDao grupoDao = new GrupoDao(super.obterConexao());

			super.iniciarTransacao();
			this.obterDao().alterar(usuario);
			grupoDao.excluirGrupoUsuarioPorCodigoUsuario(usuario.getCodigoUsuario());

			if (!ValidacaoUtil.estaVazio(usuario.getGrupos())) {
				for (Grupo grupo : usuario.getGrupos()) {
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

	public RespostaServico<Usuario> consultarPorCodigo(Integer codigoUsuario) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_USUARIO.obterValor(), codigoUsuario);
		try {
			Usuario usuario = this.obterDao().consultarPorCodigo(codigoUsuario);
			usuario.setGrupos(new GrupoRN(super.obterConexao()).listarAtivosPorCodigoUsuario(codigoUsuario).getDados());
			return new RespostaServico<Usuario>(usuario);
		}
		catch (DaoException excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Usuario>> consultar(Usuario usuarioFiltro, Paginacao paginacao) throws ValidacaoException, FalhaExecucaoException {

		if (usuarioFiltro != null) {
			Validador validador = new Validador();
			validador.adicionarValidacao("nome", "Nome", usuarioFiltro.getNome()).validarTamanho(1, 100);
			validador.adicionarValidacao("login", "Login", usuarioFiltro.getLogin()).validarTamanho(1, 100);
			validador.adicionarValidacao("email", "E-mail", usuarioFiltro.getEmail()).validarTamanho(1, 100);
			validador.validar();
		}
		try {
			List<Usuario> lista = this.obterDao().consultar(usuarioFiltro, paginacao);
			return new RespostaServico<List<Usuario>>(lista);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> gerarSenhaAleatoria(Integer codigoUsuario) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_USUARIO.obterValor(), codigoUsuario);
		Usuario usuario = null;
		try {
			usuario = this.obterDao().consultarPorCodigo(codigoUsuario);
		}
		catch (DaoException excecao) {
			throw new FalhaExecucaoException(excecao);
		}
		if (!usuario.getAtivo()) {
			throw new ValidacaoException(new Mensagem("O usuário deve estar ativo para essa operação."));
		}
		try {
			RespostaServico<Void> respostaServico = new RespostaServico<Void>();

			super.iniciarTransacao();
			this.gerarSenhaAleatoria(usuario);
			super.confirmarTransacao();

			respostaServico.getMensagensAlerta().add(new Mensagem(Dicionario.USUARIO_SENHA_GERADA.obterValor()));
			return respostaServico;
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Usuario> autenticar(String login, String senha) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.adicionarValidacao("login", "Login", login).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao("senha", "Senha", senha).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.validar();

		Usuario usuario = null;
		try {
			usuario = this.validarAcesso(null, login, senha);
			usuario = this.obterDao().consultarPorCodigo(usuario.getCodigoUsuario());
		}
		catch (RegistroInexistenteException excecao) {
			throw new ValidacaoException(new Mensagem("Login e/ou senha inválidos ou usuário com restrição de acesso."));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
		RespostaServico<Usuario> respostaServico = new RespostaServico<Usuario>(usuario);
		usuario.setNecessitaAlteracaoSenha(false);

		if (usuario.getDataAlteracaoSenha() == null) {
			usuario.setNecessitaAlteracaoSenha(true);
			respostaServico.getMensagensAlerta().add(new Mensagem(Dicionario.USUARIO_SENHA_EXPIRADA.obterValor()));
		}
		else {
			Calendar dataExpiracaoSenha = Calendar.getInstance();
			dataExpiracaoSenha.setTime(DataUtil.definirHoraMaxima(usuario.getDataAlteracaoSenha()));
			dataExpiracaoSenha.add(Calendar.DAY_OF_MONTH, Configuracao.SENHA_TEMPO_EXPIRACAO_EM_DIAS.obterValorInteiro());

			if (new Date().after(dataExpiracaoSenha.getTime())) {
				usuario.setNecessitaAlteracaoSenha(true);
				respostaServico.getMensagensAlerta().add(new Mensagem(Dicionario.USUARIO_SENHA_EXPIRADA.obterValor()));
			}
		}
		return respostaServico;
	}

	public RespostaServico<Void> alterarSenha(final Integer codigoUsuario, final String senhaAtual, String senhaNova) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.adicionarValidacao(null, Dicionario.CODIGO_USUARIO.obterValor(), codigoUsuario).validarPreenchimento();
		validador.adicionarValidacao("senhaAtual", "Senha atual", senhaAtual).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao("senhaNova", "Senha nova", senhaNova).validarPreenchimento().validarTamanho(Configuracao.SENHA_QUANTIDADE_MINIMA_CARACTERES.obterValorInteiro(), 100).dependeValidacaoAnterior();
		validador.validar();
		try {
			this.validarAcesso(codigoUsuario, null, senhaAtual);
		}
		catch (RegistroInexistenteException excecao) {
			throw new ValidacaoException(new Mensagem("senhaAtual", "Senha atual inválida ou usuário com restrição de acesso."));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
		Usuario usuario = new Usuario();
		usuario.setCodigoUsuario(codigoUsuario);
		usuario.setSenha(CriptografiaUtil.encriptarSemVolta(senhaNova));
		usuario.setDataAlteracaoSenha(new Date());

		boolean existeSenha = false;
		try {
			existeSenha = this.obterDao().existeSenha(usuario);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
		if (senhaNova.equals(senhaAtual) || existeSenha) {
			throw new ValidacaoException(new Mensagem("senhaNova", "A senha nova deve ser diferente das senhas anteriores."));
		}
		try {
			super.iniciarTransacao();
			this.obterDao().adicionarHistoricoSenha(usuario.getCodigoUsuario());
			this.obterDao().alterarSenha(usuario);
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Boolean> existeCodigo(Integer codigoUsuario) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_USUARIO.obterValor(), codigoUsuario);
		Usuario usuario = new Usuario(codigoUsuario);
		usuario.setAtivo(true);
		try {
			return new RespostaServico<Boolean>(this.obterDao().existeCodigo(usuario));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Usuario>> listarAtivosPorCodigoCliente(Integer codigoCliente) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_CLIENTE.obterValor(), codigoCliente);
		try {
			return new RespostaServico<List<Usuario>>(this.obterDao().listarAtivosPorCodigoCliente(codigoCliente));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Usuario>> listarAtivosPorCodigoGrupo(Integer codigoGrupo) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_GRUPO.obterValor(), codigoGrupo);
		try {
			return new RespostaServico<List<Usuario>>(this.obterDao().listarAtivosPorCodigoGrupo(codigoGrupo));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	private void validar(final Usuario usuario, boolean alteracao) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.validarNulo("usuario", usuario);

		if (alteracao) {
			validador.adicionarValidacao(null, Dicionario.CODIGO_USUARIO.obterValor(), usuario.getCodigoUsuario()).validarPreenchimento().identificacaoValidacao(1);
		}
		else {
			usuario.setCodigoUsuario(0);
			validador.adicionarValidacao("cliente", "Cliente", (usuario.getCliente() != null ? usuario.getCliente().getCodigoCliente() : null)).validarPreenchimento().validarCadastrado(new ExecutorConsulta() {

				public boolean estaCadastrado() throws Exception {
					return new ClienteRN(obterConexao()).existeCodigo(usuario.getCliente().getCodigoCliente()).getDados();
				}
			}).dependeValidacaoAnterior();
		}
		validador.adicionarValidacao("tipoUsuario", "Tipo de usuário", usuario.getTipoUsuario()).validarPreenchimento();
		validador.adicionarValidacao("nome", "Nome", usuario.getNome()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao("login", "Login", usuario.getLogin()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior().validarNaoCadastrado(new ExecutorConsulta() {

			public boolean estaCadastrado() throws Exception {
				return obterDao().existeLogin(usuario);
			}
		}).dependeValidacaoAnterior().dependeValidacaoIdentificada(1);
		validador.adicionarValidacao("email", "E-mail", usuario.getEmail()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior().validarEmail().dependeValidacaoAnterior();
		validador.adicionarValidacao("ativo", "Ativo", usuario.getAtivo()).validarPreenchimento();

		if (!ValidacaoUtil.estaVazio(usuario.getGrupos())) {

			for (final Grupo grupo : usuario.getGrupos()) {
				validador.adicionarValidacao(null, Dicionario.CODIGO_GRUPO.obterValor(), grupo.getCodigoGrupo()).validarPreenchimento().validarCadastrado(new ExecutorConsulta() {

					public boolean estaCadastrado() throws Exception {
						return new GrupoRN(obterConexao()).existeCodigo(grupo.getCodigoGrupo()).getDados();
					}
				}).dependeValidacaoAnterior();
			}
		}
		validador.validar();
	}

	private void gerarSenhaAleatoria(Usuario usuario) throws DaoException {
		String senhaAleatoria = gerarSenhaAleatoria();
		usuario.setSenha(CriptografiaUtil.encriptarSemVolta(senhaAleatoria));
		usuario.setDataAlteracaoSenha(null);

		this.obterDao().adicionarHistoricoSenha(usuario.getCodigoUsuario());
		this.obterDao().alterarSenha(usuario);

		usuario.setSenha(senhaAleatoria);
		// TODO: Descomentar
		// enviarEmailSenha(usuario);
		System.out.println(senhaAleatoria);
	}

	private Usuario validarAcesso(Integer codigoUsuario, String login, String senha) throws RegistroInexistenteException, DaoException {
		Usuario usuario = new Usuario();
		usuario.setCodigoUsuario(codigoUsuario);
		usuario.setLogin(login);
		usuario.setSenha(CriptografiaUtil.encriptarSemVolta(senha));
		usuario.setAtivo(true);
		usuario.setCliente(new Cliente());
		usuario.getCliente().setAtivo(true);
		return this.obterDao().validarAcesso(usuario);
	}

	private static String gerarSenhaAleatoria() {
		StringBuilder senhaAleatoria = new StringBuilder();
		Random random = new Random();
		int quantidadeCaracteres = Configuracao.SENHA_QUANTIDADE_MINIMA_CARACTERES.obterValorInteiro();

		for (int cont = 0; cont < quantidadeCaracteres; cont++) {
			senhaAleatoria.append(CARACTERES_SENHA_ALEATORIA[random.nextInt(CARACTERES_SENHA_ALEATORIA.length)]);
		}
		return senhaAleatoria.toString();
	}

	private static void enviarEmailSenha(Usuario usuario) throws Exception {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("<html>");
		mensagem.append("<body>");
		mensagem.append("Prezado " + usuario.getNome() + ",");
		mensagem.append("<br />");
		mensagem.append("Segue abaixo a sua nova senha de acesso ao sistema EasyDoc:");
		mensagem.append("<br /><br />");
		mensagem.append("<b>Login:</b> " + usuario.getLogin());
		mensagem.append("<br />");
		mensagem.append("<b>Senha:</b> " + usuario.getSenha());
		mensagem.append("</body>");
		mensagem.append("</html>");
		davidsolutions.easydoc.servico.utilidade.Utilidade.enviarEmail("EasyDoc - Senha de acesso", mensagem.toString(), usuario.getEmail());
	}
}