<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Inclusão/alteração de documento</h1>
<h2>${tipoDocumento.nome}</h2>
<t:mensagemUsuario />
<form id="formularioAjax" action="Documento?acao=${not empty documento.codigoDocumento ? 'alterar' : 'adicionar'}" method="post" enctype="multipart/form-data">
	<input type="hidden" name="documento.tipoDocumento.codigoTipoDocumento" value="${f:encriptar(tipoDocumento.codigoTipoDocumento)}" />
	<c:forEach var="indice" items="${tipoDocumento.todosIndices}" varStatus="cont">
		<t:inputText label="${indice.nome}${indice.preenchimentoObrigatorio ? '*' : ''}:" id="valor${cont.index}" name="documento.indices[${cont.index}].valores" maxlength="200" classe="${indice.tipoIndice.classe}" />
		<input type="hidden" name="documento.indices[${cont.index}].codigoIndice" value="${f:encriptar(indice.codigoIndice)}" />
		<br />
	</c:forEach>
	<div id="arquivos">
		<h3>Arquivos</h3>
		<c:choose>
			<c:when test="${not empty documento.arquivos}">
				<c:forEach var="arquivo" items="${documento.arquivos}" varStatus="cont">
					<t:arquivo arquivo="${arquivo}" posicao="${cont.index}" botaoExcluir="${cont.index gt 0}" />
				</c:forEach>
			</c:when>
			<c:otherwise>
				<t:arquivo posicao="0" />
			</c:otherwise>
		</c:choose>
	</div>
	<br />
	<input type="button" id="adicionarArquivo" value="Adicionar arquivo" />
	<input type="submit" value="Salvar" />
</form>
<div id="arquivoModelo" style="display: none;">
	<t:arquivo posicao="#" botaoExcluir="true" />
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#adicionarArquivo").click(function() {
			$("#arquivos").append($("#arquivoModelo").html().replace(/#/g, $("#arquivos div").length));
		});
		$("#arquivos").on("click", ".excluiArquivo", function() {
			$(this).parent().remove();
		});
	});
</script>