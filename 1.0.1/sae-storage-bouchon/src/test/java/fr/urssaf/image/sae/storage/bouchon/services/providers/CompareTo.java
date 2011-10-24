package fr.urssaf.image.sae.storage.bouchon.services.providers;

/**
 * Contient les informations de contrôle
 * 
 * @author akenore
 * 
 */
public final class CompareTo {

	public static final  String[] THESE_UUIDS = {
			"110E8400-E29B-11D4-A716-446655440000",
			"510E8200-E29B-18C4-A716-446677440120",
			"48758200-A29B-18C4-B616-455677840120" };
	public static final  String THIS_UUID = THESE_UUIDS[0];

	public static final  String[] METADATA_CODE = {  "CodeRND",
			"NumeroCotisant", "Siret", "DenominationCompte", "CodeOrganisme",
			"DateOrigine" };
	public static final  String[] THIS_DATA_1 = {
			 "3.1.3.1.1", "704815",
			"49980055500017", "SPOHN ERWAN MARIE MAX", "UR030", "2011-06-03" };

	public static final  String[] THIS_DATA_2 = {
			 "1.A.X.X.X", "723804",
			"07413151710009", "CHEVENIER ANDRE", "UR030", "2011-06-03" };
	public static final  String[] THIS_DATA_3 = {
			 "1.2.3.3.1", "719900",
			"07412723410007", "COUTURIER GINETTE", "UR030", "2011-06-03" };

	// Nombre de storagedocument inséré en masse
	@SuppressWarnings("PMD.LongVariable")
	public static final  int THIS_LIST_OF_STORAGEDOCUMENT_SIZE = 3;
	// Nombre de métadonnée récupérées
	@SuppressWarnings("PMD.LongVariable")
	public static final  int THIS_LIST_OF_METADATA_RETRIEVE_SIZE = 5;

	/**
	 * Cette classe n'est pas fait pour être instanciée
	 */
	private CompareTo() {
		assert false;
	}
}
