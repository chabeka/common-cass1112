package fr.urssaf.image.sae.rnd.dao.support;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

/**
 * Classe de gesion des CF Rnd et CorrespondancesRnd et des paramètres
 * 
 * 
 */
@Component
public class SaeBddSupport {

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CorrespondancesRndSupport correspondancesRndSupport;

   @Autowired
   private JobClockSupport clockSupport;

   /**
    * Récupère les informations due la version actuelle du RND dans le SAE
    * 
    * @return Un objet {@link VersionRnd} contenant les informations sur la
    *         version RND
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final VersionRnd getVersionRnd() throws SaeBddRuntimeException {

      try {
         String nomVersion;

         nomVersion = parametersService.getVersionRndNumero();
         Date dateMajVersion = parametersService.getVersionRndDateMaj();
         VersionRnd versionRnd = new VersionRnd();
         versionRnd.setDateMiseAJour(dateMajVersion);
         versionRnd.setVersionEnCours(nomVersion);
         return versionRnd;

      } catch (ParameterNotFoundException e) {
         throw new SaeBddRuntimeException(e);
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }

   }

   /**
    * Met à jour les informations sur la version actuelle du RND dans le SAE
    * 
    * @param versionRnd
    *           Version à mettre à jour
    * @throws SaeBddRuntimeException 
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final void updateVersionRnd(VersionRnd versionRnd) throws SaeBddRuntimeException {
      try {
         parametersService.setVersionRndDateMaj(versionRnd.getDateMiseAJour());
         parametersService.setVersionRndNumero(versionRnd.getVersionEnCours());
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }
   }

   /**
    * Met à jour la CF Rnd dans la bdd Cassandra
    * 
    * @param listeTypeDocs
    *           Liste des types de document à mettre à jour
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final void updateRnd(List<TypeDocument> listeTypeDocs)
         throws SaeBddRuntimeException {
      try {
         for (TypeDocument typeDocument : listeTypeDocs) {
            TypeDocument typeDocumentRecup = rndSupport.getRnd(typeDocument
                  .getCode());
            if (!typeDocument.equals(typeDocumentRecup)) {
               rndSupport.ajouterRnd(typeDocument, clockSupport.currentCLock());
            }
         }
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }
   }

   /**
    * Met à jour la CF CorrespondancesRnd dans la base de données Cassandra et
    * passe les codes temporaires ayant une correspondance à l'état clôturé
    * 
    * @param listeCorrespondances
    *           Correspondances entre codes temporaires et code
    * @param version la version en cours dans le SAE           
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final void updateCorrespondances(
         Map<String, String> listeCorrespondances, String version)
         throws SaeBddRuntimeException {

      try {

         Set<String> listeCodesTemporaires = listeCorrespondances.keySet();
         Iterator<String> iterateur = listeCodesTemporaires.iterator();
         while (iterateur.hasNext()) {
            Object codeTemporaire = iterateur.next();
            String codeDefinitif = listeCorrespondances.get(codeTemporaire);

            // Ajout de la ligne dans la table des correspondances
            Correspondance correspondance = new Correspondance();
            correspondance.setCodeDefinitif(codeDefinitif);
            correspondance.setCodeTemporaire((String) codeTemporaire);
            correspondance.setEtat(EtatCorrespondance.CREATED);
            correspondance.setVersionCourante(version);
            correspondancesRndSupport.ajouterCorrespondance(correspondance,
                  clockSupport.currentCLock());

            // On passe le code type temporaire à l'état cloturé
            TypeDocument typeDoc = rndSupport.getRnd((String) codeTemporaire);
            if (typeDoc != null) {
               typeDoc.setCloture(true);
               rndSupport.ajouterRnd(typeDoc, clockSupport.currentCLock());
            }

         }
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }

   }

   /**
    * Récupère la liste de toutes les correspondances en cours dans le SAE
    * 
    * @return une liste de {@link Correspondance}
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final List<Correspondance> getAllCorrespondances()
         throws SaeBddRuntimeException {
      try {
         return correspondancesRndSupport.getAllCorrespondances();
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }
   }

   /**
    * Met la correspondance à l'état démarré et positionne la date de début
    * 
    * @param correspondance
    *           La correspondance dont la mise à jour des docs a commencé
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   public final void startMajCorrespondance(Correspondance correspondance)
         throws SaeBddRuntimeException {
      try {
         correspondance.setEtat(EtatCorrespondance.STARTING);
         correspondance.setDateDebutMaj(new Date());
         correspondancesRndSupport.ajouterCorrespondance(correspondance,
               clockSupport.currentCLock());
      } catch (Exception e) {
         throw new SaeBddRuntimeException(e);
      }
   }
}
