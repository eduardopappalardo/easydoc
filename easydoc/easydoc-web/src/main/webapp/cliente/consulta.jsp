<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Consulta de Cliente</h1>
<t:mensagemUsuario />
<form action="Cliente" method="get">
	<input type="hidden" name="acao" value="consultar" />
	<t:inputText label="Nome:" id="nome" name="cliente.nome" value="${cliente.nome}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="cliente.ativo" value="${cliente.ativo}" />
	<br />
	<input type="submit" value="Consultar" />
</form>

<c:if test="${not empty clientes}">
	<table>
		<thead>
			<tr>
				<th>Nome</th>
				<th>Ativo</th>
				<th>Opções</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="cliente" items="${clientes}">
				<tr>
					<td>${f:imprimir(cliente.nome)}</td>
					<td>${f:imprimir(cliente.ativo)}</td>
					<td><a href="Cliente?acao=iniciarAlteracao&proximaAcao=alterar&cliente.codigoCliente=${f:encriptar(cliente.codigoCliente)}">Editar</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<t:paginacao url="Cliente" />
</c:if>