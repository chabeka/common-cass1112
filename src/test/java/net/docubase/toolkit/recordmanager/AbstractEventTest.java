package net.docubase.toolkit.recordmanager;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import net.docubase.toolkit.Authentication;
import net.docubase.toolkit.base.AbstractBaseTestCase;
import net.docubase.toolkit.base.MyTagControl;
import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.model.recordmanager.EventReadFilter;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractEventTest extends AbstractBaseTestCase {
    /** Identifiant de la base ged */
    public final static String BASEID = "JUNIT";

    /** Instance de la base GED. Utilis�e pour d�finir / modifier la base GED */
    protected static Base base;

    /** Document � injecter */
    protected final static String DOC = "doc1.pdf";

    /** Initialisation des noms des cat�gories */
    protected static final String[] catNames = { "Cat�gorie z�ro",
	    "Cat�gorie un", "Cat�gorie deux", "Cat bool�enne", "Cat enti�re",
	    "Cat d�cimale", "Cat date", "Cat date et heure" };

    @BeforeClass
    public static void beforeClass() throws Exception {
	Authentication.openSession("_ADMIN", "DOCUBASE", "cer69-ds4int", 4020, 1);
	base = getOrCreateBaseThenStarts();
    }

    public static Base getOrCreateBaseThenStarts() {
	Base base = ServiceProvider.getBaseAdministrationService().getBase(
		BASEID);

	// la base n'existe pas.
	if (base == null) {
	    base = createBase();
	}

	// On s'assure que la base est d�marr�e et d'ailleurs on la d�marre.
	ServiceProvider.getBaseAdministrationService().startBase(base);

	return base;
    }

    @AfterClass
    public static void afterClass() throws Exception {
	deleteBase(base);
	Authentication.closeSession();
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
	// Le propri�taire d'un document n'est pas modifiable � post�riori de
	// son injection
	base.setDocumentOwnerModify(false);
	/*
	 * Masque de titre. C'est encore maintenu en DS4, m�me si maintenant on
	 * peut remonter les valeurs de cat�gorie dans les listes de solution
	 * sans avoir besoin de cet artifice.
	 */
	base.setDocumentTitleMask("C0+\" < \"+C1");
	// taille maximum d'un titre
	base.setDocumentTitleMaxSize(255);
	// Impossible de modifier un titre � post�riori
	base.setDocumentTitleModify(false);
	base.setDocumentTitleSeparator(">");

	Category category0 = storageAdministrationService.findOrCreateCategory(
		catNames[0], CategoryDataType.STRING);
	Category category1 = storageAdministrationService.findOrCreateCategory(
		catNames[1], CategoryDataType.STRING);
	Category category2 = storageAdministrationService.findOrCreateCategory(
		catNames[2], CategoryDataType.STRING);
	Category categoryBoolean = storageAdministrationService
		.findOrCreateCategory(catNames[3], CategoryDataType.BOOLEAN);
	Category categoryInteger = storageAdministrationService
		.findOrCreateCategory(catNames[4], CategoryDataType.INTEGER);
	Category categoryDecimal = storageAdministrationService
		.findOrCreateCategory(catNames[5], CategoryDataType.DECIMAL);
	Category categoryDate = storageAdministrationService
		.findOrCreateCategory(catNames[6], CategoryDataType.DATE);

	BaseCategory baseCategory0 = toolkitFactory.createBaseCategory(
		category0, true);
	baseCategory0.setMinimumValues((short) 1);
	baseCategory0.setSingle(true);
	base.addBaseCategory(baseCategory0);

	BaseCategory baseCategory1 = toolkitFactory.createBaseCategory(
		category1, true);
	baseCategory1.setEnableDictionary(false);
	base.addBaseCategory(baseCategory1);

	BaseCategory baseCategory2 = toolkitFactory.createBaseCategory(
		category2, true);
	baseCategory2.setMaximumValues((short) 10);
	baseCategory2.setEnableDictionary(false);
	base.addBaseCategory(baseCategory2);

	BaseCategory baseCategoryBoolean = toolkitFactory.createBaseCategory(
		categoryBoolean, true);
	baseCategoryBoolean.setMaximumValues((short) 10);
	baseCategoryBoolean.setEnableDictionary(false);
	base.addBaseCategory(baseCategoryBoolean);

	BaseCategory baseCategoryInteger = toolkitFactory.createBaseCategory(
		categoryInteger, true);
	baseCategoryInteger.setMaximumValues((short) 10);
	baseCategoryInteger.setEnableDictionary(false);
	base.addBaseCategory(baseCategoryInteger);

	BaseCategory baseCategoryDecimal = toolkitFactory.createBaseCategory(
		categoryDecimal, true);
	baseCategoryDecimal.setMaximumValues((short) 10);
	baseCategoryDecimal.setEnableDictionary(false);
	base.addBaseCategory(baseCategoryDecimal);

	BaseCategory baseCategoryDate = toolkitFactory.createBaseCategory(
		categoryDate, true);
	baseCategoryDate.setMaximumValues((short) 10);
	baseCategoryDate.setEnableDictionary(false);
	base.addBaseCategory(baseCategoryDate);

	ServiceProvider.getBaseAdministrationService().createBase(base);

	/*
	 * On va, alors qu'il n'y a aucun document, modifier la base pour y
	 * ajouter la derni�re cat�gorie
	 */
	Category categoryDateHeure = storageAdministrationService
		.findOrCreateCategory(catNames[7], CategoryDataType.DATETIME);

	BaseCategory baseCategoryDateHeure = toolkitFactory.createBaseCategory(
		categoryDateHeure, true);
	baseCategoryDateHeure.setMaximumValues((short) 10);
	baseCategoryDateHeure.setEnableDictionary(false);
	base.addBaseCategory(baseCategoryDateHeure);

	ServiceProvider.getBaseAdministrationService().updateBase(base);

	return base;
    }

    /**
     * Inserer un document dans la base
     * 
     * @param filename
     *            document � inserer
     * @return uuid du document inser�
     */
    public static Document insertDocument(String filename, Base base) {
	UUID docUUID = null;
	File fileRef = getFile(filename, AbstractEventTest.class);
	assertNotNull(fileRef);

	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);
	documentTag.setCreationDate(generateCreationDate())
		.setDocFileName(fileRef.getName()).setDocType("pdf");

	documentTag.addCriterion(base.getBaseCategory(catNames[0]), "FileRef");

	ICustomTagControl control = new MyTagControl(catNames);
	boolean stored;
	try {
	    stored = ServiceProvider.getStoreService().storeDocument(
		    documentTag, fileRef, control);
	} catch (CustomTagControlException e) {
	    throw new RuntimeException(e);
	}
	assertTrue(stored);
	documentTag.getVersionDigest();

	docUUID = documentTag.getUUID();
	assertNotNull(docUUID);
	return documentTag;// docUUID;
    }

    /**
     * Construire un filtre avec la date du jour : startDate � 9h et endDate �
     * 19h
     * 
     * @return le filtre construit
     */
    public static EventReadFilter buildFilter() {
	Calendar calStart = GregorianCalendar.getInstance();
	calStart.set(Calendar.HOUR_OF_DAY, 9);
	calStart.set(Calendar.MINUTE, 0);
	calStart.set(Calendar.SECOND, 0);
	Calendar calEnd = GregorianCalendar.getInstance();
	calEnd.set(Calendar.HOUR_OF_DAY, 19);
	calEnd.set(Calendar.MINUTE, 0);
	calEnd.set(Calendar.SECOND, 0);
	return ToolkitFactory.getInstance().createEventReadFilter(
		calStart.getTime(), calEnd.getTime());
    }
}
