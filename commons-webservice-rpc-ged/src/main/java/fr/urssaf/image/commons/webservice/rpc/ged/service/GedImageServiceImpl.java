package fr.urssaf.image.commons.webservice.rpc.ged.service;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.CategorieTypeZoneWS;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.FichierWS;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImagePortType;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageServiceLocator;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.IndexValeur;
import fr.urssaf.image.commons.webservice.rpc.ged.modele.TypeZoneWS;

public class GedImageServiceImpl implements GedImageService {

	protected static final Logger LOG = Logger
			.getLogger(GedImageServiceImpl.class);

	private GedImagePortType port;

	public GedImageServiceImpl() {
		fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageService service = new GedImageServiceLocator();
		try {
			port = service.getGedImagePort();
		} catch (ServiceException e) {
		   LOG.error(e);
		}
	}
	
	public String ping() throws RemoteException {
		return port.ping();
	}

	public boolean deleteDocumentGED(AuthInfo login, String base,
			String idDocument) throws RemoteException {
		return port.deleteDocumentGED(login, base, idDocument);
	}

	public CategorieTypeZoneWS[] getCategorieTypesZonesBase(AuthInfo login,
			String base) throws RemoteException {
		return port.getCategorieTypesZonesBase(login, base);
	}

	public String[] getDictionnaire(AuthInfo login, String base,
			String typezone, String masque) throws RemoteException {
		return port.getDictionnaire(login, base, typezone, masque);
	}

	public TypeZoneWS[] getTypesZonesBase(AuthInfo login, String base)
			throws RemoteException {
		return port.getTypesZonesBase(login, base);
	}

	public String insertDocumentGED(AuthInfo login, String base,
			IndexValeur[] listeIndexValeur, FichierWS file)
			throws RemoteException {
		return port.insertDocumentGED(login, base, listeIndexValeur, file);
	}

	public String moveDocumentGED(AuthInfo login, String baseOrig,
			String idDocument, String baseDest, IndexValeur[] listeIndexValeur)
			throws RemoteException {
		return port.moveDocumentGED(login, baseOrig, idDocument, baseDest,
				listeIndexValeur);
	}

	public boolean updateIndexDocumentGED(AuthInfo login, String base,
			String idDocument, IndexValeur[] listeIndexValeur)
			throws RemoteException {
		return port.updateIndexDocumentGED(login, base, idDocument,
				listeIndexValeur);
	}
}
