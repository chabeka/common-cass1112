package fr.urssaf.image.commons.maquette.template.parser;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.template.parser.exception.MissingSourceParserException;

/**
 * @author CER6990172
 * @desc parse la balise body de toute cha�ne ou fichier de template pour en r�cup�rer les �l�ments de la balise <body>
 * 		 attention, s'il le faut il peut recr�er le body 
 */
public class BodyParser
{
	private Boolean forceBodyTag = false ;
	private Element bodyTag ;
	
	/**
	 * @desc default constructor
	 */
	public BodyParser() {
		
	}
	
	/**
	 * @desc ex�cute le doParse dans la foul�e
	 * @param sc
	 * @throws MissingSourceParserException 
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	public BodyParser( Source sc ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException{
		doParse(sc) ;
	}
	
	/**
	 * @desc ex�cute le doParse dans la foul�e
	 * @param sc
	 * @param forceBodyTag si aucun tag body n'est trouv�, on rajoute ce tag � la source et on ne l�ve pas d'exception
	 * @throws MissingSourceParserException 
	 * @throws MissingHtmlElementInTemplateParserException 
	 */
	public BodyParser( Source sc, Boolean forceBodyTag ) throws MissingSourceParserException, MissingHtmlElementInTemplateParserException{
		this.forceBodyTag = forceBodyTag ;
		doParse(sc) ;
	}

	/**
	 * @return the bodyTag
	 */
	public Element getBodyTag() {
		return bodyTag;
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
			bodyTag = doGetBodyTag( sc ) ;
		}
		else
			throw new MissingSourceParserException("Body") ;
	}
	
	/**
	 * @desc	retourne la balise body
	 * @param sc
	 * @throws MissingBodyTagBodyParserException 
	 */
	protected Element doGetBodyTag( Source sc ) throws MissingHtmlElementInTemplateParserException {
		List<Element> elBody = sc.getAllElements("body");
		
		Element body ;
		// prise en compte du cas o� l'on a pas de body dans le html de l'application
		// On reconstruit donc le tag manquant
		if( elBody.size() == 0 )
		{
			if( forceBodyTag )
			{
				String newHtml = "<body>" + sc.toString() + "</body>" ;
				Source newSc = new Source( newHtml ) ;
				elBody = newSc.getAllElements("body");
			}
			else
				throw new MissingHtmlElementInTemplateParserException("Body tag") ;
		}

		body = elBody.get(0) ;
		return body ;
	}
		
}
