package davidsolutions.caixaferramentas.servico;

import java.util.ArrayList;
import java.util.List;

import davidsolutions.caixaferramentas.constante.Dicionario;
import davidsolutions.caixaferramentas.validacao.Mensagem;

public class RespostaServico<T> {

	private T dados;
	private List<Mensagem> mensagensAlerta = new ArrayList<Mensagem>();
	private List<Mensagem> mensagensSucesso = new ArrayList<Mensagem>();

	public RespostaServico() {
		this(null);
	}

	public RespostaServico(T dados) {
		this.dados = dados;
		this.mensagensSucesso.add(new Mensagem(Dicionario.OPERACAO_EXECUTADA_SUCESSO.obterValor()));

		if (dados instanceof List && ((List<?>) dados).isEmpty()) {
			this.mensagensAlerta.add(new Mensagem(Dicionario.NENHUM_REGISTRO_ENCONTRADO.obterValor()));
		}
	}

	public T getDados() {
		return this.dados;
	}

	public List<Mensagem> getMensagensAlerta() {
		return this.mensagensAlerta;
	}

	public List<Mensagem> getMensagensSucesso() {
		return this.mensagensSucesso;
	}
}