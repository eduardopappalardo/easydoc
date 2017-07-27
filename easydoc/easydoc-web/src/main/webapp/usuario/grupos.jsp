<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:if test="${not empty grupos}">
	<table>
		<thead>
			<tr>
				<th>Grupos</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="grupo" items="${grupos}" varStatus="cont">
				<tr>
					<td><input type="checkbox" name="usuario.grupos[${cont.index}].codigoGrupo" value="${f:encriptar(grupo.codigoGrupo)}" ${f:listaContem(usuario.grupos, grupo) ? 'checked="checked"' : ''} /></td>
					<td>${f:imprimir(grupo.nome)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>