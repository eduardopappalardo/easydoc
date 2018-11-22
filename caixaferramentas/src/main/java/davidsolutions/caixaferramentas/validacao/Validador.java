package davidsolutions.caixaferramentas.validacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import davidsolutions.caixaferramentas.constante.Dicionario;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;

public class Validador {

	private List<Parametro<?, ?>> parametros = new ArrayList<Parametro<?, ?>>();

	public ParametroString adicionarValidacao(String identificacaoParametro, String nomeParametro,
			String valorParametro) {
		ParametroString parametro = new ParametroString(identificacaoParametro, nomeParametro, valorParametro);
		this.parametros.add(parametro);
		return parametro;
	}

	public ParametroNumerico adicionarValidacao(String identificacaoParametro, String nomeParametro,
			Number valorParametro) {
		ParametroNumerico parametro = new ParametroNumerico(identificacaoParametro, nomeParametro, valorParametro);
		this.parametros.add(parametro);
		return parametro;
	}

	public ParametroLista adicionarValidacao(String identificacaoParametro, String nomeParametro,
			List<?> valorParametro) {
		ParametroLista parametro = new ParametroLista(identificacaoParametro, nomeParametro, valorParametro);
		this.parametros.add(parametro);
		return parametro;
	}

	public ParametroGenerico adicionarValidacao(String identificacaoParametro, String nomeParametro,
			Object valorParametro) {
		ParametroGenerico parametro = new ParametroGenerico(identificacaoParametro, nomeParametro, valorParametro);
		this.parametros.add(parametro);
		return parametro;
	}

	public Validador adicionarValidacao(Validacao validacao) {
		// this.validacoes.add(validacao);
		return this;
	}

	public void validarNulo(String nomeParametro, Object valorParametro) throws ValidacaoException {

		if (valorParametro == null) {
			throw new ValidacaoException(
					new Mensagem(Dicionario.VALIDACAO_DADOS_PARAMETRO_NULO.obterValor(nomeParametro)));
		}
	}

	public void validar() throws ValidacaoException, FalhaExecucaoException {
		ValidacaoException validacaoException = new ValidacaoException();

		for (Parametro parametro : parametros) {

			for (Object validacaoTemp : parametro.getValidacoes()) {
				Validacao validacao = (Validacao) validacaoTemp;
				try {
					validacao.validar();

				} catch (ValidacaoException excecao) {
					validacaoException.obterMensagens().addAll(excecao.obterMensagens());

				} catch (FalhaExecucaoException excecao) {
					throw excecao;

				} catch (Exception excecao) {
					throw new FalhaExecucaoException(excecao);
				}
			}
		}
		this.parametros.clear();

		if (!validacaoException.obterMensagens().isEmpty()) {
			throw validacaoException;
		}
	}

	private abstract class Parametro<T1, T2 extends Parametro<?, ?>> {

		protected String identificacao;
		protected String nome;
		protected T1 valor;
		protected List<Validacao> validacoes = new ArrayList<Validacao>();

		private Parametro(String identificacao, String nome, T1 valor) {
			this.identificacao = identificacao;
			this.nome = nome;
			this.valor = valor;
		}

		protected List<Validacao> getValidacoes() {
			return this.validacoes;
		}

		public T2 validarPreenchimento() {
			this.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (estaVazio()) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_VALOR_OBRIGATORIO.obterValor(nome)));
					}
				}
			});
			return (T2) this;
		}

		public T2 validarCadastrado(final ExecutorConsulta executorConsulta) {
			this.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException, FalhaExecucaoException {
					boolean estaCadastrado;
					try {
						estaCadastrado = executorConsulta.estaCadastrado();
					} catch (Exception excecao) {
						throw new FalhaExecucaoException(excecao);
					}
					if (!estaCadastrado) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_VALOR_NAO_CADASTRADO.obterValor(nome)));
					}
				}
			});
			return (T2) this;
		}

		public T2 validarNaoCadastrado(final ExecutorConsulta executorConsulta) {
			this.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException, FalhaExecucaoException {
					boolean estaCadastrado;
					try {
						estaCadastrado = executorConsulta.estaCadastrado();
					} catch (Exception excecao) {
						throw new FalhaExecucaoException(excecao);
					}
					if (estaCadastrado) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_VALOR_CADASTRADO.obterValor(nome)));
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
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio()) {

						if (obterTamanho().compareTo(tamanhoMinimo) == -1) {
							throw new ValidacaoException(new Mensagem(identificacao,
									obterMensagemTamanhoMinimo().obterValor(nome, tamanhoMinimo)));
						} else if (obterTamanho().compareTo(tamanhoMaximo) == 1) {
							throw new ValidacaoException(new Mensagem(identificacao,
									obterMensagemTamanhoMaximo().obterValor(nome, tamanhoMaximo)));
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
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarEmail(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_EMAIL_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarCpf() {
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarCpf(valor)) {
						throw new ValidacaoException(
								new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_CPF_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarCnpj() {
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarCnpj(valor)) {
						throw new ValidacaoException(
								new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_CNPJ_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarData() {
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarData(valor)) {
						throw new ValidacaoException(
								new Mensagem(identificacao, Dicionario.VALIDACAO_DADOS_DATA_INVALIDA.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarNumero() {
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarNumero(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_NUMERO_INVALIDO.obterValor(nome)));
					}
				}
			});
			return this;
		}

		public ParametroString validarNumeroInteiro() {
			super.validacoes.add(new Validacao() {

				public void validar() throws ValidacaoException {

					if (!estaVazio() && !ValidacaoUtil.validarNumeroInteiro(valor)) {
						throw new ValidacaoException(new Mensagem(identificacao,
								Dicionario.VALIDACAO_DADOS_NUMERO_INVALIDO.obterValor(nome)));
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