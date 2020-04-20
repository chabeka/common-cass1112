package fr.urssaf.image.sae.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;

/**
 * Service pour fournir des méthodes communes pour les tests de l'artefact<br>
 * <br>
 * La classe peut-être injecté par {@link Autowired}
 *
 *
 */
@Component
public class SAEServiceTestProvider {

  private final DFCEServices dfceServices;

  /**
   * initialise la façade des services de sae-storage
   *
   * @param dfceServices
   *           connexion à DFCE
   * @throws ConnectionServiceEx
   *            une exception est levée lors de l'ouverture de la connexion
   */
  @Autowired
  public SAEServiceTestProvider(final DFCEServices dfceServices)
      throws ConnectionServiceEx {

    Assert.assertNotNull(dfceServices);
    this.dfceServices = dfceServices;

  }

  /**
   * Permet de retrouver un document dans le SAE à partir de son identifiant
   * unique d'archivage<br>
   * <br>
   * Cette méthode peut s'avérer utile pour les tests unitaires simplement pour
   * vérifier qu'un document a bien été inséré dans le SAE
   *
   * @param uuid
   *           identifiant unique du document à retrouver dans le SAE
   * @return instance du {@link Document} correspond au paramètre
   *         <code>uuid</code>
   */
  public final Document searchDocument(final UUID uuid) {

    return dfceServices.getDocumentByUUID(uuid);

  }

  /**
   * Permet de retrouver le contenu d'un document archivé dans le SAE
   *
   * @param doc
   *           document dans le SAE
   * @return contenu du document
   */
  public final InputStream loadDocumentFile(final Document doc) {

    return dfceServices.getDocumentFile(doc);

  }

  /**
   * Permet de supprimer un document dans le SAE à partir de son identifiant
   * unique d'archivage<br>
   * <br>
   * Cette méthode peut s'avérer utile pour les tests unitaires simplement pour
   * supprimer un document du SAE qui vient d'être inséré et n'est plus utile
   *
   * @param uuid
   *           identifiant unique du document à supprimer SAE
   */
  public final void deleteDocument(final UUID uuid) {

    try {
      dfceServices.deleteDocument(uuid);
    } catch (final FrozenDocumentException e) {
      throw new NestableRuntimeException(e);
    }

  }

  /**
   *
   * Permet d'insérer un document dans le SAE<br>
   * <br>
   * Cette méthode peut s'avérer utile pour les tests unitaires pour consulter
   * ou recherche un document du SAE
   *
   * @param content
   *           contenu du document à archiver
   * @param metadatas
   *           liste des métadonnées
   * @param documentTitle
   *           titre du document
   * @param documentType
   *           type du document
   * @param dateCreation
   *           date de création du document
   * @param dateDebutConservation
   *           date de début de conservation du document
   * @param codeRND
   *           codeRDN
   * @param title
   *           titre
   * @return UUID du document dans le SAE
   */
  public final UUID captureDocument(final byte[] content,
                                    final Map<String, Object> metadatas, final String documentTitle,
                                    final String documentType, final Date dateCreation, final Date dateDebutConservation,
                                    final String codeRND, final String title, final String note) {

    try {
      final Base base = dfceServices.getBase();
      final Document document = ToolkitFactory.getInstance().createDocument(base,
                                                                            documentTitle, documentType);

      document.setCreationDate(dateCreation);
      document.setTitle(title);
      document.setType(codeRND);
      document.setLifeCycleReferenceDate(dateDebutConservation);

      for (final Entry<String, Object> entry : metadatas.entrySet()) {
        final BaseCategory baseCategory = base.getBaseCategory(entry.getKey());
        document.addCriterion(baseCategory, entry.getValue());

      }

      final InputStream docContent = new ByteArrayInputStream(content);
      final UUID uuidDoc = dfceServices.storeDocument(
                                                      document, docContent).getUuid();
      if (note != null) {
        dfceServices.addNote(uuidDoc, note);
      }

      return uuidDoc;

    } catch (final TagControlException e) {
      throw new NestableRuntimeException(e);
    } catch (final FrozenDocumentException e) {
      throw new NestableRuntimeException(e);      }

  }

  /**
   * Permet d'ajouter une note à un document
   *
   * @param uuid
   *           L'identifiant du document
   * @param contenu
   *           Le contenu de la note
   */
  public final void addNoteDocument(final UUID uuid, final String contenu) {
    try {
      dfceServices.addNote(uuid, contenu);
    } catch (final FrozenDocumentException e) {
      throw new NestableRuntimeException(e);
    } catch (final TagControlException e) {
      throw new NestableRuntimeException(e);
    }

  }

  /**
   * Permet de récupérer la liste des notes d'un document
   *
   * @param uuid
   *           L'identifiant du document
   */
  public List<Note> getNoteDocument(final UUID uuid) {

    return dfceServices.getNotes(uuid);

  }

  /**
   * Getter
   * @return the dfceServices
   */
  public DFCEServices getDfceServices() {
    return dfceServices;
  }
}
