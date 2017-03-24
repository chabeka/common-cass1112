/*
 * @type : class
 * @name : MenuClass
 * @desc : gere le fonctionnement du menu de type :
 *      <ul><li><a class="firstrow">niveau1</a><ul><li><a></a></li>[...]</ul></li></ul>[...]
 */
var DebugToolboxClass = new Class(
{
	debugToolBoxId: 'DebugToolBox',
    
    initialize: function( launchSpy )
    {
        if( launchSpy == true )
            this.spyOnFocus();

        this.buildDebugZone();
    },
    
    /*
     * @type : method
     * @name : dump
     * @desc : permet d'afficher le contenu de la variable
     */
    dump: function ( dataToDisplay, replace )
    {
        previousData = $( this.debugToolBoxId ).get( 'html' ) + '<br />' ;
        
        if( replace )
            previousData = null ;
        
        if( Browser.Engine.gecko && console )
            console.log( dataToDisplay ) ;
        else
            $( this.debugToolBoxId ).set( 'html', previousData + dataToDisplay ) ;
    },
    
    /*
     * @type : method
     * @name : spyOnFocus
     * @desc : surveille l'ensemble de la fenetre et detecte les changements de focus
     */
    spyOnFocus: function()
    {
        $$('body')[0].getElements('').addEvents({
           'focus': function(event){
                this.dump( 'focus sur : ' + $(event.target).get('tag') + ' ' + $(event.target) );
            }.bind( this ),
            'blur': function(event){
                this.dump( 'blur depuis : ' + $(event.target).get('tag') + ' ' + $(event.target) );
            }.bind( this )
        } ) ;
    },
    
    /*
     * @type : method
     * @name : buildDebugZone
     * @desc : construit l'element qui va contenir les messages de debug
     */
    buildDebugZone: function()
    {
        if( !$( this.debugToolBoxId ) )
        {
            var debugToolBox  = new Element('div', {'id': this.debugToolBoxId,
            										'styles':{'text-align':'left',
            												  'color':'black'} } );
            debugToolBox.inject( $$('body').getLast(), 'bottom' ) ;
        }
    }
    
} ) ;