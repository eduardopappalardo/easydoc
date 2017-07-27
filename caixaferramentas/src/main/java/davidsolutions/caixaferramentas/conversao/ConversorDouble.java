package davidsolutions.caixaferramentas.conversao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.validacao.ConversaoUtil;

public class ConversorDouble extends Conversor {

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setDouble(posicao, (Double) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getDouble(rotuloColuna);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return ConversaoUtil.converterStringParaNumero(valor).doubleValue();
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ConversaoUtil.converterNumeroParaString((Double) valor);
	}
}