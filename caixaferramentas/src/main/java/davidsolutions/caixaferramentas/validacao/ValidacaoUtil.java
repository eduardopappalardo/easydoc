package davidsolutions.caixaferramentas.validacao;

import java.util.List;

public class ValidacaoUtil {

	private static final String REGEX_EMAIL = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,6})$";
	private static final String REGEX_CPF = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}";
	private static final String REGEX_CNPJ = "\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}";
	private static final int[] PESO_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] PESO_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private ValidacaoUtil() {
	}

	public static boolean estaVazio(String string) {
		return (string == null || string.trim().isEmpty());
	}

	public static boolean estaVazio(List<?> lista) {
		return (lista == null || lista.isEmpty());
	}

	public static boolean estaVazio(Object[] array) {
		return (array == null || array.length == 0);
	}

	public static boolean estaVazio(Object objeto) {
		return (objeto == null || (objeto instanceof String && estaVazio((String) objeto)) || (objeto instanceof List<?> && estaVazio((List<?>) objeto)) || (objeto instanceof Object[] && estaVazio((Object[]) objeto)));
	}

	public static boolean validarEmail(String email) {
		return (!estaVazio(email) && email.matches(REGEX_EMAIL));
	}

	public static boolean validarData(String data) {

		if (!estaVazio(data)) {
			try {
				ConversaoUtil.converterStringParaData(data);
				return true;
			}
			catch (Exception excecao) {
				return false;
			}
		}
		return false;
	}

	public static boolean validarNumero(String numero) {

		if (!estaVazio(numero)) {
			try {
				ConversaoUtil.converterStringParaNumero(numero);
				return true;
			}
			catch (Exception excecao) {
				return false;
			}
		}
		return false;
	}

	public static boolean validarNumeroInteiro(String numero) {
		return !estaVazio(numero) && numero.matches("\\d+");
	}

	public static boolean validarCpf(String cpf) {

		if (!estaVazio(cpf) && cpf.matches(REGEX_CPF)) {
			String cpfTemp = cpf.toString().replaceFirst("\\D", "");

			if (cpfTemp.length() != 11) {
				return false;
			}
			String cpfSemDigito = cpfTemp.substring(0, 9);
			int digito1 = calcularDigitoCpfCnpj(cpfSemDigito, PESO_CPF);
			int digito2 = calcularDigitoCpfCnpj(cpfSemDigito + digito1, PESO_CPF);

			return cpfTemp.equals(cpfSemDigito + digito1 + digito2);
		}
		return false;
	}

	public static boolean validarCnpj(String cnpj) {

		if (!estaVazio(cnpj) && cnpj.matches(REGEX_CNPJ)) {
			String cnpjTemp = cnpj.toString().replaceFirst("\\D", "");

			if (cnpjTemp.length() != 14) {
				return false;
			}
			String cnpjSemDigito = cnpjTemp.substring(0, 12);
			int digito1 = calcularDigitoCpfCnpj(cnpjSemDigito, PESO_CNPJ);
			int digito2 = calcularDigitoCpfCnpj(cnpjSemDigito + digito1, PESO_CNPJ);

			return cnpjTemp.equals(cnpjSemDigito + digito1 + digito2);
		}
		return false;
	}

	private static int calcularDigitoCpfCnpj(String cpfCnpj, int[] pesoCpfCnpj) {
		int soma = 0;
		int indicePeso = pesoCpfCnpj.length - cpfCnpj.length();
		int digito;

		for (char character : cpfCnpj.toCharArray()) {
			digito = Character.getNumericValue(character);
			soma += digito * pesoCpfCnpj[indicePeso];
			indicePeso++;
		}
		soma = 11 - (soma % 11);
		return soma > 9 ? 0 : soma;
	}
}
