package davidsolutions.caixaferramentas.validacao;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConversaoUtil {

	private static final String DATA_SEM_HORA_FORMATO = "dd/MM/yyyy";
	private static final String DATA_COM_HORA_FORMATO = "dd/MM/yyyy HH:mm:ss";
	private static final String NUMERO_FRACIONADO_FORMATO = "#,###.00";
	private static final String REGEX_DATA_SEM_HORA_FORMATO = "\\d{1,2}/\\d{1,2}/\\d{4}";
	private static final String REGEX_DATA_COM_HORA_FORMATO = "\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}";

	private ConversaoUtil() {
	}

	public static Date converterStringParaData(String data) throws ParseException {
		SimpleDateFormat simpleDateFormat = null;

		if (data.matches(REGEX_DATA_COM_HORA_FORMATO)) {
			simpleDateFormat = new SimpleDateFormat(DATA_COM_HORA_FORMATO);
		}
		else if (data.matches(REGEX_DATA_SEM_HORA_FORMATO)) {
			simpleDateFormat = new SimpleDateFormat(DATA_SEM_HORA_FORMATO);
		}
		else {
			throw new ParseException("Formato de data inválido.", -1);
		}
		simpleDateFormat.setLenient(false);
		return simpleDateFormat.parse(data);
	}

	public static String converterDataParaString(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		if (calendar.get(Calendar.HOUR_OF_DAY) > 0 || calendar.get(Calendar.MINUTE) > 0 || calendar.get(Calendar.SECOND) > 0) {
			return new SimpleDateFormat(DATA_COM_HORA_FORMATO).format(data);
		}
		else {
			return new SimpleDateFormat(DATA_SEM_HORA_FORMATO).format(data);
		}
	}

	public static BigDecimal converterStringParaNumero(String numero) {
		return new BigDecimal(numero.replaceAll("\\.", "").replaceAll(",", "\\."));
	}

	public static String converterNumeroParaString(Number numero) {

		if (numero instanceof Float || numero instanceof Double || numero instanceof BigDecimal) {
			return new DecimalFormat(NUMERO_FRACIONADO_FORMATO).format(numero);
		}
		else {
			return numero.toString();
		}
	}

	public static String converterBooleanParaString(Boolean valor) {
		return (valor ? "Sim" : "Não");
	}
}