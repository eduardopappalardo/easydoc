package davidsolutions.easydoc.servico.entidade;

import java.io.InputStream;

public class Arquivo {

	private Integer codigoArquivo;
	private Documento documento;
	private String nomeOriginal;
	private String caminho;
	private String descricao;
	private InputStream conteudo;

	public Integer getCodigoArquivo() {
		return this.codigoArquivo;
	}

	public void setCodigoArquivo(Integer codigoArquivo) {
		this.codigoArquivo = codigoArquivo;
	}

	public Documento getDocumento() {
		return this.documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public String getNomeOriginal() {
		return this.nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public String getCaminho() {
		return this.caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public InputStream getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(InputStream conteudo) {
		this.conteudo = conteudo;
	}
}