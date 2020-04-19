/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.utils;

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
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe permettant de gérer les droits pour les traitements de masse.
 * <br>
 * <br>
 * <b>Note :</b> A la différence de l'ajout des jobs dans la pile
 * qui passe par un contexte de sécurité initialisé par le WebService,
 * le lancement de job utilise cette classe.
 * 
 * @see {@link TraitementAsynchroneService#lancerJob(java.util.UUID) }
 */
public final class BatchAuthentificationUtils {

  private static final String SPRING_ROLE_PRFX = "ROLE_";

  private static final String ROLE_RECHERCHE = "ROLE_recherche";

  private static final String ROLE_RECHERCHE_ITERATEUR = "ROLE_recherche_iterateur";

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(BatchAuthentificationUtils.class);

  private BatchAuthentificationUtils() {
  }

  /**
   * @param job
   *          le job
   * @return le token d'authentification
   */
  public static AuthenticationToken getToken(final JobRequest job) {
    VIContenuExtrait viExtrait = job.getVi();
    AuthenticationToken token;

    final String trcPrefix = "getToken()";

    // chargement des droits autorisant tout afin d'associer les droits ses
    // droits en recherche à la capture de masse.
    // Dans le cas d'un rollback, si on ne possède pas les droits en
    // recherche, le programme s'arrête en erreur
    final List<String> pagms = new ArrayList<>();
    pagms.add("ACCES_FULL_PAGM");
    final SaeDroitServiceSkipImpl impl = new SaeDroitServiceSkipImpl();
    SaeDroits saeDroits = new SaeDroits();
    try {
      // ---------------------------------------------
      SaeDroitsEtFormat saeDroitsEtFormat = new SaeDroitsEtFormat();
      saeDroitsEtFormat = impl.loadSaeDroits("CS_ANCIEN_SYSTEME", pagms);

      saeDroits = saeDroitsEtFormat.getSaeDroits();

      // saeDroits = impl.loadSaeDroits("CS_ANCIEN_SYSTEME", pagms);
    }
    catch (final ContratServiceNotFoundException e) {
      LOG.warn("impossible de créer un accès total");
    }

    final List<SaePrmd> prmdList = saeDroits.get("recherche");
    
    if (viExtrait == null) {
      LOG.debug("{} - le Vi est null, on met toutes les autorisations",
                trcPrefix);

      viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("aucun contrat de service");
      viExtrait.setIdUtilisateur("aucun contrat de service");
      viExtrait.setSaeDroits(saeDroits);

    }

    final List<String> lRoles = new ArrayList<>();
    for (final String role : viExtrait.getSaeDroits().keySet()) {
      lRoles.add(SPRING_ROLE_PRFX + role);
    }

    if (!lRoles.contains(ROLE_RECHERCHE)) {
      lRoles.add(ROLE_RECHERCHE);
    }
    // Attention, en suppression de masse et restore de masse
    // nous avons besoin de faire de la recherche par iterateur
    // on ajoute les droits si le vi ne l'a pas
    if (!lRoles.contains(ROLE_RECHERCHE_ITERATEUR)) {
      lRoles.add(ROLE_RECHERCHE_ITERATEUR);
    }

    final String[] roles = new String[lRoles.size()];
    lRoles.toArray(roles);
    viExtrait.getSaeDroits().put("recherche", prmdList);
    viExtrait.getSaeDroits().put("recherche_iterateur", prmdList);

    token = AuthenticationFactory.createAuthentication(viExtrait
                                                                .getIdUtilisateur(),
                                                       viExtrait,
                                                       roles);

    return token;
  }
}
