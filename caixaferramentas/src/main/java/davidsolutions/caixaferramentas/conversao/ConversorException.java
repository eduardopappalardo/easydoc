package davidsolutions.caixaferramentas.conversao;

public class ConversorException extends Exception {

	private static final long serialVersionUID = 1L;

	ConversorException(String mensagem) {
		super(mensagem);
	}

	ConversorException(Throwable excecao) {
		super(excecao);
	}
}