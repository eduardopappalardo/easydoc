<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Inclusão/alteração de Tipo de documento</h1>
<t:mensagemUsuario />
<form id="formularioAjax" action="TipoDocumento?acao=${param.proximaAcao}" method="post">
	<c:choose>
		<c:when test="${not empty clientes}">
			<t:select label="Cliente:" id="cliente" name="tipoDocumento.cliente.codigoCliente" value="${tipoDocumento.cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
			<br />
		</c:when>
		<c:otherwise>
			<input type="hidden" id="codigoTipoDocumento" name="tipoDocumento.codigoTipoDocumento" value="${f:encriptar(tipoDocumento.codigoTipoDocumento)}" />
			<t:inputText label="Cliente:" id="cliente" name="tipoDocumento.cliente.nome" value="${tipoDocumento.cliente.nome}" maxlength="100" readonly="true" />
			<br />
		</c:otherwise>
	</c:choose>
	<t:inputText label="Nome:" id="nome" name="tipoDocumento.nome" value="${tipoDocumento.nome}" maxlength="100" />
	<div id="divTipoDocumentoPai">
		<c:import url="tipoDocumento/tipoDocumentoPai.jsp" />
	</div>
	<div id="divIndicesHerdados">
		<c:import url="tipoDocumento/indicesHerdados.jsp" />
	</div>
	<div id="indices">
		<h2>Índices</h2>
		<c:choose>
			<c:when test="${not empty tipoDocumento.indices}">
				<c:forEach var="indice" items="${tipoDocumento.indices}" varStatus="cont">
					<t:indice indice="${indice}" posicao="${cont.index}" botaoExcluir="${cont.index gt 0}" />
				</c:forEach>
			</c:when>
			<c:otherwise>
				<t:indice posicao="0" />
			</c:otherwise>
		</c:choose>
	</div>
	<br />
	<input type="button" id="adicionarIndice" value="Adicionar índice" />
	<input type="submit" value="Salvar" />

	<c:if test="${not empty tipoDocumento.codigoTipoDocumento}">
		<input type="button" id="excluir" value="Excluir" />
	</c:if>
</form>
<div id="indiceModelo" style="display: none;">
	<t:indice posicao="#" botaoExcluir="true" />
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#excluir").click(function() {
			$.post("TipoDocumento?acao=excluir", "tipoDocumento.codigoTipoDocumento=" + $("#codigoTipoDocumento").val(), function(resultado) {
				processarJSON(resultado);
			});
		});
		$("#adicionarIndice").click(function() {
			$("#indices").append($("#indiceModelo").html().replace(/#/g, $("#indices div").length));
		});
		$("#indices").on("click", ".excluiIndice", function() {
			$(this).parent().remove();
		});
		$("#cliente").change(function() {
			$.get("TipoDocumento?acao=listarTipoDocumentoPorCodigoCliente&tipoDocumento.cliente.codigoCliente=" + $(this).val(), function(resultado) {
				processarJSON(resultado);
				$("#divIndicesHerdados").empty();
			});
		});
		$("body").on("change", "#tipoDocumentoPai", function() {
			$.get("TipoDocumento?acao=obterIndicesTipoDocumento&tipoDocumento.codigoTipoDocumento=" + $(this).val(), function(resultado) {
				processarJSON(resultado);
			});
		});
	});
</script>