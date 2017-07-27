<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<html>
	<head>
		<title>${pagina.titulo}</title>
		
		<c:forEach var="estilo" items="${pagina.estilos}">
			<link rel="stylesheet" type="text/css" href="css/${estilo}">
		</c:forEach>
		
		<c:forEach var="script" items="${pagina.scripts}">
			<script type="text/javascript" src="js/${script}"></script>
		</c:forEach>
	</head>
	<body>
		<c:if test="${not empty pagina.cabecalho}">
			<div id="divCabecalho">
				<c:import url="${pagina.cabecalho}" />
			</div>
		</c:if>
		<div id="divCorpo">
			<c:import url="${pagina.corpo}" />
		</div>
	</body>
</html>