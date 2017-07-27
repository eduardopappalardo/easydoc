package davidsolutions.caixaferramentas.validacao;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Mensagem> mensagens = new ArrayList<Mensagem>();

	public ValidacaoException(Mensagem mensagem) {
		super(mensagem.getMensagem());
		this.adicionarMensagem(mensagem);
	}

	ValidacaoException() {
	}

	public List<Mensagem> obterMensagens() {
		return this.mensagens;
	}

	void adicionarMensagem(Mensagem mensagem) {
		this.mensagens.add(mensagem);
	}
}