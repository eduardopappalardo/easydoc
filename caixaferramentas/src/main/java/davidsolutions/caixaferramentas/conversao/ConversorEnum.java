package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.caixaferramentas.reflection.ReflectionUtil;

public class ConversorEnum extends Conversor {

	private Class<?> tipoValor;

	public ConversorEnum(Class<?> tipoValor) {
		this.tipoValor = tipoValor;
	}

	@Override
	Object converterAntesMetodoPreparedStatement(Object valor) throws ConversorException {
		return ((Enum<?>) valor).name();
	}

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setString(posicao, (String) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getString(rotuloColuna);
	}

	@Override
	Object converterPosMetodoResultSet(Object valor) throws ConversorException {
		try {
			return ReflectionUtil.obterEnum((Class<? extends Enum>) this.tipoValor, (String) valor);
		}
		catch (ReflectionException excecao) {
			throw new ConversorException(excecao);
		}
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		try {
			return ReflectionUtil.obterEnum((Class<? extends Enum>) this.tipoValor, valor);
		}
		catch (ReflectionException excecao) {
			throw new ConversorException(excecao);
		}
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ((Enum<?>) valor).name();
	}
}