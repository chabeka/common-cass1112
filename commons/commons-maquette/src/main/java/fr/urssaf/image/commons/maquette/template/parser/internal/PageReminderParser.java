package fr.urssaf.image.commons.maquette.template.parser.internal;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingSourceParserException;

/**
 * @author CER6990172
 * @desc parse la balise body de toute cha�ne ou fichier de template pour en r�cup�rer les �l�ments de la balise <div id="pagereminder">
 */
public class PageReminderParser extends AbstractParser
{
	private Element pageReminderTag ;
	
	/**
	 * @desc default constructor
	 */
	public PageReminderParser() {
		
	}
	
	/**
	 * @desc ex�cute le doParse dans la foul�e
	 * @param sc
	 * @throws MissingSourceParserException 
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	public PageReminderParser( Source sc ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException{
		doParse(sc) ;
	}
	
	/**
	 * @return the contentAppTag
	 */
	public Element getPageReminderTag() {
		return pageReminderTag;
	}


	/**
	 * @desc lance le parsing des �l�ments de la balise body contenu dans l'attribut Source
	 * @param sc
	 * @throws MissingHtmlElementInTemplateParserException 
	 * @throws MissingSourceParserException 
	 */
	protected void doParse( Source sc ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException
	{	
		if( sc != null )
		{
			pageReminderTag = doGetPageReminderTag( sc );
		}
		else
			throw new MissingSourceParserException("PageReminder") ;
	}
	
	/**
	 * @desc	retourne la balise div pagereminder
	 * @param sc
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	protected Element doGetPageReminderTag( Source sc ) throws MissingHtmlElementInTemplateParserException {
		return getElementById( sc, "pagereminder" ) ;
	}
	
}
