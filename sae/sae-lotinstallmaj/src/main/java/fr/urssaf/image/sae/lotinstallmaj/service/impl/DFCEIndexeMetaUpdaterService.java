package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.docubase.dfce.commons.metadata.MetadataType;

import fr.urssaf.image.sae.lotinstallmaj.component.DFCEConnexionComponent;
import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;
import fr.urssaf.image.sae.lotinstallmaj.modele.SaeCategory;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.model.reference.Metadata;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

@Service
public class DFCEIndexeMetaUpdaterService {

   private static final Logger LOG = LoggerFactory.getLogger(DFCEIndexeMetaUpdaterService.class);

   @Autowired
   private DFCEConnexionComponent dfceConnexionComponent;

   @Autowired
   private CassandraConfig cassandraConfig;

   @Autowired
   private GedCassandraUpdater gedUpdater;

   @Autowired
   private RefMetaInitialisationService refMetaInitialisationService;

   @Value("${sae.path.fichier.cql.lotinstallmaj}")
   private String pathFichierUpdateCql;

   @Value("${sae.typePlateforme}")
   private String typePlatform;

   /**
    * 
    */
   public void demarreCreateMetadatasIndexesDroitsSAE() {
      LOG.debug("Démarrage des opérations de création des métadatas sur la base SAE");
      if (typePlatform.equals("GNT")) {
         // Update des droits GED
         updateCassandraDroitsGed();
      }
      final String lastLotName = LotVersion.getLastLotName();
      updateMetaAndIndexesComposites(lastLotName);

      LOG.debug("Opérations de création des métadatas terminées sur la base SAE");
   }

   /**
    * Mise à jour des Métadonnées et des indexes composites dans DFCE et SAE
    * 
    * @param nomOperation
    *           représente le nom du lot
    */
   public void updateMetaAndIndexesComposites(final String nomOperation) {
      try {

         // MAJ des métadonnées dans DFCE puis SAE
         // updateMetaDfce("META_" + nomOperation);
         updateMetaDfce("META_" + nomOperation);

         // Mise à jour des méta SAE
         refMetaInitialisationService.initialiseRefMeta();

         // Ajout des index composites
         addIndexesCompositeToDfce("META_" + nomOperation);

      }
      catch (final IOException e) {
         // Opération inconnue => log + exception runtime
         final String message = String.format("Erreur technique : %s ", e.getMessage());
         LOG.error(message);
         throw new MajLotRuntimeException(e.getMessage());
      }
   }

   // ATTENTION, depuis la version 160600, afin de différencier GNT/GNS, cette
   // méthode ne gère que les méta DFCE et plus les indexes composite
   // Pour ajouter les indexes composites il faut utiliser
   // addIndexesCompositeToDfce
   private void updateMetaDfce(final String operation) {

      // -- Récupération de la liste des métadonnées
      LOG.debug("Lecture du fichier XML contenant les métadonnées à ajouter - Début");
      final List<MetadataReference> metadonnees = refMetaInitialisationService.getListMetas();
      LOG.info("Début de l'opération : Création des nouvelles métadonnées ({})", operation);

      // -- Mise à jour des métas
      updateBaseDfce(refMetaInitialisationService.genereMetaBaseDfce(metadonnees));

      LOG.info("Fin de l'opération : Création des nouvelles métadonnées ({})", operation);
   }

   public void updateBaseDfce(final List<SaeCategory> categories) {

      try {
         // -- Ouverture connexion DFCE
         dfceConnexionComponent.connectDfce();

         final String baseName = dfceConnexionComponent.getDfceConnexionParameter().getBaseName();
         final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();
         final Base base = serviceProvider.getBaseAdministrationService().getBase(baseName);

         final ToolkitFactory toolkit = ToolkitFactory.getInstance();

         LOG.debug("Création des métadonnées dans DFCE - Début");

         for (final SaeCategory category : categories) {

            StorageAdministrationService service;
            service = serviceProvider.getStorageAdministrationService();

            // -- Test de l'existence de la métadonnée dans DFCE
            final Category catFound = service.getCategory(category.getName());

            if (catFound == null) {

               // -- Création de la catégory

               final Category categoryDfce = service.findOrCreateCategory(category.getName(),
                                                                          category.categoryDataType());

               final BaseCategory baseCategory = toolkit.createBaseCategory(categoryDfce, category.isIndex());

               baseCategory.setEnableDictionary(category.isEnableDictionary());
               baseCategory.setMaximumValues(category.getMaximumValues());
               baseCategory.setMinimumValues(category.getMinimumValues());
               baseCategory.setSingle(category.isSingle());
               base.addBaseCategory(baseCategory);

               LOG.info("La métadonnée {} sera ajoutee.", category.getDescriptif());
            } else {

               // -- Mise à jour de la catégory

               BaseCategory baseCategory = base.getBaseCategory(catFound.getName());
               
               if(baseCategory == null) {
                 baseCategory = toolkit.createBaseCategory(catFound, category.isIndex());
               }
               
               baseCategory.setEnableDictionary(category.isEnableDictionary());
               baseCategory.setMaximumValues(category.getMaximumValues());
               baseCategory.setMinimumValues(category.getMinimumValues());
               baseCategory.setSingle(category.isSingle());
               baseCategory.setIndexed(category.isIndex());

               LOG.info("La métadonnée {} existe :elle sera mise a jour", category.getDescriptif());
            }
         }
         serviceProvider.getBaseAdministrationService().updateBase(base);
      } 
      finally {
         // -- Fermeture connexion DFCE
         dfceConnexionComponent.disconnectDfce();
      }
   }

   private void addIndexesCompositeToDfce(final String operation) throws IOException {

      LOG.info("Début de l'opération : Création des nouveaux index composite ({})", operation);
      // -- Crétion des indexes composites (Si ils n'existent pas déjà)
      if ("GNS".equals(typePlatform)) {
         createIndexesCompositeIfNotExist(refMetaInitialisationService.getIndexesCompositesGNS());
      } else if ("GNT".equals(typePlatform)) {
         createIndexesCompositeIfNotExist(refMetaInitialisationService.getIndexesCompositesGNT());
      }

      LOG.info("Fin de l'opération : Création des nouveaux index composite ({})", operation);
   }

   /**
    * Création des indexes composites à partir d'une liste de noms d'indexes.
    * Les indexes composites sont créés seulement si ils n'existent pas déjà.
    * Indexe à vide l'index composite si besoin
    * 
    * @param indexes
    *           Liste contenant des tableaux de code courts de méta et le
    *           boolean indiquant si on doit indexer à vide Chaque tableau de
    *           codes meta correspond à la composition de l'indexe composite à
    *           créer.
    * @throws IOException
    */
   private void createIndexesCompositeIfNotExist(final Map<String[], String> indexes) throws IOException {

      // -- dcfe connect
      dfceConnexionComponent.connectDfce();

      final ServiceProvider serviceProvider = dfceConnexionComponent.getServiceProvider();
      final StorageAdministrationService storageAdminService = serviceProvider.getStorageAdministrationService();
      final DFCEUpdater dfceUpdater = new DFCEUpdater(cassandraConfig);

      for (final Entry<String[], String> entry : indexes.entrySet()) {
         final String[] metas = entry.getKey();
         final String aIndexerVide = entry.getValue();

         final StringBuffer nomIndex = new StringBuffer();
         final Category[] categories = new Category[metas.length];

         // -- Cache de catégories pour ne pas réinterroger la base
         Map<String, Category> cacheCategories;
         cacheCategories = new HashMap<>();

         for (int i = 0; i < metas.length; i++) {

            final String codeCourt = metas[i];

            // -- On récupère la catégorie qui n'est pas encore dans le
            // cache
            if (!cacheCategories.containsKey(codeCourt)) {
               Category category;
               category = storageAdminService.getCategory(codeCourt);
               if (category == null) {
                  LOG.error("Impossible de récupérer la Category pour code {}", codeCourt);
                  throw new MajLotRuntimeException("La category '" + codeCourt + "' n'a pas ete trouvee");
               }
               cacheCategories.put(codeCourt, category);
               LOG.info("Category {} récupérée", category.getName());
            }

            categories[i] = cacheCategories.get(codeCourt);

            nomIndex.append(codeCourt);
            nomIndex.append('&');
         }

         // -- creation de l'index composite
         LOG.info("Creation de l'index composite {}", nomIndex);
         final CompositeIndex indexComposite = storageAdminService.findOrCreateCompositeIndex(categories);
         if (indexComposite == null) {
            final String mssgErreur = "Impossible de créer l'index composite: " + nomIndex;
            throw new MajLotRuntimeException(mssgErreur);
         }

         // Indexation à vide si besoin
         if ("oui".equals(aIndexerVide)) {
            LOG.info("Indexation de l'index composite {}", nomIndex);
            dfceUpdater.indexeAVideCompositeIndex(nomIndex.toString(), pathFichierUpdateCql);
         }
      }
      dfceConnexionComponent.disconnectDfce();
   }

   /**
    * Ajout des droits GED
    */
   private void updateCassandraDroitsGed() {
      LOG.info("Début de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
      gedUpdater.updateAuthorizationAccess();
      LOG.info("Fin de l'opération : Lot 130700 - Mise à jour du keyspace SAE");
   }

   /**
    * Mise des méta à la fois dans DFCE et SAE à partir du fichier reférentiel des métas
    * 
    * @throws MetadataReferenceNotFoundException
    */
   /*
    * public void updateMetaDFCEAndSAE(final String nomOperation) {
    * // -- Trace
    * LOG.info("Initialisation du nouveau référentiel des métadonnées. Lot : {}", nomOperation);
    * // -- On récupère la liste des métas
    * final List<MetadataReference> metadonnees = refMetaInitialisationService.getListMetas();
    * LOG.info("Nombre de métadonnées à créer : {}", metadonnees.size());
    * // -- Création des métadonnées en base Cassandra SAE et dans DFCE
    * for (final MetadataReference metadonnee : metadonnees) {
    * try {
    * saeMetadataService.modify(metadonnee);
    * LOG.info("Mise à jour de la meta [{}]", metadonnee.getLabel());
    * }
    * catch (final MetadataReferenceNotFoundException e) {
    * LOG.error("La métadonnée {} n'existe pas par conséquent ne peut être modifiée. "
    * + "Elle va être créée", metadonnee.getLabel());
    * saeMetadataService.create(metadonnee);
    * LOG.info("Création de la meta [{}]", metadonnee.getLabel());
    * }
    * }
    * LOG.info("Fin de l'initialisation du nouveau référentiel des métadonnées");
    * }
    */
}
