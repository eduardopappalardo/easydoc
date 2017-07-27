package davidsolutions.caixaferramentas.servico;

import davidsolutions.caixaferramentas.constante.Dicionario;

public class FalhaExecucaoException extends Exception {

	private static final long serialVersionUID = 1L;

	public FalhaExecucaoException(Throwable excecao) {
		super(Dicionario.OPERACAO_EXECUTADA_FALHA.obterValor(), excecao);
		excecao.printStackTrace();
	}
}