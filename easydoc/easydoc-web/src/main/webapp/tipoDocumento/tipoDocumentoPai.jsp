<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:if test="${not empty tiposDocumento}">
	<t:select label="Tipo de documento Pai:" id="tipoDocumentoPai" name="tipoDocumento.tipoDocumentoPai.codigoTipoDocumento" value="${tipoDocumento.tipoDocumentoPai.codigoTipoDocumento}" lista="${tiposDocumento}" propriedadeCodigo="codigoTipoDocumento" propriedadeDescricao="nome" />
	<br />
</c:if>