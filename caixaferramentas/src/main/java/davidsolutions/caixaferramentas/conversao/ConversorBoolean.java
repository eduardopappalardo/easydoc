package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.validacao.ConversaoUtil;

public class ConversorBoolean extends Conversor {

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setBoolean(posicao, (Boolean) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getBoolean(rotuloColuna);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return Boolean.valueOf(valor);
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ConversaoUtil.converterBooleanParaString((Boolean) valor);
	}
}