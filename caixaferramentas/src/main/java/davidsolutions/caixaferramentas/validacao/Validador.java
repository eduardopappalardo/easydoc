package davidsolutions.caixaferramentas.validacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import davidsolutions.caixaferramentas.constante.Dicionario;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.validacao.Validacao.SituacaoValidacao;

public class Validador {

	private List<Validacao> validacoes = new ArrayList<Validacao>();
	private Map<Integer, Validacao> validacoesIdentificadas = new HashMap<Integer, Validacao>();

	public ParametroString adicionarValidacao(String identificacaoParametro, String nomeParametro, String valorParametro) {
		return new ParametroString(identificacaoParametro, nomeParametro, valorParametro);
	}

	public ParametroNumerico adicionarValidacao(String identificacaoParametro, String nomeParametro, Number valorParametro) {
		return new ParametroNumerico(identificacaoParametro, nomeParametro, valorParametro);
	}

	public ParametroLista adicionarValidacao(String identificacaoParametro, String nomeParametro, List<?> valorParametro) {
		return new ParametroLista(identificacaoParametro, nomeParametro, valorParametro);
	}

	public ParametroGenerico adicionarValidacao(String identificacaoParametro, String nomeParametro, Object valorParametro) {
		return new ParametroGenerico(identificacaoParametro, nomeParametro, valorParametro);
	}

	public Validador adicionarValidacao(Validacao validacao) {
		this.validacoes.add(validacao);
		return this;
	}

	public Validador dependeValidacaoAnterior() {

		if (!this.validacoes.isEmpty()) {
			Validacao validacao = this.validacoes.get(this.validacoes.size() - 1);
			validacao.setDependeValidacaoAnterior(true);
		}
		return this;
	}

	public Validador identificacaoValidacao(Integer identificacaoValidacao) {

		if (!this.validacoes.isEmpty()) {
			Validacao validacao = this.validacoes.get(this.validacoes.size() - 1);
			validacao.setIdentificacaoValidacao(identificacaoValidacao);
			this.validacoesIdentificadas.put(identificacaoValidacao, validacao);
		}
		return this;
	}

	public Validador dependeValidacaoIdentificada(Integer identificacaoValidacaoDependencia) {

		if (!this.validacoes.isEmpty()) {
			Validacao validacao = this.validacoes.get(this.validacoes.size() - 1);
			validacao.setIdentificacaoValidacaoDependencia(identificacaoValidacaoDependencia);
		}
		return this;
	}

	public void validarNulo(String nomeParametro, Object valorParametro) throws ValidacaoException {

		if (valorParametro == null) {
			throw new ValidacaoException(new Mensagem(Dicionario.VALIDACAO_DADOS_PARAMETRO_NULO.obterValor(nomeParametro)));
		}
	}

	public void validar() throws ValidacaoException, FalhaExecucaoException {
		ValidacaoException validacaoException = new ValidacaoException();
		SituacaoValidacao situacaoValidacaoAnterior = null;
		Validacao validacaoDependencia = null;

		for (Validacao validacao : this.validacoes) {

			if (validacao.getDependeValidacaoAnterior() && (SituacaoValidacao.PROCESSADO_ERRO.equals(situacaoValidacaoAnterior) || SituacaoValidacao.NAO_PROCESSADO.equals(situacaoValidacaoAnterior))) {
				continue;
			}
			else if (validacao.getIdentificacaoValidacaoDependencia() != null) {
				validacaoDependencia = this.validacoesIdentificadas.get(validacao.getIdentificacaoValidacaoDependencia());

				if (validacaoDependencia != null && (validacaoDependencia.getSituacaoValidacao().equals(SituacaoValidacao.PROCESSADO_ERRO) || validacaoDependencia.getSituacaoValidacao().equals(SituacaoValidacao.NAO_PROCESSADO))) {
					continue;
				}
			}
			try {
				validacao.validar();
				validacao.setSituacaoValidacao(SituacaoValidacao.PROCESSADO_SUCESSO);
			}
			catch (ValidacaoException excecao) {
				validacaoException.obterMensagens().addAll(excecao.obterMensagens());
				validacao.setSituacaoValidacao(SituacaoValidacao.PROCESSADO_ERRO);
			}
			catch (FalhaExecucaoException excecao) {
				throw excecao;
			}
			catch (Exception excecao) {
				throw new FalhaExecucaoException(excecao);
			}
			situacaoValidacaoAnterior = validacao.getSituacaoValidacao();
		}
		this.validacoes.clear();
		this.validacoesIdentificadas.clear();

		if (!validacaoException.obterMensagens().isEmpty()) {
			throw validacaoException;
		}
	}

	private abstract class Parametro<T1, T2 extends Parametro<?, ?>> {

		protected String identificacao;
		protected String nome;
		protected T1 valor;

		private Parametro(String identificacao, String nome, T1 valor) {
			this.identificacao = identificacao;
			this.nome = nome;
			this.valor = valor;
		}

		public T2 dependeValidacaoAnterior() {
			Validador.this.dependeValidacaoAnterior();
			return (T2) this;
		}

		public T2 identificacaoValidacao(Integer identificacaoValidacao) {
			Validador.this.identificacaoValidacao(identificacaoValidacao);
			return (T2) this;
		}

		public T2 dependeValidacaoIdentificada(Integer identificacaoValidacaoDependencia) {
			Validador.this.dependeValidacaoIdentificada(identificacaoValidacaoDependencia);
			return (T2) this;
		}

		public T2 validarPreenchimento() {
			Validador.this.adicionarValidacao(new Validacao() {

				public void validar() throws ValidacaoException {

					if (estaVazio()) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_VALOR_OBRIGATORIO.obterValor(nome)));
					}
				}
			});
			return (T2) this;
		}

		public T2 validarCadastrado(final ExecutorConsulta executorConsulta) {
			Validador.this.adicionarValidacao(new Validacao() {

				public void validar() throws ValidacaoException, FalhaExecucaoException {
					boolean estaCadastrado;
					try {
						estaCadastrado = executorConsulta.estaCadastrado();
					}
					catch (Exception excecao) {
						throw new FalhaExecucaoException(excecao);
					}
					if (!estaCadastrado) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_VALOR_NAO_CADASTRADO.obterValor(nome)));
					}
				}
			});
			return (T2) this;
		}

		public T2 validarNaoCadastrado(final ExecutorConsulta executorConsulta) {
			Validador.this.adicionarValidacao(new Validacao() {

				public void validar() throws ValidacaoException, FalhaExecucaoException {
					boolean estaCadastrado;
					try {
						estaCadastrado = executorConsulta.estaCadastrado();
					}
					catch (Exception excecao) {
						throw new FalhaExecucaoException(excecao);
					}
					if (estaCadastrado) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_VALOR_CADASTRADO.obterValor(nome)));
					}
				}
			});
			return (T2) this;
		}

		protected boolean estaVazio() {
			return this.valor == null;
		}
	}

	private abstract class ParametroComum<T1, T2 extends ParametroComum<?, ?>> extends Parametro<T1, T2> {

		private ParametroComum(String identificacao, String nome, T1 valor) {
			super(identificacao, nome, valor);
		}

		public T2 validarTamanho(long tamanhoMinimo, long tamanhoMaximo) {
			return this.validarTamanho(new BigDecimal(tamanhoMinimo), new BigDecimal(tamanhoMaximo));
		}

		protected T2 validarTamanho(final BigDecimal tamanhoMinimo, final BigDecimal tamanhoMaximo) {
			Validador.this.adicionarValidacao(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio()) {

						if (obterTamanho().compareTo(tamanhoMinimo) == -1) {
							throw new ValidacaoException(new Mensagem(identificacao, obterMensagemTamanhoMinimo().obterValor(nome, tamanhoMinimo)));
						}
						else if (obterTamanho().compareTo(tamanhoMaximo) == 1) {
							throw new ValidacaoException(new Mensagem(identificacao, obterMensagemTamanhoMaximo().obterValor(nome, tamanhoMaximo)));
						}
					}
				}
			});
			return (T2) this;
		}

		protected abstract BigDecimal obterTamanho();

		protected abstract Dicionario obterMensagemTamanhoMinimo();

		protected abstract Dicionario obterMensagemTamanhoMaximo();

	}

	public class ParametroString extends ParametroComum<String, ParametroString> {

		private ParametroString(String identificacao, String nome, String valor) {
			super(identificacao, nome, valor);
		}

		public ParametroString validarEmail() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarEmail(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_EMAIL_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarCpf() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarCpf(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_CPF_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarCnpj() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarCnpj(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_CNPJ_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarData() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarData(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_DATA_INVALIDA.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarNumero() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarNumero(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_NUMERO_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarNumeroInteiro() {
			Validador.this.adicionarValidacao(new Validacao() {

				@Override
				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarNumeroInteiro(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_NUMERO_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		@Override
		protected boolean estaVazio() {
			return ValidacaoUtil.estaVazio(super.valor);
		}

		@Override
		protected BigDecimal obterTamanho() {
			return new BigDecimal(super.valor.length());
		}

		@Override
		protected Dicionario obterMensagemTamanhoMinimo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MINIMO_STRING;
		}

		@Override
		protected Dicionario obterMensagemTamanhoMaximo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MAXIMO_STRING;
		}
	}

	public class ParametroNumerico extends ParametroComum<Number, ParametroNumerico> {

		private ParametroNumerico(String identificacao, String nome, Number valor) {
			super(identificacao, nome, valor);
		}

		public ParametroNumerico validarTamanho(BigDecimal tamanhoMinimo, BigDecimal tamanhoMaximo) {
			return super.validarTamanho(tamanhoMinimo, tamanhoMaximo);
		}

		@Override
		protected BigDecimal obterTamanho() {
			return new BigDecimal(super.valor.toString());
		}

		@Override
		protected Dicionario obterMensagemTamanhoMinimo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MINIMO_NUMERICO;
		}

		@Override
		protected Dicionario obterMensagemTamanhoMaximo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MAXIMO_NUMERICO;
		}
	}

	public class ParametroLista extends ParametroComum<List<?>, ParametroLista> {

		private ParametroLista(String identificacao, String nome, List<?> valor) {
			super(identificacao, nome, valor);
		}

		@Override
		protected boolean estaVazio() {
			return ValidacaoUtil.estaVazio(super.valor);
		}

		@Override
		protected BigDecimal obterTamanho() {
			return new BigDecimal(super.valor.size());
		}

		@Override
		protected Dicionario obterMensagemTamanhoMinimo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MINIMO_COLECAO;
		}

		@Override
		protected Dicionario obterMensagemTamanhoMaximo() {
			return Dicionario.VALIDACAO_DADOS_TAMANHO_MAXIMO_COLECAO;
		}
	}

	public class ParametroGenerico extends Parametro<Object, ParametroGenerico> {

		private ParametroGenerico(String identificacao, String nome, Object valor) {
			super(identificacao, nome, valor);
		}
	}
}