{
	"adicionar":
	
		"INSERT INTO Indice (
			codigoIndice,
			codigoTipoDocumento,
			codigoTipoIndice,
			nome,
			preenchimentoObrigatorio
		)
		VALUES (
			?codigoIndice,
			?tipoDocumento.codigoTipoDocumento,
			?tipoIndice.codigo,
			?nome,
			?preenchimentoObrigatorio
		)",
		
	"alterar":
	
		"UPDATE Indice
		SET
			codigoTipoIndice = ?tipoIndice.codigo,
			nome = ?nome,
			preenchimentoObrigatorio = ?preenchimentoObrigatorio
		WHERE codigoIndice = ?codigoIndice",
		
	"excluir":
	
		"DELETE FROM Indice
		WHERE codigoIndice = ?codigoIndice",
		
	"excluirPorCodigoTipoDocumento":
	
		"DELETE FROM Indice
		WHERE codigoTipoDocumento = ?codigoTipoDocumento",
		
	"consultarPorCodigoTipoDocumento":
	
		"SELECT
			codigoIndice,
			codigoTipoDocumento AS tipoDocumento_codigoTipoDocumento,
			codigoTipoIndice AS tipoIndice,
			nome,
			preenchimentoObrigatorio
		FROM Indice
		WHERE codigoTipoDocumento = ?codigoTipoDocumento"
	
}