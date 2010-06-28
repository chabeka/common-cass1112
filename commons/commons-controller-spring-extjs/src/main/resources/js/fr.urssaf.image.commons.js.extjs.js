Ext.namespace('fr.urssaf.image.commons.extjs');

// todo	Echec d'utilisation du constructeur, j'ai tente divers syntaxe mais pas moyen
fr.urssaf.image.commons.extjs = {
 /**
  * desc 	Ext.form.FormPanel
  */
  formulaire: null, 
  
  /**
   * desc 	permet a l'objet de savoir si le formulaire a deja ete dessine. On ne peut dessiner le formulaire qu'une seule fois.
   */
  _hasBeenRendered: false,
  
  /**
   * desc 	name given in array SimpleStore.fields and used to define display column data
   */
  _displayFieldColName: 'displayCol',
  
  /**
   * desc 	name given in array SimpleStore.fields and used to define value column data
   */
  _valueFieldColName: 'valueCol',
  
  /**
   * desc 	name of the field that store the json result
   */
  _errorFieldName: 'formErrorField',
  
  /**
   * desc	action to realize on success submit
   */
  _actionOnSuccess: {url:null, zoneName:null},
  
  /**
   * desc	action to realize on failure submit
   */
  _actionOnFailure: {url:null, zoneName:null},

  /**
   * desc 		Creation d'un nouveau formulaire params return this
   * 			Ajoute le champ action:xxx par defaut
   * params	pUrl (string)
   * params	pTitle (string)
   * params	pStandardSubmit (boolean)
   * params	pWidth (int)
   * params	pLabelWidth (int)
   * return this
   */
  creerFormulaire: function( pActionController, pUrl, pTitle, pStandardSubmit, pWidth, pLabelWidth )
  {
    // initialisation
    this.formulaire = null ;
    
    // configuration
    var config = this._getStandardConfigOptions();
    this._addConfigValue( config, 'url', pUrl );
    this._addConfigValue( config, 'title', pTitle );
    if( pStandardSubmit && Ext.isBoolean( pStandardSubmit ) )
     	config.standardSubmit = pStandardSubmit ;
    if( pWidth && Ext.isNumber( pWidth ) )
    	config.width = pWidth ;
    if( pLabelWidth && Ext.isNumber( pLabelWidth ) )
    	config.labelWidth = pLabelWidth ;
    config.frame = true ;
    config.buttonAlign = 'left' ;
    
    // creation
    this.formulaire = new Ext.FormPanel( config );
    
    // Normalement il etait prevu d'utiliser les attributs name/value des boutons, mais ce n'est pas possible en ExtJS
    // car Ext.Button ne dispose pas de ces 2 attributs
    var monChampController = new Ext.form.Hidden({
    	name: 'action:' + pActionController,
 		value: pActionController
    });
    this.formulaire.add( monChampController ) ;
    
    // ajout du champ d'erreur en mode Standard uniquement
    if( pStandardSubmit && Ext.isBoolean( pStandardSubmit ) && pStandardSubmit == false )
    {
    	this.ajouterChampErreur() ;
    }
    return this;
  },

  /**
   * desc 	Affichage des erreurs etendus return void
   */
   activerAffichageEtenduDesErreurs: function ()
   {
     if( !Ext.QuickTips.isEnabled() )
     {
       Ext.QuickTips.init();
       Ext.form.Field.prototype.msgTarget = 'side';
     }
   },
   
 /**
  * desc 	Affichage des erreurs etendus return void
  */
  desactiverAffichageEtenduDesErreurs: function ()
  {
    if( Ext.QuickTips.isEnabled() )
    {
      Ext.QuickTips.disable();
    }
  },
  
  /**
   * desc 	Ajout d'un champ texte stockant la chaine json de resultat, ou mise a jour du contenu du champ
   * params	pValue (string) base64 encoded
   * return this
   */
  ajouterChampErreur: function( pValue )
  {
	if( this.formulaire && Ext.isObject(this.formulaire) )
	{
		if( !this.formulaire.getForm().findField( this._errorFieldName ) )
		{
			var config = this._getStandardConfigOptions( this._errorFieldName, pValue );
			config.submitValue = false ;
			
			var monChampGestionErreur = new Ext.form.Hidden( config );
			
			this.formulaire.add( monChampGestionErreur ) ;
		}
		else
		{
			if( pValue && pValue != 'undefined' )
				this.formulaire.getForm().findField( this._errorFieldName ).setValue( pValue ) ;
		}
	}
	return this;
  },
  
  /**
   * desc 	Ajout d'un champ texte simple au formulaire
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * params pWidth (int)
   * return 	this
   */
   ajouterChampTexteSimple: function( pLabel, pName, pValue, pWidth )
   {
	 var config = this._getStandardConfigOptions( pName, pValue, pLabel );
	 if( pWidth && Ext.isNumber( pWidth ) )
		 config.width = pWidth ;
	 
	 var monChampTexteSimple = new Ext.form.TextField( config );
 		
     this.formulaire.add( monChampTexteSimple ) ; 		
     return this;
   },
   
   /**
    * desc 	Ajout d'un champ mot de passe au formulaire
    * params	pLabel (string)
    * params	pName (string)
    * params	pValue (string)
    * params 	pWidth (int)
    * return 	this
    */
  ajouterChampPassword: function( pLabel, pName, pValue, pWidth )
  {
	var config = this._getStandardConfigOptions( pName, pValue, pLabel );
	config.inputType = 'password' ;
	if( pWidth && Ext.isNumber( pWidth ) )
		 config.width = pWidth ;
	
	var monChampPassword = new Ext.form.TextField( config );
		
    this.formulaire.add( monChampPassword ) ; 		
    return this;
  },

   /**
    * desc 	Ajout d'un champ cache au formulaire
    * params	pName (string)
    * params	pValue (string)
    * return 	this
    */
  ajouterChampCache: function( pName, pValue )
  {
	var config = this._getStandardConfigOptions( pName, pValue );
	
	var monChampCache = new Ext.form.Hidden( config );
		
    this.formulaire.add( monChampCache ) ; 		
    return this;
  },
  
  /**
   * desc 	Ajout d'un champ cache qui ne sera pas envoye par le submit
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * return 	this
   */
  ajouterChampStockage: function( pName, pValue )
  {
	var config = this._getStandardConfigOptions( pName, pValue );
	config.submitValue = false ;
	
	var monChampStockage = new Ext.form.Hidden( config );
		
    this.formulaire.add( monChampStockage ) ; 		
    return this;
  },
  
  /**
   * desc 	Ajout d'un champ date au formulaire
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * return 	this
   */
  ajouterChampDate: function( pLabel, pName, pValue )
  {
	 var config = this._getStandardConfigOptions( pName, pValue, pLabel );
	 config.format = 'd/m/Y' ;

     var monChampDate = new Ext.form.DateField( config );

     this.formulaire.add( monChampDate ) ; 		
     return this;
  },

  /**
   * desc 	Ajout d'une zone de texte au formulaire
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * params	pWidth (int) default to 450
   * params	pHeight (int) default to 200
   * return 	this
   */
  ajouterZoneDeTexte: function( pLabel, pName, pValue, pWidth, pHeight )
  {
	var config = this._getStandardConfigOptions( pName, pValue, pLabel );
	config.width = ( pWidth && Ext.isNumber( pWidth ) ) ? pWidth : 450 ;
	config.height = ( pHeight && Ext.isNumber( pHeight ) ) ? pHeight : 200 ;
	if( !pLabel )
	{
		config.hideLabel = true ;
		config.labelSeparator = '' ;
	}
	
    maZoneDeTexte = new Ext.form.TextArea( config ) ;

    this.formulaire.add( maZoneDeTexte ) ; 		
    return this;
  },

  /**
   * desc 	Ajout d'une zone de texte au formulaire
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * params	pChecked (bool)
   * params pAddResetHiddenField (bool)
   * return 	this
   */
  ajouterCaseACocher: function( pLabel, pName, pValue, pChecked, pAddResetHiddenField )
  {
	var config = this._getStandardConfigOptions( pName, null, pLabel );
	this._addConfigValue( config, 'inputValue', pValue ) ;
	if( pChecked && Ext.isBoolean(pChecked) )
		config.checked = pChecked ;
	
	maCaseACocher = new Ext.form.Checkbox( config ) ;

    this.formulaire.add( maCaseACocher ) ;
    
    // Ajout d'un champ cache pour le comportement des checkbox sous commons-controller-spring
    if( pAddResetHiddenField 
    	&& Ext.isBoolean( pAddResetHiddenField ) 
    	&& pAddResetHiddenField == true )
    {
    	this.ajouterChampCache( 'reset', pName ) ;
    }
    
    return this;
  },

  /**
   * desc 	Ajout d'un bouton radio au formulaire
   * params	pGroupLabel (string)
   * params	pBoxLabel (string)
   * params	pName (string)
   * params	pValue (string)
   * params	pChecked (bool)
   * return 	this
   */
  ajouterBoutonRadio: function( pGroupLabel, pBoxLabel, pName, pValue, pChecked )
  {
	// cas 1 : il existe deja un bouton radio, on va donc rajouter
	if( this.formulaire.getForm().findField( pName ) )
	{
		var config = this._getStandardConfigOptions( pName, null, null );
		config.hideLabel = false ;
	}
	// cas 2 : on cree un nouveau bouton
	else
	{
		var config = this._getStandardConfigOptions( pName, null, pGroupLabel );
	}
	
	this._addConfigValue( config, 'boxLabel', pBoxLabel ) ;
	this._addConfigValue( config, 'inputValue', pValue ) ;
	this._addConfigValue( config, 'checked', pChecked ) ;
	
	var monBoutonRadio = new Ext.form.Radio( config );
	
	this.formulaire.add( monBoutonRadio ) ;  
	return this;
  },
  
  /**
   * desc 	Ajoute une liste deroulante au formulaire
   * params	pLabel (string)
   * params	pName (string)
   * params	pValue (array) (array) ex: pValue = [["homme", 12], ["femme", 5]];
   * params	pWidth (int)
   * return this
   */
  ajouterListeDeroulante: function( pLabel, pName, pValue, pWidth )
  {
	var config = this._getStandardConfigOptions( pName, null, pLabel );
	config.hiddenName = pName; // propriete utilise pour envoyer la valeur de la liste et non pas son label
	config.width = ( pWidth && Ext.isNumber( pWidth ) ) ? pWidth : 130 ;
	config.mode = 'local' ;
	config.store = this._getSimpleStoreFromArray( pValue ) ;
	config.displayField = this._displayFieldColName ;
	config.valueField = this._valueFieldColName ;
	config.selectOnFocus = true;
	config.editable = false ;

	var maListeDeroulante = new Ext.form.ComboBox( config );
	
	this.formulaire.add( maListeDeroulante ) ;
	return this;
  },  

  /**
   * desc 	Creation d'un bouton d'annulation return this
   * 		Les boutons sont alignes sur la meme ligne si le formulaire n'a pas deja ete calcule (methode render)
   * 		Sinon ils seront empiles les uns au dessus des autres. Il est donc preferable de creer les boutons avant 
   * 		d'appeler la methode dessinerFormulaire 
   * return this
   */
  ajouterBoutonAnnulation: function()
  {
	if( this._hasBeenRendered == false )
	{
		config = {text: 'Annuler'} ;
	    handler = function(){fr.urssaf.image.commons.extjs.formulaire.getForm().reset();} ;
	    this.formulaire.addButton( config, handler );
	}
	else
	{
	    var monBoutonAnnulation = new Ext.Button({
		  	text: 'Annuler',
		  	handler: function(){
	    		fr.urssaf.image.commons.extjs.formulaire.getForm().reset();
		  	}
		  } );	
	    this.formulaire.add( monBoutonAnnulation ) ;
	}
    
    return this;
  },
  
  /**
   * desc 	Creation d'un bouton de validation (principe identique a la creation du bouton d'annulation)
   * params successFunction (function(param1,param2))
   * params failureFunction (function(param1,param2))
   * return this
   */
  ajouterBoutonValidation: function( successFunction, failureFunction )
  {
	if( this._hasBeenRendered == false )
	{
		config = {text: 'Valider'};
		handler = function(){
    		var myExtJs = fr.urssaf.image.commons.extjs ;
    		myExtJs.formulaire.getForm().submit({
	  			success: function(f,a){
						// Si une fonction perso est precise, elle est prioritaire sur les comportements par defaut
    					if( successFunction )
	    				{
	    					successFunction(f,a);
	    				}else
	    				{
	    					// redirection
	    		  			if( myExtJs._actionOnSuccess.url != null )
	    		  			{
	    		  				window.location = myExtJs._actionOnSuccess.url ;
	    		  			}
	    		  			// mise a jour d'une zone
	    		  			else if( myExtJs._actionOnSuccess.zoneName != null )	
	    		  			{
								var el = Ext.get( myExtJs._actionOnSuccess.zoneName ) ;
								if( el )
									el.update( a.response.responseText );
	    		  			}
	    				}

    				},
 	  			failure: function(f,a){
    					// erreur de connexion
    					if (a.failureType === Ext.form.Action.CONNECT_FAILURE) 
    					{
    						// Si une fonction perso est precise, elle est prioritaire sur les comportements par defaut
                        	if( failureFunction )
    	    				{
        						failureFunction(f,a);
    	    				}
                        	// comportements par defaut
                        	else
    	    				{
                        		// redirection
    	    		  			if( myExtJs._actionOnFailure.url != null )
    	    		  			{
    	    		  				window.location = myExtJs._actionOnFailure.url ;
    	    		  			}
    	    		  			// mise a jour d'une zone
    	    		  			else if( myExtJs._actionOnFailure.zoneName )	
    	    		  			{
    	    		  				var el = Ext.get( myExtJs._actionOnFailure.zoneName ) ;
    	    		  				if( el )
    	    		  					el.update( a.response.responseText );
    	    		  			}
    	    				}
                        	
                        }
    					// success: false
                        if (a.failureType === Ext.form.Action.SERVER_INVALID)
                        {
                            Ext.Msg.alert('Erreur',
                                'Status: '+a.response.status+': '+a.response.statusText);
                        }
    				}
	   			});
	  		} ;
	  	
	  	this.formulaire.addButton( config, handler ) ;
	}
	else
	{
	    var monBoutonValidation = new Ext.Button({
		  	text: 'Valider',
		  	handler: function(){
	    		var myExtJs = fr.urssaf.image.commons.extjs ;
	    		myExtJs.formulaire.getForm().submit({
		  			success: function(f,a){
							// Si une fonction perso est precise, elle est prioritaire sur les comportements par defaut
	    					if( successFunction )
		    				{
		    					successFunction(f,a);
		    				}else
		    				{
		    					// redirection
		    		  			if( myExtJs._actionOnSuccess.url != null )
		    		  			{
		    		  				window.location = myExtJs._actionOnSuccess.url ;
		    		  			}
		    		  			// mise a jour d'une zone
		    		  			else if( myExtJs._actionOnSuccess.zoneName != null )	
		    		  			{
									var el = Ext.get( myExtJs._actionOnSuccess.zoneName ) ;
									if( el )
										el.update( a.response.responseText );
		    		  			}
		    				}
	
	    				},
	 	  			failure: function(f,a){
	    					// erreur de connexion
	    					if (a.failureType === Ext.form.Action.CONNECT_FAILURE) 
	    					{
	    						// Si une fonction perso est precise, elle est prioritaire sur les comportements par defaut
	                        	if( failureFunction )
	    	    				{
	        						failureFunction(f,a);
	    	    				}
	                        	// comportements par defaut
	                        	else
	    	    				{
	                        		// redirection
	    	    		  			if( myExtJs._actionOnFailure.url != null )
	    	    		  			{
	    	    		  				window.location = myExtJs._actionOnFailure.url ;
	    	    		  			}
	    	    		  			// mise a jour d'une zone
	    	    		  			else if( myExtJs._actionOnFailure.zoneName )	
	    	    		  			{
	    	    		  				var el = Ext.get( myExtJs._actionOnFailure.zoneName ) ;
	    	    		  				if( el )
	    	    		  					el.update( a.response.responseText );
	    	    		  			}
	    	    				}
	                        	
	                        }
	    					// success: false
	                        if (a.failureType === Ext.form.Action.SERVER_INVALID)
	                        {
	                            Ext.Msg.alert('Erreur',
	                                'Status: '+a.response.status+': '+a.response.statusText);
	                        }
	    				}
		   			});
		  		}
		  });
	    
	    this.formulaire.add( monBoutonValidation ) ;
	}
	
    return this;
  },
  
  /**
   * desc 	dessine le formulaire return this
   * 		Ne peut etre appele qu'une seule fois
   * params	pCible (string)
   * return	Boolean
   */
   dessinerFormulaire: function( pCible )
   {
 	 displayDone = false ;
 	
 	 if( this._hasBeenRendered == false )
 	 {
 		this.formulaire.render( pCible ) ;
 		this._hasBeenRendered = true ;
 		this.formulaire.doLayout();
 	    
 	    Ext.onReady( function(){ fr.urssaf.image.commons.extjs._addErrorManagementInStandardMode() ; } ) ;
 	    
 	    displayZone = true ;
 	 }
 	    
     return displayZone ;
   },
   
   /**
    * desc		defini les url de redirection en cas de succes et d'echec de submit
    * params	pUrlOnSuccess (string) 
    * params	pUrlOnFailure (string) 	
    * return 	void
    */
   setUrlUsedOnResponse: function( pUrlOnSuccess, pUrlOnFailure )
   {
	   this._setUrlOnSuccess( pUrlOnSuccess );
	   this._setUrlOnFailure( pUrlOnFailure );
   },
   
   /**
    * desc		defini les nom des zones de rafraichissement en cas de succes ou d'echec de submit
    * params	pZoneNameOnSuccess (string)
    * params	pZoneNameOnFailure (string)	
    * return 	void
    */
   setZoneNameUsedOnResponse: function( pZoneNameOnSuccess, pZoneNameOnFailure )
   {
	   this._setZoneOnSuccess( pZoneNameOnSuccess );
	   this._setZoneOnFailure( pZoneNameOnFailure );
   },
   
   /**
    * desc		defini l'url de redirection en cas de succes de submit
    * params	pUrl (string)
    * return 	void
    */
   _setUrlOnSuccess: function( pUrl ){
	   this._actionOnSuccess.zoneName = null ;
	   this._actionOnSuccess.url = pUrl ;
   },
   
   /**
    * desc		defini le nom de la zone de rafraichissement en cas de succes de submit
    * params	pZoneName (string) 	
    * return 	void
    */
   _setZoneOnSuccess: function( pZoneName ){
	   this._actionOnSuccess.zoneName = pZoneName ;
	   this._actionOnSuccess.url = null ;
   },
   
   /**
    * desc		defini le nom de la zone de rafraichissement en cas d'echec de submit
    * params	pUrl (string)	
    * return 	void
    */
   _setUrlOnFailure: function( pUrl ){
	   this._actionOnFailure.zoneName = null ;
	   this._actionOnFailure.url = pUrl ;
   },
   
   /**
    * desc		defini le nom de la zone de rafraichissement en cas d'echec de submit
    * params	pZoneName (string) 	
    * return 	void
    */
   _setZoneOnFailure: function( pZoneName ){
	   this._actionOnFailure.zoneName = pZoneName ;
	   this._actionOnFailure.url = null ;
   },
   
   /**
    * desc 		gere le traitement des erreurs en mode non ajax
    * return 	void
    */
  _addErrorManagementInStandardMode: function()
  {
	var myExtJs = fr.urssaf.image.commons.extjs ;
  	if( myExtJs.formulaire 
  		&& myExtJs.formulaire.getForm().findField( myExtJs._errorFieldName )
  		&& myExtJs.formulaire.getForm().findField( myExtJs._errorFieldName ).getValue().length > 0 )
  	{
  		var base64String = Ext.util.base64.decode( myExtJs.formulaire.getForm().findField( myExtJs._errorFieldName ).getValue() ) ;
  		var formResult =  Ext.util.JSON.decode( base64String );
  		
  		if( formResult && Ext.isObject( formResult) )
  		{
  		
  			if( formResult.success
  				&& Ext.isBoolean( formResult.success )
  				&& formResult.success == true )
	  		{
	  			Ext.Msg.alert( 'Resultat', 'Le formulaire a ete envoye avec succes' );
	  		}
	  		else
	  		{
	  			Ext.Msg.alert( 'Resultat', 'Des erreurs sont apparus lors de la validation du formulaire' );
	  			if( formResult.errors )
	  			{
	  				var errorList = formResult.errors ;
		  			for ( var attribute in errorList ) {
		  				var monChampEnErreur = myExtJs.formulaire.getForm().findField( attribute ) ;
		  				monChampEnErreur.markInvalid( errorList[attribute] ) ;
		  			}
	  			}
	  		}
  			
  		}
  	}  
  },
  
   /**
    * desc 		Construit l'objet configuration
    * params	pName (string)
    * params	pValue (string)
    * params	pLabel (string)
    * return 	config (object)
    */
  _getStandardConfigOptions: function( pName, pValue, pLabel )
  {
	var config = {} ;
	
	this._addConfigValue( config, 'name', pName ) ;
	this._addConfigValue( config, 'value', pValue ) ;
	this._addConfigValue( config, 'fieldLabel', pLabel ) ;
	
	return config ;
  },
  
   /**
    * desc 		Ajoute un attribut pKey de valeur pValue a l'objet pConfig
    * params	pConfig (object)
    * params	pKey (string)
    * params	pValue (string)
    * return	void
    */
  _addConfigValue: function( pConfig, pKey, pValue )
  {
	if( Ext.isObject( pConfig ) && pKey && pValue )
		pConfig[pKey] = pValue ; // utilisation de la syntaxe tableau car pKey est dynamique
  },
  
   /**
    * desc 		Cree une source de donnee ayant 2 champs : key et field
    * params	pArray
    * return	Ext.data.SimpleStore
    */
  _getSimpleStoreFromArray: function( pArray )
  {
	  if( pArray && Ext.isArray( pArray ) )
	  {
		  var config = this._getStandardConfigOptions() ;
		  config.fields = new Array( this._displayFieldColName, this._valueFieldColName ) ;
		  config.data = pArray ;
	  
		  return new Ext.data.SimpleStore( config ) ;
	  }
  }
  
} ;