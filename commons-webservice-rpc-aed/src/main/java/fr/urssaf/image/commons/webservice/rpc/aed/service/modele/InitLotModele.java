package fr.urssaf.image.commons.webservice.rpc.aed.service.modele;

import javax.xml.rpc.holders.BooleanHolder;

import fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder;
import fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder;

public class InitLotModele {

	private BooleanHolder isLotCree;
	private ListeDocumentsHolder listeDocuments;
	private ErreurHolder erreur;

	public InitLotModele(BooleanHolder isLotCree,
			ListeDocumentsHolder listeDocuments, ErreurHolder erreur) {
		this.isLotCree = isLotCree;
		this.listeDocuments = listeDocuments;
		this.erreur = erreur;
	}

	public BooleanHolder getIsLotCree() {
		return isLotCree;
	}

	public ListeDocumentsHolder getListeDocuments() {
		return listeDocuments;
	}

	public ErreurHolder getErreur() {
		return erreur;
	}

}
