<%@ attribute name="indice" required="false" type="davidsolutions.easydoc.servico.entidade.Indice"%>
<%@ attribute name="posicao" required="true"%>
<%@ attribute name="botaoExcluir" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<%@ tag import="davidsolutions.easydoc.servico.entidade.TipoIndice"%>

<c:set var="botaoExcluir" value="${empty botaoExcluir ? false : botaoExcluir}" />
<div>
	<c:if test="${not empty indice.codigoIndice}">
		<input type="hidden" name="tipoDocumento.indices[${posicao}].codigoIndice" value="${f:encriptar(indice.codigoIndice)}">
	</c:if>
	<t:inputText label="Nome do índice:" id="nomeIndice${posicao}" name="tipoDocumento.indices[${posicao}].nome" value="${indice.nome}" maxlength="100" />
	<t:select label="Tipo de índice:" id="tipoIndice${posicao}" name="tipoDocumento.indices[${posicao}].tipoIndice" value="${indice.tipoIndice.codigo}" lista="<%=TipoIndice.values()%>" />
	<t:inputCheckboxBoolean label="Preenchimento obrigatório:" id="preenchimentoObrigatorio${posicao}" name="tipoDocumento.indices[${posicao}].preenchimentoObrigatorio" value="${indice.preenchimentoObrigatorio}" />
	<c:if test="${botaoExcluir}">
		<input type="button" value="Excluir" class="excluiIndice" />
	</c:if>
</div>