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
import davidsolutions.easydoc.servico.entidade.Grupo;
import davidsolutions.easydoc.servico.regranegocio.ClienteRN;
import davidsolutions.easydoc.servico.regranegocio.GrupoRN;
import davidsolutions.easydoc.servico.regranegocio.UsuarioRN;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;
import davidsolutions.easydoc.web.utilidade.Pagina;
import davidsolutions.easydoc.web.utilidade.ResultadoAjaxUtil;

@WebServlet("/Grupo")
public class GrupoServlet extends AvaliadorAcaoServlet {

	private static final long serialVersionUID = 1L;

	@Get
	public String iniciarInclusao(HttpServletRequest request, Boolean listaCliente) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("grupo/inclusaoAlteracao.jsp");
		try {
			if (listaCliente == null || listaCliente) {
				request.setAttribute("clientes", new ClienteRN().listarAtivos().getDados());
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAlteracao(@BeanForm(referencia = "grupo", encriptados = { "codigoGrupo" }) Grupo grupo, HttpServletRequest request) {
		try {
			RespostaServico<Grupo> respostaServico = new GrupoRN().consultarPorCodigo(grupo.getCodigoGrupo());
			request.setAttribute("grupo", respostaServico.getDados());
			request.setAttribute("usuarios", respostaServico.getDados().getUsuarios());
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
		pagina.setCorpo("grupo/consulta.jsp");
		try {
			request.setAttribute("clientes", new ClienteRN().listar().getDados());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Post
	public ResultadoAjax adicionar(@BeanForm(referencia = "grupo", encriptados = { "cliente.codigoCliente", "usuarios.codigoUsuario" }) Grupo grupo, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new GrupoRN().adicionar(grupo);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Post
	public ResultadoAjax alterar(@BeanForm(referencia = "grupo", encriptados = { "codigoGrupo", "usuarios.codigoUsuario" }) Grupo grupo, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new GrupoRN().alterar(grupo);
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
	public String consultar(@BeanForm(referencia = "grupo", encriptados = { "cliente.codigoCliente" }) Grupo grupo, @BeanForm("paginacao") Paginacao paginacao, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<List<Grupo>> respostaServico = new GrupoRN().consultar(grupo, paginacao);
			request.setAttribute("grupos", respostaServico.getDados());
			mensagemUsuario.adicionarMensagensAlerta(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return this.iniciarConsulta(request);
	}

	@Get
	public ResultadoAjax listarUsuariosPorCodigoCliente(@BeanForm(referencia = "grupo", encriptados = { "cliente.codigoCliente" }) Grupo grupo, HttpServletRequest request) {
		ResultadoAjaxUtil resultadoAjaxUtil = new ResultadoAjaxUtil("divUsuarios", "grupo/usuarios.jsp");
		try {
			if (grupo.getCliente() != null && grupo.getCliente().getCodigoCliente() != null) {
				request.setAttribute("usuarios", new UsuarioRN().listarAtivosPorCodigoCliente(grupo.getCliente().getCodigoCliente()).getDados());
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request);
			resultadoAjaxUtil.adicionarMensagemUsuario();
		}
		return resultadoAjaxUtil;
	}
}