{
	"adicionar":
	
		"INSERT INTO Grupo (
			codigoGrupo,
			codigoCliente,
			nome,
			ativo
		)
		VALUES (
			?codigoGrupo,
			?cliente.codigoCliente,
			?nome,
			?ativo
		)",
		
	"alterar":
	
		"UPDATE Grupo
		SET
			nome = ?nome,
			ativo = ?ativo
		WHERE codigoGrupo = ?codigoGrupo",
		
	"consultarPorCodigo":
	
		"SELECT
			g.codigoGrupo,
			g.codigoCliente AS cliente_codigoCliente,
			g.nome,
			g.ativo,
			c.nome AS cliente_nome
		FROM Grupo AS g
		INNER JOIN
		Cliente AS c ON g.codigoCliente = c.codigoCliente
		WHERE g.codigoGrupo = ?codigoGrupo",
		
	"consultar":
	
		"SELECT
			g.codigoGrupo,
			g.codigoCliente AS cliente_codigoCliente,
			g.nome,
			g.ativo,
			c.nome AS cliente_nome
		FROM Grupo AS g
		INNER JOIN
		Cliente AS c ON g.codigoCliente = c.codigoCliente
		{g.codigoCliente = ?cliente.codigoCliente}
		{g.nome LIKE ?%nome}
		{g.ativo = ?ativo}
		ORDER BY g.codigoGrupo DESC",
	
	"existeCodigo":
	
		"SELECT 1
		FROM Grupo
		WHERE codigoGrupo = ?codigoGrupo
		AND ativo = ?ativo",
		
	"listarAtivosPorCodigoCliente":
	
		"SELECT
			codigoGrupo,
			nome
		FROM Grupo
		WHERE codigoCliente = ?codigoCliente
		AND ativo = ?ativo
		ORDER BY nome ASC",
		
	"listarAtivosPorCodigoUsuario":
	
		"SELECT
			g.codigoGrupo,
			g.nome
		FROM Grupo AS g
		INNER JOIN
		GrupoUsuario AS gu ON g.codigoGrupo = gu.codigoGrupo
		WHERE gu.codigoUsuario = ?codigoUsuario
		AND g.ativo = ?ativo
		ORDER BY g.nome ASC",
		
	"adicionarGrupoUsuario":
	
		"INSERT INTO GrupoUsuario (codigoGrupo, codigoUsuario)
		VALUES (?codigoGrupo, ?codigoUsuario)",
		
	"excluirGrupoUsuarioPorCodigoUsuario":
	
		"DELETE FROM GrupoUsuario
		WHERE codigoUsuario = ?codigoUsuario",
		
	"excluirGrupoUsuarioPorCodigoGrupo":
	
		"DELETE FROM GrupoUsuario
		WHERE codigoGrupo = ?codigoGrupo"
	
}