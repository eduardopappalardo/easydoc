package davidsolutions.caixaferramentas.constante;

import davidsolutions.caixaferramentas.utilidade.PropertiesUtil;

public enum Dicionario {

	VALIDACAO_DADOS_PARAMETRO_NULO,
	VALIDACAO_DADOS_VALOR_OBRIGATORIO,
	VALIDACAO_DADOS_TAMANHO_MINIMO_STRING,
	VALIDACAO_DADOS_TAMANHO_MAXIMO_STRING,
	VALIDACAO_DADOS_TAMANHO_MINIMO_COLECAO,
	VALIDACAO_DADOS_TAMANHO_MAXIMO_COLECAO,
	VALIDACAO_DADOS_TAMANHO_MINIMO_NUMERICO,
	VALIDACAO_DADOS_TAMANHO_MAXIMO_NUMERICO,
	VALIDACAO_DADOS_EMAIL_INVALIDO,
	VALIDACAO_DADOS_CPF_INVALIDO,
	VALIDACAO_DADOS_CNPJ_INVALIDO,
	VALIDACAO_DADOS_DATA_INVALIDA,
	VALIDACAO_DADOS_NUMERO_INVALIDO,
	VALIDACAO_DADOS_VALOR_INVALIDO,
	VALIDACAO_DADOS_VALOR_NAO_CADASTRADO,
	VALIDACAO_DADOS_VALOR_CADASTRADO,
	REGISTRO_NAO_ENCONTRADO,
	NENHUM_REGISTRO_ENCONTRADO,
	OPERACAO_EXECUTADA_SUCESSO,
	OPERACAO_EXECUTADA_FALHA;

	private static PropertiesUtil propertiesUtil = new PropertiesUtil(Dicionario.class);

	public String obterValor() {
		return propertiesUtil.obterValor(this.name());
	}

	public String obterValor(Object... parametros) {
		return propertiesUtil.obterValor(this.name(), parametros);
	}

	@Override
	public String toString() {
		return this.obterValor();
	}
}