<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:if test="${not empty indicesHerdados}">
	<h2>Índices Herdados</h2>
	<c:forEach var="indice" items="${indicesHerdados}">
		<div>
			<label>Nome do índice:</label> ${f:imprimir(indice.nome)}
			<label>Tipo de índice:</label> ${f:imprimir(indice.tipoIndice)}
			<label>Preenchimento obrigatório:</label> ${f:imprimir(indice.preenchimentoObrigatorio)}
		</div>
	</c:forEach>
</c:if>