package fr.urssaf.image.sae.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Service pour fournir des méthodes communes pour les tests de l'artefact<br>
 * <br>
 * La classe peut-être injecté par {@link Autowired}
 * 
 * 
 */
@Component
public class SAEServiceTestProvider {

   private final ServiceProvider serviceProvider;

   private final Base base;

   /**
    * initialise la façade des services de sae-storage
    * 
    * @param connection
    *           connection à DFCE
    * @throws ConnectionServiceEx
    *            une exception est levée lors de l'ouverture de la connexion
    */
   @Autowired
   public SAEServiceTestProvider(DFCEConnection dfceConnection)
         throws ConnectionServiceEx {

      Assert.notNull(dfceConnection);

      this.serviceProvider = ServiceProvider.newServiceProvider();

      this.serviceProvider.connect(dfceConnection.getLogin(), dfceConnection
            .getPassword(), dfceConnection.getServerUrl().toString(),
            dfceConnection.getTimeout());

      this.base = serviceProvider.getBaseAdministrationService().getBase(
            dfceConnection.getBaseName());
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
   public final Document searchDocument(UUID uuid) {

      return serviceProvider.getSearchService().getDocumentByUUID(base, uuid);

   }

   /**
    * Permet de retrouver le contenu d'un document archivé dans le SAE
    * 
    * @param doc
    *           document dans le SAE
    * @return contenu du document
    */
   public final InputStream loadDocumentFile(Document doc) {

      return serviceProvider.getStoreService().getDocumentFile(doc);

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
   public final void deleteDocument(UUID uuid) {

      try {
         serviceProvider.getStoreService().deleteDocument(uuid);
      } catch (FrozenDocumentException e) {
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
   public final UUID captureDocument(byte[] content,
         Map<String, Object> metadatas, String documentTitle,
         String documentType, Date dateCreation, Date dateDebutConservation,
         String codeRND, String title) {

      try {
         Document document = ToolkitFactory.getInstance().createDocumentTag(
               base);

         document.setCreationDate(dateCreation);
         document.setTitle(title);
         document.setType(codeRND);
         document.setLifeCycleReferenceDate(dateDebutConservation);

         for (Entry<String, Object> entry : metadatas.entrySet()) {
            BaseCategory baseCategory = base.getBaseCategory(entry.getKey());
            document.addCriterion(baseCategory, entry.getValue());

         }

         InputStream docContent = new ByteArrayInputStream(content);
         return serviceProvider.getStoreService().storeDocument(document,
               documentTitle, documentType, docContent).getUuid();

      } catch (TagControlException e) {
         throw new NestableRuntimeException(e);
      }

   }

}
