package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.dao.BeanEnum;
import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;

public class ConversorBeanEnum extends Conversor {

	private Class<?> tipoValor;

	public ConversorBeanEnum(Class<?> tipoValor) {
		this.tipoValor = tipoValor;
	}

	@Override
	Object converterAntesMetodoPreparedStatement(Object valor) throws ConversorException {
		return ((BeanEnum) valor).getCodigo();
	}

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setInt(posicao, (Integer) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getInt(rotuloColuna);
	}

	@Override
	Object converterPosMetodoResultSet(Object valor) throws ConversorException {
		try {
			return ReflectionUtil.obterBeanEnumPorCodigo((Class<? extends BeanEnum>) this.tipoValor, (Integer) valor);
		}
		catch (ReflectionException excecao) {
			throw new ConversorException(excecao);
		}
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		try {
			return ReflectionUtil.obterBeanEnumPorCodigo((Class<? extends BeanEnum>) this.tipoValor, Integer.valueOf(valor));
		}
		catch (ReflectionException excecao) {
			throw new ConversorException(excecao);
		}
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		BeanEnum beanEnum = (BeanEnum) valor;
		return beanEnum.getCodigo() + " - " + beanEnum.getDescricao();
	}
}