<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Seleção de Tipo de documento</h1>
<t:mensagemUsuario />
<t:select label="Cliente:" id="cliente" name="cliente.codigoCliente" value="${cliente.codigoCliente}" lista="${clientes}" propriedadeCodigo="codigoCliente" propriedadeDescricao="nome" />
<br />
<div id="divTiposDocumento">
	<c:import url="documento/tiposDocumento.jsp" />
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#cliente").change(function() {
			$.get("Documento?acao=listarTipoDocumentoPorCodigoCliente&proximaAcao=${param.proximaAcao}&cliente.codigoCliente=" + $(this).val(), function(data) {
				processarJSON(data);
			});
		});
	});
</script>