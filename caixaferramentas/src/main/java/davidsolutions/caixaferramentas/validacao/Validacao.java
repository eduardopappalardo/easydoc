package davidsolutions.caixaferramentas.validacao;

import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;

public interface Validacao {

	public void validar() throws ValidacaoException, FalhaExecucaoException;

}