/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.facade.FormatControlProfilSupportFacade;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.service.FormatControlProfilService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Implémentation de l'interface décrivant les méthodes proposées par
 * FormatControlProfilService Facade (Thrift et Cql)
 */
@Service
public class FormatControlProfilServiceImpl implements
FormatControlProfilService {

  private final FormatControlProfilSupportFacade formatControlSupportFacade;


  /**
   * Gestion du cache
   */
  private final LoadingCache<String, FormatControlProfil> formatsControlProfil;

  /**
   * Constructeur
   * 
   * @param formatSup
   *          la classe support
   * @param clockSupport
   *          l'horloge {@link JobClockSupport}
   * @param value
   *          durée du cache
   *          Récupération du conteneur de clockSupport
   *          sae.format.control.profil.cache à définir dans un fichier tel
   *          sae-config.properties dans src/test/resources/config
   * @param initCacheOnStartup
   *          flag indiquant si initialise le cache au demarrage du serveur d'application
   *          sae.format.control.profil.initCacheOnStartup à définir dans un fichier tel
   *          sae-config.properties dans src/test/resources/config
   */
  @Autowired
  public FormatControlProfilServiceImpl(final FormatControlProfilSupportFacade formatControlSupportFacade,

                                              @Value("${sae.format.control.profil.cache}") final int value,
                                              @Value("${sae.format.control.profil.initCacheOnStartup}") final boolean initCacheOnStartup) {

    this.formatControlSupportFacade = formatControlSupportFacade;

    // Mise en cache
    formatsControlProfil = CacheBuilder.newBuilder()
        .refreshAfterWrite(value,
                           TimeUnit.MINUTES)
        .build(
               new CacheLoader<String, FormatControlProfil>() {

                 @Override
                 public FormatControlProfil load(final String identifiant) {

                   return formatControlSupportFacade.find(identifiant);
                 }
               });

    if (initCacheOnStartup) {
      populateCache();
    }
  }

  private void populateCache() {
    // initialisation du cache des profils de controle de format


    final List<FormatControlProfil> allFcp = formatControlSupportFacade.findAll();

    for (final FormatControlProfil fcp : allFcp) {
      formatsControlProfil.put(fcp.getFormatCode(), fcp);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean formatControlProfilExists(final String code) {

    try {
      formatsControlProfil.getUnchecked(code);
      return true;
    }
    catch (final InvalidCacheLoadException e) {
      return false;
    }
    catch (final UncheckedExecutionException e) {
      throw new DroitRuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void addFormatControlProfil(
                                           final FormatControlProfil formatControlProfil) {

    formatControlSupportFacade.create(formatControlProfil);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void modifyFormatControlProfil(
                                              final FormatControlProfil formatControlProfil)
                                                  throws FormatControlProfilNotFoundException {
    if (formatControlProfilExists(formatControlProfil.getFormatCode())) {
      addFormatControlProfil(formatControlProfil);
    } else {
      throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
                                                     .loadMessage("erreur.format.control.profil.not.found",
                                                                  formatControlProfil.getFormatCode()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void deleteFormatControlProfil(final String codeFormatControlProfil)

      throws FormatControlProfilNotFoundException {

    formatControlSupportFacade.delete(codeFormatControlProfil);


  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final FormatControlProfil getFormatControlProfil(
                                                          final String codeFormatControlProfil)
                                                              throws FormatControlProfilNotFoundException {

    FormatControlProfil formatControlProfil;
    try {
      formatControlProfil = formatsControlProfil
          .getUnchecked(codeFormatControlProfil);
      return formatControlProfil;
    }
    catch (final InvalidCacheLoadException e) {
      throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
                                                     .loadMessage("erreur.format.control.profil.not.found",
                                                                  codeFormatControlProfil),
                                                     e);
    }
    catch (final UncheckedExecutionException e) {
      throw new DroitRuntimeException(e);
    }
  }

  @Override
  public final List<FormatControlProfil> getAllFormatControlProfil() {
    final List<FormatControlProfil> allFcp = new ArrayList<>();

    return formatControlSupportFacade.findAll();
  }

}
