package davidsolutions.easydoc.web.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.web.AvaliadorAcaoServlet;
import davidsolutions.caixaferramentas.web.BeanForm;
import davidsolutions.caixaferramentas.web.Get;
import davidsolutions.caixaferramentas.web.MensagemUsuario;
import davidsolutions.caixaferramentas.web.Post;
import davidsolutions.caixaferramentas.web.ResultadoAjax;
import davidsolutions.easydoc.servico.entidade.TipoUsuario;
import davidsolutions.easydoc.servico.entidade.Usuario;
import davidsolutions.easydoc.servico.regranegocio.ClienteRN;
import davidsolutions.easydoc.servico.regranegocio.GrupoRN;
import davidsolutions.easydoc.servico.regranegocio.UsuarioRN;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;
import davidsolutions.easydoc.web.utilidade.Pagina;
import davidsolutions.easydoc.web.utilidade.ResultadoAjaxUtil;

@WebServlet("/Usuario")
public class UsuarioServlet extends AvaliadorAcaoServlet {

	private static final long serialVersionUID = 1L;

	@Get
	public String iniciarInclusao(HttpServletRequest request, Boolean listaCliente) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("usuario/inclusaoAlteracao.jsp");
		try {
			if (listaCliente == null || listaCliente) {
				request.setAttribute("clientes", new ClienteRN().listarAtivos().getDados());
			}
			request.setAttribute("tiposUsuario", TipoUsuario.values());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAlteracao(@BeanForm(referencia = "usuario", encriptados = { "codigoUsuario" }) Usuario usuario, HttpServletRequest request) {
		try {
			RespostaServico<Usuario> respostaServico = new UsuarioRN().consultarPorCodigo(usuario.getCodigoUsuario());
			request.setAttribute("usuario", respostaServico.getDados());
			request.setAttribute("grupos", respostaServico.getDados().getGrupos());
			return this.iniciarInclusao(request, false);
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
			return this.iniciarConsulta(request);
		}
	}

	@Get
	public String iniciarConsulta(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("usuario/consulta.jsp");
		try {
			request.setAttribute("clientes", new ClienteRN().listar().getDados());
			request.setAttribute("tiposUsuario", TipoUsuario.values());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAutenticacao(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCabecalho(null);
		pagina.setCorpo("usuario/autenticacao.jsp");
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAlteracaoSenha(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("usuario/alteracaoSenha.jsp");
		return pagina.getPaginaBase();
	}

	@Post
	public ResultadoAjax adicionar(@BeanForm(referencia = "usuario", encriptados = { "cliente.codigoCliente", "tipoUsuario", "grupos.codigoGrupo" }) Usuario usuario, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new UsuarioRN().adicionar(usuario);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Post
	public ResultadoAjax alterar(@BeanForm(referencia = "usuario", encriptados = { "codigoUsuario", "tipoUsuario", "grupos.codigoGrupo" }) Usuario usuario, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new UsuarioRN().alterar(usuario);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (RegistroInexistenteException excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
			this.iniciarConsulta(request);
			return new ResultadoAjaxUtil().adicionarMensagemUsuario().adicionarCorpo(GerenciadorAtributosRequest.obterPagina(request).getCorpo());
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Get
	public String consultar(@BeanForm(referencia = "usuario", encriptados = { "cliente.codigoCliente", "tipoUsuario" }) Usuario usuario, @BeanForm("paginacao") Paginacao paginacao, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<List<Usuario>> respostaServico = new UsuarioRN().consultar(usuario, paginacao);
			request.setAttribute("usuarios", respostaServico.getDados());
			mensagemUsuario.adicionarMensagensAlerta(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return this.iniciarConsulta(request);
	}

	@Post
	public ResultadoAjax gerarSenhaAleatoria(@BeanForm(referencia = "usuario", encriptados = { "codigoUsuario" }) Usuario usuario, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new UsuarioRN().gerarSenhaAleatoria(usuario.getCodigoUsuario());
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Post
	public String autenticar(@BeanForm("usuario") Usuario usuario, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Usuario> respostaServico = new UsuarioRN().autenticar(usuario.getLogin(), usuario.getSenha());
			GerenciadorAtributosRequest.gravarUsuarioAutenticado(respostaServico.getDados(), request);
			mensagemUsuario.adicionarMensagensAlerta(respostaServico);

			if (respostaServico.getDados().getNecessitaAlteracaoSenha()) {
				return this.iniciarAlteracaoSenha(request);
			}
			else {
				return this.iniciarConsulta(request);
			}
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
			return this.iniciarAutenticacao(request);
		}
	}

	@Post
	public ResultadoAjax alterarSenha(@BeanForm("usuarioAlteracaoSenha") UsuarioAlteracaoSenha usuario, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			Usuario usuarioAutenticado = GerenciadorAtributosRequest.obterUsuarioAutenticado(request);
			RespostaServico<Void> respostaServico = new UsuarioRN().alterarSenha(usuarioAutenticado.getCodigoUsuario(), usuario.getSenhaAtual(), usuario.getSenhaNova());
			usuarioAutenticado.setNecessitaAlteracaoSenha(false);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Get
	public ResultadoAjax listarGruposPorCodigoCliente(@BeanForm(referencia = "usuario", encriptados = { "cliente.codigoCliente" }) Usuario usuario, HttpServletRequest request) {
		ResultadoAjaxUtil resultadoAjaxUtil = new ResultadoAjaxUtil("divGrupos", "usuario/grupos.jsp");
		try {
			if (usuario.getCliente() != null && usuario.getCliente().getCodigoCliente() != null) {
				request.setAttribute("grupos", new GrupoRN().listarAtivosPorCodigoCliente(usuario.getCliente().getCodigoCliente()).getDados());
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request);
			resultadoAjaxUtil.adicionarMensagemUsuario();
		}
		return resultadoAjaxUtil;
	}

	@Get
	public String abandonarSessao(HttpServletRequest request) {
		request.getSession().invalidate();
		return this.iniciarAutenticacao(request);
	}

	public static class UsuarioAlteracaoSenha {

		private String senhaAtual;
		private String senhaNova;
		private String senhaNovaRepetida;

		public String getSenhaAtual() {
			return this.senhaAtual;
		}

		public void setSenhaAtual(String senhaAtual) {
			this.senhaAtual = senhaAtual;
		}

		public String getSenhaNova() {
			return this.senhaNova;
		}

		public void setSenhaNova(String senhaNova) {
			this.senhaNova = senhaNova;
		}

		public String getSenhaNovaRepetida() {
			return this.senhaNovaRepetida;
		}

		public void setSenhaNovaRepetida(String senhaNovaRepetida) {
			this.senhaNovaRepetida = senhaNovaRepetida;
		}
	}
}