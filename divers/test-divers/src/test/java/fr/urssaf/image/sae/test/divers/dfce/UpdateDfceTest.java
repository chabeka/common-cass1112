package fr.urssaf.image.sae.test.divers.dfce;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
public class UpdateDfceTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDfceTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void updateDocumentToDomaineCotisant() throws TagControlException, FrozenDocumentException {
      UUID idDoc = UUID.fromString("457afc4b-feab-4fc3-9d36-6839af79fe62");
      // Base de dev
      String nomBase = "SAE-TEST";
      
      // Base d'integration cliente gns
      //String nomBase = "SAE-INT";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            nomBase);
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      final StoreService storeService = serviceProvider.getStoreService();
      
      Criterion metaDomaineCotisant = findMeta(doc, "cot");
      if (metaDomaineCotisant != null) {
         LOGGER.debug("Domaine cotisant : {}", metaDomaineCotisant.getWord());
      } else {
         LOGGER.debug("Pas de domaine cotisant");
      }
      Criterion metaDomaineCompta = findMeta(doc, "cpt");
      if (metaDomaineCompta != null) {
         LOGGER.debug("Domaine comptable : {}", metaDomaineCompta.getWord());
      } else {
         LOGGER.debug("Pas de domaine comptable");
      }
      Criterion metaDomaineRH = findMeta(doc, "drh");
      if (metaDomaineRH != null) {
         LOGGER.debug("Domaine RH : {}", metaDomaineRH.getWord());
      } else {
         LOGGER.debug("Pas de domaine RH");
      }
      Criterion metaDomaineTechnique = findMeta(doc, "dte");
      if (metaDomaineTechnique != null) {
         LOGGER.debug("Domaine technique : {}", metaDomaineTechnique.getWord());
      } else {
         LOGGER.debug("Pas de domaine technique");
      }
      
      LOGGER.debug("Mise a jour du document : {}", idDoc.toString());
      if (metaDomaineCotisant != null) {
         metaDomaineCotisant.setWord(Boolean.TRUE);
      } else {
         BaseCategory category = base.getBaseCategory("cot");
         doc.addCriterion(category, Boolean.TRUE);
      }
      storeService.updateDocument(doc);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void updateDocumentsToDomaineCotisant() throws TagControlException, FrozenDocumentException {

      String[] uuids = {
            "d24c41fb-19ea-4ba7-9a73-42d319b3d373",
            "7d257843-5e7a-408b-8628-a63f84a821cd",
            "c80df605-7be7-406c-8a83-d55fd5fad5b2",
            "5bbf3b5b-51ad-4152-987b-f6505eec693f",
            "74b7cde6-01db-4630-a2e2-e8c543286168",
            "ee433f7e-5b9c-41aa-8960-38ae139137b2",
            "7865a997-120c-48aa-a32b-307501c90bb0",
            "4900ce90-aa69-444d-8f55-81fd434cc4e7",
            "6a8e03b3-2aeb-45b0-918d-091798863ed9",
            "58387b0c-7f13-4168-968d-ca9da8a6c28c",
            "711e02d1-034d-4fd0-9346-2d6594b66035",
            "c6c74a5b-8cbc-4d1f-89c4-a8e6576be412",
            "e3114c0d-82f4-4b30-89d5-5dacbb697261",
            "9eba112d-2a44-4d52-ba17-a95cadd4037b",
            "1e74ab63-f1d3-47dc-9077-ac429b8458e6",
            "04ebf279-c9e4-45d8-ad17-fb50e49b23fe",
            "4a6d4b6a-5fd3-47de-a8bc-b6bfe574cfd3",
            "59fdcecc-c183-4b3a-a261-83e9fbaae9ec"
      };
      
      // Base de dev
      String nomBase = "SAE-TEST";
      
      // Base d'integration cliente gns
      //String nomBase = "SAE-INT";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            nomBase);
      
      for (String uuid : uuids) {
         
         UUID idDoc = UUID.fromString(uuid);
      
         LOGGER.debug("Recuperation du document : {}", idDoc.toString());
         final Document doc = searchService.getDocumentByUUID(base, idDoc);
         
         if (doc != null) {
            final StoreService storeService = serviceProvider.getStoreService();
            
            Criterion metaDomaineCotisant = findMeta(doc, "cot");
            if (metaDomaineCotisant != null) {
               LOGGER.debug("Domaine cotisant : {}", metaDomaineCotisant.getWord());
            } else {
               LOGGER.debug("Pas de domaine cotisant");
            }
            Criterion metaDomaineCompta = findMeta(doc, "cpt");
            if (metaDomaineCompta != null) {
               LOGGER.debug("Domaine comptable : {}", metaDomaineCompta.getWord());
            } else {
               LOGGER.debug("Pas de domaine comptable");
            }
            Criterion metaDomaineRH = findMeta(doc, "drh");
            if (metaDomaineRH != null) {
               LOGGER.debug("Domaine RH : {}", metaDomaineRH.getWord());
            } else {
               LOGGER.debug("Pas de domaine RH");
            }
            Criterion metaDomaineTechnique = findMeta(doc, "dte");
            if (metaDomaineTechnique != null) {
               LOGGER.debug("Domaine technique : {}", metaDomaineTechnique.getWord());
            } else {
               LOGGER.debug("Pas de domaine technique");
            }
            
            LOGGER.debug("Mise a jour du document : {}", idDoc.toString());
            if (metaDomaineCotisant != null) {
               metaDomaineCotisant.setWord(Boolean.TRUE);
            } else {
               BaseCategory category = base.getBaseCategory("cot");
               doc.addCriterion(category, Boolean.TRUE);
            }
            storeService.updateDocument(doc);
         } else {
            LOGGER.debug("Le document {} n'existe pas", idDoc.toString());
         }
      } 
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void updateDocumentsToDomaineTechnique() throws TagControlException, FrozenDocumentException {

      String[] uuids = {
            "99640ff5-ea64-4ff5-8207-f85310f68273",
            "0af576d4-7453-4b56-84c2-9a3d292790b7",
            "b82f8e65-550b-4a20-9950-18771888aaa5",
            "a299af79-9a99-4ce3-b3ed-8675b82eb2d9",
            "04e535ab-734d-4e6a-b590-06c48eaf616f",
            "c9ac8757-2ab5-448d-bf1b-6344416253e6",
            "1d7171a2-dcdf-4df3-b5e1-62bdf7f7e967",
            "b141dfbb-37c8-43c8-b356-9f74fe7fb9e2",
            "362f83c4-fb0c-4939-9ab0-cf94660559bf",
            "39bb67db-185a-43cc-84f7-686de86b7b0e",
            "fde73aea-7866-40ef-943e-fe7097a21ddd",
            "f714744a-ca55-459a-b285-d58f6b067635",
            "a427034e-d370-44ca-850b-47d32b8a8b4f",
            "7137a438-79ab-4e06-af95-1e33330dd45f",
            "8002f7e5-86c9-4428-85cc-5b9a3041b18e",
            "e173c3e2-4322-4ef6-a34f-1470ea85f6a7"
      };
      
      // Base de dev
      String nomBase = "SAE-TEST";
      
      // Base d'integration cliente gns
      //String nomBase = "SAE-INT";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            nomBase);
      
      for (String uuid : uuids) {
         
         UUID idDoc = UUID.fromString(uuid);
      
         LOGGER.debug("Recuperation du document : {}", idDoc.toString());
         final Document doc = searchService.getDocumentByUUID(base, idDoc);
         if (doc != null) {
         
            final StoreService storeService = serviceProvider.getStoreService();
            
            Criterion metaDomaineCotisant = findMeta(doc, "dte");
            if (metaDomaineCotisant != null) {
               LOGGER.debug("Domaine cotisant : {}", metaDomaineCotisant.getWord());
            } else {
               LOGGER.debug("Pas de domaine cotisant");
            }
            Criterion metaDomaineCompta = findMeta(doc, "cpt");
            if (metaDomaineCompta != null) {
               LOGGER.debug("Domaine comptable : {}", metaDomaineCompta.getWord());
            } else {
               LOGGER.debug("Pas de domaine comptable");
            }
            Criterion metaDomaineRH = findMeta(doc, "drh");
            if (metaDomaineRH != null) {
               LOGGER.debug("Domaine RH : {}", metaDomaineRH.getWord());
            } else {
               LOGGER.debug("Pas de domaine RH");
            }
            Criterion metaDomaineTechnique = findMeta(doc, "dte");
            if (metaDomaineTechnique != null) {
               LOGGER.debug("Domaine technique : {}", metaDomaineTechnique.getWord());
            } else {
               LOGGER.debug("Pas de domaine technique");
            }
            
            LOGGER.debug("Mise a jour du document : {}", idDoc.toString());
            if (metaDomaineTechnique != null) {
               metaDomaineTechnique.setWord(Boolean.TRUE);
            } else {
               BaseCategory category = base.getBaseCategory("dte");
               doc.addCriterion(category, Boolean.TRUE);
            }
            storeService.updateDocument(doc);
         } else {
            LOGGER.debug("Le document {} n'existe pas", idDoc.toString());
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   private Criterion findMeta(final Document doc, final String nomMeta) {
      Criterion retour = null;
      for (Criterion meta : doc.getAllCriterions()) {
         if (meta.getCategoryName().equals(nomMeta)) {
            retour = meta;
            break;
         }
      }
      return retour;
   }
}
