package davidsolutions.caixaferramentas.conversao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.validacao.ConversaoUtil;

public class ConversorBigDecimal extends Conversor {

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setBigDecimal(posicao, (BigDecimal) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getBigDecimal(rotuloColuna);
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		return ConversaoUtil.converterStringParaNumero(valor);
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ConversaoUtil.converterNumeroParaString((BigDecimal) valor);
	}
}