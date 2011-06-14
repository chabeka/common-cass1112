package net.docubase.toolkit.base;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentVersioningTest extends
	AbstractTestCaseCreateAndPrepareBase {

    /** The logger. */
    private static Logger logger = Logger
	    .getLogger(DocumentVersioningTest.class);

    /** The file. */
    private static File file;

    @BeforeClass
    public static void beforeClass() throws Exception {
	file = getFile("48pages.pdf", DocumentVersioningTest.class);
    }

    /**
     * Check the given digest against the computed digest of the given file.
     * 
     * @param digest
     *            the digest
     * @param algorithm
     *            the algorithm
     * @param file
     *            the file
     */
    private void checkDigest(String digest, String algorithm, File file) {
	try {
	    MessageDigest instance = MessageDigest.getInstance(algorithm);
	    InputStream input = new FileInputStream(file);
	    byte[] buffer = new byte[4 * 1024];
	    int read = input.read(buffer, 0, 4 * 1024);

	    while (read > -1) {
		instance.update(buffer, 0, read);
		read = input.read(buffer, 0, 4 * 1024);
	    }

	    assertEquals(Hex.encodeHexString(instance.digest()), digest);
	} catch (NoSuchAlgorithmException e) {
	    fail(e.getMessage());
	} catch (FileNotFoundException e) {
	    fail(e.getMessage());
	} catch (IOException e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Store document.
     * 
     * @return the document the rhea toolkit exception
     * @throws CustomTagControlException
     */
    private Document storeDocument() throws CustomTagControlException {
	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.setCreationDate(generateCreationDate())
		.setDocFileName(file.getName()).setDocType("pdf");
	document.addCriterion(base.getBaseCategory(catNames[0]), "FileRef_"
		+ System.currentTimeMillis());

	ICustomTagControl control = new MyTagControl(catNames);
	boolean stored = ServiceProvider.getStoreService().storeDocument(
		document, file, control);

	assertTrue(stored);

	return document;
    }

    /**
     * Test store document.
     * 
     * @throws CustomTagControlException
     * 
     */
    @Test
    public void testStoreDocument() throws CustomTagControlException {
	Document document = storeDocument();
	UUID uuid = document.getUUID();
	assertNotNull(uuid);

	assertEquals(1, document.getVersionNumber());
	String digest = document.getVersionDigest();
	String algorithm = document.getVersionDigestAlgorithm();

	assertNotNull("Digest should not be null.", digest);
	assertNotNull("digest algorithm should not be null.", algorithm);

	checkDigest(digest, algorithm, file);

	File documentFile = ServiceProvider.getStoreService().getDocumentFile(
		document);
	checkDigest(digest, algorithm, documentFile);

	logger.info("Digest = " + digest);
	logger.info("Digest algorithm = " + algorithm);
	logger.info("UUID du document du référence :" + uuid);
    }

    /**
     * Test update document version.
     * 
     * @throws CustomTagControlException
     * 
     */
    // Versioning is not active by default.
    @Test(expected = RuntimeException.class)
    public void testUpdateDocumentVersion() throws CustomTagControlException {
	Document document = storeDocument();

	boolean updated = ServiceProvider.getStoreService().updateFileVersion(
		document, file, "newVersionName", false);
	assertTrue(updated);

	document = ServiceProvider.getSearchService().getDocumentByUUID(base,
		document.getUUID());
	assertNotNull(document);
	assertEquals(2, document.getVersionNumber());

    }
}
