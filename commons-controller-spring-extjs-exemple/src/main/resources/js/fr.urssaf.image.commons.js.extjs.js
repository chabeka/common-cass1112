Ext.namespace('fr.urssaf.image.commons.extjs');

// todo	Echec d'utilisation du constructeur, j'ai tente divers syntaxe mais pas moyen
fr.urssaf.image.commons.extjs = {
 /**
  * desc 	type comportement popup
  */
 __BEHAVIOR_POPUP__: 0,
 
 /**
  * desc 	type comportement zone
  */
 __BEHAVIOR_ZONE__: 1,
 
 /**
  * desc 	type comportement url
  */
 __BEHAVIOR_URL__: 2,
 
 /**
  * desc 	type comportement fonction
  */
 __BEHAVIOR_FUNCTION__: 3,
 		
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
   * desc 	behavior when ajax call is a success
   */
  _successBehavior: {type:null, value:null},
  
  /**
   * desc 	behavior when ajax call is a failure
   */
  _failureBehavior: {type:null, value:null},

  /**
   * desc 	mode d'envoi du formulaire
   */
  _ajaxMode: null,

  /**
   * desc 	affichage des messages dans la console
   */
  _debugInConsole: false,
  
  /**
   * desc 	message de succes par defaut
   */
  _defaultSuccessMessage : 'Le formulaire a bien &eacute;t&eacute; valid&eacute;',
  
  /**
   * desc 	message d'echec par defaut
   */
  _defaultFailureMessage : 'Des erreurs sont survenues',
    
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
    {
     	config.standardSubmit = pStandardSubmit ;
     	this._ajaxMode = false ;
    }
    else
    	this._ajaxMode = true ;
    if( pWidth && Ext.isNumber( pWidth ) )
    	config.width = pWidth ;
    if( pLabelWidth && Ext.isNumber( pLabelWidth ) )
    	config.labelWidth = pLabelWidth ;
    config.frame = true ;
    config.buttonAlign = 'left' ;
    
    // creation
    this.formulaire = new Ext.FormPanel( config );
    
    // configuration du comportement 
    this.setSuccessComportement( this.__BEHAVIOR_POPUP__ ) ;
    this.setFailureComportement( this.__BEHAVIOR_POPUP__ ) ;
    
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
   * return 	this
   */
  // ajouterCaseACocher: function( pLabel, pName, pValue, pChecked, pAddResetHiddenField )
  ajouterCaseACocher: function( pLabel, pName, pValue, pChecked )
  {
	var config = this._getStandardConfigOptions( pName, null, pLabel );
	this._addConfigValue( config, 'inputValue', pValue ) ;
	if( pChecked && Ext.isBoolean(pChecked) )
		config.checked = pChecked ;
	
	maCaseACocher = new Ext.form.Checkbox( config ) ;

    this.formulaire.add( maCaseACocher ) ;
    
    // Ajout d'un champ cache pour le comportement des checkbox sous commons-controller-spring
    // OBSOLETE
    /*
    if( pAddResetHiddenField 
    	&& Ext.isBoolean( pAddResetHiddenField ) 
    	&& pAddResetHiddenField == true )
    {
    	this.ajouterChampCache( 'reset', pName ) ;
    }
    /* */
    
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
	config.selectOnFocus = true; // uniquement si editable == true
//	config.editable = false ; // si false, tout le champ est cliquable, mais la saisie predictive est supprimee
	config.forceSelection = true ;
	config.triggerAction = 'all'; // permet de pouvoir changer d'element apres en avoir deja selectionne un 

	var maListeDeroulante = new Ext.form.ComboBox( config );
	
	this.formulaire.add( maListeDeroulante ) ;
	
	return this;
  },  
  
  /**
   * desc 	Creation d'un bouton d'annulation return this
   * 		Les boutons sont alignes sur la meme ligne Mais doivent etre crees avant de dessiner le formulaire
   * return this
   */
  ajouterBoutonAnnulation: function()
  {
	var config = {text: 'Annuler'} ;
	if( this._hasBeenRendered == false )
	{
	    var handler = function(){fr.urssaf.image.commons.extjs.formulaire.getForm().reset();} ;
	    this.formulaire.addButton( config, handler );
	}
    
    return this;
  },
  
  /**
   * desc 	Creation d'un bouton de validation (principe identique a la creation du bouton d'annulation)
   * return this
   */
  ajouterBoutonValidation: function()
  {
	var config = {text: 'Valider'};
		
	if( this._hasBeenRendered == false )
	{
		var handler = function(){
    		var myExtJs = fr.urssaf.image.commons.extjs ;
    		myExtJs.formulaire.getForm().submit({
    			
	  			success: function(f,a){
    			
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'success' );

					var message = myExtJs._getMessageAttributeFromJson( a.response.responseText ) ;
					if( !message )
						message = myExtJs._defaultSuccessMessage ;
							
    				switch( myExtJs._successBehavior.type )
    				{
	    				case myExtJs.__BEHAVIOR_FUNCTION__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Succès : fonction' );
	    					myExtJs._successBehavior.value(f,a);
	    					break ;
	    					
	    				case myExtJs.__BEHAVIOR_URL__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Succès : url' );
	    					window.location = myExtJs._successBehavior.value;
	    					break ;
	    					
	    				case myExtJs.__BEHAVIOR_ZONE__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Succès : zone' );
	    					var el = Ext.get( myExtJs._successBehavior.value ) ;
	    					if( el )
	    						el.update( message ) ;
	    					break ;
	    					
	    				case myExtJs.__BEHAVIOR_POPUP__ :
	    				default :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Succès : Popup/default' );
	    					Ext.Msg.alert( "Succ&egrave;s", message );
    				}
    				
    			},
 	  			failure: function(f,a){
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'failure' );
		    			// 1) erreur de connexion 
    					// 2) erreur fonctionnelle
		    			if (a.failureType === Ext.form.Action.CONNECT_FAILURE
		    			 || a.failureType === Ext.form.Action.SERVER_INVALID ) 
		    			{
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'failureBehavior : ' + myExtJs._failureBehavior.type );
							var userFormErrors = false ;
						
							// verification si erreur de saisie ou fonctionnelle 
							if( a.failureType === Ext.form.Action.SERVER_INVALID )
							{
								var responseObject =  Ext.util.JSON.decode( a.response.responseText );
								userFormErrors = ( responseObject.errors ) ? true : false ;
							}
							
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'userFormErrors : ' + userFormErrors ) ;

							// si on a des erreurs de saisies on bloque cette gestion des erreurs et on laisse la main a ExtJS
							if( !userFormErrors )
							{
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Aucune erreurs de saisies' );
			    				switch( myExtJs._failureBehavior.type )
			    				{
				    				case myExtJs.__BEHAVIOR_FUNCTION__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Echec : fonction' );
				    					myExtJs._failureBehavior.value(f,a);
				    					break ;
				    					
				    				case myExtJs.__BEHAVIOR_URL__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Echec : url' );
				    					window.location = myExtJs._failureBehavior.value;
				    					break ;
				    					
				    				case myExtJs.__BEHAVIOR_ZONE__ :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Echec : zone' );
				    					var el = Ext.get( myExtJs._failureBehavior.value ) ;
				    					if( el )
				    					{
				    						// si erreur fonctionnelle on va parser la chaîne Json
				    						if( a.failureType === Ext.form.Action.SERVER_INVALID )
				    						{
				    							var message = myExtJs._getMessageAttributeFromJson( a.response.responseText, el ) ;
				    							// si le developpeur a oublie de renvoyer un attribut message, on met un message specifique
				    							if( !message )
				    								el.update( myExtJs._defaultFailureMessage ) ;
				    						}
				    						
				    						// si erreur de connexion on affiche la chaine html 
				    						else if( a.failureType === Ext.form.Action.CONNECT_FAILURE )
				    						{
				    							el.update( "Echec de connexion (" + a.response.status + ")" + "<br />" + a.response.responseText );
				    						}
				    					}
				    						
				    					break ;
				    					
				    				case myExtJs.__BEHAVIOR_POPUP__ :
				    				default :
if( !Ext.isIE && myExtJs._debugInConsole ) console.info( 'Echec : Popup/default' );

				    					var message = null ;
				    					var title = null ;
				    					switch( a.failureType )
				    					{
					    					case Ext.form.Action.CONNECT_FAILURE:
					    						title = "Echec de connexion (" + a.response.status + ")" ;
					    						message = a.response.statusText ;
					    						break;
					    					case Ext.form.Action.SERVER_INVALID:
					    					default :
					    						title = "Echec" ;
					    						if( jsonAttMessage = myExtJs._getMessageAttributeFromJson( a.response.responseText ) )
						    						message = jsonAttMessage ;
					    						else
					    							message = myExtJs._defaultFailureMessage ;
				    					}
				    					
				    					Ext.Msg.alert( title, message ) ;
			    				}
			    				
			    			}
							else
							{
								if( !Ext.isIE && myExtJs._debugInConsole )
									console.info( 'Erreurs de saisies utilisateur' );
							}
		    			}
		    			// 3) de toute maniere si on trouve dans la chaine json un attribut errors, Ext.BasicForm prend la main
		    			// pour faire la gestion automatique
    				}
    			
	   			});
	  		} ;
	  	
	  	this.formulaire.addButton( config, handler ) ;
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
 	 displayZone = false ;
 	
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
    * desc		defini le comportement lors d'un appel ajax en succes
    * params	pType (int) 
    * params	pValue (string) 	
    * return 	void
    */
   setSuccessComportement: function( pType, pValue )
   {
	   if( this._checkTypeBehavior( pType, pValue ) )
	   {
		   this._successBehavior.type = pType ;
		   this._successBehavior.value = pValue ;
	   }
	   else
		   if( !Ext.isIE && this._debugInConsole ) console.warn( 'Erreur lors de l appel a setSuccessComportement' ) ;
   },
   
   /**
    * desc		defini le comportement lors d'un appel ajax en erreur
    * params	pType (int) 
    * params	pValue (string) 	
    * return 	void
    */
   setFailureComportement: function( pType, pValue )
   {
	   if( this._checkTypeBehavior( pType, pValue ) )
	   {
		   this._failureBehavior.type = pType ;
		   this._failureBehavior.value = pValue ;
	   }
	   else
		   if( !Ext.isIE && this._debugInConsole ) console.warn( 'Erreur lors de l appel a setFailureComportement' ) ;
   },
   
   /**
    * desc		verifie les valeurs d'un comportement
    * params	pType (int) 
    * params	pValue (string) 	
    * return 	boolean
    */
   _checkTypeBehavior: function( pType, pValue )
   {
	   // on doit etre en mode Ajax
	   if( !this._ajaxMode )
	   {
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : le formulaire est en mode d\'envoi standard et pas Ajax' ) ;
		   return false ;
	   }
	   
	   // pType doit etre un chiffre
	   if( !Ext.isNumber( pType ) )
	   {
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : pType doit etre un chiffre (' + pType + ')' ) ;
		   return false ;
	   }

	   // pType doit correspondre a l'une des constantes definies
	   if( pType != this.__BEHAVIOR_POPUP__
		&& pType != this.__BEHAVIOR_ZONE__
		&& pType != this.__BEHAVIOR_URL__
		&& pType != this.__BEHAVIOR_FUNCTION__ )
	   {
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : pType ne correspond pas a une constante de la classe (' + pType + ')' ) ;
		   return false ;
	   }

	   // pValue doit etre null si on utilise une popup
	   if( pType == this.__BEHAVIOR_POPUP__ 
		&& pValue != null )
		{
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : pType/pValue est incoherent avec le type popup (' + pType + ', ' + pValue + ')' ) ;
		   return false ;
		}

	   // pValue doit etre un element existant du dom si on utilise une zone
	   if( pType == this.__BEHAVIOR_ZONE__ )
	   {
		   var el = Ext.get( pValue ) ;
		   if( !el )
		   {
			   console.warn( '_checkTypeBehavior : pType/pValue est incoherent avec le type zone (' + pType + ', ' + pValue + ')' ) ;
			   return false ;
		   }
	   }

	   // pValue ne doit pas forcément commencer par http(s), uniquement si on veut quitter le site courant
	   if( pType == this.__BEHAVIOR_URL__ 
		&& ( pValue == null 
			/* || ( pValue.substr( 0, 7 ) != 'http://' 
				&& pValue.substr( 0, 8 ) != 'https://' ) */ ) )
	   {
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : pType/pValue est incoherent avec le type url (' + pType + ', ' + pValue + ')' ) ;
		   return false ;
	   }
 
	   // pValue doit etre une fonction si on utilise une fonction
	   if( pType == this.__BEHAVIOR_FUNCTION__
		&& !Ext.isFunction( pValue ) )
	   {
		   if( !Ext.isIE && this._debugInConsole ) console.warn( '_checkTypeBehavior : pType/pValue est incoherent avec le type fonction (' + pType + ', ' + pValue + ')' ) ;
		   return false ;
	   }

	   return true ;
   },
   
   /**
    * desc		recupere le message associé à la réponse ajax (attribut message de la réponse)
    * 			si el est spécifié, le contenu d'el sera mis à jour avec le message de réponse
    * params	responseText (string)
    * params	el (Element) 	
    * return 	message (string)
    */
   _getMessageAttributeFromJson: function( responseText, el )
   {
	    var message = null ;
	 	var responseObject =  Ext.util.JSON.decode( responseText );
	 	
	 	if( responseObject.message 
	 		&& responseObject.message != null 
	 		&& responseObject.message != "" )
	 		message = responseObject.message ;
	 	 	
	 	if( el )
	 		el.update( message );
	 	
	 	return message ;
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
  		var errorFieldContent = myExtJs.formulaire.getForm().findField( myExtJs._errorFieldName ).getValue() ;
  		var formResult =  Ext.util.JSON.decode( errorFieldContent );
  		/*
  		var base64String = Ext.util.base64.decode( errorFieldContent ) ;
  		var formResult =  Ext.util.JSON.decode( base64String );
  		*/
  		
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