package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversorCharacter extends Conversor {

	@Override
	Object converterAntesMetodoPreparedStatement(Object valor) throws ConversorException {
		return ((Character) valor).toString();
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
		return ((String) valor).charAt(0);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return valor.charAt(0);
	}
}