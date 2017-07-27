package davidsolutions.easydoc.web.utilidade;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import davidsolutions.caixaferramentas.conversao.Conversor;
import davidsolutions.caixaferramentas.conversao.ConversorException;
import davidsolutions.caixaferramentas.utilidade.CriptografiaUtil;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;

public class FuncoesEl {

	public static boolean listaContem(List<?> lista, Object item) {
		return lista != null && lista.contains(item);
	}

	public static String imprimir(Object valor) {

		if (valor != null) {
			try {
				Conversor conversor = Conversor.obterConversor(valor.getClass());
				return StringEscapeUtils.escapeHtml(conversor.converterParaUsuario(valor));
			}
			catch (ConversorException excecao) {
				return StringEscapeUtils.escapeHtml(valor.toString());
			}
		}
		else {
			return "";
		}
	}

	public static String encriptar(String valor) {
		return (!ValidacaoUtil.estaVazio(valor) ? CriptografiaUtil.encriptar(valor) : "");
	}
}