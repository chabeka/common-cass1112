function tsToDate_init() {

   // Le formulaire
   var monFormulaire = new Ext.FormPanel( {
      renderTo : 'calcDiv2',
      frame : true,
      id : 'tsToDate_formPanel',
      width : 1100
   });
   
   // Champ pour la saisie du timestamp
   var timestampField = new Ext.form.NumberField( {
      fieldLabel : 'Timestamp (en ms)',
      name : 'tsToDate_timestamp',
      id : 'tsToDate_timestamp',
      width : 120,
      allowBlank : false,
      blankText : 'Il faut saisir le timestamp',
      allowDecimals : false,
      allowNegative : false
   });
   
   // Champ pour afficher la date
   var dateField = new Ext.form.DateField( {
      fieldLabel : 'Date',
      name : 'tsToDate_date',
      id : 'tsToDate_date',
      width : 110,
      format : "d/m/Y"
   });
   
   // Champ pour afficher l'heure
   var heureField = new Ext.form.TimeField( {
      fieldLabel : 'Heure',
      name : 'tsToDate_heure',
      id : 'tsToDate_heure',
      width : 90,
      format : "H:i:s"
   });
   
   // Bouton submit
   var submit = new Ext.Button( {
      text : 'Calcul',
      id : 'tsToDate_btnSubmit',
      handler : function() {
         tsToDate_calcul(monFormulaire, timestampField, dateField, heureField);
      }
   });
   

   // Ajoute les champs au formulaire
   monFormulaire.add(timestampField);
   monFormulaire.add(submit);
   monFormulaire.add(dateField);
   monFormulaire.add(heureField);
   
   // Dessin du formulaire
   monFormulaire.doLayout();
}


function tsToDate_calcul(form, timestampField, dateField, heureField) {
   
   if (!form.getForm().isValid()) {
      alert('La saisie n\'est pas valide');
      return ;
   }
   
   dateField.setValue(null);
   heureField.setValue(null);
   
   var timestampValue = timestampField.getValue();
   
   Ext.Ajax.request( {
      url : 'calcTemp.do',
      params : {
         action: 'convertTimestampToDate',
         timestamp : timestampValue
      },
      method : 'POST',
      success : function(response, opts) {

         var jsonData = Ext.util.JSON.decode(response.responseText);

         if (jsonData.success) {
            dateField.setValue(jsonData.date);
            heureField.setValue(jsonData.heure);
         } else {
            alert(jsonData.message);
         }
      },
      failure : function(response, opts) {
         alert("Erreur lors de l'appel de la méthode de calcul !");
      }
   });
   
}



tsToDate_init();


