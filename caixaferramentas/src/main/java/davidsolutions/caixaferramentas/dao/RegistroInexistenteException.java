package davidsolutions.caixaferramentas.dao;

import davidsolutions.caixaferramentas.constante.Dicionario;

public class RegistroInexistenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public RegistroInexistenteException() {
		super(Dicionario.REGISTRO_NAO_ENCONTRADO.obterValor());
	}
}