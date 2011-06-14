package net.docubase.toolkit.base;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;

public class VirtualDocumentTest extends AbstractTestCaseCreateAndPrepareBase {
    /** UUID du document de réféence pour les tests. */
    private static UUID referenceUUID;

    @BeforeClass
    public static void beforeClass() throws Exception {
	if (referenceUUID == null) {
	    referenceUUID = insertReferenceDocument();
	}
    }

    /**
     * Cette méthode insert le docuemnt de référence utiliser pour les tests.
     * 
     * @return the uUID
     * @throws CustomTagControlException
     */
    private static UUID insertReferenceDocument()
	    throws CustomTagControlException {
	UUID virtuelReferenceUUID = null;

	File fileRef = getFile("48pages.pdf", VirtualDocumentTest.class);
	assertNotNull(fileRef);

	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);

	document.addCriterion(base.getBaseCategory(catNames[0]), "FileRef");

	ICustomTagControl control = new MyTagControl(catNames);

	boolean stored = ServiceProvider.getStoreService().storeDocument(
		document, fileRef, control);

	assertTrue(stored);
	document.getVersionDigest();

	virtuelReferenceUUID = document.getUUID();
	assertNotNull(virtuelReferenceUUID);

	logger.info("UUID du document du référence :" + virtuelReferenceUUID);

	return virtuelReferenceUUID;
    }

    /**
     * Insert un document virtuel.
     * 
     * @param referenceUUID
     *            uuid du document de référence
     * @param startPage
     *            première page
     * @param endPage
     *            dernière page
     * @param name
     *            nom du document virtuel.
     * 
     * @return le doucument virtuel.
     * @throws CustomTagControlException
     */
    private static Document insertVirtualDocument(UUID referenceUUID,
	    int startPage, int endPage, String name)
	    throws CustomTagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.setCreationDate(generateCreationDate()).setDocFileName(name)
		.setDocType("pdf").setVirtualReferenceUUID(referenceUUID)
		.setVirtualStartPage(startPage).setVirtualEndPage(endPage);
	document.addCriterion(base.getBaseCategory(catNames[0]),
		"DocumentVirtual" + UUID.randomUUID());

	ICustomTagControl control = new MyTagControl(catNames);
	boolean stored = ServiceProvider.getStoreService().storeDocument(
		document, control);
	assertTrue(stored);
	assertEquals(referenceUUID, document.getVirtualReferenceUUID());
	assertEquals(startPage, document.getVirtualStartPage());
	assertEquals(endPage, document.getVirtualEndPage());

	return document;
    }

    /**
     * This method try to valid the given PDF.
     * 
     * @param pdf
     *            the pdf
     * @param numberOfpage
     *            the number ofpage
     */
    private void checkPDF(File pdf, int numberOfpage) {
	assertTrue("The PDF file does not exist.", pdf.exists());

	PdfReader pdfReader;

	try {
	    pdfReader = new PdfReader(new FileInputStream(pdf));
	    assertTrue("PDF Version is not valid.",
		    pdfReader.getPdfVersion() > 0);
	    assertEquals("Numbers of pages is not valid.", numberOfpage,
		    pdfReader.getNumberOfPages());
	} catch (FileNotFoundException e) {
	    logger.error("The pdf file cannot be found.", e);
	} catch (IOException e) {
	    logger.error("An I/O erros occured while reading the pdf file", e);
	}
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_EndPage_Smaller_Than_StartPage() {
	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);
	documentTag.setVirtualReferenceUUID(referenceUUID)
		.setVirtualStartPage(10).setVirtualEndPage(5);
	ServiceProvider.getStoreService().storeDocument(documentTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_StartPage_Smaller_Than_1() {
	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);
	documentTag.setVirtualReferenceUUID(referenceUUID)
		.setVirtualStartPage(0).setVirtualEndPage(10);
	ServiceProvider.getStoreService().storeDocument(documentTag);
    }

    /**
     * Dans se test, on stocke dans un premier temps un document de référence à
     * partir duquel seront indéxé des documents virtuels.
     * 
     * @throws CustomTagControlException
     */
    @Test
    public void testInsertVirtualDocument() throws CustomTagControlException {
	logger.info("Start testVirtualDocument()...");
	int startPage = 3;
	int endPage = 5;

	Document document = insertVirtualDocument(referenceUUID, startPage,
		endPage, "VirtualDoc.pdf");

	assertNotNull(document.getUUID());
	assertEquals(referenceUUID.toString(), document
		.getVirtualReferenceUUID().toString());
	assertEquals(startPage, document.getVirtualStartPage());
	assertEquals(endPage, document.getVirtualEndPage());

	File documentFile = ServiceProvider.getStoreService().getDocumentFile(
		document);
	checkPDF(documentFile, 3);

	logger.info("... end testVirtualDocument().");
    }

    @Test
    public void test_Modify_StartPage() throws CustomTagControlException {

	int startPage = 3;
	int endPage = 5;

	Document document = insertVirtualDocument(referenceUUID, startPage,
		endPage, "StartPage3.pdf");
	document.setVirtualStartPage(1);

	ICustomTagControl control = new MyTagControl(catNames);

	assertTrue(
		"Erreur lors de la mise à jour du document.",
		ServiceProvider.getStoreService().updateDocument(document,
			control));

	document = ServiceProvider.getSearchService().getDocumentByUUID(base,
		document.getUUID());
	assertEquals(1, document.getVirtualStartPage());

	File pdf = ServiceProvider.getStoreService().getDocumentFile(document);

	checkPDF(pdf, 5);

    }
}
