package davidsolutions.easydoc.servico.regranegocio;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.Mensagem;
import davidsolutions.caixaferramentas.validacao.Validacao;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.dao.ArquivoDao;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.entidade.Arquivo;

public class ArquivoRN extends Servico<ArquivoDao> {

	public ArquivoRN() {
		super(new GerenciadorConexaoProducao());
	}

	public ArquivoRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	void validar(List<Arquivo> arquivos) throws ValidacaoException, FalhaExecucaoException {

		if (!ValidacaoUtil.estaVazio(arquivos)) {
			Validador validador = new Validador();

			for (int posicao = 0; posicao < arquivos.size(); posicao++) {
				final Arquivo arquivo = arquivos.get(posicao);
				final int posicaoTemp = posicao;
				validador.adicionarValidacao(("descricao" + posicaoTemp), "Descrição", arquivo.getDescricao())
						.validarTamanho(1, 100);
				validador.adicionarValidacao(null, "Nome do arquivo", arquivo.getNomeOriginal()).validarPreenchimento()
						.validarTamanho(1, 100);
				validador.adicionarValidacao(new Validacao() {

					public void validar() throws ValidacaoException, FalhaExecucaoException {
						try {
							if (arquivo.getConteudo() == null || arquivo.getConteudo().available() == 0) {
								throw new ValidacaoException(new Mensagem("conteudo" + posicaoTemp,
										"É obrigatória a escolha de um arquivo."));
							}
						} catch (IOException excecao) {
							throw new FalhaExecucaoException(excecao);
						}
					}
				});
			}
			validador.validar();
		}
	}
}