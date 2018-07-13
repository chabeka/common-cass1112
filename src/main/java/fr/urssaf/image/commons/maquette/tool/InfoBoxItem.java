package fr.urssaf.image.commons.maquette.tool;

import org.apache.commons.lang.StringUtils;


/**
 * Représente une boîte dans la colonne de gauche de la maquette
 * 			
 */
public final class InfoBoxItem 
{	
	
   /**
    * Identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
    */
   private String shortIdentifier ;
   
   
   /**
    * Titre de la boîte, rendu dans une balise HTML &lt;H3&gt;<br>
    * <br>
    * <b>NB : les caractères spéciaux seront automatiquement transformés en leur équivalent 
    * HTML lors du processus de rendu.</b>
    */
	private String title;
	
	
	/**
	 * Texte qui sera affiché en survolant la boîte avec la souris<br>
    * <br>
    * <b>NB : les caractères spéciaux seront automatiquement transformés en leur équivalent 
    * HTML lors du processus de rendu.</b>
	 */
	private String boxDesc;
	
	
	/**
	 * Contenu HTML de la boîte
	 */
	private String content;
	
	
	/**
	 * Constructeur
	 */
	public InfoBoxItem() {
		shortIdentifier = "" ;
		title = "" ;
		boxDesc = "" ;
		content = "" ;
	}
	
	
	/**
	 * Constructeur
	 * @param shortIdentifier Identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
	 * @param title Titre de la boîte, rendu dans une balise HTML &lt;H3&gt;. <b>NB : les 
	 *        caractères spéciaux seront automatiquement transformés en leur équivalent 
	 *        HTML lors du processus de rendu.</b>
	 * @param boxDesc Texte qui sera affiché en survolant la boîte avec la souris <b>NB : les 
    *        caractères spéciaux seront automatiquement transformés en leur équivalent 
    *        HTML lors du processus de rendu.</b>
	 */
	public InfoBoxItem(String shortIdentifier, String title, String boxDesc) {
		this.shortIdentifier = shortIdentifier ;
		this.title = title;
		this.boxDesc = boxDesc;
		content = "" ;
	}

	
	/**
	 * Renvoie l'identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
	 * @return Identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
	 */
	public String getShortIdentifier() {
		return shortIdentifier;
	}
	
	
	/**
	 * Définit l'identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
	 * @param shortIdentifier Identifiant court utilisé pour la construction des attributs 
    * des balises HTML (<code>id</code>, <code>class</code>, ...). 
	 */
	public void setShortIdentifier(String shortIdentifier) {
		this.shortIdentifier = shortIdentifier;
	}
	
	
	/**
	 * Renvoie le titre de la boîte, rendu dans une balise HTML &lt;H3&gt;
	 * @return Titre de la boîte, rendu dans une balise HTML &lt;H3&gt;
	 */
	public String getTitle() {
		return title;
	}
	
	
	/**
	 * Définit le titre de la boîte, rendu dans une balise HTML &lt;H3&gt;<br>
	 * <br>
	 * <b>NB : les caractères spéciaux seront automatiquement transformés en leur équivalent 
    * HTML lors du processus de rendu.</b>
	 * @param title Titre de la boîte, rendu dans une balise HTML &lt;H3&gt;
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	/**
	 * Renvoie le texte qui sera affiché en survolant la boîte avec la souris
	 * @return Texte qui sera affiché en survolant la boîte avec la souris
	 */
	public String getBoxDesc() {
		return boxDesc;
	}
	
	
	/**
	 * Définit le texte qui sera affiché en survolant la boîte avec la souris<br>
	 * <br>
	 * <b>NB : les caractères spéciaux seront automatiquement transformés en leur équivalent 
    * HTML lors du processus de rendu.</b>
	 * @param boxDesc Texte qui sera affiché en survolant la boîte avec la souris
	 */
	public void setBoxDesc(String boxDesc) {
		this.boxDesc = boxDesc;
	}
	
	
	/**
	 * Renvoie le contenu HTML de la boîte
	 * @return Contenu HTML de la boîte
	 */
	public String getContent() {
		return content;
	}
	
	
	
	/**
	 * Définit le contenu HTML de la boîte
	 * @param content Contenu HTML de la boîte
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
	/**
	 * Ajoute une balise &lt;span&gt; au contenu HTML de la boîte
	 * @param attId complément pour l'attribut <code>id</code> de la balise 
	 * &lt;span&gt; (il est préfixé par {@link #shortIdentifier})
	 * @param attTitle attribut <code>title</code> de la balise &lt;span&gt; 
	 * @param tagContent contenu de la balise &lt;span&gt;
	 */
	public void addSpan(String attId, String attTitle, String tagContent)
	{
		
	   String span = "<span id=\"[id]\" title=\"[title]\">[contenu]</span><br />";
	   
	   String identifiant = shortIdentifier + "-" + attId;
	   
	   span = StringUtils.replace(span,"[id]",identifiant);
	   span = StringUtils.replace(span,"[title]",attTitle);
	   span = StringUtils.replace(span,"[contenu]",tagContent);
	   
	   content += span;
	   content += MaquetteConstant.HTML_CRLF;
	   
	}
	
	
	/**
	 * Ajoute un bouton au contenu HTML de la boîte (sous la forme d'un tag 
	 * <code>&lt;input type="button"&gt;</code>)
	 * 
	 * @param attId complément pour les attributs <code>id</code> et 
	 *        <code>class</code> de la balise &lt;span&gt; (ils sont 
	 *        préfixés par {@link #shortIdentifier})
	 *        
	 * @param name attribut <code>value<code> du bouton
	 * @param javascript Javascript du <code>onclick</code>
	 */
	public void addBtn(String attId, String name, String javascript)
	{
		
	   String bouton = "<input id=\"[id]\" class=\"[class]\" type=\"button\" value=\"[value]\" onclick=\"[onclick]\" tabindex=\"0\" />";
	   
	   String identifiant = shortIdentifier + "-" + attId;
	   
	   bouton = StringUtils.replace(bouton,"[id]",identifiant);
	   bouton = StringUtils.replace(bouton,"[class]",identifiant);
	   bouton = StringUtils.replace(bouton,"[value]",name);
	   bouton = StringUtils.replace(bouton,"[onclick]",javascript);
	   
	   content += bouton;
	   
	}
	
}
