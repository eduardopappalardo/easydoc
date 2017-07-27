<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Inclusão/alteração de Grupo</h1>
<t:mensagemUsuario />
<form id="formularioAjax" action="Grupo?acao=${param.proximaAcao}" method="post">
	<c:choose>
		<c:when test="${not empty clientes}">
			<t:select label="Cliente:" id="cliente" name="grupo.cliente.codigoCliente" value="${grupo.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
			<br />
		</c:when>
		<c:otherwise>
			<input type="hidden" name="grupo.codigoGrupo" value="${f:encriptar(grupo.codigoGrupo)}" />
			<t:inputText label="Cliente:" id="cliente" name="grupo.cliente.nome" value="${grupo.cliente.nome}" maxlength="100" readonly="true" />
			<br />
		</c:otherwise>
	</c:choose>
	<t:inputText label="Nome:" id="nome" name="grupo.nome" value="${grupo.nome}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="grupo.ativo" value="${grupo.ativo}" />
	<br />
	<div id="divUsuarios">
		<c:import url="grupo/usuarios.jsp" />
	</div>
	<input type="submit" value="Salvar" />
</form>
<script type="text/javascript">
	$(document).ready(function() {
		$("#cliente").change(function() {
			$.get("Grupo?acao=listarUsuariosPorCodigoCliente&grupo.cliente.codigoCliente=" + $(this).val(), function(resultado) {
				processarJSON(resultado);
			});
		});
	});
</script>