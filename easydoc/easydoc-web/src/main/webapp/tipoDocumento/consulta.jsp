<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Consulta de Tipo de documento</h1>
<t:mensagemUsuario />
<form action="TipoDocumento" method="get">
	<input type="hidden" name="acao" value="consultar" />
	<t:select label="Cliente:" id="cliente" name="tipoDocumento.cliente.codigoCliente" value="${tipoDocumento.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
	<br />
	<t:inputText label="Nome:" id="nome" name="tipoDocumento.nome" value="${tipoDocumento.nome}" maxlength="100" />
	<br />
	<input type="submit" value="Consultar" />
</form>

<c:if test="${not empty tiposDocumento}">
	<table>
		<thead>
			<tr>
				<th>Cliente</th>
				<th>Nome</th>
				<th>Opções</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="tipoDocumento" items="${tiposDocumento}">
				<tr>
					<td>${f:imprimir(tipoDocumento.cliente.nome)}</td>
					<td>${f:imprimir(tipoDocumento.nome)}</td>
					<td><a href="TipoDocumento?acao=iniciarAlteracao&proximaAcao=alterar&tipoDocumento.codigoTipoDocumento=${f:encriptar(tipoDocumento.codigoTipoDocumento)}">Editar</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<t:paginacao url="TipoDocumento" />
</c:if>