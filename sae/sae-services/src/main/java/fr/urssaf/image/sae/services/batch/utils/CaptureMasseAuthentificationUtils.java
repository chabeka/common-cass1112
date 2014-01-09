/**
 * 
 */
package fr.urssaf.image.sae.services.batch.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.impl.skip.SaeDroitServiceSkipImpl;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe permettant de gérer les droits pour la capture de masse
 * 
 */
public final class CaptureMasseAuthentificationUtils {

   private static final String ROLE_RECHERCHE = "recherche";

   private static final Logger LOG = LoggerFactory
         .getLogger(CaptureMasseAuthentificationUtils.class);

   private CaptureMasseAuthentificationUtils() {
   }

   /**
    * 
    * @param job
    *           le job
    * @return le token d'authentification
    */
   public static AuthenticationToken getToken(JobRequest job) {
      VIContenuExtrait viExtrait = job.getVi();
      AuthenticationToken token;

      String trcPrefix = "getToken()";

      // chargement des droits autorisant tout afin d'associer les droits ses
      // droits en recherche à la capture de masse.
      // Dans le cas d'un rollback, si on ne possède pas les droits en
      // recherche, le programme s'arrête en erreur
      List<String> pagms = new ArrayList<String>();
      pagms.add("ACCES_FULL_PAGM");
      SaeDroitServiceSkipImpl impl = new SaeDroitServiceSkipImpl();
      SaeDroits saeDroits = new SaeDroits();
      try {
         //---------------------------------------------
         SaeDroitsEtFormat saeDroitsEtFormat = new SaeDroitsEtFormat();
         saeDroitsEtFormat = impl.loadSaeDroits("CS_ANCIEN_SYSTEME", pagms);
         
         saeDroits = saeDroitsEtFormat.getSaeDroits();
         
         //saeDroits = impl.loadSaeDroits("CS_ANCIEN_SYSTEME", pagms);
      } catch (ContratServiceNotFoundException e) {
         LOG.warn("impossible de créer un accès total");
      } 

      List<SaePrmd> prmdList = saeDroits.get(ROLE_RECHERCHE);

      if (viExtrait == null) {
         LOG.debug("{} - le Vi est null, on met toutes les autorisations",
               trcPrefix);

         viExtrait = new VIContenuExtrait();
         viExtrait.setCodeAppli("aucun contrat de service");
         viExtrait.setIdUtilisateur("aucun contrat de service");
         viExtrait.setSaeDroits(saeDroits);

      }

      List<String> lRoles = new ArrayList<String>();
      lRoles.addAll(viExtrait.getSaeDroits().keySet());
      if (!lRoles.contains(ROLE_RECHERCHE)) {
         lRoles.add(ROLE_RECHERCHE);
      }

      String[] roles = new String[lRoles.size()];
      lRoles.toArray(roles);
      viExtrait.getSaeDroits().put(ROLE_RECHERCHE, prmdList);

      token = AuthenticationFactory.createAuthentication(viExtrait
            .getIdUtilisateur(), viExtrait, roles);

      return token;
   }
}
