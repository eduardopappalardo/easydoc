package davidsolutions.easydoc.servico.regranegocio;

import java.sql.Connection;
import java.util.List;

import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.dao.Paginacao;
import davidsolutions.caixaferramentas.dao.RegistroInexistenteException;
import davidsolutions.caixaferramentas.servico.FalhaExecucaoException;
import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.servico.Servico;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;
import davidsolutions.caixaferramentas.validacao.Validador;
import davidsolutions.easydoc.servico.constante.Dicionario;
import davidsolutions.easydoc.servico.dao.ClienteDao;
import davidsolutions.easydoc.servico.dao.GerenciadorConexaoProducao;
import davidsolutions.easydoc.servico.entidade.Cliente;

public class ClienteRN extends Servico<ClienteDao> {

	public ClienteRN() {
		super(new GerenciadorConexaoProducao());
	}

	public ClienteRN(Connection conexao) throws DaoException {
		super(conexao);
	}

	public RespostaServico<Void> adicionar(Cliente cliente) throws ValidacaoException, FalhaExecucaoException {
		this.validar(cliente, false);
		try {
			super.iniciarTransacao();
			this.obterDao().adicionar(cliente);
			super.confirmarTransacao();

			return new RespostaServico<Void>();
		}
		catch (Exception excecao) {
			super.cancelarTransacao();
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Void> alterar(Cliente cliente) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		this.validar(cliente, true);
		try {
			super.iniciarTransacao();
			this.obterDao().alterar(cliente);
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

	public RespostaServico<Cliente> consultarPorCodigo(Integer codigoCliente) throws ValidacaoException, RegistroInexistenteException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_CLIENTE.obterValor(), codigoCliente);
		try {
			Cliente cliente = this.obterDao().consultarPorCodigo(codigoCliente);
			cliente.setGrupos(new GrupoRN(super.obterConexao()).listarAtivosPorCodigoCliente(codigoCliente).getDados());
			return new RespostaServico<Cliente>(cliente);
		}
		catch (DaoException excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Cliente>> consultar(Cliente clienteFiltro, Paginacao paginacao) throws ValidacaoException, FalhaExecucaoException {

		if (clienteFiltro != null) {
			Validador validador = new Validador();
			validador.adicionarValidacao("nome", "Nome", clienteFiltro.getNome()).validarTamanho(1, 100);
			validador.validar();
		}
		try {
			List<Cliente> lista = this.obterDao().consultar(clienteFiltro, paginacao);
			return new RespostaServico<List<Cliente>>(lista);
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<Boolean> existeCodigo(Integer codigoCliente) throws ValidacaoException, FalhaExecucaoException {
		new Validador().validarNulo(Dicionario.CODIGO_CLIENTE.obterValor(), codigoCliente);
		Cliente cliente = new Cliente(codigoCliente);
		cliente.setAtivo(true);
		try {
			return new RespostaServico<Boolean>(this.obterDao().existeCodigo(cliente));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Cliente>> listarAtivos() throws FalhaExecucaoException {
		Cliente cliente = new Cliente();
		cliente.setAtivo(true);
		try {
			return new RespostaServico<List<Cliente>>(this.obterDao().listar(cliente));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	public RespostaServico<List<Cliente>> listar() throws FalhaExecucaoException {
		try {
			return new RespostaServico<List<Cliente>>(this.obterDao().listar(null));
		}
		catch (Exception excecao) {
			throw new FalhaExecucaoException(excecao);
		}
	}

	private void validar(Cliente cliente, boolean alteracao) throws ValidacaoException, FalhaExecucaoException {
		Validador validador = new Validador();
		validador.validarNulo("cliente", cliente);

		if (alteracao) {
			validador.adicionarValidacao(null, Dicionario.CODIGO_CLIENTE.obterValor(), cliente.getCodigoCliente()).validarPreenchimento();
		}
		validador.adicionarValidacao("nome", "Nome", cliente.getNome()).validarPreenchimento().validarTamanho(1, 100).dependeValidacaoAnterior();
		validador.adicionarValidacao("ativo", "Ativo", cliente.getAtivo()).validarPreenchimento();
		validador.validar();
	}
}