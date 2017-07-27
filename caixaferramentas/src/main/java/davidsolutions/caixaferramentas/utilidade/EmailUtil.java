package davidsolutions.caixaferramentas.utilidade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

public class EmailUtil {

	private EmailUtil() {
	}

	public static void enviarEmail(Email email) throws Exception {
		HtmlEmail htmlEmail = new HtmlEmail();
		htmlEmail.setFrom(email.getEmissor());
		htmlEmail.setSubject(email.getAssunto());

		for (String destinatario : email.getDestinatarios()) {
			htmlEmail.addTo(destinatario);
		}
		if (email.getMensagemHtml()) {
			htmlEmail.setHtmlMsg(email.getMensagem());
		}
		else {
			htmlEmail.setMsg(email.getMensagem());
		}
		for (File arquivoAnexo : email.getArquivosAnexos()) {
			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setPath(arquivoAnexo.getPath());
			htmlEmail.attach(emailAttachment);
		}
		htmlEmail.setHostName(email.getEnderecoSmtp());
		htmlEmail.setSmtpPort(email.getPortaSmtp());
		htmlEmail.setAuthentication(email.getUsuarioSmtp(), email.getSenhaSmtp());
		htmlEmail.setSSLOnConnect(email.getConexaoSsl());
		htmlEmail.send();
	}

	public static class Email {

		private String emissor;
		private List<String> destinatarios = new ArrayList<String>();
		private String assunto;
		private String mensagem;
		private List<File> arquivosAnexos = new ArrayList<File>();
		private boolean mensagemHtml = true;
		private String enderecoSmtp;
		private int portaSmtp;
		private String usuarioSmtp;
		private String senhaSmtp;
		private boolean conexaoSsl = true;

		public String getEmissor() {
			return this.emissor;
		}

		public void setEmissor(String emissor) {
			this.emissor = emissor;
		}

		public List<String> getDestinatarios() {
			return this.destinatarios;
		}

		public void adicionarDestinatario(String destinatario) {
			this.destinatarios.add(destinatario);
		}

		public String getAssunto() {
			return this.assunto;
		}

		public void setAssunto(String assunto) {
			this.assunto = assunto;
		}

		public String getMensagem() {
			return this.mensagem;
		}

		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}

		public List<File> getArquivosAnexos() {
			return this.arquivosAnexos;
		}

		public void adicionarArquivoAnexo(File arquivoAnexo) {
			this.arquivosAnexos.add(arquivoAnexo);
		}

		public boolean getMensagemHtml() {
			return this.mensagemHtml;
		}

		public void setMensagemHtml(boolean mensagemHtml) {
			this.mensagemHtml = mensagemHtml;
		}

		public String getEnderecoSmtp() {
			return this.enderecoSmtp;
		}

		public void setEnderecoSmtp(String enderecoSmtp) {
			this.enderecoSmtp = enderecoSmtp;
		}

		public int getPortaSmtp() {
			return this.portaSmtp;
		}

		public void setPortaSmtp(int portaSmtp) {
			this.portaSmtp = portaSmtp;
		}

		public String getUsuarioSmtp() {
			return this.usuarioSmtp;
		}

		public void setUsuarioSmtp(String usuarioSmtp) {
			this.usuarioSmtp = usuarioSmtp;
		}

		public String getSenhaSmtp() {
			return this.senhaSmtp;
		}

		public void setSenhaSmtp(String senhaSmtp) {
			this.senhaSmtp = senhaSmtp;
		}

		public boolean getConexaoSsl() {
			return this.conexaoSsl;
		}

		public void setConexaoSsl(boolean conexaoSsl) {
			this.conexaoSsl = conexaoSsl;
		}
	}
}