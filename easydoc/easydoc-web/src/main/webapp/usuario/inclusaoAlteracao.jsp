<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Inclusão/alteração de Usuário</h1>
<t:mensagemUsuario />
<form id="formularioAjax" action="Usuario?acao=${param.proximaAcao}" method="post">
	<c:choose>
		<c:when test="${not empty clientes}">
			<t:select label="Cliente:" id="cliente" name="usuario.cliente.codigoCliente" value="${usuario.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
			<br />
		</c:when>
		<c:otherwise>
			<input type="hidden" id="codigoUsuario" name="usuario.codigoUsuario" value="${f:encriptar(usuario.codigoUsuario)}" />
			<t:inputText label="Cliente:" id="cliente" name="usuario.cliente.nome" value="${usuario.cliente.nome}" maxlength="100" readonly="true" />
			<br />
		</c:otherwise>
	</c:choose>
	<t:select label="Tipo de usuário:" id="tipoUsuario" name="usuario.tipoUsuario" value="${usuario.tipoUsuario.codigo}" lista="${tiposUsuario}" />
	<br />
	<t:inputText label="Nome:" id="nome" name="usuario.nome" value="${usuario.nome}" maxlength="100" />
	<br />
	<t:inputText label="Login:" id="login" name="usuario.login" value="${usuario.login}" maxlength="100" />
	<br />
	<t:inputText label="E-mail:" id="email" name="usuario.email" value="${usuario.email}" maxlength="100" />
	<br />
	<t:selectBoolean label="Ativo:" id="ativo" name="usuario.ativo" value="${usuario.ativo}" />
	<br />
	<div id="divGrupos">
		<c:import url="usuario/grupos.jsp" />
	</div>
	<input type="submit" value="Salvar" />

	<c:if test="${not empty usuario.codigoUsuario and usuario.ativo}">
		<input type="button" id="gerarSenha" value="Gerar senha" />
	</c:if>
</form>
<script type="text/javascript">
	$(document).ready(function() {
		$("#gerarSenha").click(function() {
			$.post("Usuario?acao=gerarSenhaAleatoria", "usuario.codigoUsuario=" + $("#codigoUsuario").val(), function(resultado) {
				processarJSON(resultado);
			});
		});
		$("#cliente").change(function() {
			$.get("Usuario?acao=listarGruposPorCodigoCliente&usuario.cliente.codigoCliente=" + $(this).val(), function(resultado) {
				processarJSON(resultado);
			});
		});
	});
</script>