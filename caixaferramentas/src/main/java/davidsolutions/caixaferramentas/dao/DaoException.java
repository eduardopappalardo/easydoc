package davidsolutions.caixaferramentas.dao;

public class DaoException extends Exception {

	private static final long serialVersionUID = 1L;

	public DaoException(String mensagem) {
		super(mensagem);
	}

	public DaoException(Throwable excecao) {
		super(excecao);
	}
}