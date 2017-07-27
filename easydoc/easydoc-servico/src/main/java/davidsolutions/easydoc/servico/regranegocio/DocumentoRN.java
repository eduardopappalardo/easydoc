package davidsolutions.easydoc.servico.regranegocio;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.Validacao;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.caixaferramentas.validacao.Validador.ParametroString;
import davidsolutions.easydoc.servico.constante.Dicionario;
import davidsolutions.easydoc.servico.dao.DocumentoDao;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.entidade.Arquivo;
import davidsolutions.easydoc.servico.entidade.Documento;
import davidsolutions.easydoc.servico.entidade.Indice;
import davidsolutions.easydoc.servico.entidade.TipoDocumento;

public class DocumentoRN extends Servico<DocumentoDao> {

	private static final String CAMINHO_REPOSITORIO_ARQUIVO = "F:/Desenvolvimento/apache-tomcat-7.0.54/webapps/easydoc-web/arquivos";

	public DocumentoRN() {
		super(new GerenciadorConexaoProducao());
	}

	public DocumentoRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	public RespostaServico<Void> adicionar(Documento documento) throws ValidacaoException, FalhaExecucaoException, RegistroInexistenteException, DaoException {
		this.validar(documento, false);
		try {
			super.iniciarTransacao();
			super.obterDao().adicionar(documento);

			for (Indice indice : documento.getIndices()) {
				if (!ValidacaoUtil.estaVazio(indice.getValores())) {
					for (String valor : indice.getValores()) {
						if (!ValidacaoUtil.estaVazio(valor)) {
							super.obterDao().adicionarDocumentoIndice(documento.getCodigoDocumento(), indice.getCodigoIndice(), valor);
						}
					}
				}
			}
			if (!ValidacaoUtil.estaVazio(documento.getArquivos())) {
				String caminhoArquivo = obterCaminhoArquivo(documento.getTipoDocumento());
				new File(caminhoArquivo).mkdirs();

				for (Arquivo arquivo : documento.getArquivos()) {
					arquivo.setDocumento(documento);
					arquivo.setCaminho(caminhoArquivo);
					super.obterDao().adicionarArquivo(arquivo);

					byte[] buffer = new byte[arquivo.getConteudo().available()];
					arquivo.getConteudo().read(buffer);
					FileOutputStream fileOutputStream = new FileOutputStream(new File(arquivo.getCaminho()));
					fileOutputStream.write(buffer);
					fileOutputStream.close();
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

	private void validar(final Documento documento, boolean alteracao) throws ValidacaoException, FalhaExecucaoException, RegistroInexistenteException, DaoException {
		Validador validador = new Validador();
		validador.validarNulo("documento", documento);

		if (alteracao) {
			validador.adicionarValidacao(null, Dicionario.CODIGO_DOCUMENTO.obterValor(), documento.getCodigoDocumento()).validarPreenchimento();
		}
		validador.adicionarValidacao(null, Dicionario.CODIGO_TIPO_DOCUMENTO.obterValor(), (documento.getTipoDocumento() != null ? documento.getTipoDocumento().getCodigoTipoDocumento() : null)).validarPreenchimento();

		if (documento.getTipoDocumento() != null && documento.getTipoDocumento().getCodigoTipoDocumento() != null) {
			TipoDocumento tipoDocumento = new TipoDocumentoRN(super.obterConexao()).consultarPorCodigo(documento.getTipoDocumento().getCodigoTipoDocumento()).getDados();
			documento.setTipoDocumento(tipoDocumento);
			List<Indice> todosIndices = documento.getTipoDocumento().getTodosIndices();

			for (Indice indice : todosIndices) {
				Indice indiceValor = null;
				String valor = null;
				int posicao = (documento.getIndices() != null ? documento.getIndices().indexOf(indice) : -1);

				if (posicao != -1) {
					indiceValor = documento.getIndices().get(posicao);

					if (!ValidacaoUtil.estaVazio(indiceValor.getValores())) {
						valor = indiceValor.getValores().get(0);
					}
				}
				ParametroString parametroString = validador.adicionarValidacao(("valor" + posicao), indice.getNome(), valor);

				if (indice.getPreenchimentoObrigatorio()) {
					parametroString.validarPreenchimento();
					parametroString.validarTamanho(1, 200).dependeValidacaoAnterior();
				}
				else {
					parametroString.validarTamanho(1, 200);
				}
				indice.getTipoIndice().adicionarValidacao(parametroString);
			}
		}
		validador.adicionarValidacao(null, "Arquivos", documento.getArquivos()).validarPreenchimento();
		validador.adicionarValidacao(new Validacao() {

			@Override
			public void validar() throws ValidacaoException, FalhaExecucaoException {
				new ArquivoRN().validar(documento.getArquivos());
			}
		}).dependeValidacaoAnterior();
		validador.validar();
	}

	private static String obterCaminhoArquivo(TipoDocumento tipoDocumento) {
		return CAMINHO_REPOSITORIO_ARQUIVO + File.separator + tipoDocumento.getCliente().getCodigoCliente() + File.separator + tipoDocumento.getCodigoTipoDocumento() + File.separator;
	}
}