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
<select id="${id}" name="${name}">
	<option value="">Selecione</option>
	<option value="true" ${value ? 'selected="selected"' : ''}>Sim</option>
	<option value="false" ${value eq 'false' ? 'selected="selected"' : ''}>Não</option>
</select>