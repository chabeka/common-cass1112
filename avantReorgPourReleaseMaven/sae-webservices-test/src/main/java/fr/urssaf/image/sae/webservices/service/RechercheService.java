package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.webservices.service.factory.RequestServiceFactory;

/**
 * Service client pour la recherche dans le SAE
 * 
 * 
 */
@Service
public class RechercheService {

   private final SaeServiceStub service;

   /**
    * 
    * @param service
    *           stub du client des web services du SAE
    */
   @Autowired
   public RechercheService(SaeServiceStub service) {
      Assert.notNull(service, "SaeServiceStub is required");
      this.service = service;
   }

   /**
    * appel du service de recherche du SAE
    * 
    * @param lucene
    *           requête LUCENE
    * @param codes
    *           listes des codes des métadonnées
    * @return réponse de la recherche
    * @throws RemoteException
    *            levée par le service web
    */
   public final RechercheResponseType recherche(String lucene, String... codes)
         throws RemoteException {

      Recherche request = RequestServiceFactory.createRecherche(lucene, codes);

      return service.recherche(request).getRechercheResponse();
   }
}
