<%@ attribute name="id" required="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="maxlength" required="true"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelSeparador" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>

<c:if test="${not empty label}">
	<label for="${id}">${label}</label>
	${labelSeparador}
</c:if>
<input type="password" id="${id}" name="${name}" value="${f:imprimir(value)}" maxlength="${maxlength}" />