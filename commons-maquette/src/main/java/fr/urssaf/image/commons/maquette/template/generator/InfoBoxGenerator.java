package fr.urssaf.image.commons.maquette.template.generator;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.exception.MissingInfoBoxPropertyException;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;

/**
 * Cette classe génère le HTML des boîtes de gauche de la maquette 
 * 			
 */
public final class InfoBoxGenerator
{		
	
   private InfoBoxGenerator() {
      
   }
   
   /**
    * Génère le HTML de la boîte de gauche passée en paramètre<br>
    * <br>
    * Le titre ainsi que la description sont automatiquement traités pour le
    * remplacement des caractères spéciaux par leurs équivalents HTML
    * 
    * @param infoBoxItem la boîte de gauche dont il faut faire le rendu HTML
    * 
    * @return le rendu HTML
    * 
    * @throws MissingInfoBoxPropertyException S'il manque une propriété requise à l'InfoBox
    */
   public static String build(InfoBoxItem infoBoxItem) 
	throws MissingInfoBoxPropertyException
	{
		
      // Vérifie que l'id n'est pas vide
      if (StringUtils.isEmpty(infoBoxItem.getShortIdentifier())) {
         throw new MissingInfoBoxPropertyException(null,"shortIdentifier");
      }
      
      // Vérifie que le contenu n'est pas vide
      if (StringUtils.isEmpty(infoBoxItem.getContent())) {
         throw new MissingInfoBoxPropertyException(null,"content");
		}
		
      // Vérifie que le titre n'est pas vide
		if (StringUtils.isEmpty(infoBoxItem.getTitle())) {
		   throw new MissingInfoBoxPropertyException(null,"title");
		}
		
		// Vérifie que la description n'est pas vide
		if (StringUtils.isEmpty(infoBoxItem.getBoxDesc())) {
		   throw new MissingInfoBoxPropertyException(null,"boxDesc");
		}
		
		// Traite les caractères spéciaux dans le titre et la description
		String title = StringEscapeUtils.escapeHtml(infoBoxItem.getTitle());
		String desc = StringEscapeUtils.escapeHtml(infoBoxItem.getBoxDesc());
		
		// Génère le HTML
		StringBuilder sbHtml = new StringBuilder();
		// Balise <h3> avec le titre
		sbHtml.append(
		      String.format(
		            "<h3 class=\"boxTitle\" id=\"%s-title\">",
		            infoBoxItem.getShortIdentifier()));
		sbHtml.append(title);
		sbHtml.append("</h3>");
		sbHtml.append(MaquetteConstant.HTML_CRLF);
		// Balise <p> avec le contenu
		sbHtml.append(
            String.format(
                  "<p class=\"boxContent\" id=\"%s\" title=\"%s\">",
                  infoBoxItem.getShortIdentifier(),
                  desc));
		sbHtml.append(MaquetteConstant.HTML_CRLF);
		sbHtml.append(infoBoxItem.getContent());
		sbHtml.append("</p>");
      sbHtml.append(MaquetteConstant.HTML_CRLF);
		
		// Renvoie du résultat
      return sbHtml.toString();
		
	}
}
