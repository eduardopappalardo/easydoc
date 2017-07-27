package davidsolutions.easydoc.servico.entidade;

import java.util.Date;
import java.util.List;

public class Usuario {

	private Integer codigoUsuario;
	private Cliente cliente;
	private TipoUsuario tipoUsuario;
	private String nome;
	private String login;
	private String senha;
	private String email;
	private Date dataAlteracaoSenha;
	private Date dataCadastro;
	private Boolean ativo;
	private Boolean necessitaAlteracaoSenha;
	private List<Grupo> grupos;

	public Usuario() {
	}

	public Usuario(Integer codigoUsuario) {
		this.setCodigoUsuario(codigoUsuario);
	}

	public Integer getCodigoUsuario() {
		return this.codigoUsuario;
	}

	public void setCodigoUsuario(Integer codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoUsuario getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataAlteracaoSenha() {
		return this.dataAlteracaoSenha;
	}

	public void setDataAlteracaoSenha(Date dataAlteracaoSenha) {
		this.dataAlteracaoSenha = dataAlteracaoSenha;
	}

	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getNecessitaAlteracaoSenha() {
		return this.necessitaAlteracaoSenha;
	}

	public void setNecessitaAlteracaoSenha(Boolean necessitaAlteracaoSenha) {
		this.necessitaAlteracaoSenha = necessitaAlteracaoSenha;
	}

	public List<Grupo> getGrupos() {
		return this.grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.codigoUsuario == null) ? 0 : this.codigoUsuario.hashCode());
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
		Usuario other = (Usuario) obj;
		if (this.codigoUsuario == null) {
			if (other.codigoUsuario != null)
				return false;
		}
		else if (!this.codigoUsuario.equals(other.codigoUsuario))
			return false;
		return true;
	}
}