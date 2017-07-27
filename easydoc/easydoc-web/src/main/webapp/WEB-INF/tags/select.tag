<%@ attribute name="id" required="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="lista" required="true" type="java.lang.Object"%>
<%@ attribute name="value" required="false" type="java.lang.Object"%>
<%@ attribute name="propriedadeCodigo" required="false"%>
<%@ attribute name="propriedadeDescricao" required="false"%>
<%@ attribute name="propriedadeDescricaoSeparador" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelSeparador" required="false"%>
<%@ attribute name="encriptaValor" required="false"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<c:set var="propriedadeCodigo" value="${empty propriedadeCodigo ? 'codigo' : propriedadeCodigo}" />
<c:set var="propriedadeDescricao" value="${empty propriedadeDescricao ? 'descricao' : propriedadeDescricao}" />
<c:set var="propriedadeDescricaoSeparador" value="${empty propriedadeDescricaoSeparador ? ' - ' : propriedadeDescricaoSeparador}" />
<c:set var="encriptaValor" value="${empty encriptaValor ? true : encriptaValor}" />
<c:set var="propriedadesDescricao" value="${fn:split(propriedadeDescricao, ', *')}" />

<c:if test="${not empty label}">
	<label for="${id}">${label}</label>
	${labelSeparador}
</c:if>
<select id="${id}" name="${name}">
	<option value="">Selecione</option>
	<c:forEach var="item" items="${lista}">
		<option value="${encriptaValor ? f:encriptar(item[propriedadeCodigo]) : item[propriedadeCodigo]}" ${item[propriedadeCodigo] eq value ? 'selected="selected"' : ''}>
			<c:forEach var="propriedadeDescricaoTemp" items="${propriedadesDescricao}" varStatus="cont">
				<c:if test="${cont.index ge 1}">
					${propriedadeDescricaoSeparador}
				</c:if>
				${f:imprimir(item[propriedadeDescricaoTemp])}
			</c:forEach>
		</option>
	</c:forEach>
</select>