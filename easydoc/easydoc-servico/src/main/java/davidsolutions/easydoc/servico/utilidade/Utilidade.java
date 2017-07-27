package davidsolutions.easydoc.servico.utilidade;

import davidsolutions.caixaferramentas.utilidade.EmailUtil;
import davidsolutions.caixaferramentas.utilidade.EmailUtil.Email;
import davidsolutions.easydoc.servico.constante.Configuracao;

public class Utilidade {

	private Utilidade() {
	}

	public static void enviarEmail(String assunto, String conteudo, String... destinatarios) throws Exception {
		Email email = new Email();
		email.setEnderecoSmtp(Configuracao.SMTP_HOST.obterValor());
		email.setPortaSmtp(Configuracao.SMTP_PORTA.obterValorInteiro());
		email.setUsuarioSmtp(Configuracao.SMTP_USUARIO.obterValor());
		email.setSenhaSmtp(Configuracao.SMTP_SENHA.obterValor());
		email.setConexaoSsl(Configuracao.SMTP_SSL.obterValorBooleano());
		email.setEmissor(Configuracao.EMAIL_REMETENTE.obterValor());
		email.setAssunto(assunto);
		email.setMensagem(conteudo);

		for (String destinatario : destinatarios) {
			email.adicionarDestinatario(destinatario);
		}
		EmailUtil.enviarEmail(email);
	}
}