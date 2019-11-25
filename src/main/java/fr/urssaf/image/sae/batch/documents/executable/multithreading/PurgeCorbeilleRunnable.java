package fr.urssaf.image.sae.batch.documents.executable.multithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import net.docubase.toolkit.model.document.Document;

/**
 * Thread de purge d'un document dans la corbeille
 */
public class PurgeCorbeilleRunnable implements Runnable {

  /**
   * Logger de la classe.
   */
  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(PurgeCorbeilleRunnable.class);

  /**
   * Informations du document.
   */
  private Document document;

  /**
   * Service d'accès couche DFCE
   */
  private DfceService dfceService;

  /**
   * Constructeur de la classe : Initialise le document et la liste des
   * métadonnées à ajouter
   *
   * @param dfceService
   *          services DFCE
   * @param doc
   *          document
   * @param metas
   *          métadonnées
   */
  public PurgeCorbeilleRunnable(final DfceService dfceService, final Document doc) {
    super();
    setDocument(doc);
    setDfceService(dfceService);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void run() {
    // Suppression du document
    try {
      dfceService.getDfceServices().deleteDocumentFromRecycleBin(document.getUuid());

    }
    catch (final FrozenDocumentException e) {
      final String error = e.getMessage();
      LOGGER.warn("Erreur lors de la suppression de la corbeille du document {}:",
                  document
                          .getUuid()
                      + "(" + error + ")");
    }
  }

  /**
   * Permet de récupérer l'objet Document.
   *
   * @return Document
   */
  public final Document getDocument() {
    return document;
  }

  /**
   * Permet de modifier l'objet Document.
   *
   * @param document
   *          document DFCE
   */
  public final void setDocument(final Document document) {
    this.document = document;
  }

  /**
   * Getter
   * 
   * @return the dfceService
   */
  public DfceService getDfceService() {
    return dfceService;
  }

  /**
   * Setter
   * 
   * @param dfceService
   *          service DFCe
   */
  private void setDfceService(final DfceService dfceService) {
    this.dfceService = dfceService;
  }

}
