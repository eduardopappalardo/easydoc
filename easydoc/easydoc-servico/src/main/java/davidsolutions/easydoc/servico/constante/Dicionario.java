package davidsolutions.easydoc.servico.constante;

import davidsolutions.caixaferramentas.utilidade.PropertiesUtil;

public enum Dicionario {

	USUARIO_SENHA_EXPIRADA,
	USUARIO_SENHA_GERADA,
	CODIGO_USUARIO,
	CODIGO_CLIENTE,
	CODIGO_GRUPO,
	CODIGO_TIPO_DOCUMENTO,
	CODIGO_INDICE,
	CODIGO_DOCUMENTO;

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