/**
 * 
 */
package fr.urssaf.image.commons.maquette.tool;

/**
 * Constantes
 */
@SuppressWarnings({"PMD.LongVariable","PMD.AvoidDuplicateLiterals"})
public final class MaquetteConstant {
   
   
   private MaquetteConstant() {
      
   }
   
	public static final String GETRESOURCEURI = "getResourceImageMaquette.do" ;
	
	public static final String THEME_AED = "aed";
	public static final String THEME_GED = "ged";
	
	
	/**
	 * Le chemin au sein des ressources JAR vers le template HTML de la maquette<br>
	 * 
	 */
	public static final String CHEMIN_TMPL_MAIN_HTML = "/resource/html/main.html";
	
	
	/**
	 * Retour charriot à utiliser dans les rendus HTML
	 */
	public static final String HTML_CRLF = "\r\n";
	
}
