package fr.urssaf.image.commons.maquette.template.parser.internal;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingSourceParserException;

/**
 * @author CER6990172
 * @desc parse la balise body de toute cha�ne ou fichier de template pour en r�cup�rer les �l�ments de la balise <div id="content-application">
 */
public class ContentAppParser extends AbstractParser
{
	private Element contentAppTag ;
	private List<Element> noScriptTag ;
	
	/**
	 * @desc default constructor
	 */
	public ContentAppParser() {
		
	}
	
	/**
	 * @desc ex�cute le doParse dans la foul�e
	 * @param sc
	 * @throws MissingSourceParserException 
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	public ContentAppParser( Source sc ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException{
		doParse(sc) ;
	}
	
	/**
	 * @return the contentAppTag
	 */
	public Element getContentAppTag() {
		return contentAppTag;
	}

	/**
	 * @return the noScriptTag
	 */
	public List<Element> getNoScriptTag() {
		return noScriptTag;
	}

	/**
	 * @desc lance le parsing des �l�ments de la balise body contenu dans l'attribut Source
	 * @param sc
	 * @throws MissingSourceParserException
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	protected void doParse( Source sc ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException
	{	
		if( sc != null )
		{
			contentAppTag = doGetContentAppTag( sc );
			noScriptTag = doGetNoScriptTag( sc ) ;
		}
		else
			throw new MissingSourceParserException("ContentApp") ;
	}
	
	/**
	 * @desc	retourne la balise div content-application
	 * @param sc
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	protected Element doGetContentAppTag( Source sc ) throws MissingHtmlElementInTemplateParserException {
		return getElementById( sc, "content-application" ) ;
	}
	
	/**
	 * @desc	r�cup�re les balises noscript
	 * @param sc
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	protected List<Element> doGetNoScriptTag( Source sc ) throws MissingHtmlElementInTemplateParserException {
		List<Element> elList = sc.getAllElements("noscript") ;
		if( elList.size() > 0 )
			return elList ;
		
		throw new MissingHtmlElementInTemplateParserException( "noscript" );
	}
	
}
