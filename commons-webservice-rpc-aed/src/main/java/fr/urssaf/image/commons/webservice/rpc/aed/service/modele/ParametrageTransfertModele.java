package fr.urssaf.image.commons.webservice.rpc.aed.service.modele;

import javax.xml.rpc.holders.StringHolder;

import fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder;

public class ParametrageTransfertModele {

	private StringHolder typeTransfert;
	private StringHolder urlFTP;
	private StringHolder login;
	private StringHolder password;
	private StringHolder chemin;
	private ErreurHolder erreur;
	private StringHolder idTransfert;

	public ParametrageTransfertModele(StringHolder typeTransfert,
			StringHolder urlFTP, StringHolder login, StringHolder password,
			StringHolder chemin, ErreurHolder erreur, StringHolder idTransfert) {
		this.typeTransfert = typeTransfert;
		this.urlFTP = urlFTP;
		this.login = login;
		this.password = password;
		this.chemin = chemin;
		this.erreur = erreur;
		this.idTransfert = idTransfert;
	}

	public StringHolder getTypeTransfert() {
		return typeTransfert;
	}

	public StringHolder getUrlFTP() {
		return urlFTP;
	}

	public StringHolder getLogin() {
		return login;
	}

	public StringHolder getPassword() {
		return password;
	}

	public StringHolder getChemin() {
		return chemin;
	}

	public ErreurHolder getErreur() {
		return erreur;
	}

	public StringHolder getIdTransfert() {
		return idTransfert;
	}

}
