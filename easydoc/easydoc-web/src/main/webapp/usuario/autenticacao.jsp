<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Acesso</h1>
<t:mensagemUsuario />
<form action="Usuario?acao=autenticar" method="post">
	<t:inputText label="Login:" id="login" name="usuario.login" value="${usuario.login}" maxlength="100" />
	<br />
	<t:inputPassword label="Senha:" id="senha" name="usuario.senha" maxlength="100" />
	<br />
	<input type="submit" value="Enviar" />
</form>