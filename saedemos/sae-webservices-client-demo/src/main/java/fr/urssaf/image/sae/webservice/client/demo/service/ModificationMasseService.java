/**
 * 
 */
package fr.urssaf.image.sae.webservice.client.demo.service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasseResponse;
import fr.urssaf.image.sae.webservice.client.demo.component.PropertiesLoader;

/**
 * @author CER6990937
 *
 */
public class ModificationMasseService {

   private static final Logger LOG = LoggerFactory.getLogger(PingService.class);

   private static final String SOMMAIRE_PROPERTIES_SEPARATOR = ";";

   /**
    * Sae service
    */
   private SaeServiceStub saeService;

   static List<String> listeUrlEcdeSommaire;

   static List<String> listeTypeHash;

   static List<String> listeHash;

   static List<String> listeCodeTraitement;

   static String nbAppelCodeTraitement;

   /**
    * 
    */
   public ModificationMasseService() {
   }

   /**
    * @param args
    * @param listeUUIDJobLaunch
    */
   public static void main(Object[] args,
         ConcurrentLinkedQueue<String> listeUUIDJobLaunch) {
      LOG.debug("======= Début lancement WS modification_masse =======");
      int nbRelaunchService = PropertiesLoader.getInstance()
            .getNbRelaunchService();
      long timeDelayRelaunchService = PropertiesLoader.getInstance()
            .getTimeDelayRelaunchService();
      ModificationMasseService modifService = new ModificationMasseService();

      // URL ECDE du fichier sommaire.xml
      String urlSommaireProperties = PropertiesLoader.getInstance()
            .getUrlEcdeSommaire();
      if (urlSommaireProperties.contains(SOMMAIRE_PROPERTIES_SEPARATOR)) {
         listeUrlEcdeSommaire = Arrays.asList(urlSommaireProperties
               .split(SOMMAIRE_PROPERTIES_SEPARATOR));
      } else {
         listeUrlEcdeSommaire = Arrays.asList(urlSommaireProperties.trim());
      }
      // Hash SHA-1 du fichier sommaire.xml
      String typeHashProperties = PropertiesLoader.getInstance().getTypeHash();

      if (typeHashProperties.contains(SOMMAIRE_PROPERTIES_SEPARATOR)) {
         listeTypeHash = Arrays.asList(typeHashProperties
               .split(SOMMAIRE_PROPERTIES_SEPARATOR));
      } else {
         listeTypeHash = Arrays.asList(typeHashProperties.trim());
      }

      String hashProperties = PropertiesLoader.getInstance().getHash();
      if (hashProperties.contains(SOMMAIRE_PROPERTIES_SEPARATOR)) {
         listeHash = Arrays.asList(hashProperties
               .split(SOMMAIRE_PROPERTIES_SEPARATOR));
      } else {
         listeHash = Arrays.asList(hashProperties.trim());
      }

      if (listeUrlEcdeSommaire == null || listeTypeHash == null
            || listeHash == null || listeUrlEcdeSommaire.isEmpty()
            || listeTypeHash.isEmpty() || listeHash.isEmpty()) {
         LOG.debug("l'URL ECDE, le type de hash et le hash du fichier sommaire.xml sont obligatoires.");
         return;
      }

      String codeTraitement = PropertiesLoader.getInstance()
            .getCodeTraitement();
      if (codeTraitement.contains(SOMMAIRE_PROPERTIES_SEPARATOR)) {
         listeCodeTraitement = Arrays.asList(codeTraitement
               .split(SOMMAIRE_PROPERTIES_SEPARATOR));
      } else {
         listeCodeTraitement = Arrays.asList(codeTraitement.trim());
      }

      int nbAppelCodeTraitement = PropertiesLoader.getInstance()
            .getNbAppelCodeTraitement();

      int incr = 0;
      int incrNbAppelCdTraitement = 0;
      int indiceCodeTraitement = 0;
      int tailleListeCodeTrait = listeCodeTraitement.size();
      String codeTraitementActuelle;

      while (incr < nbRelaunchService) {
         try {
            String contexteLog = UUID.randomUUID().toString();
            MDC.put("log_contexte_uuid", contexteLog);
            int incrLst = 0;
            for (String urlECDE : listeUrlEcdeSommaire) {
               codeTraitementActuelle = listeCodeTraitement.get(
                     indiceCodeTraitement).trim();
               if (incrNbAppelCdTraitement >= nbAppelCodeTraitement) {
                  if (indiceCodeTraitement < (tailleListeCodeTrait - 1)) {
                     indiceCodeTraitement++;
                  } else {
                     indiceCodeTraitement = 0;
                  }
                  incrNbAppelCdTraitement = 0;
               }
               urlECDE = urlECDE.trim();
               if (!urlECDE.isEmpty()) {
                  modifService.modification(urlECDE,
                        listeTypeHash.get(incrLst), listeHash.get(incrLst),
                        codeTraitementActuelle, listeUUIDJobLaunch);
               }
               incrLst++;
               incrNbAppelCdTraitement++;
            }

            if (timeDelayRelaunchService > 0) {
               Thread.sleep(timeDelayRelaunchService);
            }
         } catch (RemoteException e) {
            LOG.debug("Error lors du lancement du service de modification de masse "
                  + e.getCause() + " - " + e.getMessage());
         } catch (InterruptedException e) {
            LOG.debug("Error dans la pause de " + timeDelayRelaunchService
                  + " ms");
         }

         if (nbRelaunchService == -1) {
            break;
         }

         incr++;
      }

      LOG.debug("======= Fin lancement WS modification_masse =======");

   }

   public void modification(String urlECDE, String typeHash, String hash,
         String codeTraitement, ConcurrentLinkedQueue<String> listeUUIDJobLaunch)
         throws RemoteException {
      if (StringUtils.isEmpty(urlECDE) || StringUtils.isEmpty(typeHash)
            || StringUtils.isEmpty(hash) || StringUtils.isEmpty(codeTraitement)) {
         LOG.debug("Error lors du traitement du service de modification de masse - Un des parametres du webservice est null ou vide : urlEcde = "
               + urlECDE
               + " - hash : "
               + hash
               + " - type hash : "
               + typeHash
               + " - code traitement : " + codeTraitement);
         return;
      }
      // Construction du Stub
      this.saeService = StubFactory.createStubAvecAuthentification();
      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ModificationMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeModificationMasse(urlECDE, typeHash, hash,
                  codeTraitement);
      
      // Appel de l'opération archivageMasseAvecHash
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      ModificationMasseResponse reponse =  saeService.modificationMasse(paramsEntree);
      String idTraitementSae = reponse.getModificationMasseResponse().getUuid();
      listeUUIDJobLaunch.add(idTraitementSae);
      // sysout
      LOG.debug("La demande de prise en compte de la modification de masse a été envoyée");
      LOG.debug("URL ECDE du sommaire.xml : " + urlECDE);
      LOG.debug("Hash " + typeHash + " du sommaire.xml : " + hash);
      LOG.debug("Code traitement : " + codeTraitement);
      LOG.debug("Identifiant unique du traitement de masse affecté par le SAE : "
            + idTraitementSae);
   }

   /**
    * @return the saeService
    */
   public SaeServiceStub getSaeService() {
      return saeService;
   }
}
