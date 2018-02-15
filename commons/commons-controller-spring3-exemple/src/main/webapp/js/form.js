function populateSelectMultiple(controller, select) {
	var values = "";
	for ( var i = 0; i < select.options.length; i++) {
		if (select.options[i].selected) {
			
			values = values + encodeURIComponent(select.name) + "="
					+ encodeURIComponent(select.options[i].value)+"&";
		}
	}

	return values;

}

function populateInput(controller, field) {

	if (field.type == 'select-multiple') {
		var valeur = populateSelectMultiple(controller, field);
	} else {

		var valeur = field.name + "="
				+ field.value+"&";
	}

	if(valeur == '&'){
		return;
	}
	
	var parametres = '&' + valeur + 'field=' + field.name;
	
	new Ajax.Request(controller+'/populateField.do', {
		parameters : parametres,
		evalScripts : true,
		onSuccess : function(transport) {
			
			var json = transport.responseText.evalJSON();
			for ( var field in json) {
				exception(json, field);
			}

		},
		onException : function(request, exception) {
			
		},
		onFailure : function(transport) {
			
			var json = transport.responseText.evalJSON();
			for ( var field in json) {
				exception(json, field);
			}
			
		}

	});
}

function exception(json, field) {

	error(field, "");
	error(field, json[field]);
}

function error(field, text) {

	var errorId = $('error_'.concat(field));
	if (errorId != null) {
		errorId.innerHTML = text;
	}
}
