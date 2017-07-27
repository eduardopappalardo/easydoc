package davidsolutions.easydoc.servico.constante;

import davidsolutions.caixaferramentas.utilidade.PropertiesUtil;

public enum Configuracao {

	JNDI_CONEXAO_BANCO,
	URL_CONEXAO_BANCO,
	SMTP_HOST,
	SMTP_PORTA,
	SMTP_USUARIO,
	SMTP_SENHA,
	SMTP_SSL,
	EMAIL_REMETENTE,
	SENHA_QUANTIDADE_MINIMA_CARACTERES,
	SENHA_TEMPO_EXPIRACAO_EM_DIAS;

	private static PropertiesUtil propertiesUtil = new PropertiesUtil(Configuracao.class);

	public String obterValor() {
		return propertiesUtil.obterValor(this.name());
	}

	public Integer obterValorInteiro() {
		return propertiesUtil.obterValorInteiro(this.name());
	}

	public boolean obterValorBooleano() {
		return propertiesUtil.obterValorBooleano(this.name());
	}
}