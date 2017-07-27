package davidsolutions.easydoc.web.utilidade;

import javax.servlet.http.HttpServletRequest;

import davidsolutions.caixaferramentas.web.MensagemUsuario;
import davidsolutions.easydoc.servico.entidade.Usuario;

public class GerenciadorAtributosRequest {

	private GerenciadorAtributosRequest() {
	}

	public static MensagemUsuario obterMensagemUsuario(HttpServletRequest request) {
		MensagemUsuario mensagemUsuario = (MensagemUsuario) request.getAttribute("mensagemUsuario");

		if (mensagemUsuario == null) {
			mensagemUsuario = new MensagemUsuario();
			request.setAttribute("mensagemUsuario", mensagemUsuario);
		}
		return mensagemUsuario;
	}

	public static Pagina obterPagina(HttpServletRequest request) {
		Pagina pagina = (Pagina) request.getAttribute("pagina");

		if (pagina == null) {
			pagina = new Pagina();
			request.setAttribute("pagina", pagina);
		}
		return pagina;
	}

	public static void gravarUsuarioAutenticado(Usuario usuario, HttpServletRequest request) {
		request.getSession().setAttribute("usuarioAutenticado", usuario);
	}

	public static Usuario obterUsuarioAutenticado(HttpServletRequest request) {
		return (Usuario) request.getSession().getAttribute("usuarioAutenticado");
	}
}