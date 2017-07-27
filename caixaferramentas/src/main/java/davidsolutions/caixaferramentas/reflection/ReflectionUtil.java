package davidsolutions.caixaferramentas.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import davidsolutions.caixaferramentas.dao.BeanEnum;

public class ReflectionUtil {

	private static final String SEPARADOR_NOME_ATRIBUTO = "\\.";

	private ReflectionUtil() {
	}

	public static Method obterMetodo(Class<?> classe, String nomeMetodo, Class<?>... argumentos) throws ReflectionException {
		try {
			return classe.getMethod(nomeMetodo, argumentos);
		}
		catch (Exception excecao) {
			throw new ReflectionException("Método '" + classe.getName() + "." + nomeMetodo + "' inexistente ou inacessível.", excecao);
		}
	}

	public static Object executarMetodo(Method metodo, Object objeto, Object... parametros) throws ReflectionException {
		try {
			return metodo.invoke(objeto, parametros);
		}
		catch (Exception excecao) {
			throw new ReflectionException("Falha ao executar método '" + metodo.getDeclaringClass().getName() + "." + metodo.getName() + "'.", excecao);
		}
	}

	public static Object instanciarObjeto(Class<?> classe) throws ReflectionException {

		if (classe.equals(List.class)) {
			return new ArrayList();
		}
		try {
			return classe.newInstance();
		}
		catch (Exception excecao) {
			throw new ReflectionException("Falha ao instanciar classe '" + classe.getName() + "'.", excecao);
		}
	}

	public static Class<?> obterClassePropriedade(Class<?> classe, String nomePropriedade) throws ReflectionException {
		try {
			return obterMetodoGet(classe, nomePropriedade).getReturnType();
		}
		catch (Exception excecao) {
			throw new ReflectionException("Propriedade '" + classe.getName() + "." + nomePropriedade + "' inexistente ou inacessível.", excecao);
		}
	}

	public static Class<?> obterTipoGenericoPropriedade(Class<?> classe, String nomePropriedade) throws ReflectionException {
		Method metodoGet = null;
		try {
			metodoGet = obterMetodoGet(classe, nomePropriedade);
		}
		catch (Exception excecao) {
			throw new ReflectionException("Propriedade '" + classe.getName() + "." + nomePropriedade + "' inexistente ou inacessível.", excecao);
		}
		if (!(metodoGet.getGenericReturnType() instanceof ParameterizedType) || ((ParameterizedType) metodoGet.getGenericReturnType()).getActualTypeArguments().length == 0) {
			throw new ReflectionException("Tipo genérico não definido na propriedade '" + classe.getName() + "." + nomePropriedade + "'.");
		}
		return (Class<?>) ((ParameterizedType) metodoGet.getGenericReturnType()).getActualTypeArguments()[0];
	}

	public static <T extends Annotation> T obterAnotacaoParametroMetodo(Method metodo, int posicaoParametro, Class<T> classeAnotacao) {

		for (Annotation anotacao : metodo.getParameterAnnotations()[posicaoParametro]) {

			if (anotacao.annotationType().equals(classeAnotacao)) {
				return (T) anotacao;
			}
		}
		return null;
	}

	public static Method obterMetodoGet(Class<?> classe, String nomePropriedade) throws ReflectionException {
		String nomeMetodo = "get" + capitalizarPalavra(nomePropriedade);
		Method metodo = obterMetodo(classe, nomeMetodo);

		if (metodo.getReturnType().equals(void.class)) {
			throw new ReflectionException("Retorno não definido no método '" + metodo.getDeclaringClass().getName() + "." + metodo.getName() + "'.");
		}
		return metodo;
	}

	public static Object obterValorPropriedade(Object objeto, String sequenciaNomesPropriedades) throws ReflectionException {
		String[] nomesPropriedades = sequenciaNomesPropriedades.split(SEPARADOR_NOME_ATRIBUTO);
		Object objetoAtual = objeto;

		for (int cont = 0; cont <= (nomesPropriedades.length - 2); cont++) {
			objetoAtual = obterValorPropriedade(objetoAtual, nomesPropriedades[cont]);

			if (objetoAtual == null) {
				return null;
			}
		}
		Method metodoGet = obterMetodoGet(objetoAtual.getClass(), nomesPropriedades[(nomesPropriedades.length - 1)]);
		return executarMetodo(metodoGet, objetoAtual);
	}

	public static void atribuirValorPropriedade(Object objeto, String nomePropriedade, Class<?> classePropriedade, Object valorPropriedade) throws ReflectionException {
		String nomeMetodo = "set" + capitalizarPalavra(nomePropriedade);
		Method metodo = obterMetodo(objeto.getClass(), nomeMetodo, classePropriedade);
		executarMetodo(metodo, objeto, valorPropriedade);
	}

	public static <T extends BeanEnum> T obterBeanEnumPorCodigo(Class<T> classeBeanEnum, Integer codigo) throws ReflectionException {

		for (T beanEnum : classeBeanEnum.getEnumConstants()) {

			if (beanEnum.getCodigo().equals(codigo)) {
				return beanEnum;
			}
		}
		throw new ReflectionException("BeanEnum '" + classeBeanEnum.getName() + "' inexistente para o código '" + codigo + "'.");
	}

	public static <T extends Enum<T>> T obterEnum(Class<T> classeEnum, String nomeEnum) throws ReflectionException {
		try {
			return Enum.valueOf(classeEnum, nomeEnum);
		}
		catch (Exception excecao) {
			throw new ReflectionException("Enum '" + classeEnum.getName() + "." + nomeEnum + "' inexistente ou inacessível.", excecao);
		}
	}

	public static String capitalizarPalavra(String palavra) {
		return Character.toUpperCase(palavra.charAt(0)) + palavra.substring(1);
	}

	public static String descapitalizarPalavra(String palavra) {
		return Character.toLowerCase(palavra.charAt(0)) + palavra.substring(1);
	}
}