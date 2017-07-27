package davidsolutions.caixaferramentas.utilidade;

import java.util.List;

public class ListaUtil {

	private ListaUtil() {
	}

	public static <T> T obterPrimeiroElemento(List<T> lista) {
		return (lista != null && !lista.isEmpty() ? lista.get(0) : null);
	}
}