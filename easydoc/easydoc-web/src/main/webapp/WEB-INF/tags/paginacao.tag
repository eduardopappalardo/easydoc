<%@ attribute name="url" required="true"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty paginacao}">
	<%
		String parametroPagina = "&paginacao.numeroPagina=";
		String queryString = request.getQueryString().replaceFirst(parametroPagina + "\\d+", "");
	%>
	<div>
		<c:if test="${paginacao.numeroPagina gt 1}">
			<a href="${url}?<%=queryString%><%=parametroPagina%>${paginacao.numeroPagina - 1}">Anterior</a>
		</c:if>
		<c:forEach begin="1" end="${paginacao.totalPaginas}" var="numeroPagina">
			<c:choose>
				<c:when test="${numeroPagina eq paginacao.numeroPagina}">
					<b>${numeroPagina}</b>
				</c:when>
				<c:otherwise>
					<a href="${url}?<%=queryString%><%=parametroPagina%>${numeroPagina}">${numeroPagina}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${paginacao.numeroPagina lt paginacao.totalPaginas}">
			<a href="${url}?<%=queryString%><%=parametroPagina%>${paginacao.numeroPagina + 1}">Próxima</a>
		</c:if>
	</div>
	<div>Exibindo de ${paginacao.posicaoInicialPagina} até ${paginacao.posicaoFinalPagina} de ${paginacao.totalRegistros} registro(s).</div>
</c:if>