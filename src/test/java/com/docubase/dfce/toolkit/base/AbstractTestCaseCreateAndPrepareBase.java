package com.docubase.dfce.toolkit.base;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractTestCaseCreateAndPrepareBase extends AbstractBaseTestCase {
   protected static Logger logger = Logger.getLogger(AbstractTestCaseCreateAndPrepareBase.class);

   public static final String BASEID = "RICHGED";

   protected static final String[] catNames = { "Catégorie zéro", "Catégorie un", "Catégorie deux",
         "Cat booléenne", "Cat entière", "Cat décimale", "Cat date", "Cat date et heure",
         "CatInteger8" };

   protected static BaseCategory baseCategory0;

   @BeforeClass
   public static void before() {
      Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
      base = deleteAndCreateBaseThenStarts();
   }

   @AfterClass
   public static void after() {
      deleteBase(base);
      Authentication.closeSession();
   }

   protected static Base deleteAndCreateBaseThenStarts() {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(BASEID);
      if (base != null) {
         deleteBase(base);
      }
      base = createBase();

      ServiceProvider.getBaseAdministrationService().startBase(base);

      base = ServiceProvider.getBaseAdministrationService().getBase(BASEID);
      return base;
   }

   private static Base createBase() {
      ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
      StorageAdministrationService storageAdministrationService = ServiceProvider
            .getStorageAdministrationService();

      Base base = toolkitFactory.createBase(BASEID);

      base.setDescription("My-Ged-Is-Rich");

      // D�clare une date de cr�ation disponible mais optionnell
      base.setDocumentCreationDateConfiguration(Base.DocumentCreationDateConfiguration.OPTIONAL);
      // Pas de fond de page
      base.setDocumentOverlayFormConfiguration(Base.DocumentOverlayFormConfiguration.NONE);
      // Pas de groupe de document
      base.setDocumentOwnerDefault(Base.DocumentOwnerType.PUBLIC);
      // Le propriétaire d'un document n'est pas modifiable à postériori de
      // son injection
      base.setDocumentOwnerModify(false);
      /*
       * Masque de titre. C'est encore maintenu en DS4, m�me si maintenant on
       * peut remonter les valeurs de cat�gorie dans les listes de solution sans
       * avoir besoin de cet artifice.
       */
      base.setDocumentTitleMask("C0+\" < \"+C1");
      // taille maximum d'un titre
      base.setDocumentTitleMaxSize(255);
      // Impossible de modifier un titre � post�riori
      base.setDocumentTitleModify(false);
      base.setDocumentTitleSeparator(">");

      Category category0 = storageAdministrationService.findOrCreateCategory(catNames[0],
            CategoryDataType.STRING);
      Category category1 = storageAdministrationService.findOrCreateCategory(catNames[1],
            CategoryDataType.STRING);
      Category category2 = storageAdministrationService.findOrCreateCategory(catNames[2],
            CategoryDataType.STRING);
      Category categoryBoolean = storageAdministrationService.findOrCreateCategory(catNames[3],
            CategoryDataType.BOOLEAN);
      Category categoryInteger = storageAdministrationService.findOrCreateCategory(catNames[4],
            CategoryDataType.INTEGER);
      Category categoryDecimal = storageAdministrationService.findOrCreateCategory(catNames[5],
            CategoryDataType.DOUBLE);
      Category categoryDate = storageAdministrationService.findOrCreateCategory(catNames[6],
            CategoryDataType.DATE);
      Category catInteger8 = storageAdministrationService.findOrCreateCategory(catNames[8],
            CategoryDataType.INTEGER);

      baseCategory0 = toolkitFactory.createBaseCategory(category0, true);
      baseCategory0.setMinimumValues((short) 1);
      baseCategory0.setMaximumValues((short) 1);
      baseCategory0.setSingle(true);
      base.addBaseCategory(baseCategory0);

      BaseCategory baseCategory1 = toolkitFactory.createBaseCategory(category1, true);
      baseCategory1.setEnableDictionary(false);
      base.addBaseCategory(baseCategory1);

      BaseCategory baseCategory2 = toolkitFactory.createBaseCategory(category2, true);
      baseCategory2.setMaximumValues((short) 10);
      baseCategory2.setEnableDictionary(false);
      base.addBaseCategory(baseCategory2);

      BaseCategory baseCategoryBoolean = toolkitFactory.createBaseCategory(categoryBoolean, true);
      baseCategoryBoolean.setMaximumValues((short) 10);
      baseCategoryBoolean.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryBoolean);

      BaseCategory baseCategoryInteger = toolkitFactory.createBaseCategory(categoryInteger, true);
      baseCategoryInteger.setMaximumValues((short) 10);
      baseCategoryInteger.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryInteger);

      BaseCategory baseCategoryDecimal = toolkitFactory.createBaseCategory(categoryDecimal, true);
      baseCategoryDecimal.setMaximumValues((short) 10);
      baseCategoryDecimal.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDecimal);

      BaseCategory baseCategoryDate = toolkitFactory.createBaseCategory(categoryDate, true);
      baseCategoryDate.setMaximumValues((short) 10);
      baseCategoryDate.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDate);

      BaseCategory baseCategory8 = toolkitFactory.createBaseCategory(catInteger8, true);
      baseCategory8.setMaximumValues((short) 10);
      baseCategory8.setEnableDictionary(false);
      base.addBaseCategory(baseCategory8);

      try {
         ServiceProvider.getBaseAdministrationService().createBase(base);
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("base : " + base.getBaseId() + " already exists");
      }

      /*
       * On va, alors qu'il n'y a aucun document, modifier la base pour y
       * ajouter la dernière catégorie
       */
      Category categoryDateHeure = storageAdministrationService.findOrCreateCategory(catNames[7],
            CategoryDataType.DATETIME);

      BaseCategory baseCategoryDateHeure = toolkitFactory.createBaseCategory(categoryDateHeure,
            true);
      baseCategoryDateHeure.setMaximumValues((short) 10);
      baseCategoryDateHeure.setEnableDictionary(false);
      base.addBaseCategory(baseCategoryDateHeure);

      ServiceProvider.getBaseAdministrationService().updateBase(base);

      return base;
   }

   protected static Document storeDocument(Document document, File file) throws TagControlException {
      InputStream in = null;

      try {
         in = new FileInputStream(file);
         return ServiceProvider.getStoreService().storeDocument(document, in);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      } finally {
         if (in != null) {
            try {
               in.close();
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         }
      }
   }
}
