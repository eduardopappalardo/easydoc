package davidsolutions.easydoc.servico.entidade;

import java.util.List;

public class Documento {

	private Integer codigoDocumento;
	private TipoDocumento tipoDocumento;
	private List<Indice> indices;
	private List<Arquivo> arquivos;

	public Integer getCodigoDocumento() {
		return this.codigoDocumento;
	}

	public void setCodigoDocumento(Integer codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<Indice> getIndices() {
		return this.indices;
	}

	public void setIndices(List<Indice> indices) {
		this.indices = indices;
	}

	public List<Arquivo> getArquivos() {
		return this.arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}
}