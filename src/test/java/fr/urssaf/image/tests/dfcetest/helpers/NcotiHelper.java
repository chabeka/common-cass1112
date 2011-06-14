/**
 * 
 */
package fr.urssaf.image.tests.dfcetest.helpers;

import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.Base.DocumentCreationDateConfiguration;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import fr.urssaf.image.tests.dfcetest.Categories;
import fr.urssaf.image.tests.dfcetest.NcotiCategories;

/**
 * 
 * 
 */
public class NcotiHelper {
   private static ToolkitFactory toolkit;

   private NcotiHelper() {
   }

   static {
      toolkit = ToolkitFactory.getInstance();
   }

   public static Base createOrReplaceBase(String baseId) throws Exception {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(baseId);
      
      if (base != null ) {
         DocubaseHelper.dropBase(base);
      }
      
      base = toolkit.createBase(baseId);
      base.setDescription("Base des cotisants");
      // Déclare une date de création disponible mais optionnelle
      base.setDocumentCreationDateConfiguration(DocumentCreationDateConfiguration.OPTIONAL);
      // Pas de fond de page
      base.setDocumentOverlayFormConfiguration(Base.DocumentOverlayFormConfiguration.NONE);
      // Pas de groupe de document
      base.setDocumentOwnerDefault(Base.DocumentOwnerType.PUBLIC);
      // Le propriétaire d'un document n'est pas modifiable après son injection
      base.setDocumentOwnerModify(false);
      // Propriétés du titre
      base.setDocumentTitleMask("C0+\" - \"+C1+\" - \"+C6+\" - \"+C2+\" - \"+C3+\" - \"+C4+\" - \"+C5+\" - \"+C7+\" - \"+C9");
      base.setDocumentTitleSeparator("-");
      base.setDocumentTitleMaxSize(255);
      base.setDocumentTitleModify(false);

      for (BaseCategory baseCategory : createCategories()) {
         base.addBaseCategory(baseCategory);
      }
      
      /*BaseCategory catWithDict = base.getBaseCategory(Categories.TYPE_DOC.toString());
      ServiceProvider.getStorageAdministrationService().addDictionaryTerm(catWithDict, ".pdf");
      ServiceProvider.getStorageAdministrationService().addDictionaryTerm(catWithDict, ".txt");
      ServiceProvider.getStorageAdministrationService().addDictionaryTerm(catWithDict, ".doc");*/
      
      ServiceProvider.getBaseAdministrationService().createBase(base);
      return base;
   }
   
   private static List<BaseCategory> createCategories() {
      List<BaseCategory> categories = new ArrayList<BaseCategory>();

      StorageAdministrationService storageAdministrationService = ServiceProvider
            .getStorageAdministrationService();
      
      Category TYPE_DOC_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.TYPE_DOC.toString(), CategoryDataType.STRING);       
      Category APPLI_SOURCE_REF = storageAdministrationService.findOrCreateCategory(
            Categories.APPLI_SOURCE.toString(), CategoryDataType.STRING);     
      Category TITRE_REF = storageAdministrationService.findOrCreateCategory(
            Categories.TITRE.toString(), CategoryDataType.STRING);
      Category DATE_REF = storageAdministrationService.findOrCreateCategory(
            Categories.DATE.toString(), CategoryDataType.DATE);   
      Category DATETIME_REF = storageAdministrationService.findOrCreateCategory(
            Categories.DATETIME.toString(), CategoryDataType.DATETIME);  
      Category DECIMAL_REF = storageAdministrationService.findOrCreateCategory(
            Categories.DECIMAL.toString(), CategoryDataType.DECIMAL);
      Category INTEGER_REF = storageAdministrationService.findOrCreateCategory(
            Categories.INTEGER.toString(), CategoryDataType.INTEGER); 
      Category BOOLEAN_REF = storageAdministrationService.findOrCreateCategory(
            Categories.BOOLEAN.toString(), CategoryDataType.BOOLEAN);  
      
      BaseCategory cx;
      
      // Catégories obligatoires
      //
      cx = toolkit.createBaseCategory(TYPE_DOC_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      //cx.setEnableDictionary(true);
      categories.add(cx);     
      
      cx = toolkit.createBaseCategory(TITRE_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATETIME_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      categories.add(cx);         
      
      // Catégories facultatives
      //
      cx = toolkit.createBaseCategory(APPLI_SOURCE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      
      cx = toolkit.createBaseCategory(DATE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);
   
      cx = toolkit.createBaseCategory(DECIMAL_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);
      
      cx = toolkit.createBaseCategory(INTEGER_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);
      
      cx = toolkit.createBaseCategory(BOOLEAN_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);      
      
      return categories;

   }

   private static List<BaseCategory> createNcotiCategories() {
      List<BaseCategory> categories = new ArrayList<BaseCategory>();

      StorageAdministrationService storageAdministrationService = ServiceProvider
            .getStorageAdministrationService();

      Category TYPE_DOC_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.TYPE_DOC.toString(), CategoryDataType.STRING);
      Category TYPE_DOC_LIBELLE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.TYPE_DOC_LIBELLE.toString(), CategoryDataType.STRING);
      Category NUM_CPT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_CPT.toString(), CategoryDataType.STRING);
      Category SIRET_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.SIRET.toString(), CategoryDataType.STRING);
      Category NNI_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NNI.toString(), CategoryDataType.STRING);
      Category PERIODE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.PERIODE.toString(), CategoryDataType.STRING);
      Category CODE_ORGA_CPT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_ORGA_CPT.toString(), CategoryDataType.STRING);
      Category CODE_UR_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_UR.toString(), CategoryDataType.STRING);
      Category NUM_PERS_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_PERS.toString(), CategoryDataType.STRING);
      Category NUM_AFFAIRE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_AFFAIRE.toString(), CategoryDataType.STRING);
      Category DENOM_CPT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DENOM_CPT.toString(), CategoryDataType.STRING);
      Category NUM_PIECE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_PIECE.toString(), CategoryDataType.STRING);
      Category APPLI_SOURCE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.APPLI_SOURCE.toString(), CategoryDataType.STRING);
      Category NUM_LOT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_LOT.toString(), CategoryDataType.STRING);
      Category CODE_COMMUNE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_COMMUNE.toString(), CategoryDataType.STRING);
      Category CATEGORIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CATEGORIE.toString(), CategoryDataType.STRING);
      Category NUM_GROUPE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_GROUPE.toString(), CategoryDataType.STRING);
      Category DATE_ORIGINE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_ORIGINE.toString(), CategoryDataType.STRING);
      Category DATE_TRAITEMENT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_TRAITEMENT.toString(), CategoryDataType.STRING);
      Category DATE_EFFET_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_EFFET.toString(), CategoryDataType.STRING);
      Category JOURNEE_COMPTABLE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.JOURNEE_COMPTABLE.toString(), CategoryDataType.STRING);
      Category NUM_STRUCT_CTX_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_STRUCT_CTX.toString(), CategoryDataType.STRING);
      Category NUM_PARTENAIRE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_PARTENAIRE.toString(), CategoryDataType.STRING);
      Category NUM_INTER_CTX_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_INTER_CTX.toString(), CategoryDataType.STRING);
      Category NUM_ALLOCATAIRE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_ALLOCATAIRE.toString(), CategoryDataType.STRING);
      Category CODE_USER_INDEX_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_USER_INDEX.toString(), CategoryDataType.STRING);
      Category CODE_USER_STOCK_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_USER_STOCK.toString(), CategoryDataType.STRING);
      Category NUM_DOSSIER_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_DOSSIER.toString(), CategoryDataType.STRING);
      Category DATE_VERIF_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_VERIF.toString(), CategoryDataType.STRING);
      Category AGENT_VERIF_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.AGENT_VERIF.toString(), CategoryDataType.STRING);
      Category CODE_VERIF_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_VERIF.toString(), CategoryDataType.STRING);
      Category DATE_CLASSEMENT_GED_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_CLASSEMENT_GED.toString(), CategoryDataType.STRING);
      Category NUM_CPT_INT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_CPT_INT.toString(), CategoryDataType.STRING);
      Category NUM_CPT_ANCIEN_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_CPT_ANCIEN.toString(), CategoryDataType.STRING);
      Category TYPE_DOC_ARCHIVAGE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.TYPE_DOC_ARCHIVAGE.toString(), CategoryDataType.STRING);
      Category NUM_ID_ARCHIVAGE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_ID_ARCHIVAGE.toString(), CategoryDataType.STRING);
      Category CERTIFICAT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CERTIFICAT.toString(), CategoryDataType.STRING);
      Category CODE_ORGA_MUTUALISE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_ORGA_MUTUALISE.toString(), CategoryDataType.STRING);
      Category NOM_NAISS_SALARIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NOM_NAISS_SALARIE.toString(), CategoryDataType.STRING);
      Category NOM_MARITAL_SALARIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NOM_MARITAL_SALARIE.toString(), CategoryDataType.STRING);
      Category PRENOM_SALARIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.PRENOM_SALARIE.toString(), CategoryDataType.STRING);
      Category CODE_INT_SALARIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.CODE_INT_SALARIE.toString(), CategoryDataType.STRING);
      Category NATURE_EMPLOI_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NATURE_EMPLOI.toString(), CategoryDataType.STRING);
      Category DATE_NAISS_SALARIE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_NAISS_SALARIE.toString(), CategoryDataType.STRING);
      Category NUM_COMPOSTAGE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_COMPOSTAGE.toString(), CategoryDataType.STRING);
      Category DATE_VERSEMENT_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.DATE_VERSEMENT.toString(), CategoryDataType.STRING);
      Category NUM_VOLET_SOCIAL_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_VOLET_SOCIAL.toString(), CategoryDataType.STRING);
      Category RFP_FONCTION_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.RFP_FONCTION.toString(), CategoryDataType.STRING);
      Category RFP_ACTIVITE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.RFP_ACTIVITE.toString(), CategoryDataType.STRING);
      Category NOM_PERS_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NOM_PERS.toString(), CategoryDataType.STRING);
      Category ID_COMPLEMENTAIRE_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.ID_COMPLEMENTAIRE.toString(), CategoryDataType.STRING);
      Category NNI_EMPLOYEUR_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NNI_EMPLOYEUR.toString(), CategoryDataType.STRING);
      Category NUM_RECOURS_REF = storageAdministrationService.findOrCreateCategory(
            NcotiCategories.NUM_RECOURS.toString(), CategoryDataType.STRING);

      BaseCategory cx;

      cx = toolkit.createBaseCategory(TYPE_DOC_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      //cx.setEnableDictionary(true);
      categories.add(cx);

      cx = toolkit.createBaseCategory(TYPE_DOC_LIBELLE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_CPT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(SIRET_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NNI_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(PERIODE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_ORGA_CPT_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_UR_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_PERS_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_AFFAIRE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DENOM_CPT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_PIECE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(APPLI_SOURCE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_LOT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_COMMUNE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CATEGORIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_GROUPE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_ORIGINE_REF, true);
      cx.setMinimumValues((short) 1);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_TRAITEMENT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_EFFET_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(JOURNEE_COMPTABLE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_STRUCT_CTX_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_PARTENAIRE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_INTER_CTX_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_ALLOCATAIRE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_USER_INDEX_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_USER_STOCK_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_DOSSIER_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_VERIF_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(AGENT_VERIF_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_VERIF_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_CLASSEMENT_GED_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_CPT_INT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_CPT_ANCIEN_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(TYPE_DOC_ARCHIVAGE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_ID_ARCHIVAGE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CERTIFICAT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_ORGA_MUTUALISE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NOM_NAISS_SALARIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NOM_MARITAL_SALARIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(PRENOM_SALARIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(CODE_INT_SALARIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NATURE_EMPLOI_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_NAISS_SALARIE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_COMPOSTAGE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(DATE_VERSEMENT_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_VOLET_SOCIAL_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(RFP_FONCTION_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(RFP_ACTIVITE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NOM_PERS_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(ID_COMPLEMENTAIRE_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NNI_EMPLOYEUR_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      cx = toolkit.createBaseCategory(NUM_RECOURS_REF, true);
      cx.setMinimumValues((short) 0);
      cx.setMaximumValues((short) 1);
      categories.add(cx);

      return categories;
   }
}
