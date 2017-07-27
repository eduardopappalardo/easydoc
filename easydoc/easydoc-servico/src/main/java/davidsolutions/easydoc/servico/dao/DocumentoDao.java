package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import davidsolutions.caixaferramentas.dao.Dao;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.easydoc.servico.entidade.Arquivo;
import davidsolutions.easydoc.servico.entidade.Documento;

public class DocumentoDao extends Dao {

	public DocumentoDao(Connection conexao) throws DaoException {
		super(conexao);
	}

	public void adicionar(Documento documento) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionar");
		documento.setCodigoDocumento(super.gerarSequencial("Documento"));
		super.executarInstrucaoSql(instrucaoSql, documento);
	}

	public void adicionarDocumentoIndice(Integer codigoDocumento, Integer codigoIndice, String valor) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionarDocumentoIndice");
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("codigoDocumento", codigoDocumento);
		mapaParametros.put("codigoIndice", codigoIndice);
		mapaParametros.put("valor", valor);
		super.executarInstrucaoSql(instrucaoSql, mapaParametros);
	}

	public void adicionarArquivo(Arquivo arquivo) throws DaoException {
		String instrucaoSql = super.obterInstrucaoSql("adicionarArquivo");
		arquivo.setCodigoArquivo(super.gerarSequencial("Arquivo"));
		arquivo.setCaminho(arquivo.getCaminho() + arquivo.getCodigoArquivo());
		super.executarInstrucaoSql(instrucaoSql, arquivo);
	}
}