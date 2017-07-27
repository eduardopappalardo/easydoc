<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:forEach var="tipoDocumento" items="${tiposDocumento}">
	<a href="Documento?acao=${param.proximaAcao}&tipoDocumento.codigoTipoDocumento=${f:encriptar(tipoDocumento.codigoTipoDocumento)}">${tipoDocumento.nome}</a>
	<br />
</c:forEach>