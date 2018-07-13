package fr.urssaf.image.commons.maquette.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException;

/**
 * Cette classe représente un élément de menu dans le menu déroulant 
 * de la maquette (menu haut).<br>
 * <br>
 * L'interface {@link fr.urssaf.image.commons.maquette.definition.IMenu} 
 * contient une méthode qui renvoie une liste de MenuItem.<br>
 * <br>
 * Le site qui utilise la maquette doit implémenter cette interface pour
 * générer son menu.<br>
 * <br>
 * <b><u>Le paramétrage est décrit dans la documentation Word du composant commons-maquette</u></b><br>
 *
 */
public final class MenuItem implements Serializable
{

   private static final long serialVersionUID = 119370622941249722L;


   /**
	 * Compteur statique d'item de menus, permettant de gérer
	 * des identifiants uniques d'items de menu
	 */
   private static int counter = 0 ;
	
   
	/**
	 * L'identifiant unique de l'item de menu
	 */
   private final int idUnique ;
   
   
   /**
    * Le lien vers lequel pointe l'item de menu
    */
   private String link = "" ;
   
   
   /**
    * Le texte de l'item de menu
    */
	private String title = ""  ;
	
	
	/**
	 * Description affichée au survol avec la souris de l'item de menu
	 */
	private String description = "" ;
	
	
	/**
	 * Le parent
	 */
	private MenuItem parent  ;
	
	
	/**
	 * La liste des enfants
	 */
	private final List<MenuItem> children ;
	
	
	/**
	 * Constructeur
	 */
	public MenuItem() {
		
		// J'affecte l'identifiant unique
		idUnique = MenuItem.counter ;
		
		// J'incrémente le compteur pour la prochaine instance
		MenuItem.counter++ ;
		
		// Création de la liste des enfants
		children = new ArrayList<MenuItem>() ;
		
	}
	
	
	/**
	 * Renvoie l'identifiant unique de l'item de menu
	 * @return L'identifiant unique de l'item de menu
	 */
	protected int getIdUnique() {
		return idUnique;
	}

	
	/**
	 * Renvoie le lien vers lequel pointe l'item de menu
	 * @return Le lien vers lequel pointe l'item de menu
	 */
	public String getLink() {
		return link;
	}
	
	
	/**
	 * Définit le lien vers lequel pointe l'item de menu
	 * @param link Le lien vers lequel pointe l'item de menu
	 */
	public void setLink(String link) {
      this.link = link;
   }

   	
	/**
	 * Renvoie le texte de l'item de menu
	 * @return Le texte de l'item de menu
	 */
	public String getTitle() {
		return title;
	}
	
	
	/**
	 * Définit le texte de l'item de menu
	 * @param title Le texte de l'item de menu
	 */
	public void setTitle(String title) {
      this.title = title;
   }
	
	
	/**
	 * Renvoie la description affichée au survol avec la souris de l'item de menu
	 * @return Description affichée au survol avec la souris de l'item de menu
	 */
	public String getDescription() {
		return description;
	}
	
	
	/**
	 * Définit la description affichée au survol avec la souris de l'item de menu
	 * @param description Description affichée au survol avec la souris de l'item de menu
	 */
	public void setDescription(String description) {
	   this.description = description; 
	}
	
	
	/**
	 * Renvoie l'item parent
	 * @return l'item parent
	 */
	public MenuItem getParent() {
		return parent;
	}
	
	
	/**
	 * Renvoie un flag indiquant si l'item de menu possède un parent
	 * @return true si l'item de menu possède un parent, false dans le cas contraire
	 */
	public Boolean hasParent()
	{
		return parent!=null;
	}
	
	
	/**
	 * Définit le parent
	 * @param leParent le parent
	 * @throws ReferentialIntegrityException
	 */
	public void setParent( MenuItem leParent ) {
      
	   // Vérification intégrité référentielle
      
	   if( leParent.getIdUnique() == idUnique ) {
         throw new ReferentialIntegrityException(
               "(" + leParent.getIdUnique() + ") " + 
               leParent.getTitle() + " ne peut être parent de lui même" ) ;
      }
      
      if( isAChild( this, leParent ) ) {
         throw new ReferentialIntegrityException( 
               "(" + leParent.getIdUnique() + ") " + 
               leParent.getTitle() + " ne peut être parent car il est déjà enfant" ) ;
      }

      parent = leParent ;
      
   }

	
	/**
	 * Renvoie la liste des enfants
	 * @return la liste des enfants
	 */
	public List<MenuItem> getChildren() {
      return children;
   }
   
	
	/**
	 * Renvoie un flag indiquant si l'item de menu possède des enfants
	 * @return true si l'item de menu a des enfants, false dans le cas contraire
	 */
   public Boolean hasChildren()
   {
      return !children.isEmpty() ;
   }
	
	
	/**
	 * Ajoute un enfant
	 * @param enfant l'enfant
	 * @throws ReferentialIntegrityException
	 */
	public void addChild( MenuItem enfant ) {
		// vérification de l'intégrité
		if( enfant==parent) // NOPMD
		{
			throw new ReferentialIntegrityException( "(" + enfant.getIdUnique() + ") " + enfant.getTitle() + " ne peut être enfant car il est déjà parent" ) ;
		}
		if( isAChild(this, enfant) ) {
			throw new ReferentialIntegrityException( "(" + enfant.getIdUnique() + ") " + enfant.getTitle() + " ne peut être enfant car il est déjà enfant" ) ;
		}
		
		// ajout de l'item à la liste
		children.add( enfant ) ;
		
		// synchronisation avec l'item en lui affectant le parent
		enfant.setParent( this ) ;
		
	}

	
	/**
	 * Détermine si le paramètre unParent possède dans sa descendance l'enfant unEnfant 
	 * @param unParent le parent dans lequel rechercher
	 * @param enfantAchercher l'enfant à trouver
	 * @return true si l'enfant est dans la descendance du parent, false dans le cas contraire
	 */
	protected static Boolean isAChild( MenuItem unParent, MenuItem enfantAchercher )
   {
      Boolean result = false ;
      for(MenuItem enfant: unParent.getChildren())
      {
         if( enfant.getIdUnique() == enfantAchercher.getIdUnique() )
         {
            result = true ;
            break ;
         }
         else if( enfant.hasChildren() )
         {
            result = isAChild(enfant, enfantAchercher);
            if( result ) {
               break ;
            }
         }
      }
      return result ;
   }

}
