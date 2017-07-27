package davidsolutions.caixaferramentas.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.json.simple.JSONObject;

import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;

public abstract class AvaliadorAcaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String NOME_PARAMETRO_ACAO = "acao";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.avaliarAcao(Get.class, request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.avaliarAcao(Post.class, request, response);
	}

	private void avaliarAcao(Class<? extends Annotation> anotacaoMetodo, HttpServletRequest request, HttpServletResponse response) {
		String acao = request.getParameter(NOME_PARAMETRO_ACAO);
		Method metodoAcao = null;

		if (ValidacaoUtil.estaVazio(acao)) {
			throw new RuntimeException("Parâmetro '" + NOME_PARAMETRO_ACAO + "' não definido.");
		}
		for (Method metodo : this.getClass().getDeclaredMethods()) {

			if (metodo.getName().equals(acao)) {
				metodoAcao = metodo;
				break;
			}
		}
		if (metodoAcao == null) {
			throw new RuntimeException("Método '" + this.getClass().getName() + "." + acao + "' inexistente ou inacessível.");
		}
		else if (!metodoAcao.isAnnotationPresent(anotacaoMetodo)) {
			throw new RuntimeException("Método HTTP inválido para o método '" + this.getClass().getName() + "." + metodoAcao.getName() + "'.");
		}
		Object[] parametros = new Object[metodoAcao.getParameterTypes().length];
		Class<?> classeParametro = null;
		String nomeAtributo = null;

		for (int posicao = 0; posicao < metodoAcao.getParameterTypes().length; posicao++) {
			classeParametro = metodoAcao.getParameterTypes()[posicao];

			if (classeParametro.equals(HttpServletRequest.class)) {
				parametros[posicao] = request;
			}
			else if (classeParametro.equals(HttpServletResponse.class)) {
				parametros[posicao] = response;
			}
			else {
				BeanForm anotacaoBeanForm = ReflectionUtil.obterAnotacaoParametroMetodo(metodoAcao, posicao, BeanForm.class);
				Id anotacaoId = ReflectionUtil.obterAnotacaoParametroMetodo(metodoAcao, posicao, Id.class);

				if (anotacaoId != null && !ValidacaoUtil.estaVazio(anotacaoId.value())) {
					nomeAtributo = anotacaoId.value();
				}
				else {
					nomeAtributo = ReflectionUtil.descapitalizarPalavra(classeParametro.getSimpleName());
				}
				if (anotacaoBeanForm != null) {
					try {
						parametros[posicao] = ConversorBeanForm.converter(anotacaoBeanForm, request, classeParametro);
					}
					catch (Exception excecao) {
						throw new RuntimeException(excecao);
					}
					request.setAttribute(nomeAtributo, parametros[posicao]);
				}
				else if (anotacaoId != null) {
					try {
						parametros[posicao] = ReflectionUtil.instanciarObjeto(classeParametro);
					}
					catch (ReflectionException excecao) {
						throw new RuntimeException(excecao);
					}
					request.setAttribute(nomeAtributo, parametros[posicao]);
				}
				else {
					parametros[posicao] = null;
				}
			}
		}
		try {
			response.setCharacterEncoding("UTF-8");
			Object objetoRetorno = ReflectionUtil.executarMetodo(metodoAcao, this, parametros);

			if (objetoRetorno instanceof String) {
				request.getRequestDispatcher((String) objetoRetorno).forward(request, response);
			}
			else if (objetoRetorno instanceof ResultadoAjax) {
				ResultadoAjax resultadoAjax = (ResultadoAjax) objetoRetorno;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("redirecionamento", resultadoAjax.getRedirecionamento());

				if (resultadoAjax.getRedirecionamento()) {
					jsonObject.put("urlRedirecionamento", resultadoAjax.getUrlRedirecionamento());
				}
				else if (!resultadoAjax.getFragmentosPagina().isEmpty()) {
					JSONObject fragmentosPagina = new JSONObject();
					MyHttpServletResponseWrapper myHttpServletResponseWrapper = null;

					for (Entry<String, String> chaveValor : resultadoAjax.getFragmentosPagina().entrySet()) {
						myHttpServletResponseWrapper = new MyHttpServletResponseWrapper(response);
						request.getRequestDispatcher(chaveValor.getValue()).include(request, myHttpServletResponseWrapper);
						fragmentosPagina.put(chaveValor.getKey(), myHttpServletResponseWrapper.toString());
					}
					jsonObject.put("fragmentosPagina", fragmentosPagina);
				}
				response.getWriter().print(jsonObject.toJSONString());
			}
		}
		catch (Exception excecao) {
			throw new RuntimeException(excecao);
		}
	}

	private static class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {

		private StringWriter stringWriter = new StringWriter();

		public MyHttpServletResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return new PrintWriter(this.stringWriter);
		}

		@Override
		public String toString() {
			return this.stringWriter.toString();
		}
	};
}