package davidsolutions.caixaferramentas.web;

import java.util.HashMap;
import java.util.Map;

public class ResultadoAjax {

	private boolean redirecionamento = false;
	private String urlRedirecionamento;
	private Map<String, String> fragmentosPagina = new HashMap<String, String>();

	public ResultadoAjax() {
	}

	public ResultadoAjax(String urlRedirecionamento) {
		this.urlRedirecionamento = urlRedirecionamento;
		this.redirecionamento = true;
	}

	public ResultadoAjax(String referencia, String caminhoFragmento) {
		this.adicionarFragmentoPagina(referencia, caminhoFragmento);
	}

	public boolean getRedirecionamento() {
		return this.redirecionamento;
	}

	public String getUrlRedirecionamento() {
		return this.urlRedirecionamento;
	}

	public Map<String, String> getFragmentosPagina() {
		return this.fragmentosPagina;
	}

	public void adicionarFragmentoPagina(String referencia, String caminhoFragmento) {
		this.fragmentosPagina.put(referencia, caminhoFragmento);
	}
}