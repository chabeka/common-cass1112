function dateToTs_init() {

   // Le formulaire
   var monFormulaire = new Ext.FormPanel( {
      renderTo : 'calcDiv3',
      frame : true,
      id : 'dateToTs_formPanel',
      width : 1100
   });
   
   // Champ pour la saisie de la date
   var dateField = new Ext.form.DateField( {
      fieldLabel : 'Date',
      name : 'dateToTs_date',
      id : 'dateToTs_date',
      width : 110,
      format : "d/m/Y",
      allowBlank : false,
      blankText : 'Il faut saisir la date'
   });
   
   // Champ pour la saisie de l'heure
   var heureField = new Ext.form.TimeField( {
      fieldLabel : 'Heure',
      name : 'dateToTs_heure',
      id : 'dateToTs_heure',
      width : 90,
      format : "H:i:s",
      allowBlank : false,
      blankText : 'Il faut saisir l\'heure'
   });
   
   // Champ pour l'affichage du timestamp
   var timestampField = new Ext.form.NumberField( {
      fieldLabel : 'Timestamp (en ms)',
      name : 'dateToTs_timestamp',
      id : 'dateToTs_timestamp',
      width : 120
   });
   
   // Bouton submit
   var submit = new Ext.Button( {
      text : 'Calcul',
      id : 'dateToTs_btnSubmit',
      handler : function() {
      dateToTs_calcul(monFormulaire, timestampField, dateField, heureField);
      }
   });
   

   // Ajoute les champs au formulaire
   monFormulaire.add(dateField);
   monFormulaire.add(heureField);
   monFormulaire.add(submit);
   monFormulaire.add(timestampField);
   
   // Dessin du formulaire
   monFormulaire.doLayout();
}


function dateToTs_calcul(form, timestampField, dateField, heureField) {
   
   if (!form.getForm().isValid()) {
      alert('La saisie n\'est pas valide');
      return ;
   }
   
   timestampField.setValue(null);
   
   var dateValue = dateField.getValue();
   dateValue = Ext.util.Format.date(dateValue,'d/m/Y');
   
   var heureValue = heureField.getValue();
   
   Ext.Ajax.request( {
      url : 'calcTemp.do',
      params : {
         action: 'convertDateToTimestamp',
         date : dateValue,
         heure : heureValue
      },
      method : 'POST',
      success : function(response, opts) {

         var jsonData = Ext.util.JSON.decode(response.responseText);

         if (jsonData.success) {
            timestampField.setValue(jsonData.timestamp);
         } else {
            alert(jsonData.message);
         }
      },
      failure : function(response, opts) {
         alert("Erreur lors de l'appel de la méthode de calcul !");
      }
   });
   
}



dateToTs_init();


