package davidsolutions.caixaferramentas.utilidade;

public class StringUtil {

	private StringUtil() {
	}

	public static String removerEspacos(String texto) {
		return (texto != null ? texto.trim().replaceAll(" {2,}", " ") : null);
	}
}