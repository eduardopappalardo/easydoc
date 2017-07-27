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
import davidsolutions.easydoc.servico.entidade.TipoDocumento;
import davidsolutions.easydoc.servico.regranegocio.ClienteRN;
import davidsolutions.easydoc.servico.regranegocio.TipoDocumentoRN;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;
import davidsolutions.easydoc.web.utilidade.Pagina;
import davidsolutions.easydoc.web.utilidade.ResultadoAjaxUtil;

@WebServlet("/TipoDocumento")
public class TipoDocumentoServlet extends AvaliadorAcaoServlet {

	private static final long serialVersionUID = 1L;

	@Get
	public String iniciarInclusao(HttpServletRequest request, Boolean listaCliente) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("tipoDocumento/inclusaoAlteracao.jsp");
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
	public String iniciarAlteracao(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		try {
			TipoDocumentoRN tipoDocumentoRN = new TipoDocumentoRN();
			RespostaServico<TipoDocumento> respostaServico = tipoDocumentoRN.consultarPorCodigo(tipoDocumento.getCodigoTipoDocumento());
			request.setAttribute("tipoDocumento", respostaServico.getDados());

			List<TipoDocumento> listaTipoDocumento = tipoDocumentoRN.listarPorCodigoCliente(respostaServico.getDados().getCliente().getCodigoCliente()).getDados();
			listaTipoDocumento.remove(tipoDocumento);
			request.setAttribute("tiposDocumento", listaTipoDocumento);
			request.setAttribute("indicesHerdados", respostaServico.getDados().getIndicesHerdados());

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
		pagina.setCorpo("tipoDocumento/consulta.jsp");
		try {
			request.setAttribute("clientes", new ClienteRN().listar().getDados());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Post
	public ResultadoAjax adicionar(@BeanForm(referencia = "tipoDocumento", encriptados = { "cliente.codigoCliente", "tipoDocumentoPai.codigoTipoDocumento", "indices.tipoIndice" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new TipoDocumentoRN().adicionar(tipoDocumento);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Post
	public ResultadoAjax alterar(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento", "tipoDocumentoPai.codigoTipoDocumento", "indices.codigoIndice", "indices.tipoIndice" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new TipoDocumentoRN().alterar(tipoDocumento);
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

	@Post
	public ResultadoAjax excluir(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<Void> respostaServico = new TipoDocumentoRN().excluir(tipoDocumento.getCodigoTipoDocumento());
			mensagemUsuario.adicionarMensagens(respostaServico);
			this.iniciarConsulta(request);
			return new ResultadoAjaxUtil().adicionarMensagemUsuario().adicionarCorpo(GerenciadorAtributosRequest.obterPagina(request).getCorpo());
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
			return new ResultadoAjaxUtil().adicionarMensagemUsuario();
		}
	}

	@Get
	public String consultar(@BeanForm(referencia = "tipoDocumento", encriptados = { "cliente.codigoCliente" }) TipoDocumento tipoDocumento, @BeanForm("paginacao") Paginacao paginacao, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			RespostaServico<List<TipoDocumento>> respostaServico = new TipoDocumentoRN().consultar(tipoDocumento, paginacao);
			request.setAttribute("tiposDocumento", respostaServico.getDados());
			mensagemUsuario.adicionarMensagensAlerta(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return this.iniciarConsulta(request);
	}

	@Get
	public ResultadoAjax listarTipoDocumentoPorCodigoCliente(@BeanForm(referencia = "tipoDocumento", encriptados = { "cliente.codigoCliente" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		ResultadoAjaxUtil resultadoAjaxUtil = new ResultadoAjaxUtil("divTipoDocumentoPai", "tipoDocumento/tipoDocumentoPai.jsp");
		try {
			if (tipoDocumento.getCliente() != null && tipoDocumento.getCliente().getCodigoCliente() != null) {
				List<TipoDocumento> listaTipoDocumento = new TipoDocumentoRN().listarPorCodigoCliente(tipoDocumento.getCliente().getCodigoCliente()).getDados();
				request.setAttribute("tiposDocumento", listaTipoDocumento);
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
			resultadoAjaxUtil.adicionarMensagemUsuario();
		}
		return resultadoAjaxUtil;
	}

	@Get
	public ResultadoAjax obterIndicesTipoDocumento(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		ResultadoAjaxUtil resultadoAjaxUtil = new ResultadoAjaxUtil("divIndicesHerdados", "tipoDocumento/indicesHerdados.jsp");
		try {
			if (tipoDocumento.getCodigoTipoDocumento() != null) {
				RespostaServico<TipoDocumento> respostaServico = new TipoDocumentoRN().consultarPorCodigo(tipoDocumento.getCodigoTipoDocumento());
				request.setAttribute("indicesHerdados", respostaServico.getDados().getTodosIndices());
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
			resultadoAjaxUtil.adicionarMensagemUsuario();
		}
		return resultadoAjaxUtil;
	}
}