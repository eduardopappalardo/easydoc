package davidsolutions.caixaferramentas.validacao;

import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;

public abstract class Validacao {

	private SituacaoValidacao situacaoValidacao = SituacaoValidacao.NAO_PROCESSADO;
	private boolean dependeValidacaoAnterior = false;
	private Integer identificacaoValidacao;
	private Integer identificacaoValidacaoDependencia;

	public abstract void validar() throws ValidacaoException, FalhaExecucaoException;

	SituacaoValidacao getSituacaoValidacao() {
		return this.situacaoValidacao;
	}

	void setSituacaoValidacao(SituacaoValidacao situacaoValidacao) {
		this.situacaoValidacao = situacaoValidacao;
	}

	boolean getDependeValidacaoAnterior() {
		return this.dependeValidacaoAnterior;
	}

	void setDependeValidacaoAnterior(boolean dependeValidacaoAnterior) {
		this.dependeValidacaoAnterior = dependeValidacaoAnterior;
	}

	Integer getIdentificacaoValidacao() {
		return this.identificacaoValidacao;
	}

	void setIdentificacaoValidacao(Integer identificacaoValidacao) {
		this.identificacaoValidacao = identificacaoValidacao;
	}

	Integer getIdentificacaoValidacaoDependencia() {
		return this.identificacaoValidacaoDependencia;
	}

	void setIdentificacaoValidacaoDependencia(Integer identificacaoValidacaoDependencia) {
		this.identificacaoValidacaoDependencia = identificacaoValidacaoDependencia;
	}

	enum SituacaoValidacao {
		NAO_PROCESSADO,
		PROCESSADO_SUCESSO,
		PROCESSADO_ERRO;
	}
}