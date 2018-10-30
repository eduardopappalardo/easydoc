package davidsolutions.easydoc.servico.regranegocio;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.dao.IndiceDao;
import davidsolutions.easydoc.servico.entidade.Indice;

public class IndiceRN extends Servico<IndiceDao> {

	public IndiceRN() {
		super(new GerenciadorConexaoProducao());
	}

	public IndiceRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	void validar(List<Indice> indices) throws ValidacaoException, FalhaExecucaoException {

		if (!ValidacaoUtil.estaVazio(indices)) {
			Validador validador = new Validador();
			Indice indice = null;

			for (int posicao = 0; posicao < indices.size(); posicao++) {
				indice = indices.get(posicao);
				validador.adicionarValidacao("nomeIndice" + posicao, "Nome do índice", indice.getNome())
						.validarPreenchimento().validarTamanho(1, 100);
				validador.adicionarValidacao("tipoIndice" + posicao, "Tipo de índice", indice.getTipoIndice())
						.validarPreenchimento();
				validador.adicionarValidacao("preenchimentoObrigatorio" + posicao, "Preenchimento obrigatório",
						indice.getPreenchimentoObrigatorio()).validarPreenchimento();
			}
			validador.validar();
		}
	}
}