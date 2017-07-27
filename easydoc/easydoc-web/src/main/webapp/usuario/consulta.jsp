<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Consulta de Usuário</h1>
<t:mensagemUsuario />
<form action="Usuario" method="get">
	<input type="hidden" name="acao" value="consultar" />
	<t:select label="Cliente:" id="cliente" name="usuario.cliente.codigoCliente" value="${usuario.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
	<br />
	<t:select label="Tipo de usuário:" id="tipoUsuario" name="usuario.tipoUsuario" value="${usuario.tipoUsuario.codigo}" lista="${tiposUsuario}" />
	<br />
	<t:inputText label="Nome:" id="nome" name="usuario.nome" value="${usuario.nome}" maxlength="100" />
	<br />
	<t:inputText label="Login:" id="login" name="usuario.login" value="${usuario.login}" maxlength="100" />
	<br />
	<t:inputText label="E-mail:" id="email" name="usuario.email" value="${usuario.email}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="usuario.ativo" value="${usuario.ativo}" />
	<br />
	<input type="submit" value="Consultar" />
</form>

<c:if test="${not empty usuarios}">
	<table>
		<thead>
			<tr>
				<th>Cliente</th>
				<th>Tipo de usuário</th>
				<th>Nome</th>
				<th>Login</th>
				<th>E-mail</th>
				<th>Ativo</th>
				<th>Opções</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="usuario" items="${usuarios}">
				<tr>
					<td>${f:imprimir(usuario.cliente.nome)}</td>
					<td>${f:imprimir(usuario.tipoUsuario.descricao)}</td>
					<td>${f:imprimir(usuario.nome)}</td>
					<td>${f:imprimir(usuario.login)}</td>
					<td>${f:imprimir(usuario.email)}</td>
					<td>${f:imprimir(usuario.ativo)}</td>
					<td><a href="Usuario?acao=iniciarAlteracao&proximaAcao=alterar&usuario.codigoUsuario=${f:encriptar(usuario.codigoUsuario)}">Editar</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<t:paginacao url="Usuario" />
</c:if>