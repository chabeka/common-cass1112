package fr.urssaf.image.sae.rnd.dao.support.cql;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

/**
 * Classe de gestion des CF Rnd et CorrespondancesRnd et des paramètres
 *
 *
 */
@Component
public class SaeBddCqlSupport {

  @Autowired
  private ParametersService parametersService;

  @Autowired
  private RndCqlSupport rndSupport;

  @Autowired
  private CorrespondancesRndCqlSupport correspondancesRndSupport;


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
      final Date dateMajVersion = parametersService.getVersionRndDateMaj();
      final VersionRnd versionRnd = new VersionRnd();
      versionRnd.setDateMiseAJour(dateMajVersion);
      versionRnd.setVersionEnCours(nomVersion);
      return versionRnd;

    } catch (final ParameterNotFoundException e) {
      throw new SaeBddRuntimeException(e);
    } catch (final Exception e) {
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
  public final void updateVersionRnd(final VersionRnd versionRnd)
      throws SaeBddRuntimeException {
    try {
      parametersService.setVersionRndDateMaj(versionRnd.getDateMiseAJour());
      parametersService.setVersionRndNumero(versionRnd.getVersionEnCours());
    } catch (final Exception e) {
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
  public final void updateRnd(final List<TypeDocument> listeTypeDocs)
      throws SaeBddRuntimeException {
    try {
      for (final TypeDocument typeDocument : listeTypeDocs) {
        final TypeDocument typeDocumentRecup = rndSupport.getRnd(typeDocument
                                                                 .getCode());
        if (!typeDocument.equals(typeDocumentRecup)) {
          rndSupport.ajouterRnd(typeDocument);
        }
      }
    } catch (final Exception e) {
      throw new SaeBddRuntimeException(e);
    }
  }

  /**
   * Met à jour la CF CorrespondancesRnd dans la base de données Cassandra et
   * passe les codes temporaires ayant une correspondance à l'état clôturé
   *
   * @param listeCorrespondances
   *           Correspondances entre codes temporaires et code
   * @param version
   *           la version en cours dans le SAE
   * @throws SaeBddRuntimeException
   *            Exception levée lors de la mise à jour de la BDD
   */
  public final void updateCorrespondances(
                                          final Map<String, String> listeCorrespondances, final String version)
                                              throws SaeBddRuntimeException {

    try {

      final Iterator<Entry<String, String>> iterator = listeCorrespondances
          .entrySet().iterator();

      while (iterator.hasNext()) {
        final Object codeTemporaire = iterator.next().getKey();
        final String codeDefinitif = listeCorrespondances.get(codeTemporaire);

        // Ajout de la ligne dans la table des correspondances
        final Correspondance correspondance = new Correspondance();
        correspondance.setCodeDefinitif(codeDefinitif);
        correspondance.setCodeTemporaire((String) codeTemporaire);
        correspondance.setEtat(EtatCorrespondance.CREATED);
        correspondance.setVersionCourante(version);
        correspondancesRndSupport.ajouterCorrespondance(correspondance);

        // On passe le code type temporaire à l'état cloturé
        final TypeDocument typeDoc = rndSupport.getRnd((String) codeTemporaire);
        if (typeDoc != null) {
          typeDoc.setCloture(true);
          rndSupport.ajouterRnd(typeDoc);
        }

      }
    } catch (final Exception e) {
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
      return correspondancesRndSupport.findAll();
    } catch (final Exception e) {
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
  public final void startMajCorrespondance(final Correspondance correspondance)
      throws SaeBddRuntimeException {
    try {
      correspondance.setEtat(EtatCorrespondance.STARTING);
      correspondance.setDateDebutMaj(new Date());
      correspondancesRndSupport.ajouterCorrespondance(correspondance);
    } catch (final Exception e) {
      throw new SaeBddRuntimeException(e);
    }
  }
}
