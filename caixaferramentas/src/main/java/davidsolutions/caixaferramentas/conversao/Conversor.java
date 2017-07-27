package davidsolutions.caixaferramentas.conversao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import davidsolutions.caixaferramentas.dao.BeanEnum;
import davidsolutions.caixaferramentas.utilidade.StringUtil;
import davidsolutions.caixaferramentas.validacao.ValidacaoUtil;

public abstract class Conversor {

	private static Map<Class<?>, Conversor> conversores = new HashMap<Class<?>, Conversor>();

	static {
		conversores.put(null, new ConversorNull());
		conversores.put(String.class, new ConversorString());
		conversores.put(java.util.Date.class, new ConversorDate());
		conversores.put(Character.class, new ConversorCharacter());
		conversores.put(Boolean.class, new ConversorBoolean());
		conversores.put(Byte.class, new ConversorByte());
		conversores.put(Short.class, new ConversorShort());
		conversores.put(Integer.class, new ConversorInteger());
		conversores.put(Long.class, new ConversorLong());
		conversores.put(Float.class, new ConversorFloat());
		conversores.put(Double.class, new ConversorDouble());
		conversores.put(BigDecimal.class, new ConversorBigDecimal());
	}

	public final void converterParaPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException, ConversorException {
		this.executarMetodoPreparedStatement(preparedStatement, posicao, this.converterAntesMetodoPreparedStatement(valor));
	}

	public final Object converterDeResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException, ConversorException {
		Object valor = this.executarMetodoResultSet(resultSet, rotuloColuna);
		return (!resultSet.wasNull() ? this.converterPosMetodoResultSet(valor) : null);
	}

	public final Object converterDeRequest(String valor) throws ConversorException {
		valor = StringUtil.removerEspacos(valor);
		return (!ValidacaoUtil.estaVazio(valor) ? this.converterDeRequestPosLimpeza(valor) : null);
	}

	public final String converterParaUsuario(Object valor) {
		return (valor != null ? this.converterParaUsuarioPosVerificacao(valor) : "");
	}

	Object converterAntesMetodoPreparedStatement(Object valor) throws ConversorException {
		return valor;
	}

	abstract void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException;

	abstract Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException;

	Object converterPosMetodoResultSet(Object valor) throws ConversorException {
		return valor;
	}

	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return valor;
	}

	String converterParaUsuarioPosVerificacao(Object valor) {
		return valor.toString();
	}

	public static Conversor obterConversor(Class<?> tipoValor) throws ConversorException {

		if (tipoValor != null && tipoValor.isPrimitive()) {
			throw new ConversorException("Tipo primitivo não suportado. Utilize seu respectivo objeto.");
		}
		if (conversores.containsKey(tipoValor)) {
			return conversores.get(tipoValor);
		}
		else if (BeanEnum.class.isAssignableFrom(tipoValor)) {
			return new ConversorBeanEnum(tipoValor);
		}
		else if (tipoValor.isEnum()) {
			return new ConversorEnum(tipoValor);
		}
		else {
			throw new ConversorException("Classe '" + tipoValor.getName() + "' não suportada.");
		}
	}
}