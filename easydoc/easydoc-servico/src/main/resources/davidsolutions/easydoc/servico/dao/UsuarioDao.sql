{

	"adicionar":
	
		"INSERT INTO Usuario (
			codigoUsuario,
			codigoCliente,
			codigoTipoUsuario,
			nome,
			login,
			email,
			dataCadastro,
			ativo
		)
		VALUES (
			?codigoUsuario,
			?cliente.codigoCliente,
			?tipoUsuario,
			?nome,
			?login,
			?email,
			?dataCadastro,
			?ativo
		)",
		
	"alterar":
	
		"UPDATE Usuario
		SET
			codigoTipoUsuario = ?tipoUsuario,
			nome = ?nome,
			login = ?login,
			email = ?email,
			ativo = ?ativo
		WHERE codigoUsuario = ?codigoUsuario",
		
	"consultarPorCodigo":
	
		"SELECT
			u.codigoUsuario,
			u.codigoCliente AS cliente_codigoCliente,
			u.codigoTipoUsuario AS tipoUsuario,
			u.nome,
			u.login,
			u.email,
			u.dataAlteracaoSenha,
			u.dataCadastro,
			u.ativo,
			c.nome AS cliente_nome
		FROM Usuario AS u
		INNER JOIN
		Cliente AS c ON u.codigoCliente = c.codigoCliente
		WHERE codigoUsuario = ?codigoUsuario",
		
	"consultar":
	
		"SELECT
			u.codigoUsuario,
			u.codigoCliente AS cliente_codigoCliente,
			u.codigoTipoUsuario AS tipoUsuario,
			u.nome,
			u.login,
			u.email,
			u.dataAlteracaoSenha,
			u.dataCadastro,
			u.ativo,
			c.nome AS cliente_nome
		FROM Usuario AS u
		INNER JOIN
		Cliente AS c ON u.codigoCliente = c.codigoCliente
		{u.codigoCliente = ?cliente.codigoCliente}
		{u.codigoTipoUsuario = ?tipoUsuario}
		{u.nome LIKE ?%nome}
		{u.login LIKE ?%login}
		{u.email LIKE ?%email}
		{u.ativo = ?ativo}
		ORDER BY u.codigoUsuario DESC",
		
	"existeLogin":
	
		"SELECT 1
		FROM Usuario
		WHERE login = ?login
		AND codigoUsuario <> ?codigoUsuario",
		
	"alterarSenha":
	
		"UPDATE Usuario
		SET
			senha = ?senha,
			dataAlteracaoSenha = ?dataAlteracaoSenha
		WHERE codigoUsuario = ?codigoUsuario",
		
	"existeSenha":
	
		"SELECT 1
		FROM HistoricoSenha
		WHERE codigoUsuario = ?codigoUsuario
		AND senha = ?senha",
		
	"adicionarHistoricoSenha":
	
		"INSERT INTO HistoricoSenha (
		    codigoSenha,
		    codigoUsuario,
		    senha
		)
		SELECT
		    ?codigoSenha,
		    codigoUsuario,
		    senha
		FROM Usuario
		WHERE codigoUsuario = ?codigoUsuario
		AND senha IS NOT NULL",
		
	"validarAcesso":
	
		"SELECT
			u.codigoUsuario
		FROM Usuario AS u
		INNER JOIN
		Cliente AS c ON u.codigoCliente = c.codigoCliente
		WHERE u.senha = ?senha
		AND u.ativo = ?ativo
		AND c.ativo = ?cliente.ativo
		{AND u.login = ?login}
		{AND u.codigoUsuario = ?codigoUsuario}",
		
	"existeCodigo":
	
		"SELECT 1
		FROM Usuario
		WHERE codigoUsuario = ?codigoUsuario
		AND ativo = ?ativo",
		
	"listarAtivosPorCodigoCliente":
	
		"SELECT
			codigoUsuario,
			nome
		FROM Usuario
		WHERE codigoCliente = ?codigoCliente
		AND ativo = ?ativo
		ORDER BY nome ASC",
		
	"listarAtivosPorCodigoGrupo":
	
		"SELECT
			u.codigoUsuario,
			u.nome
		FROM Usuario AS u
		INNER JOIN
		GrupoUsuario AS gu ON u.codigoUsuario = gu.codigoUsuario
		WHERE gu.codigoGrupo = ?codigoGrupo
		AND u.ativo = ?ativo
		ORDER BY u.nome ASC"
	
}