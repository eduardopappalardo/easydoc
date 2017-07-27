package davidsolutions.easydoc.servico.entidade;

import java.util.ArrayList;
import java.util.List;

public class TipoDocumento {

	private Integer codigoTipoDocumento;
	private Cliente cliente;
	private String nome;
	private List<Indice> indices;
	private TipoDocumento tipoDocumentoPai;
	private List<Indice> indicesHerdados = new ArrayList<Indice>();

	public TipoDocumento() {
	}

	public TipoDocumento(Integer codigoTipoDocumento) {
		this.setCodigoTipoDocumento(codigoTipoDocumento);
	}

	public Integer getCodigoTipoDocumento() {
		return this.codigoTipoDocumento;
	}

	public void setCodigoTipoDocumento(Integer codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Indice> getIndices() {
		return this.indices;
	}

	public void setIndices(List<Indice> indices) {
		this.indices = indices;
	}

	public TipoDocumento getTipoDocumentoPai() {
		return this.tipoDocumentoPai;
	}

	public void setTipoDocumentoPai(TipoDocumento tipoDocumentoPai) {
		this.tipoDocumentoPai = tipoDocumentoPai;
	}

	public List<Indice> getIndicesHerdados() {
		return this.indicesHerdados;
	}

	public List<Indice> getTodosIndices() {
		List<Indice> todosIndices = new ArrayList<Indice>();

		if (this.indices != null) {
			todosIndices.addAll(this.indices);
		}
		todosIndices.addAll(this.indicesHerdados);
		return todosIndices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.codigoTipoDocumento == null) ? 0 : this.codigoTipoDocumento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoDocumento other = (TipoDocumento) obj;
		if (this.codigoTipoDocumento == null) {
			if (other.codigoTipoDocumento != null)
				return false;
		}
		else if (!this.codigoTipoDocumento.equals(other.codigoTipoDocumento))
			return false;
		return true;
	}
}