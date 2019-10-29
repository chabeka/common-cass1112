package fr.urssaf.image.commons.droid.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;
import fr.urssaf.image.commons.droid.service.FormatIdentificationService;
import fr.urssaf.image.commons.droid.support.DroidAnalyseSupport;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultCollection;
import uk.gov.nationalarchives.droid.core.interfaces.RequestIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.resource.FileSystemIdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;

/**
 * Service d'identification de format par DROID.<br>
 * <br>
 * L'implémentation est FORTEMENT inspirée de :
 * <ul>
 * <li>uk.gov.nationalarchives.droid.submitter.SubmissionGateway (droid-results)
 * </li>
 * <li>uk.gov.nationalarchives.droid.command.ResultPrinter (droid-command-line)</li>
 * </ul>
 * Ceci afin de conserver le même algorithme que Droid, et donc d'obtenir les
 * mêmes résultats
 */
@Service
public class FormatIdentificationServiceImpl implements FormatIdentificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FormatIdentificationServiceImpl.class);

  private final DroidAnalyseSupport droidAnalyseSupport;

  /**
   * Constructeur
   * 
   * @param droidAnalyseSupport
   *          l'objet de support pour l'analyse DROID
   */
  @Autowired
  public FormatIdentificationServiceImpl(final DroidAnalyseSupport droidAnalyseSupport) {

    this.droidAnalyseSupport = droidAnalyseSupport;

  }

  /**
   * {@inheritDoc}
   * 
   * @throws IOException @{@link IOException}
   */
  @Override
  public final String identifie(final File file) throws IOException {
    return identifie(file, false);
  }

  private String identifie(final File file, final boolean analyserContenuArchives) throws IOException {

    // Préparation des données nécessaires au moteur d'identification
    final IdentificationRequest request = prepareIdentification(file);

    // L'implémentation est FORTEMENT inspirée de :
    // uk.gov.nationalarchives.droid.submitter.SubmissionGateway
    // (droid-results)
    // uk.gov.nationalarchives.droid.command.ResultPrinter
    // (droid-command-line)
    // notamment afin de garder le même algo de détection des formats
    // et donc d'obtenir exactement les mêmes résultats que Droid

    // TODO : Voir si on peut travailler sur un InputStream au lieu d'un File

    // Le résultat
    String idPronom;

    try {

      // Identification à l'aide des signatures binaires
      IdentificationResultCollection results = droidAnalyseSupport.handleSignatures(request);

      // Traitement des formats de type "conteneurs"
      IdentificationResultCollection containerResults = droidAnalyseSupport.handleContainer(request, results);

      // Selon si on a trouvé quelque chose en format "conteneur"
      // if (containerResults == null) {
      if (containerResults == null || CollectionUtils.isEmpty(containerResults.getResults())) {

        // no container results - process the normal results.
        results = droidAnalyseSupport.handleExtensions(request, results);

        // Are we processing archive formats?
        if (analyserContenuArchives) {

          // handleArchive(request, results);
          throw new FormatIdentificationRuntimeException("L'analyse au sein des archives n'est pas implémentée");

        } else { // just process the results so far:

          idPronom = droidAnalyseSupport.handleResult(request, results);

        }

      } else { // we have possible container formats:

        containerResults = droidAnalyseSupport.handleExtensions(request, containerResults);
        idPronom = droidAnalyseSupport.handleResult(request, containerResults);

      }

      // Trace
      LOGGER.debug("{}, Identification du format par Droid. Format trouvé : {}", request.getFileName(), idPronom);
    } finally {
      request.close();
    }

    // Renvoie du format identifié
    return idPronom;

  }

  private FileSystemIdentificationRequest prepareIdentification(final File file) {

    String fileName;
    try {
      fileName = file.getCanonicalPath();
    } catch (final IOException ex) {
      throw new FormatIdentificationRuntimeException(ex);
    }

    final RequestMetaData metaData = new RequestMetaData(file.length(), file.lastModified(), fileName);

    final RequestIdentifier identifier = new RequestIdentifier(file.toURI());
    identifier.setParentId(1L);

    final FileSystemIdentificationRequest request = new FileSystemIdentificationRequest(metaData, identifier);

    final InputStream inputStream = null;
    try {
      request.open(file);
    } catch (final FileNotFoundException ex) {
      throw new FormatIdentificationRuntimeException(ex);
    } catch (final IOException ex) {
      throw new FormatIdentificationRuntimeException(ex);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (final IOException e) {
          LOGGER.info(e.getMessage());
        }
      }
    }

    return request;

  }

}
