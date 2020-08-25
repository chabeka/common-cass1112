/**
 * 
 */
package fr.urssaf.image.sae.format.referentiel.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Implémentation de l’interface décrivant les méthodes proposées par le service
 * référentiel des formats.
 * 
 */
@Service
public class ReferentielFormatServiceImpl implements ReferentielFormatService {

  /**
   * Separateur d'extension.
   */
  private final static String EXTENSION_SEPARATOR = ",";

  private final static String DOT_SEPARATOR = ".";

  private final static String EXTENSION_SPECIAL_MIGRATION = "*";

  private final ReferentielFormatSupportFacade refFormatSupport;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ReferentielFormatServiceImpl.class);


  /**
   * Gestion du cache
   */
  private final LoadingCache<String, FormatFichier> formats;

  /**
   * Constructeur
   * 
   * @param refFormSupport
   *           la classe support
   * @param clockSupport
   *           l'horloge {@link JobClockSupport}
   * @param value
   *           durée du cache
   * 
   *           Récupération du conteneur de clockSupport
   * 
   *           sae.referentiel.format.cache à définir dans un fichier tel
   *           sae-config.properties dans src/test/resources/config
   * @param initCacheOnStartup
   *           flag indiquant si initialise le cache au demarrage du serveur d'application
   *           
   *           sae.referentiel.format.initCacheOnStartup à définir dans un fichier tel
   *           sae-config.properties dans src/test/resources/config
   */
  @Autowired
  public ReferentielFormatServiceImpl(final ReferentielFormatSupportFacade refFormSupport,
                                      @Value("${sae.referentiel.format.cache}") final int value,
                                      @Value("${sae.referentiel.format.initCacheOnStartup}") final boolean initCacheOnStartup) {

    refFormatSupport = refFormSupport;

    // Mise en cache
    formats = CacheBuilder.newBuilder().refreshAfterWrite(value,
                                                          TimeUnit.MINUTES).build(new CacheLoader<String, FormatFichier>() {

                                                            @Override
                                                            public FormatFichier load(final String identifiant) {
                                                              return refFormatSupport.find(identifiant);
                                                            }
                                                          });

    if (initCacheOnStartup) {
      populateCache();
    }
  }

  private void populateCache() {
    // initialisation du cache du referentiel des formats
    final List<FormatFichier> allFormat = refFormatSupport.findAll();
    for (final FormatFichier format : allFormat) {
      formats.put(format.getIdFormat(), format);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void addFormat(final FormatFichier refFormat) {

    refFormatSupport.create(refFormat);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void deleteFormat(final String idFormat)
      throws UnknownFormatException {

    refFormatSupport.delete(idFormat);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final FormatFichier getFormat(final String idFormat)
      throws UnknownFormatException {

    FormatFichier format;
    try {
      format = formats.getUnchecked(idFormat);
      return format;
    } catch (final InvalidCacheLoadException e) {
      LOGGER.debug(SaeFormatMessageHandler.getMessage("erreur.no.format.found", idFormat));
      throw new UnknownFormatException(SaeFormatMessageHandler.getMessage("erreur.no.format.found", idFormat), e);
    } catch (final UncheckedExecutionException e) {
      throw new ReferentielRuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<FormatFichier> getAllFormat() {

    return refFormatSupport.findAll();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean exists(final String idFormat) {

    boolean found = false;

    try {
      formats.getUnchecked(idFormat);
      found = true;

    } catch (final InvalidCacheLoadException e) {
      LOGGER.debug(SaeFormatMessageHandler.getMessage("erreur.no.format.found", idFormat));

    } catch (final UncheckedExecutionException e) {
      throw new ReferentielRuntimeException(e);
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExtensionFormatAutorisee(final String fichierName,
                                            final String idFormat) {
    boolean autorized = false;

    try {
      // On recupere le format en fonction de son ident.
      final FormatFichier formatFichier = getFormat(idFormat);

      final String extension = formatFichier.getExtension();

      if (!EXTENSION_SPECIAL_MIGRATION.equals(extension)) {
        // Gestion de la liste d'extension (separateur virgule) ou de
        // l'extension seul.
        List<String> extensions = Arrays.asList(extension);
        if (extension.contains(EXTENSION_SEPARATOR)) {
          extensions = Arrays.asList(extension.split(EXTENSION_SEPARATOR));
        }
        // Boucle sur la liste des extensions pour savoir
        // si l'extension du fichier est autorisée ou non.
        for (final String extensionFormat : extensions) {
          autorized = fichierName.toLowerCase().endsWith(DOT_SEPARATOR
                                                         + extensionFormat.toLowerCase());
          // Si on a l'autorisation, inutile de continuer à boucler.
          if (autorized) {
            break;
          }
        }
      } else {
        // On est sur des fichiers de migration WATT2, on autorise l'import.
        autorized = true;
      }

    } catch (final UnknownFormatException e) {
      LOGGER.debug(SaeFormatMessageHandler.getMessage("erreur.no.format.found", idFormat));

    } catch (final UncheckedExecutionException e) {
      throw new ReferentielRuntimeException(e);
    }

    return autorized;
  }

}
