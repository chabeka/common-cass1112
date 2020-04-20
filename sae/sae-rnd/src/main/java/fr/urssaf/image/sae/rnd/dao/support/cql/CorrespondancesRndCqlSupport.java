package fr.urssaf.image.sae.rnd.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.cql.ICorrespondancesDaoCql;
import fr.urssaf.image.sae.rnd.modele.Correspondance;

/**
 * Support de manipulation de la table rndcql
 */
@Component
public class CorrespondancesRndCqlSupport {



  private static final String FIN_LOG = "{} - fin";
  private static final String DEBUT_LOG = "{} - début";
  private static final Logger LOGGER = LoggerFactory
      .getLogger(CorrespondancesRndCqlSupport.class);

  /**
   * Séparateur entre le code temporaire et la version pour la clé
   */
  private static final String SEPARATEUR = "@_@";

  @Autowired
  ICorrespondancesDaoCql rndDaoCql;

  /**
   * Création d'une Correspondance dans la CF correspondancesrndCql
   *
   * @param correspondance
   *          correspondanceRnd à ajouter
   */
  public final void ajouterCorrespondance(final Correspondance correspondance) {

    final String trcPrefix = "ajouterCorrespondance";
    LOGGER.debug(DEBUT_LOG, trcPrefix);
    LOGGER.debug("{} - Correspondance code tempo : {}",
                 new String[] {
                               trcPrefix, correspondance.getCodeTemporaire()});
    LOGGER.debug("{} - Correspondance code définitif : {}",
                 new String[] {
                               trcPrefix, correspondance.getCodeDefinitif()});
    LOGGER.debug("{} - Correspondance version : {}",
                 new String[] {
                               trcPrefix, correspondance.getVersionCourante()});


    // ATTENTION
    // final String idCorrespondance = correspondance.getCodeTemporaire() + SEPARATEUR + correspondance.getVersionCourante();

    rndDaoCql.saveWithMapper(correspondance);

    LOGGER.info("{} - Ajout de la correspondance : {} / {}",
                new String[] {
                              trcPrefix, correspondance.getCodeTemporaire(),
                              correspondance.getCodeDefinitif()});

    LOGGER.debug(FIN_LOG, trcPrefix);
  }

  /**
   * Récupère la correspondance Rnd par rapport au code temporaire et à la version
   *
   * @param codeTemporaire
   *          le code Correspondance dont on veut le type de document
   * @param version
   *          version du code rnd
   * @return le type de document recherché
   */
  public final Correspondance find(final String codeTemporaire, final String version) {

    final Correspondance correspondance = rndDaoCql.findWithMapperByIdComposite(codeTemporaire, version).orElse(null);
    return correspondance;
  }

  /**
   * Récupération de toutes les correspondances Rnd
   * 
   * @return Une liste d’objet {@link Correpondance} représentant les correspondances
   *         présents dans le référentiel
   */
  public final List<Correspondance> findAll() {

    final Iterator<Correspondance> it = rndDaoCql.findAllWithMapper();
    final List<Correspondance> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

}
