<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Inclusão/alteração de Cliente</h1>
<t:mensagemUsuario />
<form id="formularioAjax" action="Cliente?acao=${param.proximaAcao}" method="post">
	<input type="hidden" name="cliente.codigoCliente" value="${f:encriptar(cliente.codigoCliente)}" />
	<t:inputText label="Nome:" id="nome" name="cliente.nome" value="${cliente.nome}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="cliente.ativo" value="${cliente.ativo}" />
	<br />
	<c:if test="${not empty cliente.grupos}">
		<table>
			<thead>
				<tr>
					<th>Grupos</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="grupo" items="${cliente.grupos}">
					<tr>
						<td><a href="Grupo?acao=iniciarAlteracao&proximaAcao=alterar&grupo.codigoGrupo=${f:encriptar(grupo.codigoGrupo)}">${f:imprimir(grupo.nome)}</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<input type="submit" value="Salvar" />
</form>