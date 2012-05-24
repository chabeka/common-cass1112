package recouv.cirti.anais.api.source;

public class AnaisLdapProvider {

	/** Propriété contenant le singleton AnaisLdap. */
	private static AnaisLdap anaisLdap;

	
	/**
	 * Récupère l'instance courante de l'objet AnaisLdap.
	 * Si celui ci n'existe pas un nouvel objet est créé.
	 * (Mode connecté)
	 * 
	 * @return
	 */
	public static AnaisLdap getUniqueInstance() {

		if (anaisLdap == null) {
			anaisLdap = new AnaisLdap();
		}

		return anaisLdap;

	}

	/**
	 * Crée et renvoie un objet de type AnaisLdap.
	 * (Mode déconnecté)
	 * @return
	 */
	public static AnaisLdap getSeveralInstance() {
		return new AnaisLdap();
	}

}
