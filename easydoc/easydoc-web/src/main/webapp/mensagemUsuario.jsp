<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty mensagemUsuario.mensagensErro}">
	<ul>
		<c:forEach var="mensagem" items="${mensagemUsuario.mensagensErro}">
			<li><c:out value="${mensagem.mensagem}" /></li>
		</c:forEach>
	</ul>
</c:if>
<c:if test="${not empty mensagemUsuario.mensagensAlerta}">
	<ul>
		<c:forEach var="mensagem" items="${mensagemUsuario.mensagensAlerta}">
			<li><c:out value="${mensagem.mensagem}" /></li>
		</c:forEach>
	</ul>
</c:if>
<c:if test="${not empty mensagemUsuario.mensagensSucesso}">
	<ul>
		<c:forEach var="mensagem" items="${mensagemUsuario.mensagensSucesso}">
			<li><c:out value="${mensagem.mensagem}" /></li>
		</c:forEach>
	</ul>
</c:if>
<script type="text/javascript">
	$(document).ready(function() {
		$(".campoErro").removeClass("campoErro");

		<c:forEach var="mensagem" items="${mensagemUsuario.mensagensErro}">
			<c:if test="${not empty mensagem.identificacaoCampo}">
				$("#${mensagem.identificacaoCampo}").addClass("campoErro");
			</c:if>
		</c:forEach>
	});
</script>