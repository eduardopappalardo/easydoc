package davidsolutions.caixaferramentas.web;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import davidsolutions.caixaferramentas.conversao.Conversor;
import davidsolutions.caixaferramentas.conversao.ConversorException;
import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;
import davidsolutions.caixaferramentas.utilidade.CriptografiaUtil;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;

public class ConversorBeanForm {

	private static final String REGEX_LIMPEZA_NOME_ATRIBUTO = "\\[\\d+\\]";

	private ConversorBeanForm() {
	}

	public static <T> T converter(HttpServletRequest request, Class<T> tipoObjetoRetorno) throws ReflectionException, ConversorException {
		return converter(null, null, request, tipoObjetoRetorno);
	}

	public static <T> T converter(BeanForm beanForm, HttpServletRequest request, Class<T> tipoObjetoRetorno) throws ReflectionException, ConversorException {
		return converter(obterReferenciaBeanForm(beanForm), beanForm.encriptados(), request, tipoObjetoRetorno);
	}

	public static <T> T converter(String referencia, String[] encriptados, HttpServletRequest request, Class<T> tipoObjetoRetorno) throws ReflectionException, ConversorException {
		T objeto = (T) ReflectionUtil.instanciarObjeto(tipoObjetoRetorno);
		Map<String, Object> mapaObjetosLista = new HashMap<String, Object>();
		List<String> encriptadosTemp = null;
		boolean existeEncriptados = false;

		if (!ValidacaoUtil.estaVazio(encriptados)) {
			existeEncriptados = true;
			encriptadosTemp = Arrays.asList(encriptados);
		}
		for (Entry<String, String[]> parametro : request.getParameterMap().entrySet()) {

			if (ValidacaoUtil.estaVazio(referencia)) {
				try {
					atribuirValorPropriedade((existeEncriptados && encriptadosTemp.contains(parametro.getKey().replaceAll(REGEX_LIMPEZA_NOME_ATRIBUTO, ""))), 0, parametro, objeto, mapaObjetosLista);
				}
				catch (Exception excecao) {
				}
			}
			else if (parametro.getKey().startsWith(referencia + ".")) {
				atribuirValorPropriedade((existeEncriptados && encriptadosTemp.contains(parametro.getKey().replaceAll("^" + referencia + "\\.|" + REGEX_LIMPEZA_NOME_ATRIBUTO, ""))), 1, parametro, objeto, mapaObjetosLista);
			}
		}
		return objeto;
	}

	private static void atribuirValorPropriedade(boolean decripta, int posicaoInicial, Entry<String, String[]> parametro, Object objeto, Map<String, Object> mapaObjetosLista) throws ReflectionException, ConversorException {
		String[] nomesPropriedades = parametro.getKey().split("\\.");
		String nomePropriedade = null;
		Method metodoGet = null;
		Class<?> classePropriedade = null;
		Object objetoAtual = null;
		Object objetoAnterior = objeto;
		Object valorPropriedade = null;
		Conversor conversor = null;
		StringBuilder chaveObjetoLista = new StringBuilder();

		for (int cont = posicaoInicial; cont <= (nomesPropriedades.length - 2); cont++) {
			nomePropriedade = nomesPropriedades[cont].replaceAll(REGEX_LIMPEZA_NOME_ATRIBUTO, "");
			metodoGet = ReflectionUtil.obterMetodoGet(objetoAnterior.getClass(), nomePropriedade);
			classePropriedade = metodoGet.getReturnType();
			objetoAtual = ReflectionUtil.executarMetodo(metodoGet, objetoAnterior);
			chaveObjetoLista.append(nomesPropriedades[cont] + ".");

			if (objetoAtual == null) {
				objetoAtual = ReflectionUtil.instanciarObjeto(classePropriedade);
				ReflectionUtil.atribuirValorPropriedade(objetoAnterior, nomePropriedade, classePropriedade, objetoAtual);
			}
			if (objetoAtual instanceof List) {
				List lista = (List) objetoAtual;
				objetoAtual = mapaObjetosLista.get(chaveObjetoLista.toString());

				if (objetoAtual == null) {
					objetoAtual = ReflectionUtil.instanciarObjeto(ReflectionUtil.obterTipoGenericoPropriedade(objetoAnterior.getClass(), nomePropriedade));
					mapaObjetosLista.put(chaveObjetoLista.toString(), objetoAtual);
					lista.add(objetoAtual);
				}
			}
			objetoAnterior = objetoAtual;
		}
		nomePropriedade = nomesPropriedades[(nomesPropriedades.length - 1)];
		classePropriedade = ReflectionUtil.obterClassePropriedade(objetoAnterior.getClass(), nomePropriedade);

		if (List.class.isAssignableFrom(classePropriedade)) {
			valorPropriedade = ReflectionUtil.instanciarObjeto(classePropriedade);
			Class<?> tipoGenericoLista = ReflectionUtil.obterTipoGenericoPropriedade(objetoAnterior.getClass(), nomePropriedade);
			conversor = Conversor.obterConversor(tipoGenericoLista);

			for (String valor : parametro.getValue()) {
				((List) valorPropriedade).add(conversor.converterDeRequest(decripta ? CriptografiaUtil.decriptar(valor) : valor));
			}
		}
		else {
			conversor = Conversor.obterConversor(classePropriedade);
			valorPropriedade = conversor.converterDeRequest(decripta ? CriptografiaUtil.decriptar(parametro.getValue()[0]) : parametro.getValue()[0]);
		}
		ReflectionUtil.atribuirValorPropriedade(objetoAnterior, nomePropriedade, classePropriedade, valorPropriedade);
	}

	private static String obterReferenciaBeanForm(BeanForm anotacaoBeanForm) {

		if (!ValidacaoUtil.estaVazio(anotacaoBeanForm.referencia())) {
			return anotacaoBeanForm.referencia();
		}
		else {
			return anotacaoBeanForm.value();
		}
	}
}