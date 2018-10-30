package davidsolutions.caixaferramentas.validacao;

public class Mensagem {

	private String identificacaoCampo;
	private String mensagem;

	public Mensagem(String mensagem) {
		this(null, mensagem);
	}

	public Mensagem(String identificacaoCampo, String mensagem) {
		this.identificacaoCampo = identificacaoCampo;
		this.mensagem = mensagem;
	}

	public String getIdentificacaoCampo() {
		return this.identificacaoCampo;
	}

	public String getMensagem() {
		return this.mensagem;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mensagem [identificacaoCampo=");
		builder.append(identificacaoCampo);
		builder.append(", mensagem=");
		builder.append(mensagem);
		builder.append("]");
		return builder.toString();
	}
}