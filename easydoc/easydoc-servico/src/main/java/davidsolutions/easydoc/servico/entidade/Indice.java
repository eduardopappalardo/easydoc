package davidsolutions.easydoc.servico.entidade;

import java.util.List;

public class Indice {

	private Integer codigoIndice;
	private TipoDocumento tipoDocumento;
	private String nome;
	private TipoIndice tipoIndice;
	private Boolean preenchimentoObrigatorio;
	private List<String> valores;

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Indice() {
	}

	public Indice(Integer codigoIndice) {
		this.setCodigoIndice(codigoIndice);
	}

	public Integer getCodigoIndice() {
		return this.codigoIndice;
	}

	public void setCodigoIndice(Integer codigoIndice) {
		this.codigoIndice = codigoIndice;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoIndice getTipoIndice() {
		return this.tipoIndice;
	}

	public void setTipoIndice(TipoIndice tipoIndice) {
		this.tipoIndice = tipoIndice;
	}

	public Boolean getPreenchimentoObrigatorio() {
		return this.preenchimentoObrigatorio;
	}

	public void setPreenchimentoObrigatorio(Boolean preenchimentoObrigatorio) {
		this.preenchimentoObrigatorio = preenchimentoObrigatorio;
	}

	public List<String> getValores() {
		return this.valores;
	}

	public void setValores(List<String> valores) {
		this.valores = valores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.codigoIndice == null) ? 0 : this.codigoIndice.hashCode());
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
		Indice other = (Indice) obj;
		if (this.codigoIndice == null) {
			if (other.codigoIndice != null)
				return false;
		}
		else if (!this.codigoIndice.equals(other.codigoIndice))
			return false;
		return true;
	}
}