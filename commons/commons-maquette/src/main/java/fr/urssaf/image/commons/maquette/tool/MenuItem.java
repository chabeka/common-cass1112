package fr.urssaf.image.commons.maquette.tool;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException;

public class MenuItem{

	protected static int counter = 0 ;
	
	protected int id ;
	private String link = "" ;
	private String title = ""  ;
	private String description = "" ;
	private MenuItem parent = null ;
	public List<MenuItem> children ;
	
	/**
	 * 
	 */
	public MenuItem() {
		super();
		// J'affecte l'identifiant unique
		id = MenuItem.counter ;
		
		// J'incr�mente le compteur pour la prochaine instance
		MenuItem.counter++ ;
		
		//
		parent = null ;
		
		// Cr�ation du contenu des enfants
		children = new ArrayList<MenuItem>() ;
	}
	
	protected int getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public MenuItem getParent() {
		return parent;
	}
	
	public Boolean hasParent()
	{
		Boolean result = false ;
		
		if( parent != null )
			result = true ;
		
		return result ;
	}
	
	public void addParent( MenuItem menuItem ) throws ReferentialIntegrityException {
		// V�rification int�grit� r�f�rentielle
		if( menuItem.getId() == id )
			throw new ReferentialIntegrityException( "(" + menuItem.getId() + ") " + menuItem.getTitle() + " ne peut �tre parent de lui m�me" ) ;
		if( isAChild( this, menuItem ) )
			throw new ReferentialIntegrityException( "(" + menuItem.getId() + ") " + menuItem.getTitle() + " ne peut �tre parent car il est d�j� enfant" ) ;
		
		// Ajout du parent
		parent = menuItem ;
		
		// Synchronisation du parent
		menuItem.addChild(this);			
	}
	
	public List<MenuItem> getChildren() {
		return children;
	}
	
	public Boolean hasChildren()
	{
		Boolean result = false ;
		
		if( children.size() > 0 )
			result = true ;
		
		return result ;
	}
	
	public void addChild( MenuItem implMenuItem )throws ReferentialIntegrityException {
		// v�rification de l'int�grit�
		if( implMenuItem == parent )
			throw new ReferentialIntegrityException( "(" + implMenuItem.getId() + ") " + implMenuItem.getTitle() + " ne peut �tre enfant car il est d�j� parent" ) ;
		if( isAChild(this, implMenuItem) )
			throw new ReferentialIntegrityException( "(" + implMenuItem.getId() + ") " + implMenuItem.getTitle() + " ne peut �tre enfant car il est d�j� enfant" ) ;
		
		MenuItem c = (MenuItem) implMenuItem;
		
		// ajout de l'item � la liste
		children.add( implMenuItem ) ;
		
		// synchronisation avec l'item en lui affectant le parent
		c.setParent( this ) ;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setParent( MenuItem implMenuItem ) throws fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException {
		// V�rification int�grit� r�f�rentielle
		if( implMenuItem.getId() == id )
			throw new fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException( "(" + implMenuItem.getId() + ") " + implMenuItem.getTitle() + " ne peut �tre parent de lui m�me" ) ;
		if( isAChild( this, implMenuItem ) )
			throw new fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException( "(" + implMenuItem.getId() + ") " + implMenuItem.getTitle() + " ne peut �tre parent car il est d�j� enfant" ) ;

		parent = implMenuItem ;
	}
	
	@SuppressWarnings("unused")
	private static Boolean isNotAChild( MenuItem menuItem, MenuItem expectedChild )
	{
		return !MenuItem.isAChild( menuItem, expectedChild );
	}
	
	private static Boolean isAChild( MenuItem implMenuItem, MenuItem menuItem )
	{
		Boolean result = false ;
		
		// on parcours les enfants de menuItem pour chercher si expectedChild y est d�j� r�f�renc�
		for( int i = 0 ; i < implMenuItem.getChildren().size() ; i++ )
		{
			if( implMenuItem.getChildren().get(i).getId() == menuItem.getId() )
			{
				result = true ;
				break ;
			}
			else if( implMenuItem.getChildren().get(i).hasChildren() )
			{
				for( int j = 0 ; j < implMenuItem.getChildren().get(i).getChildren().size(); j++ )
				{
					result = isAChild(implMenuItem.getChildren().get(i).getChildren().get(j), menuItem) ;
					if( result )
						break ;
				}
			}
		}
		
		return result ;
	}


}
