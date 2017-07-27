package davidsolutions.easydoc.servico.entidade;

import java.util.List;

public class Cliente {

	private Integer codigoCliente;
	private String nome;
	private Boolean ativo;
	private List<Grupo> grupos;

	public Cliente() {
	}

	public Cliente(Integer codigoCliente) {
		this.setCodigoCliente(codigoCliente);
	}

	public Integer getCodigoCliente() {
		return this.codigoCliente;
	}

	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
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

	public List<Grupo> getGrupos() {
		return this.grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
}