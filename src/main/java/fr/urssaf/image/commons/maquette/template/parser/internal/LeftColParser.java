package fr.urssaf.image.commons.maquette.template.parser.internal;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;

/**
 * Extrait du template de la maquette la balise identifiée par "leftcol"
 */
public final class LeftColParser extends AbstractParser
{
	
   
   /**
    * La balise identifiée par "leftcol"
    */
   private Element leftColTag ;
	
      
	/**
	 * Constructeur par défaut
	 */
	public LeftColParser() {
		super();
	}
	
	
	/**
    * Constructeur qui fait le parsing
    * 
    * @param source la source HTML
    * @throws MissingSourceParserException si la source HTML à parser est manquante
    * @throws MissingHtmlElementInTemplateParserException si un élément est manquant dans la source HTML 
    */
	public LeftColParser(Source source) 
	throws 
	MissingHtmlElementInTemplateParserException, 
	MissingSourceParserException {
		super();
	   doParse(source) ;
	}
	
	
	/**
	 * Renvoie la balise identifiée par "leftcol"
	 * @return La balise identifiée par "leftcol"
	 */
	public Element getLeftColTag() {
		return leftColTag;
	}

	
	/**
	 * {@inheritDoc} 
	 */
	protected void doParse(Source source)
	throws 
	MissingHtmlElementInTemplateParserException, 
	MissingSourceParserException
	{	
		if (source == null) {
		   throw new MissingSourceParserException("LeftCol") ;
		}
		else {
		   leftColTag = getElementById(source, "leftcol");
		}
	}
	
		
}
