<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Consulta de Grupo</h1>
<t:mensagemUsuario />
<form action="Grupo" method="get">
	<input type="hidden" name="acao" value="consultar" />
	<t:select label="Cliente:" id="cliente" name="grupo.cliente.codigoCliente" value="${grupo.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
	<br />
	<t:inputText label="Nome:" id="nome" name="grupo.nome" value="${grupo.nome}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="grupo.ativo" value="${grupo.ativo}" />
	<br />
	<input type="submit" value="Consultar" />
</form>

<c:if test="${not empty grupos}">
	<table>
		<thead>
			<tr>
				<th>Cliente</th>
				<th>Nome</th>
				<th>Ativo</th>
				<th>Opções</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="grupo" items="${grupos}">
				<tr>
					<td>${f:imprimir(grupo.cliente.nome)}</td>
					<td>${f:imprimir(grupo.nome)}</td>
					<td>${f:imprimir(grupo.ativo)}</td>
					<td><a href="Grupo?acao=iniciarAlteracao&proximaAcao=alterar&grupo.codigoGrupo=${f:encriptar(grupo.codigoGrupo)}">Editar</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<t:paginacao url="Grupo" />
</c:if>