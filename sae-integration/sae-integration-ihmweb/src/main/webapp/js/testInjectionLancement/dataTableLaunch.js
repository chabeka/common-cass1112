function initTable() {

	var proxyInjection = new Ext.data.HttpProxy( {
		url : 'jeuxTestInjectionLancement.do?action=getList'
	});

	var store = new Ext.data.Store( {
		proxy : proxyInjection,
		reader : new Ext.data.JsonReader( {
			root : 'tests'
		}, [ {
			name : 'name',
			type : 'string'
		}, {
			name : 'url',
			type : 'string'
		}, {
			name : 'checked',
			type : 'boolean'
		}, {
			name : 'statusTraitement',
			type : 'string'
		}, {
			name : 'erreur',
			type : 'string'
		} ])
	});

	// tableau des resultats
	var grid = new Ext.grid.GridPanel( {
		height : 280,
		width : 1050,
		title : 'Liste des cas de test',
		store : store,
		trackMouseOver : false,
		disableSelection : true,
		loadMask : true,

		columns : [
				{
					header : 'Sel',
					width : 40,
					dataIndex : 'checked',
					renderer : function(value) {
						return "<input type='checkbox' name='cck'"
								+ (value ? "checked='checked'" : "") + ">";
					}

				},
				{
					header : 'nom cas de test',
					width : 320,
					dataIndex : 'name',
					sortable : true
				},
				{
					header : 'url',
					dataIndex : 'url',
					width : 320,
					sortable : true,
					align : 'left'
				},
				{
					header : 'etat',
					dataIndex : 'statusTraitement',
					width : 60,
					sortable : true,
					align : 'left',
					renderer : function(value) {
						var img = "js/extjs/resources/images/access/grid/";
						if (value == "notConcerned" || value == "") {
							img = img + "dirty.gif";
						} else if (value == "error") {
							img = img + "drop-no.gif";
						} else if (value == "running") {
							img = img + "wait.gif";
						} else if (value == "completed") {
							img = img + "drop-yes.gif";
						} else {
							img = img + "page-last-disabled.gif";
						}

						return "<img src='" + img + "' />";
					}
				},
				{
					header : 'message',
					dataIndex : 'erreur',
					width : 300,
					sortable : true,
					align : 'left',
					renderer : function(value) {
						return '<div ext:qtip="' + value + '">' + value
								+ '</div>';
					}
				} ]
	});

	store.load();

	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	return grid;
}

function initForm(gridTable) {

	var monFormulaire = new Ext.FormPanel( {
		url : 'jeuxTestInjectionLancement.do?action=launch',
		renderTo : 'dataTableForm',
		frame : true,
		id : 'formPanel',
		// title : 'Saisie des Informations Personnelles',
		width : 1100
	});

	// Un champ texte
	var urlWS = new Ext.form.TextField( {
		fieldLabel : 'URL WS',
		name : 'url',
		id : 'url',
		width : 600
	});

	monFormulaire.add(urlWS);

	monFormulaire.add(gridTable);

	var monBoutonValidation = new Ext.Button( {
		text : 'Injecter les jeux de test',
		id : 'btnValider',
		handler : function() {
			var tempName;

			var check = document.getElementsByName('cck');
			var checkLength = check.length;
			var allRecords = gridTable.store.getRange();
			var nbCk = 0;

			var oldOnes = document.getElementsByName("treatmentList");
			var oldLength = oldOnes.length;
			for ( var i = 0; i < oldLength; i++) {
				var id = oldOnes[i].getAttribute("id");
				monFormulaire.remove(Ext.getCmp(id));
			}
			for ( var i = 0; i < checkLength; i++) {
				if (check[i].checked) {
					var tId = 'treatmentList' + i;
					nbCk++;
					tempName = new Ext.form.Hidden( {
						name : 'treatmentList',
						id : tId,
						value : allRecords[i].data["url"]
					});
					monFormulaire.add(tempName);
				}
			}
			monFormulaire.doLayout();

			if (nbCk < 1) {
				alert("Il n'y a aucun jeu de test à injecter");
			} else {
				monFormulaire.getForm().submit( {
					success : function(response, opts) {

						disable('btnValider', true);

						runner.start(task);
						runner.start(taskLookStatus);
					},
					failure : function(response, opts) {
						alert("le traitement n'est pas lancé");
					}
				});
			}
		}
	});

	Ext.Ajax.request( {
		url : 'jeuxTestInjectionLancement.do',
		params : {
			action : 'getUrl'
		},
		method : 'GET',
		success : function(response, opts) {
			var jsonData = Ext.util.JSON.decode(response.responseText);
			Ext.getCmp("url").setValue(jsonData.url);

			if (jsonData.isRunning) {
				disable('btnValider', true);
				runner.start(task);
				runner.start(taskLookStatus);
			}
		}
	});

	monFormulaire.add(monBoutonValidation);

	// Dessin du formulaire
	monFormulaire.doLayout();
}

function initTasks(gridTable) {
	task = {
		run : function() {
			gridTable.store.load();
		},
		interval : 10001
	// 1 second
	};

	taskLookStatus = {
		run : function() {
			Ext.Ajax.request( {
				url : 'jeuxTestInjectionLancement.do',
				params : {
					action : 'checkStatus'
				},
				method : 'GET',
				success : function(response, opts) {
					var jsonData = Ext.util.JSON.decode(response.responseText);
					if (!jsonData.success) {
						runner.stop(task);
						runner.stop(taskLookStatus);
						gridTable.store.load();
						alert('Traitement terminé');
						disable('btnValider', false);
					}
				},
				failure : function(result, request) {
					runner.stop(task);
					runner.stop(taskLookStatus);
					gridTable.store.load();
					alert('Traitement terminé');
					disable('btnValider', false);
				}
			});
		},
		interval : 10000
	// 1 second
	};
}

function disable(id, disable) {

	Ext.getCmp(id).disabled = disable;

}

var task;
var taskLookStatus;
var runner = new Ext.util.TaskRunner();

var gridRendered = initTable();

initTasks(gridRendered);

initForm(gridRendered);