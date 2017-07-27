<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:if test="${not empty usuarios}">
	<table>
		<thead>
			<tr>
				<th>Usuários</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="usuario" items="${usuarios}" varStatus="cont">
				<tr>
					<td><input type="checkbox" name="grupo.usuarios[${cont.index}].codigoUsuario" value="${f:encriptar(usuario.codigoUsuario)}" ${f:listaContem(grupo.usuarios, usuario) ? 'checked="checked"' : ''} /></td>
					<td>${f:imprimir(usuario.nome)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>