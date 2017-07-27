<%@ attribute name="id" required="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelSeparador" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty label}">
	<label for="${id}">${label}</label>
	${labelSeparador}
</c:if>
<input type="checkbox" id="${id}" name="${name}" value="true" ${value ? 'checked="checked"' : ''} />
<input type="hidden" name="${name}" value="false" />