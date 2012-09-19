/*
 * @type : class
 * @name : MenuClass
 * @desc : gere le fonctionnement du menu de type :
 * 		<ul><li><a class="firstrow">niveau1</a><ul><li><a></a></li>[...]</ul></li></ul>[...]
 * @info : pour cacher un bloc ul on utilise display:none, mais pour IE il semble qu'il faille coupler avec 
 * 		visibility:hidden sous peine d'avoir des sous-blocs qui restent affiches
 */
var MenuClass = new Class(
{
	// pointe sur la div menu a manipuler 
	tree: null,
	
	focus: false,
	keyDown: null,
	focusItem: null,
	blurItem: null,
	
	currentLiContainer: null,
	previousLiContainer: null, // ne sert que pour le left event
	liIndex: 0,
	ulIndex: 0,
	
	/*
	 * @type : constructor
	 * @name : initialize
	 * @desc : constructeur, affecte la propriete tree, declenche la gestion de l'affichage, les listeners (mouse,
	 * 		   keyboard), supprime les liens de la premiere page
	 * @params: menuContainer
	 */
	initialize: function( menuContainer )
	{
		if( !menuContainer )
			menuContainer = $$("div#header.frUrssafImageCommonsMaquette > div#menu")[0] ;
		
		this.tree = menuContainer ;
		
		this.initDisplay();
		
		this.initMouseEvent();
		
		this.initFocusAndBlurEvent();
		
		this.disableFirstRowLinks();
		
	},
	
	/*
	 * @type : method
	 * @name : initDisplay
	 * @desc : gere l'initialisation de l'affichage
	 */
	initDisplay: function()
	{
		// cache tous les blocs ul (ils sont caches par defaut dans le fichier de css)
		this.tree.getChildren('ul').each( function( item, index ){ 
	   		item.setStyle('display','none') ; 
	    } ) ;
		
	    // affiche uniquement la premiere ligne
	    this.tree.getChildren('ul > li').each( function( item, index ){
		   	item.setStyle('display','block') ; 
	    } ) ;
	},
	
	/*
	 * @type : method
	 * @name : initMouseEvent
	 * @desc : gere l'initialisation des listeners mouseeter/mouseleave
	 */
	initMouseEvent: function()
	{
		// menu spy mouseenter/mouseleave sur tous les noeuds ul li
		this.tree.getElements("ul li").addEvents({
			'mouseenter': function(event){
	   			if( $(this).getElements('ul li').count() > 0 ) // on test sur ul li car il existe des ul vide pour Accueil / Deconnexion...
	   			{
	   	        	$(this).getElements('ul')[0].setStyles({'display':'block','visibility':'visible'});
	   			}
	   		},
	   		'mouseleave': function(event){
	   			if( $(this).getElements('ul li').count() > 0 )
	   			{
	           		$(this).getElements('ul')[0].setStyles({'display':'none','visibility':'hidden'});
	   			}
	   		}
	   } );
	},
	
	/*
	 * @type : method
	 * @name : initFocusAndBlurEvent
	 * @desc : gere les evenements focus et blur sur les liens A du menuContainer
	 * 		   _gere la surbrillance des elements
	 * 		   _gere l'affichage des sous-menus 
	 */
	initFocusAndBlurEvent: function()
	{	
		// menu spy focus to enable keyboard spyer to do right process
		this.tree.getElements("a").addEvents({
			'focus': function(event){
// _d.dump( 'Focus fired ' );

				this.focus = true ;
				if( event )
					var el = event.target ; // recupere l'element qui a declenche l'evenement
				else if( this.focusItem )
					var el = this.focusItem ;
				
				// dans le cas ou on arrive sur le premier element pour la premiere fois
				if( !this.currentLiContainer )
		   		  	this.currentLiContainer = $( el ).getParent() ;

				var liChildren = this._getCurrentLiSubBoxItem() ;

		   		// mise en surbrillance de l'element courant
		   		$( el ).addClass('active');		
				// affichage du bloc si on est sur un a.firstrow
				if( ( $( el ).hasClass('firstrow') || this.keyDown == 'right' 
					|| this.keyDown == 'tab' || this.keyDown == 'shift+tab' ) && liChildren )
				{
					this.currentLiContainer.getChildren('ul')[0].setStyle('display','block');
					this.currentLiContainer.getChildren('ul')[0].setStyle('visibility','visible');
				}
				
				this.focusItem = null ;
			    
		  	}.bind( this ), // bind de l'element sur this : permettra d'avoir un acces a this depuis l'evenement
		  	
		  	'blur': function(event){
// _d.dump( 'Blur fired ' ); 
		
		  		this.focus = false ;
				if( event ) // event n'est pas toujours recu
					var el = event.target ;	// recupere l'element qui a declenche l'evenement
				else if( this.blurItem )
					var el = this.blurItem ;
				
		  		switch( this.keyDown )
		  		{
			  		case 'tab':		  			
		  				this._cleanFirstAndLastMenuItem();
		  				
			  			if( this.currentLiContainer )
			  			{
// _d.dump( this.ulIndex );
			  				// cas normal ou l'on nettoie les precedents
							if( this.ulIndex == 0 )
								var prevUlContainer = this.tree.getChildren()[0].getFirst() ;
							else
								var prevUlContainer = this.tree.getChildren()[this.ulIndex-1].getFirst() ;
				  			
				  			if( prevUlContainer )
				  			{
					  			var prevUlElements = prevUlContainer.getElements('ul') ;
					  			if( prevUlElements.count() > 0 ) // nettoyage des precedents
					  				prevUlElements.each( function(item,index){
					  					item.setStyle('display','none');
					  					item.setStyle('visibility','hidden');
					  				});
					  			// retire la surbrillance des liens du bloc precedent
								prevUlContainer.getElements('a').each( 
					  				//this._removeClassActiv()
										function(item){
											if(item.hasClass('active') )
												item.removeClass('active');
										}, this
								);
				  			}				
			  			}
			  			break;
			  			
			  		case 'shift+tab':
			  			this._cleanFirstAndLastMenuItem();
			  			
			  			if( this.currentLiContainer )
			  			{
				  			// dans le cas oi l'on a utilise shift+tab il fut nettoyer les suivants
			  				if( this.ulIndex == ( this.tree.getChildren().count() - 1 ) )
								var nextUlContainer = this.tree.getChildren()[this.ulIndex].getFirst() ;
							else
								var nextUlContainer = this.tree.getChildren()[this.ulIndex+1].getFirst() ;

			  				if( nextUlContainer )
							{
								var nextUlElements = nextUlContainer.getElements('ul') ;
// _d.dump( 'list des ul a effacer', true);
								if( nextUlElements.count() > 0 ) // nettoyage des suivants
									nextUlElements.each( function(item,index){
// _d.dump( item );
					  					item.setStyle('display','none');
					  					item.setStyle('visibility','hidden');
					  				});
					  			// retire la surbrillance des liens du bloc precedent
								nextUlContainer.getElements('a').each( 
					  				//this._removeClassActiv()
										function(item){
											if(item.hasClass('active') )
												item.removeClass('active');
										}, this
								);
							}
			  			}
			  			break;
			  			
			  		case 'down':
			  			if( !el.hasClass('firstrow') )
			  				$( el ).removeClass('active');
			  			break;	
			  			
			  		case 'up':
			  			$( el ).removeClass('active');
			  			break;
			  			
			  		case 'right':
			  			break;
			  			
			  		case 'left':
				  			// cache le bloc ul
							if( el.getParent().getParent() )
								el.getParent().getParent().setStyle('display','none');
				  			// retire la surbrillance des liens du bloc cache
							el.getParent().getParent().getElements('a').each(
				  				//this._removeClassActiv() 
				  				function(item){
			  						if(item.hasClass('active') )
			  							item.removeClass('active');
			  					}, this
				  			);
							this.blurItem = null ;
			  			break;
			  		default:
		  		}
			    
		  	}.bind( this )
		} ) ;
		
		// menu spy keyboard
		this.initUpDownLeftRightEvent() ;
	},
	
	/*
	 * @type: method
	 * @name: initUpDownLeftRightEvent
	 * @desc: gere la navigation au clavier du menu horizontal
	 * 		  ne doit s'occuper QUE de positionner la propriete currentliContainer, et liIndex si necessaire
	 * 		  doit egalement declencher les evenements focus, et blur si necessaire
	 * @params: li dom object 
	 * @return: void
	 */
	initUpDownLeftRightEvent: function ()
	{
	    var mySubNavKeyboardEvents = new Keyboard({
		    defaultEventType: 'keydown', 
		    active: true,
		    events: {
	    		'tab': function(event){
// _d.dump( 'Tab fired');
// _d.dump( 'tab clicked, focus : ' + this.focus );

					if( this.focus == true )
					{
// _d.dump( 'focus on' );
						this.keyDown = 'tab' ;
						
						// reset des index
						this.liIndex = -1 ;
						if( this.ulIndex + 1 == this.tree.getChildren().count() )
						{
							// on est au bout du menu, on repasse au 1er
							this.ulIndex = 0 ;
							this.tree.getChildren()[this.ulIndex].getFirst().getElement('a.firstrow').blur(); // fireEvent('blur');
							this.currentLiContainer = null ;
						}
						else
						{
							// on passe au suivant
							event.preventDefault(); // bloc la tabulation sur le prochain menu
							
							this.ulIndex += 1 ;
							
							// cache le noeud actuel
							this.tree.getChildren()[this.ulIndex-1].getFirst().getElement('a.firstrow').blur(); // fireEvent('blur');
							
							this.currentLiContainer = this.tree.getChildren()[this.ulIndex].getFirst() ;
							this.currentLiContainer.getElement('a.firstrow').focus(); // fireEvent('focus');
						}
					}
			  	}.bind( this ),
	    		
	    		'shift+tab': function(event){
// _d.dump( 'Shift Tab fired');
// _d.dump( 'tab clicked, focus : ' + this.focus );

					if( this.focus == true )
					{
						this.keyDown = 'shift+tab' ;
						
						// reset des index
						this.liIndex = -1 ;
						if( this.ulIndex - 1 < 0 )
						{
							// on est deja sur le premier element
							this.ulIndex = 0 ;
							// bloc le shift-tab sur le premier element du menu : si on commente on va boucler sur le menu en repartant du dernier
							// this.tree.getChildren()[this.ulIndex].getFirst().getElement('a.firstrow').blur(); // fireEvent('blur');
						}
						else
						{
							// on passe au précédent
							event.preventDefault(); // bloc la tabulation sur le précédent menu
							
							this.ulIndex -= 1 ;
							this.tree.getChildren()[this.ulIndex].getFirst().getElement('a.firstrow').blur() ;
							this.currentLiContainer = this.tree.getChildren()[this.ulIndex].getFirst() ;
							this.currentLiContainer.getElement('a.firstrow').focus(); // fireEvent('focus');
						}
					}
					else if( this.focus == false )
					{
						
					   var nbItems = this.tree.getChildren().count();
					   if (nbItems>0) { // Le menu n'est pas vide
					      
					      event.preventDefault(); // bloc la tabulation sur le précédent
					      
					      this.ulIndex = nbItems - 1 ;
					      
					      this.tree.getChildren()[this.ulIndex].getFirst().getElement('a.firstrow').focus() ;
					      
					   }
					   else { // Le menu est vide
					      
					      this.ulIndex = -1;
					      
					   }
					      
					}
			  	}.bind( this ),
			  				  	
		        'down': function(event){
	    			if( this.focus == true )
					{
	    				this.keyDown = 'down' ;
	    				
		    			event.preventDefault();
		    					    			
		    			var liChildren = this._getCurrentLiSubBoxItem() ;
		    			if( liChildren )
		    			{
			    			if( this.liIndex < ( liChildren.count() -1 ) )
			    				this.liIndex += 1 ;
		    			
			    			liChildren[this.liIndex].getElement('a').focus(); // fireEvent('focus') ;
		    			}
					}
	    		}.bind( this ), 
	    		
			  	'up': function(event){
			  		if( this.focus == true )
					{
			  			this.keyDown = 'up' ;
			  			
		    			event.preventDefault(); // bloc le scroll de la fenêtre	
		    			
		    			var liChildren = this._getCurrentLiSubBoxItem() ;
		    			if( liChildren )
		    			{
			    			if( this.liIndex > 0 )
			    				this.liIndex -= 1 ;
	
			    			liChildren[this.liIndex].getElement('a').focus(); // fireEvent('focus') ;
		    			}
					}
	    		}.bind( this ), 
	    		
		        'right': function(event){
	    			if( this.focus == true )
					{
	    				this.keyDown = 'right' ;
	    				
		    			event.preventDefault();
	
		    			var liChildren = this._getCurrentLiSubBoxItem() ;
		    			if( liChildren && this.liIndex >= 0 
		    				&& liChildren[this.liIndex].getElements('ul > li') 
		    				&& liChildren[this.liIndex].getElements('ul > li').count() > 0)
		    			{

		    				this.currentLiContainer = liChildren[this.liIndex] ; 
	    					this.liIndex = 0 ; // index du nouveau bloc affiche
	    					
	    					// avant de faire le focus il faut toujours que l'élément soit visible, sinon IE6 lance une erreur, et FF n'execute pas le focus a moins de changer de fenetre et de revenir
							this.currentLiContainer.getChildren('ul')[0].setStyle('display','block');
							this.currentLiContainer.getChildren('ul')[0].setStyle('visibility','visible');
							this.focusItem = this.currentLiContainer.getChildren('ul')[0].getElement('a');
							this.currentLiContainer.getChildren('ul')[0].getElement('a').focus(); // fireEvent('focus');
	    				}
					}
	    		}.bind( this ), 
	    		
		        'left': function(event){
	    			if( this.focus == true )
					{
	    				this.keyDown = 'left' ;
	    				
		    			event.preventDefault();
		    			if( this.currentLiContainer.getChildren('a.firstrow').count() == 0 )
		    			{	
		    				this.previousLiContainer = this.currentLiContainer ;

		    				// getParent()[= ul]. getParent()[= li]
		    				this.currentLiContainer = this.currentLiContainer.getParent().getParent() ;
		    				
		    				// recherche de l'index qui contenait la boite qui vient d'etre ferme
		    				var newLiChildren = this._getCurrentLiSubBoxItem() ;
		    				try
		    				{
			    				newLiChildren.each( 
			    					// this._lookForIndex(item, index)
			    					function(item,index){
				    					if( this.previousLiContainer && item == this.previousLiContainer )
				    					{
				    						this.liIndex = index;
				    						throw('trouve');
				    					}
				    				}, this ) ;
		    				}catch( e )
		    				{
		    				}
		    				
		    				this.blurItem = this.previousLiContainer.getChildren('ul')[0].getElement('a');
		    				this.blurItem.fireEvent('blur');
		    				newLiChildren[this.liIndex].getElement('a').focus() ; // fireEvent('focus');
		    				this.previousLiContainer = null ;
		    			}
					}
	    		}.bind( this ),
	    		
	    		'enter': function(event){
	    			if( this.focus == true )
					{
	    				event.preventDefault();
	    				
		    			var liChildren = this._getCurrentLiSubBoxItem() ;
		    			$(window).location = liChildren[this.liIndex].getElement('a').href ;
					}
	    		}
		    }
		});
	},
	
	/*
	 * @type : method
	 * @name : disableFirstRowLinks
	 * @desc : desactive les liens du premier niveau qui ne font que pointer vers des pages intermediaires affichant le 
	 * 		   sous-menu et qui ne servent que si l'utilisateur n'a pas active javascript
	 */
	disableFirstRowLinks: function()
	{
		// disable a.firstrow when javascript is active and only for item that didn't have any submenu
		this.tree.getElements("a.firstrow").each( function( item, index ){
// _d.dump( item.getParent().getChildren('ul').getElement('li').count() );
			if( item.getParent().getChildren('ul').getElement('li').count() > 0 )
				item.href = '#' ;
		} ) ;
	},
	
	displayBlock: function()
	{
	},
	
	hideBlock: function()
	{
	},
	
	/*
	 * @type: method
	 * @name: _getCurrentLiSubBoxItem
	 * @desc: retourne les noeuds li enfants du noeud li courant
	 * @return: array li
	 */
	_getCurrentLiSubBoxItem: function(){
		if( this.currentLiContainer && this.currentLiContainer.getElements('ul > li').count() > 0 )
			return this.currentLiContainer.getChildren('ul')[0].getChildren('li') ;
		else
			return null ;
	},
	
	/*
	 * @type : method
	 * @name : _removeClassActiv
	 * @desc : supprime la class active de l'element, devrait être utilise que sur des tableaux de tag a
	 * @TODO : actuellement cette methode est inutilisee car je n'arrive pas a recuperer 'item'
	 */
	_removeClassActiv: function(item){
		if(item.hasClass('active') )
			item.removeClass('active');
	},
	
	/*
	 * @type : method
	 * @name : _lookForIndex
	 * @desc : supprime la class active de l'element, devrait être utilise que sur des tableaux de tag a
	 * @TODO : actuellement cette methode est inutilisee car je n'arrive pas a recuperer 'item'
	 */
	_lookForIndex: function(item,index){
		if( this.previousLiContainer && item == this.previousLiContainer )
		{
			this.liIndex = index	;
			this.previousLiContainer = null ;
			throw('trouve');
		}
	},
	
	/*
	 * @type : method
	 * @name : _cleanFirstAndLastMenuItem
	 * @desc : cache les blocs du premier et dernier menu. Ne le fait que si currentLiContainer a bien ete supprime
	 * 	c'est a dire positionne a null
	 */
	_cleanFirstAndLastMenuItem: function()
	{
		if( !this.currentLiContainer )
		{
			if( this.tree.getFirst().getElement("a.firstrow").getParent().getElements('ul').count() > 0 )
			{
				// cache les blocs
				this.tree.getFirst().getElement('a.firstrow').getParent().getElements('ul').each( function(item,index){
					item.setStyle('display','none');
				});
				
				this.tree.getFirst().getElements('a').each( function(item,index){
					if( item.hasClass('active') ) item.removeClass('active');
				});
			}
			
			if( this.tree.getLast().getElement('a.firstrow').getParent().getElements('ul').count() > 0 )
			{
				this.tree.getLast().getElement('a.firstrow').getParent().getElements('ul').each( function(item,index){
					item.setStyle('display','none');
				});

				this.tree.getLast().getElements('a').each( function(item,index){
					if( item.hasClass('active') ) item.removeClass('active');
				});
			}
		}
	}
	
}) ;
