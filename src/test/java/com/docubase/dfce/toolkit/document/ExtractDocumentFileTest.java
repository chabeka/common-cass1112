package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ExtractDocumentFileTest extends
	AbstractTestCaseCreateAndPrepareBase {

    @Test
    public void testExtract() throws TagControlException,
	    FileNotFoundException, IOException, NoSuchAlgorithmException {

	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	document.addCriterion(base.getBaseCategory(catNames[0]), "testExtract");

	File file = TestUtils.getFile("48pages.pdf");

	Document documentStored = serviceProvider.getStoreService()
		.storeDocument(document,
			FilenameUtils.getBaseName(file.getName()),
			FilenameUtils.getExtension(file.getName()),
			new FileInputStream(file));

	assertNotNull(documentStored);
	assertNotNull(documentStored.getUuid());

	InputStream in = serviceProvider.getStoreService().getDocumentFile(
		documentStored);

	MessageDigest instance = MessageDigest.getInstance(documentStored
		.getDigestAlgorithm());
	byte[] originalDigest = instance.digest(IOUtils
		.toByteArray(new FileInputStream(file)));
	byte[] recalculateDigest = instance.digest(IOUtils.toByteArray(in));

	assertEquals(Hex.encodeHexString(originalDigest),
		Hex.encodeHexString(recalculateDigest));
	assertEquals(Hex.encodeHexString(recalculateDigest),
		documentStored.getDigest());
	in.close();
    }
}
