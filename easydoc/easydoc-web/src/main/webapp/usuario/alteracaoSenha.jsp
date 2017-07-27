<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/funcoes.tld" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<h1>Alteração de senha</h1>
<t:mensagemUsuario />
<form id="formularioAjax" action="Usuario?acao=alterarSenha" method="post">
	<t:inputPassword label="Senha atual:" id="senhaAtual" name="usuarioAlteracaoSenha.senhaAtual" maxlength="100" />
	<br />
	<t:inputPassword label="Senha nova:" id="senhaNova" name="usuarioAlteracaoSenha.senhaNova" maxlength="100" />
	<br />
	<t:inputPassword label="Repita a senha nova:" id="senhaNovaRepetida" name="usuarioAlteracaoSenha.senhaNovaRepetida" maxlength="100" />
	<br />
	<input type="submit" value="Enviar" />
</form>