{
	"adicionar":
	
		"INSERT INTO TipoDocumento (
			codigoTipoDocumento,
			codigoCliente,
			codigoTipoDocumentoPai,
			nome
		)
		VALUES (
			?codigoTipoDocumento,
			?cliente.codigoCliente,
			?tipoDocumentoPai.codigoTipoDocumento,
			?nome
		)",
		
	"alterar":
	
		"UPDATE TipoDocumento
		SET
			codigoTipoDocumentoPai = ?tipoDocumentoPai.codigoTipoDocumento,
			nome = ?nome
		WHERE codigoTipoDocumento = ?codigoTipoDocumento",
		
	"excluir":
	
		"DELETE FROM TipoDocumento
		WHERE codigoTipoDocumento = ?codigoTipoDocumento",
		
	"consultarPorCodigo":
	
		"SELECT
			td.codigoTipoDocumento,
			td.codigoCliente AS cliente_codigoCliente,
			td.nome,
			td.codigoTipoDocumentoPai AS tipoDocumentoPai_codigoTipoDocumento,
			c.nome AS cliente_nome
		FROM TipoDocumento AS td
		INNER JOIN
		Cliente AS c ON td.codigoCliente = c.codigoCliente
		WHERE td.codigoTipoDocumento = ?codigoTipoDocumento",
		
	"consultar":
	
		"SELECT
			td.codigoTipoDocumento,
			td.codigoCliente AS cliente_codigoCliente,
			td.nome,
			c.nome AS cliente_nome
		FROM TipoDocumento AS td
		INNER JOIN
		Cliente AS c ON td.codigoCliente = c.codigoCliente
		{td.codigoCliente = ?cliente.codigoCliente}
		{td.nome LIKE ?%nome}
		ORDER BY td.codigoTipoDocumento DESC",
		
	"listarPorCodigoCliente":
	
		"SELECT
			codigoTipoDocumento,
			nome
		FROM TipoDocumento
		WHERE codigoCliente = ?codigoCliente
		ORDER BY nome ASC"
	
}