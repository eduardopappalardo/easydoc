package davidsolutions.caixaferramentas.web;

import java.util.ArrayList;
import java.util.List;

import davidsolutions.caixaferramentas.servico.RespostaServico;
import davidsolutions.caixaferramentas.validacao.Mensagem;
import davidsolutions.caixaferramentas.validacao.ValidacaoException;

public class MensagemUsuario {

	private List<Mensagem> mensagensErro = new ArrayList<Mensagem>();
	private List<Mensagem> mensagensAlerta = new ArrayList<Mensagem>();
	private List<Mensagem> mensagensSucesso = new ArrayList<Mensagem>();

	public List<Mensagem> getMensagensErro() {
		return this.mensagensErro;
	}

	public List<Mensagem> getMensagensAlerta() {
		return this.mensagensAlerta;
	}

	public List<Mensagem> getMensagensSucesso() {
		return this.mensagensSucesso;
	}

	public void adicionarMensagens(Exception excecao) {

		if (excecao instanceof ValidacaoException) {
			this.mensagensErro.addAll(((ValidacaoException) excecao).obterMensagens());
		}
		else {
			this.mensagensErro.add(new Mensagem(excecao.getMessage()));
		}
	}

	public void adicionarMensagens(RespostaServico<?> respostaServico) {
		this.adicionarMensagensAlerta(respostaServico);
		this.adicionarMensagensSucesso(respostaServico);
	}

	public void adicionarMensagensAlerta(RespostaServico<?> respostaServico) {
		this.mensagensAlerta.addAll(respostaServico.getMensagensAlerta());
	}

	public void adicionarMensagensSucesso(RespostaServico<?> respostaServico) {
		this.mensagensSucesso.addAll(respostaServico.getMensagensSucesso());
	}
}