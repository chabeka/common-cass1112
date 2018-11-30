function init() {

	var monFormulaire = new Ext.FormPanel( {
		renderTo : 'calcDiv',
		frame : true,
		id : 'formPanel',
		width : 1100
	});

	var startDate = new Ext.form.DateField( {
		fieldLabel : 'Date de début',
		name : 'startDate',
		id : 'startDate',
		width : 190,
		allowBlank : false,
		blankText: 'Il faut spécifier la date de début',
		format : "d/m/Y"
	});

	monFormulaire.add(startDate);

	// Un champ texte
	var time = new Ext.form.TextField( {
		fieldLabel : 'Durée (en jours)',
		name : 'time',
		id : 'time',
		width : 100,
		allowBlank : false,
		blankText: 'Il faut spécifier la durée'
	});

	monFormulaire.add(time);

	// Un champ texte
	var endDate = new Ext.form.TextField( {
		fieldLabel : 'Date de fin (calculée)',
		name : 'endDate',
		id : 'endDate',
		width : 100,
		readOnly : true
	});

	var submit = new Ext.Button( {
		text : 'Lancer le calcul',
		id : 'btnSubmit',
		handler : function() {
			majEndDate(startDate, endDate, time, monFormulaire);
		}
	});

	monFormulaire.add(submit);

	monFormulaire.add(endDate);

	getDateDebut(monFormulaire, startDate);

	monFormulaire.doLayout();
}

function getDateDebut(form, startDate) {

	Ext.Ajax.request( {
		url : 'calcTemp.do',
		params : {
			action : 'getDate'
		},
		method : 'GET',
		success : function(response, opts) {
			var jsonData = Ext.util.JSON.decode(response.responseText);

			var value = jsonData.startDate;
			startDate.setValue(value);
		}
	});
}

function majEndDate(startDateField, endDate, timeField, form) {

	if (!form.getForm().isValid()) {
	   alert('La saisie n\'est pas valide');
	   return ;
	}
   
   var dateValue = Ext.util.Format.date(startDateField.getValue(), "d/m/Y");
	var timeValue = timeField.getValue();

	Ext.Ajax.request( {
		url : 'calcTemp.do',
		params : {
   	   action : 'calculDateFin',	
   	   startDate : dateValue,
			time : timeValue
		},
		method : 'POST',
		success : function(response, opts) {

			var jsonData = Ext.util.JSON.decode(response.responseText);

			if (jsonData.success) {
				endDate.setValue(jsonData.endDate);
			} else {
				alert(jsonData.message);
			}
		},
		failure : function(response, opts) {
		   alert("impossible d'appeler le calcul de temps");
		}
	});

}

Ext.QuickTips.init();

init();