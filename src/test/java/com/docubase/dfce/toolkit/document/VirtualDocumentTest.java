package com.docubase.dfce.toolkit.document;

import static junit.framework.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;
import com.itextpdf.text.pdf.PdfReader;

public class VirtualDocumentTest extends AbstractTestCaseCreateAndPrepareBase {

    /** The ref document. */
    private static FileReference fileReference;

    @BeforeClass
    public static void beforeClass() throws Exception {
	if (fileReference == null) {
	    fileReference = createFileReference();
	}
    }

    /**
     * Insert un document virtuel.
     * 
     * @param fileReference
     *            uuid du document de référence
     * @param startPage
     *            première page
     * @param endPage
     *            dernière page
     * @param name
     *            nom du document virtuel.
     * 
     * @return le doucument virtuel.
     */
    private static Document insertVirtualDocument(FileReference fileReference,
	    int startPage, int endPage, String name) throws TagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.setCreationDate(generateCreationDate());
	document.addCriterion(base.getBaseCategory(catNames[0]),
		"DocumentVirtual" + UUID.randomUUID());

	Document documentStored = serviceProvider.getStoreService()
		.storeVirtualDocument(document, fileReference, startPage,
			endPage);
	assertNotNull(documentStored);
	assertEquals(fileReference.getUuid(), documentStored.getFileUUID());
	assertEquals(startPage, documentStored.getStartPage());
	assertEquals(endPage, documentStored.getEndPage());

	return documentStored;
    }

    /**
     * This method try to valid the given PDF.
     * 
     * @param documentFile
     *            the pdf
     * @param numberOfpage
     *            the number ofpage
     * @throws IOException
     */
    private void checkPDF(InputStream documentFile, int numberOfpage)
	    throws IOException {
	assertNotNull("The PDF file does not exist.", documentFile);

	PdfReader pdfReader;

	try {
	    pdfReader = new PdfReader(documentFile);
	    assertTrue("PDF Version is not valid.",
		    pdfReader.getPdfVersion() > 0);
	    assertEquals("Numbers of pages is not valid.", numberOfpage,
		    pdfReader.getNumberOfPages());
	} finally {
	    if (documentFile != null) {
		documentFile.close();
	    }
	}
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_EndPage_Smaller_Than_StartPage()
	    throws TagControlException {
	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);

	serviceProvider.getStoreService().storeVirtualDocument(documentTag,
		fileReference, 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_StartPage_Smaller_Than_1() throws TagControlException {
	Document documentTag = ToolkitFactory.getInstance().createDocumentTag(
		base);

	serviceProvider.getStoreService().storeVirtualDocument(documentTag,
		fileReference, 0, 5);
    }

    /**
     * Dans se test, on stocke dans un premier temps un document de référence à
     * partir duquel seront indéxé des documents virtuels.
     * 
     * @throws CustomTagControlException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testInsertAndExtractVirtualDocument() throws IOException,
	    TagControlException, NoSuchAlgorithmException {
	logger.info("Start testVirtualDocument()...");
	int startPage = 3;
	int endPage = 5;

	Document document = insertVirtualDocument(fileReference, startPage,
		endPage, "VirtualDoc.pdf");
	document = serviceProvider.getSearchService().getDocumentByUUID(base,
		document.getUuid());
	assertNotNull(document.getUuid());
	assertEquals(fileReference.getUuid(), document.getFileUUID());
	assertEquals(startPage, document.getStartPage());
	assertEquals(endPage, document.getEndPage());

	InputStream documentFile = serviceProvider.getStoreService()
		.getDocumentFile(document);

	checkPDF(documentFile, 3);

	logger.info("... end testVirtualDocument().");
	documentFile.close();
    }

    @Test
    public void test_Modify_StartPage() throws IOException,
	    TagControlException, FrozenDocumentException {

	int startPage = 3;
	int endPage = 5;

	Document document = insertVirtualDocument(fileReference, startPage,
		endPage, "StartPage3.pdf");

	document = serviceProvider.getStoreService().updateVirtualDocument(
		document, 1, 7);

	assertEquals(1, document.getStartPage());

	InputStream pdf = serviceProvider.getStoreService().getDocumentFile(
		document);

	checkPDF(pdf, 7);

	pdf.close();
    }

    @Test
    public void testDelete() throws TagControlException,
	    FrozenDocumentException, IOException {
	Document documentToDelete = insertVirtualDocument(fileReference, 1, 3,
		"dv1");
	Document document = insertVirtualDocument(fileReference, 2, 4, "dv2");

	serviceProvider.getStoreService().deleteDocument(
		documentToDelete.getUuid());

	InputStream documentFile = serviceProvider.getStoreService()
		.getDocumentFile(document);

	checkPDF(documentFile, 3);
    }
}
