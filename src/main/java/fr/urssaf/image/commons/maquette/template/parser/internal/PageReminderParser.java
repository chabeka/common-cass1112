package fr.urssaf.image.commons.maquette.template.parser.internal;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;

/**
 * Extrait du template de la maquette la balise identifiée par "pagereminder"
 */
public final class PageReminderParser extends AbstractParser
{
   
   
   /**
    * La balise identifiée par "pagereminder"
    */
	private Element pageReminderTag ;
	
	
	
	/**
	 * Constructeur par défaut
	 */
	public PageReminderParser() {
	   super();
	}
	
	
	/**
    * Constructeur qui fait le parsing
    * 
    * @param source la source HTML
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
    */
	public PageReminderParser(Source source)
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException{
	   super();
	   doParse(source) ;
	}
	
	
	/**
	 * Renvoie la balise identifiée par "pagereminder"
	 * @return La balise identifiée par "pagereminder"
	 */
	public Element getPageReminderTag() {
		return pageReminderTag;
	}


	/**
	 * {@inheritDoc}
	 */
	protected void doParse(Source source)
	throws 
	MissingSourceParserException, 
	MissingHtmlElementInTemplateParserException
	{	
		if( source == null ) {
		   throw new MissingSourceParserException("PageReminder") ;
		}
		else {
		   pageReminderTag = getElementById(source, "pagereminder");
		}
	}
	
	
}
