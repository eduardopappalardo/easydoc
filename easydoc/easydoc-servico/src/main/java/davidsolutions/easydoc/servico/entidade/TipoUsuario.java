package davidsolutions.easydoc.servico.entidade;

import davidsolutions.caixaferramentas.dao.BeanEnum;

public enum TipoUsuario implements BeanEnum {

	ADMINISTRADOR_SISTEMA(1, "Administrador sistema"),
	ADMINISTRADOR_CLIENTE(2, "Administrador cliente"),
	USUARIO(3, "Usuário");

	private Integer codigo;
	private String descricao;

	private TipoUsuario(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public String getDescricao() {
		return this.descricao;
	}
}