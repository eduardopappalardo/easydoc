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
import davidsolutions.easydoc.servico.entidade.Cliente;
import davidsolutions.easydoc.servico.regranegocio.ClienteRN;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;
import davidsolutions.easydoc.web.utilidade.Pagina;
import davidsolutions.easydoc.web.utilidade.ResultadoAjaxUtil;

@WebServlet("/Cliente")
public class ClienteServlet extends AvaliadorAcaoServlet {

	private static final long serialVersionUID = 1L;

	@Get
	public String iniciarInclusao(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("cliente/inclusaoAlteracao.jsp");
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAlteracao(@BeanForm(referencia = "cliente", encriptados = { "codigoCliente" }) Cliente cliente, HttpServletRequest request) {
		try {
			RespostaServico<Cliente> respostaServico = new ClienteRN().consultarPorCodigo(cliente.getCodigoCliente());
			request.setAttribute("cliente", respostaServico.getDados());
			return this.iniciarInclusao(request);
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
			return this.iniciarConsulta(request);
		}
	}

	@Get
	public String iniciarConsulta(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("cliente/consulta.jsp");
		return pagina.getPaginaBase();
	}

	@Post
	public ResultadoAjax adicionar(@BeanForm("cliente") Cliente cliente, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new ClienteRN().adicionar(cliente);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Post
	public ResultadoAjax alterar(@BeanForm(referencia = "cliente", encriptados = { "codigoCliente" }) Cliente cliente, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new ClienteRN().alterar(cliente);
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
	public String consultar(@BeanForm("cliente") Cliente cliente, @BeanForm("paginacao") Paginacao paginacao, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<List<Cliente>> respostaServico = new ClienteRN().consultar(cliente, paginacao);
			request.setAttribute("clientes", respostaServico.getDados());
			mensagemUsuario.adicionarMensagensAlerta(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return this.iniciarConsulta(request);
	}
}