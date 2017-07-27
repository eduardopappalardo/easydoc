package davidsolutions.caixaferramentas.reflection;

public class ReflectionException extends Exception {

	private static final long serialVersionUID = 1L;

	ReflectionException(String mensagem) {
		super(mensagem);
	}

	ReflectionException(String mensagem, Throwable excecao) {
		super(mensagem, excecao);
	}
}