package davidsolutions.caixaferramentas.utilidade;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	private Properties properties = new Properties();

	public PropertiesUtil(Class<? extends Enum<?>> classe) {
		try {
			this.properties.load(classe.getResourceAsStream(classe.getSimpleName().toLowerCase() + ".properties"));
		}
		catch (IOException excecao) {
			throw new RuntimeException(excecao);
		}
	}

	public String obterValor(String chave) {
		return this.properties.getProperty(chave);
	}

	public String obterValor(String chave, Object... parametros) {
		String valor = this.obterValor(chave);

		for (int cont = 0; cont < parametros.length; cont++) {
			valor = valor.replaceFirst("\\{" + cont + "\\}", parametros[cont].toString());
		}
		return valor;
	}

	public Integer obterValorInteiro(String chave) {
		return Integer.valueOf(this.obterValor(chave));
	}

	public Boolean obterValorBooleano(String chave) {
		return Boolean.valueOf(this.obterValor(chave));
	}
}