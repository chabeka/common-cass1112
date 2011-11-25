package fr.urssaf.image.sae.dfce.admin.model;

/**
 * 
 * Cette classe contient les paramètres de connexion à la base. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li> user : Les paramètres de l'utilisateur</li>
 * <li> host :Les paramètres de la machine</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class ConnectionParameter {
	private UserParameter user;
	private HostParameter host;

	/**
	 * @return Les paramètres de l'utilisateur
	 */
	public final UserParameter getUser() {
		return user;
	}

	/**
	 * @param user
	 *            : Les paramètres de l'utilisateur
	 */
	public final void setUser(final UserParameter user) {
		this.user = user;
	}

	/**
	 * @return La machine
	 */
	public final HostParameter getHost() {
		return host;
	}

	/**
	 * @param host
	 *            : La machine
	 */
	public final void setHost(final HostParameter host) {
		this.host = host;
	}

}
