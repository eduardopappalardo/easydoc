package davidsolutions.easydoc.servico.entidade;

import java.util.List;

public class Grupo {

	private Integer codigoGrupo;
	private Cliente cliente;
	private String nome;
	private Boolean ativo;
	private List<Usuario> usuarios;

	public Grupo() {
	}

	public Grupo(Integer codigoGrupo) {
		this.setCodigoGrupo(codigoGrupo);
	}

	public Integer getCodigoGrupo() {
		return this.codigoGrupo;
	}

	public void setCodigoGrupo(Integer codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
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

	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.codigoGrupo == null) ? 0 : this.codigoGrupo.hashCode());
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
		Grupo other = (Grupo) obj;
		if (this.codigoGrupo == null) {
			if (other.codigoGrupo != null)
				return false;
		}
		else if (!this.codigoGrupo.equals(other.codigoGrupo))
			return false;
		return true;
	}
}