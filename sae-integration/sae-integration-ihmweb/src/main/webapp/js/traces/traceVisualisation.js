
function createTraceTable(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div class="table">',
        '<div id="compteur">Nombre de traces : {values.compteur}</div>',
        '<tpl for="traces">',
           '<tpl for="trace">',
           '<div class="trace" style="float:left">',
              '<div class="detail" style="width:180px;">{identifiant}</div>',
              '<div class="detail" style="width:150px;">{codeEvt}</div>',
              '<div class="detail" style="width:200px;">{[new Date(values.timestamp).toLocaleString()]}</div>',
              '<div class="detail" style="width:100px;"><tpl if="contexte!=null">{contexte}</tpl></div>',
              '<div class="detail" style="width:100px;"><tpl if="contrat!=null">{contrat}</tpl></div>',
              '<div class="detail" style="width:100px;"><tpl if="login!=null">{login}</tpl></div>',
              '<div class="detail" style="width:100px;">',
                 '<tpl for="pagms">',
                 '<div>{.}</div>',
                 '</tpl>',
              '</div>',
              '<div class="detail" style="width:100px;">',
                 '<tpl if="stacktrace!=null"><a href="#" onclick="javascript:getPopUp(&quot;{identifiant}&quot;);" >Afficher les traces</a></tpl>',
              '</div>',              
              '<div class="detail" style="width:100px;"><tpl if="infos!=null">',                 
                 '<div><a href="#" onclick="javascript:getPopUpInfo(&quot;{identifiant}&quot;);" >Afficher les infos</a></tpl></div>',
              '</div>',
              '</div>',
              '</tpl>',
           '</tpl>',
        '</tpl>',
        '<div>{Ext.get("compteur")]}</div>',
        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}



function createJournauxTable(jsonData){
   
   tpl = new Ext.XTemplate(
        '<div class="table">',
        '<div>Nombre de journaux : {values.compteur}</div>',
        '<tpl for="traces">',
           '<tpl for="trace">',
           '<div class="trace" style="float:left">',
              '<div class="detail" style="width:180px;">{identifiant}</div>',
              '<div class="detail" style="width:150px;">{codeEvt}</div>',
              '<div class="detail" style="width:200px;">{[new Date(values.timestamp).toLocaleString()]}</div>',
              '<div class="detail" style="width:100px;"><tpl if="contexte!=null">{contexte}</tpl></div>',
              '<div class="detail" style="width:100px;"><tpl if="login!=null">{login}</tpl></div>',
              '<div class="detail" style="width:100px;">',
                 '<tpl for="pagms">',
                 '<div>{.}</div>',
                 '</tpl>',
              '</div>',
              '<div class="detail" style="width:100px;"><tpl if="infos!=null">',                 
                 '<div><a href="#" onclick="javascript:getPopUpInfo(&quot;{identifiant}&quot;);" >Afficher les infos</a></tpl></div>',
              '</div>',
              '</div>',
              '</tpl>',
           '</tpl>',
        '</tpl>',
        '<div>Nombre de journaux : {values.compteur}</div>',
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
      url : 'tracePopUpInfo.do',
      params : {
         action : 'getTracePopUpInfo',
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
   
   var tabs = new Ext.Panel({
      renderTo: Ext.get('parametres'),
      activeTab: 0,
      height:100,
      width:800,
      deferredRender: false,
      forceLayout: true,      
      items: [{
         xtype : 'datefield',
         name : 'dateDebut',
         id : 'dateDebut',
         fieldLabel : 'date de début ',
         labelWidth : 40,
         value : new Date() 
      },{
         xtype : 'datefield',
         name : 'dateFin',
         id : 'dateFin',
         fieldLabel : 'date de fin ',
         labelWidth : 40,
         value : new Date() 
      },{
         xtype : 'checkbox',
         name : 'inverse',
         id : 'inverse',
         fieldLabel : 'Inverser la lecture ',
         labelWidth : 40
      },{
         xtype : 'textfield',
         name : 'nbTrace',
         id : 'nbTrace',
         fieldLabel : 'Nombre de trace à récupérer ',
         value: 1000
      }]
  });
   
      
  /** new Ext.Button({
      renderTo: Ext.get('lecture'),
      id: 'lecture',
      text: 'Lecture', 
      handler: getTrace()
        });**/
   
  /** var tabs = new Ext.TabPanel({
      renderTo: Ext.get('dataTableLaunch'),
      activeTab: 0,
      height:490,
      width:1200,
      deferredRender: false,
      forceLayout: true,      
      items: [{
          title: 'Contrat service',
          html: '<div id=csTableForm style="width:99%; font-size:12px;"></div>',
          autoScroll:true
      },{
          title: 'PRMD',
          html: '<div id=prmdTableForm style="width:99%; font-size:12px;"></div>',
          autoScroll:true
      }]
  });**/

   
}



initForm();