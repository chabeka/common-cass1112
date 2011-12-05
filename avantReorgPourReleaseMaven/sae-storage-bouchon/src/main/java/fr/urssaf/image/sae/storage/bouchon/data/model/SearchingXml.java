package fr.urssaf.image.sae.storage.bouchon.data.model;
/**
 * Cette classe permet de de construie le flux xml pour les servies de recherche
 * @author akenore
 *
 */
@SuppressWarnings("PMD")
public final class SearchingXml {
/**
 * permettant de construie le flux xml pour les servies de recherche
 * @return Le flux xml pour les servies de recherche
 */
	public static String buildXmlfile() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		stringBuilder.append("<searchServiceData>");
		
		stringBuilder.append("<searchByLuceneData>");
		stringBuilder.append("<document>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>UUID</string>");
		stringBuilder
				.append("<string>110E8400-E29B-11D4-A716-446655440000</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeRND</string>");
		stringBuilder.append("<string>3.1.3.1.1</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>NumeroCotisant</string>");
		stringBuilder.append("<string>704815</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Siret</string>");
		stringBuilder.append("<string>49980055500017</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>DenominationCompte</string>");
		stringBuilder.append("<string>SPOHN ERWAN MARIE MAX</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeOrganisme</string>");
		stringBuilder.append("<string>UR030</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("</document>");
		stringBuilder.append("</searchByLuceneData>");
		stringBuilder.append("<searchByLuceneData>");
		stringBuilder.append("<document>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>UUID</string>");
		stringBuilder
				.append("<string>510E8200-E29B-18C4-A716-446677440120</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeRND</string>");
		stringBuilder.append("<string>1.A.X.X.X</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>NumeroCotisant</string>");
		stringBuilder.append("<string>723804</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>Siret</string>");
		stringBuilder.append("<string>07413151710009</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>DenominationCompte</string>");
		stringBuilder.append("<string>CHEVENIER ANDRE</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("<entry>");
		stringBuilder.append("<string>CodeOrganisme</string>");
		stringBuilder.append("<string>UR030</string>");
		stringBuilder.append("</entry>");
		stringBuilder.append("</document>");
		stringBuilder.append("</searchByLuceneData>");
		stringBuilder.append("<searchByLuceneData>");
		stringBuilder.append("<document>");
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
		stringBuilder.append("</searchByLuceneData>");
		
		stringBuilder.append("<searchByUUIDData>");
		stringBuilder.append("<document>");
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
		stringBuilder.append("</searchByUUIDData>");
		
		stringBuilder.append("</searchServiceData>");
		return stringBuilder.toString();
	}
	/**
	 * Pas de constructeur
	 */
	private SearchingXml(){
		assert false;
	}
}
