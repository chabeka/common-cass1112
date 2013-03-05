
function createTraceTable(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div class="table">',
        '<div id="compteur">Nombre de traces : <b>{values.compteur}</b></div>',
        '<br />',
        '<table border="1" style="font-size:8pt;width:100%;">',
           '<tr style="font-weight:bold;">',
              '<td style="width:100px;">id</td>',
              '<td>code evt</td>',
              '<td style="width:100px;">timestamp</td>',
              '<td>contexte</td>',
              '<td>cs</td>',
              '<td>pagms</td>',
              '<td>login</td>',
              '<td>stacktrace</td>',
              '<td>infos</td>',
           '</tr>',
           '<tpl for="traces">',
              '<tpl for="trace">',
                 '<tr>',
                    '<td>{identifiant}</td>',
                    '<td>{codeEvt}</td>',
                    '<td>{[new Date(values.timestamp).toLocaleString()]}</td>',
                    '<td><tpl if="contexte!=null">{contexte}</tpl></td>',
                    '<td><tpl if="contrat!=null">{contrat}</tpl></td>',
                    '<td>',
                       '<tpl for="pagms">',
                       '<div>{.}</div>',
                       '</tpl>',
                    '</td>',
                    '<td><tpl if="login!=null">{login}</tpl></td>',
                    '<td>',
                       '<tpl if="stacktrace!=null"><a href="#" onclick="javascript:getPopUp(&quot;{identifiant}&quot;);" >stacktrace</a></tpl>',
                    '</td>',
                    '<td>',
                       '<tpl if="infos!=null"><div><a href="#" onclick="javascript:getPopUpInfo(&quot;{identifiant}&quot;);" >Afficher les infos</a></div></tpl>',
                    '</td>',
                 '</tr>',
              '</tpl>',
           '</tpl>',
        '</table>',
        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}



function createJournauxTable(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div class="table">',
        '<div>Nombre de traces : <b>{values.compteur}</b></div>',
        '<br />',
        '<table border="1" style="font-size:8pt;width:100%;">',
           '<tr style="font-weight:bold;">',
              '<td style="width:100px;">id</td>',
              '<td>code evt</td>',
              '<td style="width:100px;">timestamp</td>',
              '<td>contexte</td>',
              '<td>cs</td>',
              '<td>pagms</td>',
              '<td>login</td>',
              '<td>infos</td>',
           '</tr>',
           '<tpl for="traces">',
              '<tpl for="trace">',
                 '<td>{identifiant}</td>',
                 '<td>{codeEvt}</td>',
                 '<td style="width:100px;">{[new Date(values.timestamp).toLocaleString()]}</td>',
                 '<td><tpl if="contexte!=null">{contexte}</tpl></td>',
                 '<td><tpl if="contratService!=null">{contratService}</tpl></td>',
                 '<td>',
                    '<tpl for="pagms">',
                    '<div>{.}</div>',
                    '</tpl>',
                 '</td>',
                 '<td><tpl if="login!=null">{login}</tpl></td>',
                 '<td>',
                 '<tpl if="infos!=null"><div><a href="#" onclick="javascript:getPopUpInfo(&quot;{identifiant}&quot;);" >Afficher les infos</a></div></tpl>',
                 '</td>',
              '</tr>',
              '</tpl>',
           '</tpl>',
        '</table>',
        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}


function createPopUp(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div class="table">',
        
        '<tpl for="trace">',
        '<div class="trace" style="float:left">',
           '<div class="detail">{stacktrace}</div>',
        '</tpl>',

        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}


function createPopUpInfo(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div>',
           '<div>&nbsp;</div>',
           '<tpl for=".">',
              '<div style="float:left">',              
              '<div style="float:left">{.}</div>',
              '<div>&nbsp;</div>',
           '</tpl>',
        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}



function getPopUp(id){
      Ext.Ajax.request( {
      url : 'tracePopUp.do',
      params : {
         action : 'getTracePopUp',
         uuid: id
      },
      method : 'GET',
      success : function(response, opts) {
        var jsonData = Ext.util.JSON.decode(response.responseText);
        var tableDom = Ext.DomHelper;
           
        var tplInstancie = createPopUp(jsonData);
        
        new Ext.Window({           
           html: tplInstancie,
           shadow:false,
           width:800,
           height:500,
           autoScroll:true,
           title:'Détail de la stack trace'
        }).show();

      }
   });
}

function getPopUpInfo(info){
   Ext.Ajax.request( {
      url : document.getElementById("infoPopUpUrl").value,
      params : {
         action : document.getElementById("popUpAction").value,
         uuid: info
      },
      method : 'GET',
      success : function(response, opts) {
        var jsonData = Ext.util.JSON.decode(response.responseText);
        var tableDom = Ext.DomHelper;
           
        var tplInstancie = createPopUpInfo(jsonData);
        
        new Ext.Window({           
           html: tplInstancie,
           shadow:false,
           width:800,
           height:500,
           autoScroll:true,
           title:'Détail de l&quot;objet Info'
        }).show();

      }
   });
   
}

function getTrace(){
   var content = document.getElementById("traceTable").innerHTML;
   if(content!=null){
      document.getElementById("traceTable").innerHTML="";
   }
Ext.Ajax.request( {
   url : document.getElementById("url").value,
   params : {
      action : document.getElementById("action").value,
      dateDebut: Ext.getCmp('dateDebut').getRawValue(),
      dateFin: Ext.getCmp('dateFin').getRawValue(),
      inverse: Ext.getCmp('inverse').getValue(),
      nbTrace: Ext.getCmp('nbTrace').getValue()
         
   },
   method : 'GET',
   success : function(response, opts) {
     var jsonData = Ext.util.JSON.decode(response.responseText);
     var tableDom = Ext.DomHelper;
     var tplTraceInstancie=null;
     if(document.getElementById("action").value =='getTrace'){
        tplTraceInstancie = createTraceTable(jsonData);   
     }
     if(document.getElementById("action").value =='getJournal'){
        tplTraceInstancie = createJournauxTable(jsonData);  
     }
     
     
     tableDom.append("traceTable",tplTraceInstancie);    
   }
});
}



function initForm() {
   
   var tabs = new Ext.form.FormPanel({
      renderTo: Ext.get('parametres'),
      labelWidth:200,
      border:true,
      width:400,
      items: [{
         xtype : 'datefield',
         name : 'dateDebut',
         id : 'dateDebut',
         fieldLabel : 'Date de début',
         value : new Date(),
         format: 'd/m/Y',
         submitFormat: 'd/m/Y'
      },{
         xtype : 'datefield',
         name : 'dateFin',
         id : 'dateFin',
         fieldLabel : 'Date de fin',
         value : new Date(),
         format: 'd/m/Y',
         submitFormat: 'd/m/Y'
      },{
         xtype : 'checkbox',
         name : 'inverse',
         id : 'inverse',
         fieldLabel : 'Ordre décroissant',
         checked: true
      },{
         xtype : 'textfield',
         name : 'nbTrace',
         id : 'nbTrace',
         fieldLabel : 'Nombre de trace à récupérer ',
         value: 200,
         width:70
      }]
  });
   
}



initForm();