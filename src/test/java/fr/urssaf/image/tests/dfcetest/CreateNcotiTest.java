package fr.urssaf.image.tests.dfcetest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.docubase.rheatoolkit.RheaToolkitException;
import net.docubase.rheatoolkit.base.Base;
import net.docubase.rheatoolkit.base.BaseDescription;
import net.docubase.rheatoolkit.base.BaseFactory;
import net.docubase.rheatoolkit.base.BaseIndex;
import net.docubase.rheatoolkit.base.BaseProfile;
import net.docubase.rheatoolkit.base.BaseState;
import net.docubase.rheatoolkit.base.CategoryDescription;
import net.docubase.rheatoolkit.base.DataType;
import net.docubase.rheatoolkit.base.DocumentCreateDateState;
import net.docubase.rheatoolkit.base.DocumentOverlayFormState;
import net.docubase.rheatoolkit.base.DocumentOwnerType;
import net.docubase.rheatoolkit.domain.DomainFactory;
import net.docubase.rheatoolkit.reference.CategoryReferenceDescription;
import net.docubase.rheatoolkit.reference.CategoryReferenceFactory;
import net.docubase.rheatoolkit.session.UserSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Création/Suppression de la base NCOTI.
 * 
 */
@RunWith(Parameterized.class)
public class CreateNcotiTest extends AbstractNcotiTest {

   private static final String BASEID = "NCOTI_" + System.nanoTime();
   private int domainId;
   private static final String ADM_LOGIN = "_ADMIN";
   private static final String ADM_PASSWORD = "DOCUBASE";
   private UserSession adminSession;
   
   @Parameters
   public static List<Integer[]> domainIds() {
      return Arrays.asList(new Integer[][] { 
            {1, 0}, 
            {2, 0}
      });
   }

   public CreateNcotiTest(int input, int expected) {
      this.domainId = input;
      // pas besoin d'expected dans cette classe
   }

   @Before
   public void createSession() throws Exception {
      adminSession = getSession(ADM_LOGIN, ADM_PASSWORD);
   }

   @After
   public void destroySession() throws Exception {
      if (adminSession != null) {
         adminSession.disconnect();
      }
   }

   @Test 
   public void create() throws Exception {
      Base base = createBase();
      assertEquals("Base des cotisants", base.getDescription().getDescription());
   }

   @Test
   public void drop() throws Exception {
      Base base = BaseFactory.getBase(DomainFactory.getDomain(domainId), BASEID);
      deleteBase(base, adminSession);
   }

   protected static void deleteBase(Base toDel, UserSession session ) {
     stopBase(toDel, session);
     try {
        toDel.delete(session.getSessionId());
     } catch (RheaToolkitException e) {
        System.out.println("Erreur lors de la suppression de la base de test " + toDel.getDescription().getBaseId() + " : " + e.getMessage());
        throw new RuntimeException(e);
     }
   }

   /**
    * Arrête la base si nécessaire. 
    */
   protected static void stopBase(Base toStop, UserSession session) {
     
     BaseState state = null ;
     try {
        state = toStop.getState() ;
        if (state.isStarted() && session.isValid()) {
           state.stop(session.getSessionId());
        }
     } catch (RheaToolkitException e) {
        throw new RuntimeException(e);
     }
   }
   
   private Base createBase() throws Exception {

      Base base = null;

      // ne fait que construire l'instance mémoire, ne crée aucune base
      // directement.
      // il y a néanmoins une requete au framework sous jacente. Cette requête
      // initialise certains chemins d'accès
      // (seul le documentManager peut connaitre les arborescences par défaut
      // des bases ged)
      base = new Base(DomainFactory.getDomain(domainId), BASEID);
      setDescription(base);
      setProfile(base);
      BaseIndex index = base.getIndex();

      // On crée toutes les catégories puis on les ajoute à l'index de la base
      for (CategoryDescription cd : createCategories()) {
         index.addCategory(cd);
      }
      
      // Ajout de la base dans le système. Va également créer l'arborescence
      // correspondante dans le répertoire des bases.
      BaseFactory.addBase(base, adminSession.getSessionId());
      return base;
   }

   /**
    * @param base
    */
   private void setDescription(Base base) {
      BaseDescription desc = base.getDescription();
      desc.setDescription("Base des cotisants");
   }

   /**
    * @param base
    * @throws RheaToolkitException
    */
   private void setProfile(Base base) throws RheaToolkitException {
      BaseProfile profile = base.getProfile();

      // Déclare une date de création disponible mais optionnell
      profile.setDocumentCreateDate(DocumentCreateDateState.OPTIONAL);
      // Pas de fond de page
      profile.setDocumentOverlayForm(DocumentOverlayFormState.NONE);
      // Pas de groupe de document
      profile.setDocumentOwnerDefault(DocumentOwnerType.PUBLIC);
      // Le propriétaire d'un document n'est pas modifiable à postériori de
      // son injection
      profile.setDocumentOwnerModify(false);

      // Masque de titre. C'est encore maintenu en DS4, même si maintenant on
      // peut remonter les valeurs de catégorie dans les listes de solution sans
      // avoir besoin de cet artifice.
      profile.setDocumentTitleMask("C0+\" - \"+C1+\" - \"+C6+\" - \"+C2+\" - \"+C3+\" - \"+C4+\" - \"+C5+\" - \"+C7+\" - \"+C9");
      // Impossible de modifier un titre à postériori
      profile.setDocumentTitleModify(false);
      profile.setDocumentTitleSeparator("-");
   }

   private List<CategoryDescription> createCategories() throws RheaToolkitException {
      List<CategoryDescription> categories = new ArrayList<CategoryDescription>();
      CategoryReferenceDescription TYPE_DOC_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.TYPE_DOC.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription TYPE_DOC_LIBELLE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.TYPE_DOC_LIBELLE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_CPT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_CPT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription SIRET_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.SIRET.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NNI_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NNI.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription PERIODE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.PERIODE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_ORGA_CPT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_ORGA_CPT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_UR_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_UR.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_PERS_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_PERS.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_AFFAIRE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_AFFAIRE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DENOM_CPT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DENOM_CPT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_PIECE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_PIECE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription APPLI_SOURCE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.APPLI_SOURCE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_LOT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_LOT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_COMMUNE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_COMMUNE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CATEGORIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CATEGORIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_GROUPE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_GROUPE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_ORIGINE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_ORIGINE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_TRAITEMENT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_TRAITEMENT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_EFFET_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_EFFET.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription JOURNEE_COMPTABLE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.JOURNEE_COMPTABLE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_STRUCT_CTX_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_STRUCT_CTX.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_PARTENAIRE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_PARTENAIRE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_INTER_CTX_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_INTER_CTX.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_ALLOCATAIRE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_ALLOCATAIRE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_USER_INDEX_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_USER_INDEX.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_USER_STOCK_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_USER_STOCK.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_DOSSIER_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_DOSSIER.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_VERIF_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_VERIF.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription AGENT_VERIF_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.AGENT_VERIF.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_VERIF_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_VERIF.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_CLASSEMENT_GED_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_CLASSEMENT_GED.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_CPT_INT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_CPT_INT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_CPT_ANCIEN_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_CPT_ANCIEN.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription TYPE_DOC_ARCHIVAGE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.TYPE_DOC_ARCHIVAGE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_ID_ARCHIVAGE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_ID_ARCHIVAGE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CERTIFICAT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CERTIFICAT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_ORGA_MUTUALISE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_ORGA_MUTUALISE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NOM_NAISS_SALARIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NOM_NAISS_SALARIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NOM_MARITAL_SALARIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NOM_MARITAL_SALARIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription PRENOM_SALARIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.PRENOM_SALARIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription CODE_INT_SALARIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.CODE_INT_SALARIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NATURE_EMPLOI_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NATURE_EMPLOI.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_NAISS_SALARIE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_NAISS_SALARIE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_COMPOSTAGE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_COMPOSTAGE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription DATE_VERSEMENT_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.DATE_VERSEMENT.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_VOLET_SOCIAL_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_VOLET_SOCIAL.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription RFP_FONCTION_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.RFP_FONCTION.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription RFP_ACTIVITE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.RFP_ACTIVITE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NOM_PERS_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NOM_PERS.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription ID_COMPLEMENTAIRE_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.ID_COMPLEMENTAIRE.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NNI_EMPLOYEUR_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NNI_EMPLOYEUR.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryReferenceDescription NUM_RECOURS_REF = CategoryReferenceFactory
            .findOrCreateCategoryReference(Categories.NUM_RECOURS.toString(), DataType.STRING,
                  adminSession.getSessionId());
      CategoryDescription cx;

      cx = new CategoryDescription(TYPE_DOC_REF);
      cx.setMinimumKeywordsNumber((short) 1);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.setKeywordSingle(true);
      cx.enableDictionaryOpen();
      categories.add(cx);

      cx = new CategoryDescription(TYPE_DOC_LIBELLE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_CPT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(SIRET_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NNI_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(PERIODE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_ORGA_CPT_REF);
      cx.setMinimumKeywordsNumber((short) 1);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_UR_REF);
      cx.setMinimumKeywordsNumber((short) 1);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_PERS_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_AFFAIRE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DENOM_CPT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_PIECE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(APPLI_SOURCE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_LOT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_COMMUNE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CATEGORIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_GROUPE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_ORIGINE_REF);
      cx.setMinimumKeywordsNumber((short) 1);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_TRAITEMENT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_EFFET_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(JOURNEE_COMPTABLE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_STRUCT_CTX_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_PARTENAIRE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_INTER_CTX_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_ALLOCATAIRE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_USER_INDEX_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_USER_STOCK_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_DOSSIER_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_VERIF_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(AGENT_VERIF_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_VERIF_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_CLASSEMENT_GED_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_CPT_INT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_CPT_ANCIEN_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(TYPE_DOC_ARCHIVAGE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_ID_ARCHIVAGE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CERTIFICAT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_ORGA_MUTUALISE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NOM_NAISS_SALARIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NOM_MARITAL_SALARIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(PRENOM_SALARIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(CODE_INT_SALARIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NATURE_EMPLOI_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_NAISS_SALARIE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_COMPOSTAGE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(DATE_VERSEMENT_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_VOLET_SOCIAL_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(RFP_FONCTION_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(RFP_ACTIVITE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NOM_PERS_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(ID_COMPLEMENTAIRE_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NNI_EMPLOYEUR_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      cx = new CategoryDescription(NUM_RECOURS_REF);
      cx.setMinimumKeywordsNumber((short) 0);
      cx.setMaximumKeywordsNumber((short) 1);
      cx.setKeywordIndexed(true);
      // Cx.disableDictionary();
      categories.add(cx);

      return categories;
   }
}
