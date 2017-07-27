var funcaoNumeroInteiro = function() {
	$(this).mask("0#", {
		clearIfNotMatch : true,
		maxlength : false
	});
};

var funcaoNumeroFracionado = function() {
	$(this).mask('#.##0,00', {
		clearIfNotMatch : true,
		maxlength : false,
		reverse : true
	});
};

var funcaoData = function() {
	$(this).datepicker();
	$(this).mask("00/00/0000", {
		clearIfNotMatch : true,
		placeholder : "__/__/____"
	});
};

var funcaoTelefoneSemDdd = function() {
	var funcao = function(val) {
		return val.replace(/\D/g, "").length == 9 ? "00000-0000" : "0000-00009";
	}, opcoes = {
		clearIfNotMatch : true,
		placeholder : "____-____",
		onKeyPress : function(val, e, field, options) {
			field.mask(funcao.apply({}, arguments), options);
		}
	};
	$(this).mask(funcao, opcoes);
};

var funcaoCpf = function() {
	$(this).mask("000.000.000-00", {
		clearIfNotMatch : true,
		placeholder : "___.___.___-__"
	});
};

var funcaoCnpj = function() {
	$(this).mask("00.000.000/0000-00", {
		clearIfNotMatch : true,
		placeholder : "__.___.___/____-__"
	});
};

function processarJSON(json) {
	json = $.parseJSON(json);

	if (json["redirecionamento"]) {
		window.location.replace(json["urlRedirecionamento"]);
	} else {
		$.each(json["fragmentosPagina"], function(chave, valor) {
			$("#" + chave).html(valor);
		});
	}
}

$(document).ready(function() {

	$("input.numeroInteiro").each(funcaoNumeroInteiro);
	$("input.numeroFracionado").each(funcaoNumeroFracionado);
	$("input.data").each(funcaoData);
	$("input.telefoneSemDDD").each(funcaoTelefoneSemDdd);
	$("input.cpf").each(funcaoCpf);
	$("input.cnpj").each(funcaoCnpj);

	$("body").on("focus", "input.numeroInteiro", funcaoNumeroInteiro);
	$("body").on("focus", "input.numeroFracionado", funcaoNumeroFracionado);
	$("body").on("focus", "input.data", funcaoData);
	$("body").on("focus", "input.telefoneSemDDD", funcaoTelefoneSemDdd);
	$("body").on("focus", "input.cpf", funcaoCpf);
	$("body").on("focus", "input.cnpj", funcaoCnpj);

	$("#formularioAjax").submit(function(evento) {

		if ($(this).attr("enctype") == "multipart/form-data") {
			var iframe = $('<iframe src="javascript:false;" name="iframe" style="display:none;" />');
			iframe.appendTo("body");
			$(this).attr("target", "iframe");
			iframe.load(function() {
				var body = iframe.contents().find("body");
				var children = body.children();

				if (children.size() > 0) {
					processarJSON(children.text());
				} else {
					processarJSON(body.text());
				}
				iframe.remove();
			});
		} else {
			if ($(this).attr("method") == "post") {
				$.post($(this).attr("action"), $(this).serialize(), function(resultado) {
					processarJSON(resultado);
				});
			} else {
				$.get($(this).attr("action"), $(this).serialize(), function(resultado) {
					processarJSON(resultado);
				});
			}
			evento.preventDefault();
		}
	});
});