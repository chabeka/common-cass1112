/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.controller.components.EcdeTestDisplayed;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;

/**
 * 
 * 
 */
@Component
public class ThreadInitCasDeTest {

   public static final String RUNNING = "running";
   public static final String FINISHED = "finished";
   public static final String ERROR = "error";
   public static final String COMPLETED = "completed";
   public static final String NOT_CONCERNED = "notConcerned";
   public static final String WAITING = "waiting";

   public static enum EtatThread {
      RUNNING, FINISHED
   };

   private EtatThread etat;

   private HttpSession session;

   private String urlWs;

   @Autowired
   private EcdeService ecdeService;
   
   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   /**
    * Cette méthode parcours la liste des cas de tests à lancer, appel le WS de
    * masse et scrute le résultat
    * 
    * @throws InterruptedException
    */
   @Async
   public void run() {

      if (etat == null || !etat.equals(EtatThread.RUNNING)) {
         runTasks();
      }

   }

   /**
    * Traitement - boucle sur les cas de test et traitement associé
    */
   private void runTasks() {

      etat = EtatThread.RUNNING;

      List<EcdeTestDisplayed> listDisplayeds = (List<EcdeTestDisplayed>) session
            .getServletContext().getAttribute("ecdeListe");

      for (EcdeTestDisplayed ecdeTestDisplayed : listDisplayeds) {

         if (ecdeTestDisplayed.getChecked()) {
            runCurrentTask(ecdeTestDisplayed);
         }
      }

      etat = EtatThread.FINISHED;
   }

   /**
    * Insertion du cas de test courant
    * 
    * @param ecdeTestDisplayed
    *           cas de test en cours de traitement
    * @param testLibre
    *           : listener WS
    */
   private void runCurrentTask(EcdeTestDisplayed ecdeTestDisplayed) {

      ecdeTestDisplayed.setStatusTraitement(RUNNING);

      deletePreviousResult(ecdeTestDisplayed.getUrl());

      try {
         appelWsOpArchiMasse(urlWs, ecdeTestDisplayed.getUrl());

         runPostCallWS(ecdeTestDisplayed);

         ecdeTestDisplayed.setStatusTraitement(COMPLETED);

      } catch (IntegrationException e) {
         ecdeTestDisplayed.setErreur(e.toString());
         ecdeTestDisplayed.setStatusTraitement(ERROR);
      }
   }

   /**
    * Supprime les anciens fichiers de traitement :
    * <ul>
    * <li>
    * debut_traitement.flag</li>
    * <li>
    * fin_traitement.flag</li>
    * <li>
    * resultats.xml</li>
    * </ul>
    * 
    * @param url
    *           emplacement url du fichier sommaire
    */
   private void deletePreviousResult(String url) {
      File file = new File(ecdeService.convertUrlEcdeToPath(url));
      String path = file.getParentFile().getAbsolutePath();

      remove(path + File.separator + "resultats.xml");
      remove(path + File.separator + "debut_traitement.flag");
      remove(path + File.separator + "fin_traitement.flag");
   }

   /**
    * @param file
    */
   private void remove(String path) {
      File file = new File(path);
      if (file.exists()) {
         file.delete();
      }

   }

   /**
    * Appel au WS
    * 
    * @param urlServiceWeb
    *           url du webservice à appeler
    * @param wsListener
    *           : ws listener
    * @param urlSommaire
    *           : url du fichier
    * @throws IntegrationException
    */
   private void appelWsOpArchiMasse(String urlServiceWeb, String urlSommaire)
         throws IntegrationException {

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, ViStyle.VI_OK);

      // Appel du service web et gestion de erreurs
      try {

         // Construction du paramètre d'entrée de l'opération
         ArchivageMasse paramsService = SaeServiceObjectFactory
               .buildArchivageMasseRequest(urlSommaire);

         // Appel du service web
         service.archivageMasse(paramsService);

      } catch (AxisFault fault) {
         throw new IntegrationException(fault);

      } catch (RemoteException e) {
         throw new IntegrationException(e);

      }

   }

   /**
    * Traitement post appel WebService
    * 
    * @param ecdeTestDisplayed
    * @throws IntegrationException
    */
   private void runPostCallWS(EcdeTestDisplayed ecdeTestDisplayed)
         throws IntegrationException {
      String path;
      File file;

      path = ecdeService.convertUrlEcdeToPath(ecdeTestDisplayed.getUrl());

      file = new File(path);
      path = file.getParentFile().getAbsolutePath() + File.separator
            + "fin_traitement.flag";

      file = new File(path);

      try {
         while (!file.exists()) {
            Thread.sleep(5000);
         }
      } catch (InterruptedException e) {
         throw new IntegrationException(e);
      }

      path = file.getParentFile().getAbsolutePath() + File.separator
            + "resultats.xml";

      ResultatsType resultatsType = ecdeService.chargeResultatsXml(path);
      ErreurType erreur = resultatsType.getErreurBloquanteTraitement();

      if (erreur != null && erreur.getCode() != null) {
         throw new IntegrationException(erreur.getLibelle());
      } else if (resultatsType.getNonIntegratedDocumentsCount() != null
            && resultatsType.getNonIntegratedDocumentsCount().intValue() > 0) {
         throw new IntegrationException(resultatsType
               .getNonIntegratedDocumentsCount().intValue()
               + " documents non intégrés");
      }

   }

   /**
    * @return the session
    */
   public HttpSession getSession() {
      return session;
   }

   /**
    * @param session
    *           the session to set
    */
   public void setSession(HttpSession session) {
      this.session = session;
   }

   /**
    * @param urlWs
    *           the urlWs to set
    */
   public void setUrlWs(String urlWs) {
      this.urlWs = urlWs;
   }

   /**
    * @param ecdeService
    *           the ecdeService to set
    */
   public void setEcdeService(EcdeService ecdeService) {
      this.ecdeService = ecdeService;
   }

   /**
    * @return the etat
    */
   public EtatThread getEtat() {
      return etat;
   }

}
