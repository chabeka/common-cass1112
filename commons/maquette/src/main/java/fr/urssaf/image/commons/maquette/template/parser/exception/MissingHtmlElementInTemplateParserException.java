package fr.urssaf.image.commons.maquette.template.parser.exception;

public class MissingHtmlElementInTemplateParserException extends Exception {

	public MissingHtmlElementInTemplateParserException(String id) {
		super( "L'�l�ment suivant n'a pas �t� trouv� dans le template de la maquette : " + id ) ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
