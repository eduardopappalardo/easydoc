<%@ attribute name="id" required="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="maxlength" required="true"%>
<%@ attribute name="value" required="false" type="java.lang.Object"%>
<%@ attribute name="readonly" required="false"%>
<%@ attribute name="classe" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelSeparador" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:if test="${not empty label}">
	<label for="${id}">${label}</label>
	${labelSeparador}
</c:if>
<input type="text" id="${id}" name="${name}" value="${f:imprimir(value)}" maxlength="${maxlength}" class="${classe}" ${readonly ? 'readonly="readonly"' : ''} />