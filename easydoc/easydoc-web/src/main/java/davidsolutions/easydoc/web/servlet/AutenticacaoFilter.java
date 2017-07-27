package davidsolutions.easydoc.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import davidsolutions.easydoc.servico.entidade.Usuario;
import davidsolutions.easydoc.web.utilidade.GerenciadorAtributosRequest;

@WebFilter("/*")
public class AutenticacaoFilter implements Filter {

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		Usuario usuarioAutenticado = GerenciadorAtributosRequest.obterUsuarioAutenticado(httpServletRequest);
		String acao = httpServletRequest.getParameter("acao");

		if (usuarioAutenticado == null) {

			if (!("iniciarAutenticacao".equals(acao) || "autenticar".equals(acao))) {
				httpServletResponse.sendRedirect("Usuario?acao=iniciarAutenticacao");
				return;
			}
		}
		else if (usuarioAutenticado.getNecessitaAlteracaoSenha()) {

			if (!("iniciarAlteracaoSenha".equals(acao) || "alterarSenha".equals(acao))) {
				httpServletResponse.sendRedirect("Usuario?acao=iniciarAlteracaoSenha");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}