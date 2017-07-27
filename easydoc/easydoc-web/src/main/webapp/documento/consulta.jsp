<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Consulta de documento</h1>
<h2>${tipoDocumento.nome}</h2>
<t:mensagemUsuario />
<form action="Documento" method="get">
	<input type="hidden" name="acao" value="consultar" />
	<input type="hidden" name="documento.tipoDocumento.codigoTipoDocumento" value="${f:encriptar(tipoDocumento.codigoTipoDocumento)}" />
	<c:forEach var="indice" items="${tipoDocumento.todosIndices}" varStatus="cont">
		<c:choose>
			<c:when test="${indice.tipoIndice eq 'DATA'}">
				<t:inputText label="${indice.nome}: de" id="valor${cont.index}_1" name="documento.indices[${cont.index}].valores" maxlength="200" classe="${indice.tipoIndice.classe}" />
				<t:inputText label="até" id="valor${cont.index}_2" name="documento.indices[${cont.index}].valores" maxlength="200" classe="${indice.tipoIndice.classe}" />
			</c:when>
			<c:otherwise>
				<t:inputText label="${indice.nome}:" id="valor${cont.index}" name="documento.indices[${cont.index}].valores" maxlength="200" classe="${indice.tipoIndice.classe}" />
			</c:otherwise>
		</c:choose>
		<input type="hidden" name="documento.indices[${cont.index}].codigoIndice" value="${f:encriptar(indice.codigoIndice)}" />
		<br />
	</c:forEach>
	<input type="submit" value="Consultar" />
</form>

<c:if test="${not empty documentos}">
	<table>
		<thead>
			<tr>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="documento" items="${documentos}">
				<tr>
					<td></td>
					<td><a href="Documento?acao=iniciarAlteracao&proximaAcao=alterar&documento.codigoDocumento=${f:encriptar(documento.codigoDocumento)}">Editar</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<t:paginacao url="Documento" />
</c:if>