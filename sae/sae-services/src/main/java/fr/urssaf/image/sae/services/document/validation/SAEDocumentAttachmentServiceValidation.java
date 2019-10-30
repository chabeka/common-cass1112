package fr.urssaf.image.sae.services.document.validation;

import java.net.URI;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAEDocumentAttachmentServiceValidation
 * Classe de validation des arguments en entrée des implementations du service
 * SAEDocumentAttachmentService
 */
@Aspect
@Component
public class SAEDocumentAttachmentServiceValidation {

  private static final String SAEDOCUMENTATTACHMENTCLASS = "fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService.";

  private static final String PARAM_ADD_BINAIRE = "execution(void "
      + SAEDOCUMENTATTACHMENTCLASS
      + "addDocumentAttachmentBinaire(*,*,*,*))"
      + "&& args(docUuid, docName, extension, contenu)";

  private static final String PARAM_ADD_URL = "execution(void "
      + SAEDOCUMENTATTACHMENTCLASS
      + "addDocumentAttachmentUrl(*,*))"
      + "&& args(docUuid, ecdeURL)";

  private static final String PARAM_GET = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentAttachment "
      + SAEDOCUMENTATTACHMENTCLASS
      + "getDocumentAttachment(*))"
      + "&& args(docUuid)";

  /**
   * Vérifie que les paramètres d'entrée de la méthode d'ajout par binaire de
   * doc attaché de l'interface
   * {@link fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService}
   * sont bien corrects.
   * 
   * @param docUuid
   *          L'UUID du document parent
   * @param extension
   *          L'extension du document attaché
   * @param docName
   *          Le nom du document attaché
   * @param contenu
   *          Le contenu du document attaché
   */
  @Before(PARAM_ADD_BINAIRE)
  public final void addDocumentAttachmentBinaire(final UUID docUuid, final String docName,
                                                 final String extension, final DataHandler contenu) {

    Validate.notNull(docUuid,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'UUID du document parent'"));
    Validate.notEmpty(docUuid.toString(),
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'UUID du document parent'"));
    Validate.notEmpty(docName,
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'Nom du document attaché'"));
    Validate.notEmpty(extension,
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'Extension du document attaché'"));
    Validate.notNull(contenu,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'Contenu du document attaché'"));
  }

  /**
   * Vérifie que les paramètres d'entrée de la méthode d'ajout par url de doc
   * attaché de l'interface
   * {@link fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService}
   * sont bien corrects.
   * 
   * @param requete
   *          : Lucene requête.
   * @param listMetaDesired
   *          : Liste métadonnée souhaitée
   */
  @Before(PARAM_ADD_URL)
  public final void addDocumentAttachmentUrl(final UUID docUuid, final URI ecdeURL) {

    Validate.notNull(docUuid,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'UUID du document parent'"));
    Validate.notEmpty(docUuid.toString(),
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'UUID du document parent'"));
    Validate.notNull(ecdeURL,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'URL du document attaché'"));
    Validate.notEmpty(ecdeURL.toString(),
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'URL du document attaché'"));
  }

  /**
   * Vérifie que les paramètres d'entrée de la méthode d'ajout par url de doc
   * attaché de l'interface
   * {@link fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService}
   * sont bien corrects.
   * 
   * @param requete
   *          : Lucene requête.
   * @param listMetaDesired
   *          : Liste métadonnée souhaitée
   */
  @Before(PARAM_GET)
  public final void getDocumentAttachment(final UUID docUuid) {

    Validate.notNull(docUuid,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'UUID du document parent'"));
    Validate.notEmpty(docUuid.toString(),
                      ResourceMessagesUtils.loadMessage(
                                                        "argument.required",
                                                        "'UUID du document parent'"));
  }
}
