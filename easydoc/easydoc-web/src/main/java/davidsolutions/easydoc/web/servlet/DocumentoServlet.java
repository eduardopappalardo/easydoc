package davidsolutions.easydoc.web.servlet;

import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.web.AvaliadorAcaoServlet;
import davidsolutions.caixaferramentas.web.BeanForm;
import davidsolutions.caixaferramentas.web.Get;
import davidsolutions.caixaferramentas.web.MensagemUsuario;
import davidsolutions.caixaferramentas.web.Post;
import davidsolutions.caixaferramentas.web.ResultadoAjax;
import davidsolutions.easydoc.servico.entidade.Cliente;
import davidsolutions.easydoc.servico.entidade.Documento;
import davidsolutions.easydoc.servico.entidade.TipoDocumento;
import davidsolutions.easydoc.servico.regranegocio.ClienteRN;
import davidsolutions.easydoc.servico.regranegocio.DocumentoRN;
import davidsolutions.easydoc.servico.regranegocio.TipoDocumentoRN;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;
import davidsolutions.easydoc.web.utilidade.Pagina;
import davidsolutions.easydoc.web.utilidade.ResultadoAjaxUtil;

@WebServlet("/Documento")
@MultipartConfig
public class DocumentoServlet extends AvaliadorAcaoServlet {

	private static final long serialVersionUID = 1L;

	@Get
	public String selecionarTipoDocumento(HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("documento/selecaoTipoDocumento.jsp");
		try {
			request.setAttribute("clientes", new ClienteRN().listarAtivos().getDados());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarInclusao(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("documento/inclusaoAlteracao.jsp");
		try {
			RespostaServico<TipoDocumento> respostaServico = new TipoDocumentoRN().consultarPorCodigo(tipoDocumento.getCodigoTipoDocumento());
			request.setAttribute("tipoDocumento", respostaServico.getDados());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Get
	public String iniciarAlteracao(@BeanForm(referencia = "documento", encriptados = { "codigoDocumento" }) Documento documento, HttpServletRequest request) {
		try {
			// TODO
			return this.iniciarInclusao(documento.getTipoDocumento(), request);
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
			return this.iniciarConsulta(documento.getTipoDocumento(), request);
		}
	}

	@Get
	public String iniciarConsulta(@BeanForm(referencia = "tipoDocumento", encriptados = { "codigoTipoDocumento" }) TipoDocumento tipoDocumento, HttpServletRequest request) {
		Pagina pagina = GerenciadorAtributosRequest.obterPagina(request);
		pagina.setCorpo("documento/consulta.jsp");
		try {
			RespostaServico<TipoDocumento> respostaServico = new TipoDocumentoRN().consultarPorCodigo(tipoDocumento.getCodigoTipoDocumento());
			request.setAttribute("tipoDocumento", respostaServico.getDados());
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return pagina.getPaginaBase();
	}

	@Post
	public ResultadoAjax adicionar(@BeanForm(referencia = "documento", encriptados = { "tipoDocumento.codigoTipoDocumento", "indices.codigoIndice" }) Documento documento, HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = GerenciadorAtributosRequest.obterMensagemUsuario(request);
		try {
			int posicao = 0;

			for (Part arquivo : request.getParts()) {

				if (arquivo.getName().matches("documento.arquivos\\[\\d+\\].conteudo")) {
					documento.getArquivos().get(posicao).setNomeOriginal(arquivo.getName());
					documento.getArquivos().get(posicao).setConteudo(arquivo.getInputStream());
					posicao++;
				}
			}
			RespostaServico<Void> respostaServico = new DocumentoRN().adicionar(documento);
			mensagemUsuario.adicionarMensagens(respostaServico);
		}
		catch (Exception excecao) {
			mensagemUsuario.adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil().adicionarMensagemUsuario();
	}

	@Get
	public String consultar(@BeanForm(referencia = "documento", encriptados = { "tipoDocumento.codigoTipoDocumento", "indices.codigoIndice" }) Documento documento, HttpServletRequest request) {
		// TODO
		return this.iniciarConsulta(documento.getTipoDocumento(), request);
	}

	@Get
	public ResultadoAjax listarTipoDocumentoPorCodigoCliente(@BeanForm(referencia = "cliente", encriptados = { "codigoCliente" }) Cliente cliente, HttpServletRequest request) {
		try {
			if (cliente.getCodigoCliente() != null) {
				List<TipoDocumento> listaTipoDocumento = new TipoDocumentoRN().listarPorCodigoCliente(cliente.getCodigoCliente()).getDados();
				request.setAttribute("tiposDocumento", listaTipoDocumento);
			}
		}
		catch (Exception excecao) {
			GerenciadorAtributosRequest.obterMensagemUsuario(request).adicionarMensagens(excecao);
		}
		return new ResultadoAjaxUtil("divTiposDocumento", "documento/tiposDocumento.jsp").adicionarMensagemUsuario();
	}
}