package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversorLong extends Conversor {

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setLong(posicao, (Long) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getLong(rotuloColuna);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return Long.valueOf(valor);
	}
}