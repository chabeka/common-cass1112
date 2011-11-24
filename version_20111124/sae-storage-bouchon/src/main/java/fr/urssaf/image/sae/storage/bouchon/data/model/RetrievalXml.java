package fr.urssaf.image.sae.storage.bouchon.data.model;

/**
 * Cette classe permet de de construie le flux xml pour les servies d'insertion
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD")
public final class RetrievalXml {
	/**
	 * Méthode permettant de construire le flux xml pour les servies de
	 * récupération
	 * 
	 * @return Le flux xml pour les servies de récupération
	 */
	public static String buildXmlfile() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		stringBuilder.append("<retrieveServiceData>");
		stringBuilder.append("<retrieveDoc>");
		stringBuilder.append("<document>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Path</string>");
		stringBuilder.append("<string>src/main/resources/doc.pdf</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("</document>");
		stringBuilder.append("</retrieveDoc>");
		stringBuilder.append("<retrieveMetaData>");
		stringBuilder.append("<document>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeRND</string>");
		stringBuilder.append("<string>1.2.3.3.1</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>NumeroCotisant</string>");
		stringBuilder.append("<string>719900</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Siret</string>");
		stringBuilder.append("<string>07412723410007</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>DenominationCompte</string>");
		stringBuilder.append("<string>COUTURIER GINETTE</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeOrganisme</string>");
		stringBuilder.append("<string>UR030</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("</document>");
		stringBuilder.append("</retrieveMetaData>");
		stringBuilder.append("<retrieveDocMetaData>");
		stringBuilder.append("<document>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Path</string>");
		stringBuilder.append("<string>src/main/resources/doc.pdf</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>UUID</string>");
		stringBuilder
				.append("<string>48758200-A29B-18C4-B616-455677840120</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeRND</string>");
		stringBuilder.append("<string>1.2.3.3.1</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>NumeroCotisant</string>");
		stringBuilder.append("<string>719900</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Siret</string>");
		stringBuilder.append("<string>07412723410007</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>DenominationCompte</string>");
		stringBuilder.append("<string>COUTURIER GINETTE</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeOrganisme</string>");
		stringBuilder.append("<string>UR030</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("</document>");
		stringBuilder.append("</retrieveDocMetaData>");
		stringBuilder.append("</retrieveServiceData>");
		return stringBuilder.toString();
	}
	/**
	 * Pas de constructeur
	 */
	private RetrievalXml(){
		
		assert false;
	}
}
