package com.docubase.dfce.toolkit.document;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class TagControlTest extends AbstractTestCaseCreateAndPrepareBase {

    private Calendar cal;
    private BaseCategory category0;
    private BaseCategory category1;
    private BaseCategory category2;
    private BaseCategory categoryBoolean;
    private BaseCategory categoryDecimal;
    private BaseCategory categoryInteger;
    private BaseCategory categoryDate;
    private Document document;
    private File newDoc;

    @Before
    public void setupEach() {
	cal = Calendar.getInstance();
	cal.setTimeInMillis(System.currentTimeMillis());
	cal.add(Calendar.HOUR, -1);

	category0 = base.getBaseCategory(catNames[0]);
	category1 = base.getBaseCategory(catNames[1]);
	category2 = base.getBaseCategory(catNames[2]);
	categoryBoolean = base.getBaseCategory(catNames[3]);
	categoryInteger = base.getBaseCategory(catNames[4]);
	categoryDecimal = base.getBaseCategory(catNames[5]);
	categoryDate = base.getBaseCategory(catNames[6]);

	newDoc = TestUtils.getFile("doc1.pdf");

	document = ToolkitFactory.getInstance().createDocumentTag(base);
    }

    @Test
    public void testTooManyC1() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(category1, "C1 val");
	document.addCriterion(category1, "C1 val2");
	document.setUuid(UUID.randomUUID());

	Document stored = storeDocument(document, newDoc);
	Assert.assertNotNull(stored);
    }

    @Test(expected = TagControlException.class)
    public void testNoC0() throws TagControlException, ParseException {
	document.addCriterion(category1, "C1 val");
	for (int i = 1; i < 5; i++) {
	    document.addCriterion(category2, "C2 val" + i);
	}

	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date date = format.parse("2010-01-11");
	document.addCriterion(categoryDecimal, -1.54);
	document.addCriterion(categoryDecimal, "0.0");
	document.addCriterion(categoryDate, date);
	document.setCreationDate(cal.getTime());

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

    @Test
    public void testTypeInteger() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryInteger, Integer.valueOf(12));

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

    @Test(expected = TagControlException.class)
    public void testTypeIntegerWrongType() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryInteger, Float.valueOf(1.4f));

	storeDocument(document, newDoc);
    }

    @Test
    public void testTypeDecimal() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDecimal, Float.valueOf(1.4f));

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);

    }

    @Test(expected = TagControlException.class)
    public void testWrongTypeDecimal() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDecimal, Boolean.FALSE);

	storeDocument(document, newDoc);
    }

    @Test(expected = TagControlException.class)
    public void testWrongTypeDecimalNotParsable() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDecimal, "wrongType");

	storeDocument(document, newDoc);
    }

    @Test
    public void testTypeDate() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDate, new Date());

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

    @Test(expected = TagControlException.class)
    public void testUpdateNoC0() throws TagControlException,
	    FrozenDocumentException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDate, new Date());

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);

	stored.deleteCriterion(category0);
	serviceProvider.getStoreService().updateDocument(stored);
    }

    @Test(expected = TagControlException.class)
    public void testWrongTypeDate() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryDate, Boolean.TRUE);

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

    @Test
    public void testTypeBoolean() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryBoolean, Boolean.TRUE);

	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

    @Test(expected = TagControlException.class)
    public void testTypeBooleanWrongType() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);
	document.addCriterion(categoryBoolean, Integer.valueOf(12));

	storeDocument(document, newDoc);
    }

    @Test(expected = TagControlException.class)
    public void testWrongDocType() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);

	newDoc = TestUtils.getFile("unsupportedExtension.dummy");
	storeDocument(document, newDoc);
    }

    @Test
    public void testCorrectDocType() throws TagControlException {
	String c0 = "Identifier" + UUID.randomUUID();
	document.addCriterion(category0, c0);

	newDoc = TestUtils.getFile("note.txt");
	Document stored = storeDocument(document, newDoc);

	Assert.assertNotNull(stored);
    }

}