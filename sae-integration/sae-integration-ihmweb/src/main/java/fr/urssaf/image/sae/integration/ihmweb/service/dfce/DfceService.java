package fr.urssaf.image.sae.integration.ihmweb.service.dfce;

import java.util.Iterator;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;

/**
 * Services qui attaquent DFCE
 */
@Service
public class DfceService {
   
   @Autowired
   private TestConfig testConfig;
   
   private ServiceProvider serviceProvider;
   
   /**
    * Etablie une connexion dfce et
    * @return
    */
   public ServiceProvider getConnectedServiceProvider(){
      if(serviceProvider == null){
         serviceProvider = ServiceProvider.newServiceProvider();
   
         //-- Etablit la connexion à DFCE
         serviceProvider.connect(testConfig.getDfceLogin(), testConfig
               .getDfcePassword(), testConfig.getDfceServeurUrl());
      }
      return serviceProvider;
   }

   /**
    * Compte le nombre de documents à partir d'un id de traitement de masse
    * interne
    * 
    * @param idTdm
    *           l'identifiant de traitement de masse interne
    * @return le nombre de documents
    * @throws IntegrationException
    *            si un problème se produit pendant le comptage
    */
   public long compteNbDocsTdm(UUID idTdm) throws IntegrationException {

      // On capte toutes les exceptions dans un vilain gros bloc try/catch
      try {

         // Le résultat de la méthode à renvoyer
         long result = 0;

         //-- Création du ServiceProvider et ouverture de la connexion dfce
         ServiceProvider serviceProvider = getConnectedServiceProvider();
         try {

            // Récupération des services de recherche
            SearchService searchService = serviceProvider.getSearchService();

            // Récupération de l'objet Base, nécessaire en paramètre d'entrée
            // du service de recherche que l'on va utiliser
            Base base = serviceProvider.getBaseAdministrationService().getBase(
                  testConfig.getDfceBase());

            // Construction de la requête LUCENE
            // On fait une recherche sur l'identifiant de traitement de masse
            // interne
            // Code court de la métadonnée : iti
            String requeteLucene = String.format("iti:%s", idTdm.toString());

            // Lancement de la recherche
            SearchQuery searchQuery = ToolkitFactory.getInstance()
               .createMonobaseQuery(requeteLucene, base);

            Iterator<Document> iteratorDoc = searchService
               .createDocumentIterator(searchQuery);

            // Compte le nombre de résultats obtenus
            while (iteratorDoc.hasNext()) {
               iteratorDoc.next();
               result++;
            }

            // Renvoie du résultat
            return result;

         } finally {

            // Ferme la connexion à DFCE
            serviceProvider.disconnect();

         }

      } catch (Throwable e) {
         throw new IntegrationException(e);
      }

   }

}
