package fr.urssaf.image.sae.lotinstallmaj.service;

import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotAlreadyInstallUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotInexistantUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotManualUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRestartTomcatException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotUnknownDFCEVersion;
import fr.urssaf.image.sae.lotinstallmaj.modele.InfoLot;

/**
 * Pour chaque lot, il y aura une classe principale<br>
 * à lancer pour déclencher des opérations de maj, classe qui devra<br>
 * implémenter cette interface. 
 *
 */
public interface MajLotService {

  /**
   * Réalise toutes les opérations de création de la base de données pour les
   * serveurs d'integration (Tests d'integration) sur la base SAE.
   * 
   * @throws MajLotManualUpdateException
   * @throws MajLotGeneralException
   * @throws MajLotRestartTomcatException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   */
  void demarreCreateSAE() throws MajLotManualUpdateException, MajLotRestartTomcatException, MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion;

  /**
   * Réalise la mise à jour de la base du SAE jusqu'à la dernière version disponible
   * 
   * @throws MajLotGeneralException
   * @throws MajLotManualUpdateException
   * @throws MajLotInexistantUpdateException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   * @throws MajLotRestartTomcatException
   */
  default InfoLot update() throws MajLotGeneralException,
  MajLotManualUpdateException, MajLotInexistantUpdateException,
  MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion, MajLotRestartTomcatException {
    return updateToVersion(LotVersion.getLastAvailableVersion());
  }

  /**
   * Mettre à jour la base SAE jusqu'à la version spécifiée
   * 
   * @param version
   * @throws MajLotGeneralException
   * @throws MajLotManualUpdateException
   * @throws MajLotInexistantUpdateException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   * @throws MajLotRestartTomcatException
   */
  InfoLot updateToVersion(int version)
      throws MajLotGeneralException, MajLotManualUpdateException, MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
      MajLotUnknownDFCEVersion, MajLotRestartTomcatException;

  /**
   * Retourne les infos sur le dernier lot disponible et sur le dernier lot installé
   * 
   * @return
   */
  void info();

  /**
   * Affiche les informations (Nom du Lot, Descriptif des tâches effectuées) sur la version spécifiée en paramètre
   * 
   * @param version
   * @throws MajLotInexistantUpdateException
   */
  InfoLot getVersionInfo(int version) throws MajLotInexistantUpdateException;

  /**
   * force la version de la base du SAE
   * 
   * @param version
   */
  void forceVersion(int version);

  /**
   * @param version
   * @param isRedo
   *           true pour installer uniquement la version spécifié
   *           false pour rejouer toutes les versions précédentes jusqu'à la version spécifiée
   * @return
   * @throws MajLotGeneralException
   * @throws MajLotManualUpdateException
   * @throws MajLotInexistantUpdateException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   * @throws MajLotRestartTomcatException
   */
  default InfoLot updateToVersion(final int version, final boolean isRedo)
      throws MajLotGeneralException, MajLotManualUpdateException, MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
      MajLotUnknownDFCEVersion, MajLotRestartTomcatException {

    final InfoLot infoLot = beforeUpdate(version);

    // la version installé en base
    final long versionInstalled = getDatabaseVersion();
    if (isRedo || versionInstalled <= version) {
      selectUpdate(version, isRedo);
      afterUpdate(version);
    } else {
      info(version, versionInstalled);
    }

    return infoLot;
  }

  /**
   * Rejouer
   * 
   * @param version
   * @throws MajLotManualUpdateException
   * @throws MajLotGeneralException
   * @throws MajLotInexistantUpdateException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   * @throws MajLotRestartTomcatException
   */
  void redo(int version) throws MajLotGeneralException, MajLotManualUpdateException, MajLotInexistantUpdateException, MajLotAlreadyInstallUpdateException,
  MajLotUnknownDFCEVersion, MajLotRestartTomcatException;

  /**
   * Recupère les informations sur une mise à jour,
   * puis verifie si cette dernière doit être faite manuellement hors du lotinstall
   * 
   * @param version
   * @return
   * @throws MajLotManualUpdateException
   * @throws MajLotInexistantUpdateException
   */
  InfoLot beforeUpdate(int version) throws MajLotManualUpdateException, MajLotInexistantUpdateException;

  /**
   * Selectionne et lance la mise à jour correspondante au numero de version en paramètre
   * 
   * @param version
   * @param isRedo
   * @throws MajLotGeneralException
   * @throws MajLotAlreadyInstallUpdateException
   * @throws MajLotUnknownDFCEVersion
   * @throws MajLotRestartTomcatException
   * @throws MajLotInexistantUpdateException
   */
  void selectUpdate(int version, boolean isRedo)
      throws MajLotGeneralException, MajLotAlreadyInstallUpdateException, MajLotUnknownDFCEVersion, MajLotRestartTomcatException,
      MajLotInexistantUpdateException;

  /**
   * Permet de mettre à jour les méta et indexes DFCE une fois une mise à jour installée
   * Ces derniers ne sont mis à jour que si la version installée correspond à la dernière version disponible
   * 
   * @param version
   */
  void afterUpdate(int version);

  /**
   * Operation qui s'execute avant lancement de la création d'une plateforme vierge
   */
  void beforeCreate();

  /**
   * Retourne la version de la base de données stockée en base
   * @return
   */
  long getDatabaseVersion();

  /**
   * Affiche les information de la version
   * 
   * @param version
   */
  void info(final int version, long versionInstalled);
}
