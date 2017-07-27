package davidsolutions.easydoc.web.utilidade;

import java.util.ArrayList;
import java.util.List;

public class Pagina {

	private List<String> scripts = new ArrayList<String>();
	private List<String> estilos = new ArrayList<String>();
	private String titulo = "EasyDoc";
	private String paginaBase = "paginaBase.jsp";
	private String cabecalho = "link.jsp";
	private String corpo;
	private String rodape;

	public Pagina() {
		this.estilos.add("jquery-ui.min.css");
		this.estilos.add("jquery-ui.structure.min.css");
		this.estilos.add("jquery-ui.theme.min.css");
		this.estilos.add("comum.css");

		this.scripts.add("jquery-1.11.1.min.js");
		this.scripts.add("jquery-ui-1.11.1.min.js");
		this.scripts.add("jquery-mask.min.js");
		this.scripts.add("comum.js");
	}

	public List<String> getScripts() {
		return this.scripts;
	}

	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}

	public List<String> getEstilos() {
		return this.estilos;
	}

	public void setEstilos(List<String> estilos) {
		this.estilos = estilos;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPaginaBase() {
		return this.paginaBase;
	}

	public void setPaginaBase(String paginaBase) {
		this.paginaBase = paginaBase;
	}

	public String getCabecalho() {
		return this.cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String getCorpo() {
		return this.corpo;
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

	public String getRodape() {
		return this.rodape;
	}

	public void setRodape(String rodape) {
		this.rodape = rodape;
	}
}