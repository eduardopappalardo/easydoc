package davidsolutions.easydoc.web.utilidade;

import davidsolutions.caixaferramentas.web.ResultadoAjax;

public class ResultadoAjaxUtil extends ResultadoAjax {

	public ResultadoAjaxUtil() {
	}

	public ResultadoAjaxUtil(String referencia, String caminhoFragmento) {
		super(referencia, caminhoFragmento);
	}

	public ResultadoAjaxUtil(String urlRedirecionamento) {
		super(urlRedirecionamento);
	}

	public ResultadoAjaxUtil adicionarMensagemUsuario() {
		this.adicionarFragmentoPagina("divMensagemUsuario", "mensagemUsuario.jsp");
		return this;
	}

	public ResultadoAjaxUtil adicionarCorpo(String caminhoFragmento) {
		this.adicionarFragmentoPagina("divCorpo", caminhoFragmento);
		return this;
	}
}