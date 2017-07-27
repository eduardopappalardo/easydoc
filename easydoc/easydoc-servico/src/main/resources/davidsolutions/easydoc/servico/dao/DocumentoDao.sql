{
	"adicionar":
	
		"INSERT INTO Documento (
			codigoDocumento,
			codigoTipoDocumento
		)
		VALUES (
			?codigoDocumento,
			?tipoDocumento.codigoTipoDocumento
		)",
		
	"adicionarDocumentoIndice":
	
		"INSERT INTO DocumentoIndice (
			codigoDocumento,
			codigoIndice,
			valor
		)
		VALUES (
			?codigoDocumento,
			?codigoIndice,
			?valor
		)",
		
	"adicionarArquivo":
	
		"INSERT INTO Arquivo (
			codigoArquivo,
			codigoDocumento,
			nomeOriginal,
			caminho,
			descricao
		)
		VALUES (
			?codigoArquivo,
			?documento.codigoDocumento,
			?nomeOriginal,
			?caminho,
			?descricao
		)"
}