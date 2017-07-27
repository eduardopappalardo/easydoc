package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.validacao.ConversaoUtil;

public class ConversorFloat extends Conversor {

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setFloat(posicao, (Float) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getFloat(rotuloColuna);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return ConversaoUtil.converterStringParaNumero(valor).floatValue();
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ConversaoUtil.converterNumeroParaString((Float) valor);
	}
}