<%@ attribute name="arquivo" required="false" type="davidsolutions.easydoc.servico.entidade.Arquivo"%>
<%@ attribute name="posicao" required="true"%>
<%@ attribute name="botaoExcluir" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:set var="botaoExcluir" value="${empty botaoExcluir ? false : botaoExcluir}" />
<div>
	<c:if test="${not empty arquivo.codigoArquivo}">
		<input type="hidden" name="documento.arquivos[${posicao}].codigoArquivo" value="${f:encriptar(arquivo.codigoArquivo)}">
	</c:if>
	<t:inputText label="Descrição:" id="descricao${posicao}" name="documento.arquivos[${posicao}].descricao" maxlength="100" />
	<input type="file" id="conteudo${posicao}" name="documento.arquivos[${posicao}].conteudo" />
	<c:if test="${botaoExcluir}">
		<input type="button" value="Excluir" class="excluiArquivo" />
	</c:if>
</div>