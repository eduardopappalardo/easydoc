package davidsolutions.caixaferramentas.conversao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.validacao.ConversaoUtil;

public class ConversorDate extends Conversor {

	@Override
	Object converterAntesMetodoPreparedStatement(Object valor) throws ConversorException {
		return new Date(((java.util.Date) valor).getTime());
	}

	@Override
	void executarMetodoPreparedStatement(PreparedStatement preparedStatement, Integer posicao, Object valor) throws SQLException {
		preparedStatement.setDate(posicao, (Date) valor);
	}

	@Override
	Object executarMetodoResultSet(ResultSet resultSet, String rotuloColuna) throws SQLException {
		return resultSet.getDate(rotuloColuna);
	}

	@Override
	Object converterPosMetodoResultSet(Object valor) throws ConversorException {
		return new java.util.Date(((Date) valor).getTime());
	}

	@Override
	Object converterDeRequestPosLimpeza(String valor) throws ConversorException {
		try {
			return ConversaoUtil.converterStringParaData(valor);
		}
		catch (Exception excecao) {
			throw new ConversorException(excecao);
		}
	}

	@Override
	String converterParaUsuarioPosVerificacao(Object valor) {
		return ConversaoUtil.converterDataParaString((java.util.Date) valor);
	}
}