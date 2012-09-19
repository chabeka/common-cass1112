package fr.urssaf.image.commons.maquette.template.parser.internal;

import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;


/**
 * Classe mère pour les parser
 * 
 */
public abstract class AbstractParser
{
	/**
	 * Extrait la balise identifiée par id de la source HTML
	 *  
	 * @param source la source HTML
	 * @param idBalise l'identifiant de la balise HTML à extraire
	 * @throws MissingHtmlElementInTemplateParserException si la balise n'a pas été trouvée 
	 */
	protected final Element getElementById(Source source, String idBalise) 
	throws MissingHtmlElementInTemplateParserException
	{
		Element element = source.getElementById(idBalise) ;
		if(element == null) {
			throw new MissingHtmlElementInTemplateParserException(idBalise) ;
		}
		return element ;
	}
	
	
	/**
    * Parse la source HTML
    * 
    * @param source la source HTML à parser
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
    */
   protected abstract void doParse(Source source)
   throws MissingSourceParserException, 
   MissingHtmlElementInTemplateParserException ;
   
	
}
