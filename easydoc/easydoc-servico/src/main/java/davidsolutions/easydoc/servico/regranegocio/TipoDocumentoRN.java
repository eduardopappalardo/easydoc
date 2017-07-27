package davidsolutions.easydoc.servico.regranegocio;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.ExecutorConsulta;
import davidsolutions.caixaferramentas.validacao.Validacao;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.constante.Dicionario;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.dao.IndiceDao;
import davidsolutions.easydoc.servico.dao.TipoDocumentoDao;
import davidsolutions.easydoc.servico.entidade.Indice;
import davidsolutions.easydoc.servico.entidade.TipoDocumento;

public class TipoDocumentoRN extends Servico<TipoDocumentoDao> {

	public TipoDocumentoRN() {
		super(new GerenciadorConexaoProducao());
	}

	public TipoDocumentoRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	public RespostaServico<Void> adicionar(TipoDocumento tipoDocumento) throws ValidacaoException, FalhaExecucaoException {
		this.validar(tipoDocumento, false);
		try {
			super.iniciarTransacao();
			super.obterDao().adicionar(tipoDocumento);

			if (!ValidacaoUtil.estaVazio(tipoDocumento.getIndices())) {
				IndiceDao indiceDao = new IndiceDao(super.obterConexao());

				for (Indice indice : tipoDocumento.getIndices()) {

					if (indice.getCodigoIndice() == null) {
						indice.setTipoDocumento(tipoDocumento);
						indiceDao.adicionar(indice);
					}
				}
			}
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> alterar(TipoDocumento tipoDocumento) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		this.validar(tipoDocumento, true);
		try {
			IndiceDao indiceDao = new IndiceDao(super.obterConexao());
			List<Indice> indicesAtuais = indiceDao.consultarPorCodigoTipoDocumento(tipoDocumento.getCodigoTipoDocumento());

			super.iniciarTransacao();
			super.obterDao().alterar(tipoDocumento);

			for (Indice indice : tipoDocumento.getIndices()) {

				if (indice.getCodigoIndice() == null) {
					indice.setTipoDocumento(tipoDocumento);
					indiceDao.adicionar(indice);
				}
				else if (indicesAtuais.contains(indice)) {
					// TODO: Verificar consistência da base
					indiceDao.alterar(indice);
				}
			}
			for (Indice indice : indicesAtuais) {

				if (!tipoDocumento.getIndices().contains(indice)) {
					// TODO: Verificar consistência da base
					indiceDao.excluir(indice.getCodigoIndice());
				}
			}
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (RegistroInexistenteException excecao) {
			super.cancelarTransacao();
			throw excecao;
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> excluir(Integer codigoTipoDocumento) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_TIPO_DOCUMENTO.obterValor(), codigoTipoDocumento);
		try {
			IndiceDao indiceDao = new IndiceDao(super.obterConexao());

			super.iniciarTransacao();
			// TODO: Verificar consistência da base
			indiceDao.excluirPorCodigoTipoDocumento(codigoTipoDocumento);
			super.obterDao().excluir(codigoTipoDocumento);
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<TipoDocumento> consultarPorCodigo(Integer codigoTipoDocumento) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_TIPO_DOCUMENTO.obterValor(), codigoTipoDocumento);
		try {
			IndiceDao indiceDao = new IndiceDao(super.obterConexao());
			TipoDocumento tipoDocumento = super.obterDao().consultarPorCodigo(codigoTipoDocumento);
			tipoDocumento.setIndices(indiceDao.consultarPorCodigoTipoDocumento(codigoTipoDocumento));

			if (tipoDocumento.getTipoDocumentoPai() != null && tipoDocumento.getTipoDocumentoPai().getCodigoTipoDocumento() != null) {
				TipoDocumento tipoDocumentoPai = this.consultarPorCodigo(tipoDocumento.getTipoDocumentoPai().getCodigoTipoDocumento()).getDados();
				tipoDocumento.setTipoDocumentoPai(tipoDocumentoPai);
				tipoDocumento.getIndicesHerdados().addAll(tipoDocumentoPai.getIndicesHerdados());
				tipoDocumento.getIndicesHerdados().addAll(tipoDocumentoPai.getIndices());
			}
			return new RespostaServico<TipoDocumento>(tipoDocumento);
		}
		catch (DaoException excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<TipoDocumento>> consultar(TipoDocumento tipoDocumentoFiltro, Paginacao paginacao) throws ValidacaoException, FalhaExecucaoException {

		if (tipoDocumentoFiltro != null) {
			Validador validador = new Validador();
			validador.adicionarValidacao("nome", "Nome", tipoDocumentoFiltro.getNome()).validarTamanho(1, 100);
			validador.validar();
		}
		try {
			List<TipoDocumento> lista = this.obterDao().consultar(tipoDocumentoFiltro, paginacao);
			return new RespostaServico<List<TipoDocumento>>(lista);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<TipoDocumento>> listarPorCodigoCliente(Integer codigoCliente) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_CLIENTE.obterValor(), codigoCliente);
		try {
			List<TipoDocumento> lista = this.obterDao().listarPorCodigoCliente(codigoCliente);
			return new RespostaServico<List<TipoDocumento>>(lista);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	private void validar(final TipoDocumento tipoDocumento, boolean alteracao) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.validarNulo("tipoDocumento", tipoDocumento);

		if (alteracao) {
			validador.adicionarValidacao(null, Dicionario.CODIGO_TIPO_DOCUMENTO.obterValor(), tipoDocumento.getCodigoTipoDocumento()).validarPreenchimento();
		}
		else {
			validador.adicionarValidacao("cliente", "Cliente", (tipoDocumento.getCliente() != null ? tipoDocumento.getCliente().getCodigoCliente() : null)).validarPreenchimento().validarCadastrado(new ExecutorConsulta() {

				public boolean estaCadastrado() throws Exception {
					return new ClienteRN(obterConexao()).existeCodigo(tipoDocumento.getCliente().getCodigoCliente()).getDados();
				}
			}).dependeValidacaoAnterior();
		}
		validador.adicionarValidacao("nome", "Nome", tipoDocumento.getNome()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao(null, "Índices", tipoDocumento.getIndices()).validarPreenchimento();
		validador.adicionarValidacao(new Validacao() {

			@Override
			public void validar() throws ValidacaoException, FalhaExecucaoException {
				new IndiceRN().validar(tipoDocumento.getIndices());
			}
		}).dependeValidacaoAnterior();

		/*
		 * if (tipoDocumento.getTipoDocumentoPai() != null &&
		 * tipoDocumento.getTipoDocumentoPai().getCodigoTipoDocumento() != null)
		 * { validador.adicionarValidacao(new Validacao() {
		 * 
		 * @Override public void validar() throws ValidacaoException,
		 * FalhaExecucaoException { TipoDocumento tipoDocumentoPai = null; try {
		 * tipoDocumentoPai =
		 * obterDao().consultarPorCodigo(tipoDocumento.getTipoDocumentoPai
		 * ().getCodigoTipoDocumento()); } catch (Exception excecao) { throw new
		 * FalhaExecucaoException(excecao); } if
		 * (tipoDocumentoPai.getTipoDocumentoPai() != null &&
		 * tipoDocumentoPai.getTipoDocumentoPai().getCodigoTipoDocumento() !=
		 * null) { throw new ValidacaoException(new Mensagem("tipoDocumentoPai",
		 * "O Tipo de documento pai possui herança e somente 1 nível de herança é permitido."
		 * )); } } }); }
		 */
		validador.validar();
	}
}