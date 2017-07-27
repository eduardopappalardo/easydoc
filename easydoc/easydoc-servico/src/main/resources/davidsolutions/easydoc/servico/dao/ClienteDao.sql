{
	"adicionar":
	
		"INSERT INTO Cliente (
			codigoCliente,
			nome,
			ativo
		)
		VALUES (
			?codigoCliente,
			?nome,
			?ativo
		)",
		
	"alterar":
	
		"UPDATE Cliente
		SET
			nome = ?nome,
			ativo = ?ativo
		WHERE codigoCliente = ?codigoCliente",
		
	"consultarPorCodigo":
	
		"SELECT
			codigoCliente,
			nome,
			ativo
		FROM Cliente
		WHERE codigoCliente = ?codigoCliente",
		
	"consultar":
	
		"SELECT
			codigoCliente,
			nome,
			ativo
		FROM Cliente
		{nome LIKE ?%nome}
		{ativo = ?ativo}
		ORDER BY codigoCliente DESC",
	
	"existeCodigo":
	
		"SELECT 1
		FROM Cliente
		WHERE codigoCliente = ?codigoCliente
		AND ativo = ?ativo",
		
	"listar":
	
		"SELECT
			codigoCliente,
			nome
		FROM Cliente
		{WHERE ativo = ?ativo}
		ORDER BY nome ASC"
	
}