package davidsolutions.easydoc.servico.entidade;

import davidsolutions.caixaferramentas.dao.BeanEnum;
import davidsolutions.caixaferramentas.validacao.Validador.ParametroString;

public enum TipoIndice implements BeanEnum {

	TEXTO(1, "Texto", null) {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
		}
	},
	NUMERO_INTEIRO(2, "Número inteiro", "numeroInteiro") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarNumeroInteiro();
		}
	},
	DATA(3, "Data", "data") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarData();
		}
	},
	CPF(4, "CPF", "cpf") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarCpf();
		}
	},
	CNPJ(5, "CNPJ", "cnpj") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarCnpj();
		}
	},
	EMAIL(6, "E-mail", "email") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarEmail();
		}
	},
	NUMERO_FRACIONADO(7, "Número fracionado/Moeda", "numeroFracionado") {

		@Override
		public void adicionarValidacao(ParametroString parametroString) {
			parametroString.validarNumero();
		}
	};

	private Integer codigo;
	private String descricao;
	private String classe;

	private TipoIndice(Integer codigo, String descricao, String classe) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.classe = classe;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public String getClasse() {
		return this.classe;
	}

	public abstract void adicionarValidacao(ParametroString parametroString);
}