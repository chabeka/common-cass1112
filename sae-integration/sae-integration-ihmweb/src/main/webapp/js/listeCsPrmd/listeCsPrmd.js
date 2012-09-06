
function createCstable(jsonData){
   
   tpl = new Ext.XTemplate(
         '<div class="table">',
               '<div class="row">',
                 '<div class="cellCs even">Constrat Service <p>Code, Description</p> </div>',
                 '<div class="cellDetail even">',
                 '<div class="innerTable">',
                    '<div class="row">',
                       '<div class="cell even">PAGM <p> Pagma, Description, Action</p></div>',
                       '<div class="cell even">PAGMP <p>Code, Description, PRMD</p></div>',
                    '</div>',
                 '</div>',
               '</div>',
               '</div>',
                '<tpl for="cs">',                
                  '<div class="{[xindex % 2 === 0 ? "even" : "odd"]} row">',
                     '<div class="cellCs"><p class="mwidth"><b>{codeClient}</b></p> <p class="mwidth">{description}</p> <p class="mwidth">{libelle}</p></div>',
                     '<div class="cellDetail">',
                        '<div class="innerTable">',
                          '<tpl for="pagms"}>',  
                            '<div class="{[xindex % 2 === 0 ? "even" : "odd"]} row">',
                               '<div class="cell pagm"><p class="mwidth"><b>{code}</b></p><p class="mwidth">{description}</p></div>', 
                               '<div class="cell pagma">',
                                   '<tpl for="pagmas"}>',
                                      '<p class="mwidth"><b>{code}</b></p>',
                                         '<tpl for="actions"}>',
                                                    '<p class="mwidth">{code}</p><p class="mwidth"><i>{description}</i></p>',
                                          '</tpl>',                                        
                                     '</tpl>',
                                '</div>',
                                '<div class="cell pagmp">',
                                    '<tpl for="pagmps.pagmp"}>',
                                                   '<p class="mwidth"><b>{code}</b></p><p class="mwidth">{description}</p>',
                                                    '<p class="mwidth">{prmd}</p>',
                                        '</tpl>',
                                '</div>',
                                '<div class="cell prmd">',
                                  '<tpl for="pagmps.prmd"}>',
                                              '<p class="mwidth"><b>{code}</b></p><p class="mwidth">{description}</p>',
                                               '<p class="mwidth">{prmd}</p>',                                                       
                                               '<p class="mwidth">{bean}</p>',
                                               '<p class="mwidth">{lucene}</p>',
                                   '</tpl>',
                                '</div>',
                            '</div>',
                          '</tpl>',
                        '</div>',                     
                     '</div>',
                  '</div>',
               '</tpl>',        
        '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}

function createCsTableSummary(jsonData){
   
   tpl = new Ext.XTemplate(
            '<div class="table">',               
                '<tpl for="cs">',
                '<tpl exec="values.popup = parent.popup;"></tpl>',
                  '<div class="{[xindex % 2 === 0 ? "even" : "odd"]}">',
                  '<tpl if="popup==false">',
                     '<div class="text"><a href="#" onclick="csPopup(&quot;{codeClient}&quot;)"><p><b>{codeClient}</b></p></a> <tpl if="parent.popup==true"> <u>Description : </u><i>{description}</i></tpl></div>',
                  '</tpl>',
                  '<tpl if="popup==true">',
                  '<div class="text"><p><b>{codeClient}</b></p><tpl if="parent.popup==true"> <u>Description : </u><i>{description}</i></tpl></div>',
                  '</tpl>',

                          '<tpl for="pagms"}>',
                          '<tpl exec="values.popup = parent.popup;"></tpl>',
                               '<div class="blank">&nbsp;</div>',
                               '<div><p><b>{code}</b></p></div>',
                               '<tpl if="popup==true"><div class="blank">&nbsp;</div><div class="text"> <u>Description du PAGM</u> : <i>{description}</i></div></tpl>',
                                   '<tpl for="pagmas"}>',
                                   '<tpl exec="values.popup = parent.popup;"></tpl>',
                                         '<tpl if="popup==true">',
                                            '<div class="blank2">&nbsp;</div><div class="text"> <u>Code</u> : <i>{code}</i></div>',
                                         '</tpl>',
                                      '<div class="blank2">&nbsp;</div>',
                                      '<div class="text">',
                                         '<p>',
                                            '<tpl for="actions"}>',
                                                    '<span>{code}, </span>',
                                            '</tpl>',
                                         '</p>',
                                      '</div>',
                                   '</tpl>',
                                   '<div class="blank2">&nbsp;</div>',
                                   '<div>',
                                      '<tpl for="pagmps"}>',
                                         '<tpl exec="values.popup = parent.popup;"></tpl>',
                                         '<tpl for="prmd"}>',
                                                '<div><b>{code}</b></div>',
                                                '<tpl if="parent.popup==true">',
                                                   '<div class="blank2">&nbsp;</div><div class="text"> <u>Description du PRMD</u> : <i>{description}</i></div>',
                                                   '<div class="blank2">&nbsp;</div><div class="text"> <u>Bean</u> : <i>{bean}</i></div>',
                                                '</tpl>',
                                                '<div class="blank2">&nbsp;</div>',
                                                '<div class="text"><u>Lucene</u> : {lucene}</div>',
                                         '</tpl>',
                                         '<tpl for="metaString"}>',
                                            '<tpl if="parent.popup==true">',
                                               '<div class="blank2">&nbsp;</div><div class="text"> <u>Metadata</u> : <i>{.}</i></div>',
                                            '</tpl>',
                                         '</tpl>',
                                      '</tpl>',
                                      '<tpl for="parametres"}>',
                                         '<div class="blank2">&nbsp;</div>',     
                                         '<div class="text"> {.}</div>',
                                      '</tpl>',
                                   '</div>',                            
                               '</tpl>',
                     '</div>',
                '</tpl>',        
            '</div>'
     );
   
   var tplCs = tpl.apply(jsonData);
   return tplCs;
}


function createPrmdtable(jsonData){
   
   tpl = new Ext.XTemplate(
         '<div class="table" width="100%">',
                '<tpl for="listPrmd">',
                '<div class="{[xindex % 2 === 0 ? "even" : "odd"]}">',
                   '<div>',
                      '<tpl for="prmd">',
                                   '<div><p><b>{code}</b></p></div>',
                                   '<div class="blank2">&nbsp;</div><div class="text"> <u>Description du PRMD: </u><i>{description}</i></div>',
                                   '<div class="blank2">&nbsp;</div><div class="text"> <u>Bean : </u><i>{bean}</i></div>',
                                   '<div class="blank2">&nbsp;</div><div class="text"> <u>Lucene : </u><i>{lucene}</i></div>',
                      '</tpl>',
                      '<tpl for="cs">',
                         '<div class="blank">&nbsp;</div><div><a href="#" onclick="csPopup(&quot;{codeClient}&quot;)"><p><b>{codeClient}</b></p></a></div>',
                      '</tpl>',
                   '</div>',
                '</div>',         
                '</tpl>',
         '</div>'       
     );
   
   var tplPrmd = tpl.apply(jsonData);
   return tplPrmd;
}


function csPopup(csCode){
   

   
   Ext.Ajax.request( {
      url : 'listeCsPrmd.do',
      params : {
         action : 'getCs',
         csCode: csCode
      },
      method : 'GET',
      success : function(response, opts) {
        var jsonData = Ext.util.JSON.decode(response.responseText);
        var tableDom = Ext.DomHelper;
           
        var tplCsInstancie = createCsTableSummary(jsonData);
        
        new Ext.Window({           
           html: tplCsInstancie,
           shadow:false,
           width:800,
           height:400,
           autoScroll:true,
           title:'Détail du contrat de service :'+csCode
        }).show();

      }
   });
   

      
   
   
}



function initForm() {
   var tabs = new Ext.TabPanel({
      renderTo: Ext.get('dataTableLaunch'),
      activeTab: 0,
      height:900,
      width:900,
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
  });
   
   Ext.Ajax.request( {
      url : 'listeCsPrmd.do',
      params : {
         action : 'getCsList'
      },
      method : 'GET',
      success : function(response, opts) {
        var jsonData = Ext.util.JSON.decode(response.responseText);
        var tableDom = Ext.DomHelper;
           
        var tplCsInstancie = createCsTableSummary(jsonData);
        var tplPrmdInstancie = createPrmdtable(jsonData);
        tableDom.append("csTableForm",tplCsInstancie);
        tableDom.append("prmdTableForm",tplPrmdInstancie);
        
      }
   });
   
}



initForm();