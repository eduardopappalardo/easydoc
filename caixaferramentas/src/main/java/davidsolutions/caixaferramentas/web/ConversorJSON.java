package davidsolutions.caixaferramentas.web;

import java.lang.reflect.Method;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import davidsolutions.caixaferramentas.conversao.Conversor;
import davidsolutions.caixaferramentas.conversao.ConversorException;
import davidsolutions.caixaferramentas.dao.BeanEnum;
import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;

public class ConversorJSON {

	public static String converterParaJSON(Object objeto) throws ReflectionException {
		return converterObjeto(objeto).toJSONString();
	}

	private static JSONObject converterObjeto(Object objeto) throws ReflectionException {
		JSONObject jsonObject = new JSONObject();

		if (objeto == null) {
			return jsonObject;
		}
		for (Method metodo : objeto.getClass().getDeclaredMethods()) {

			if (metodo.getName().startsWith("get")) {
				String nomePropriedade = ReflectionUtil.descapitalizarPalavra(metodo.getName().substring(3));
				Object valorPropriedade = ReflectionUtil.executarMetodo(metodo, objeto);

				if (valorPropriedade != null) {

					if (valorPropriedade instanceof List) {
						JSONArray jsonArray = new JSONArray();

						for (Object item : (List<?>) valorPropriedade) {
							jsonArray.add(converterValorPropriedade(item));
						}
						jsonObject.put(nomePropriedade, jsonArray);
					}
					else {
						jsonObject.put(nomePropriedade, converterValorPropriedade(valorPropriedade));
					}
				}
			}
		}
		return jsonObject;
	}

	private static Object converterValorPropriedade(Object valorPropriedade) throws ReflectionException {

		if (valorPropriedade instanceof BeanEnum) {
			return converterObjeto(valorPropriedade);
		}
		try {
			Conversor conversor = Conversor.obterConversor(valorPropriedade != null ? valorPropriedade.getClass() : null);
			return conversor.converterParaUsuario(valorPropriedade);
		}
		catch (ConversorException excecao) {
			return converterObjeto(valorPropriedade);
		}
	}
}