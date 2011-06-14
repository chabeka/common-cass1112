package net.docubase.toolkit.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Before;
import org.junit.Test;

public class TagControlTest extends AbstractTestCaseCreateAndPrepareBase {

    private Calendar cal;
    private BaseCategory category0;
    private BaseCategory category1;
    private BaseCategory category2;
    private BaseCategory categoryBoolean;
    private BaseCategory categoryDecimal;
    private BaseCategory categoryInteger;
    private BaseCategory categoryDate;
    private Document tag;
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

	newDoc = getFile("doc1.pdf", TagControlTest.class);

	tag = ToolkitFactory.getInstance().createDocumentTag(base);
    }

    @Test
    public void testTooManyC1() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(category1, "C1 val");
	tag.addCriterion(category1, "C1 val2");

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);
	assertTrue(stored);
    }

    @Test
    public void testNoC0() {
	tag.addCriterion(category1, "C1 val");
	for (int i = 1; i < 5; i++) {
	    tag.addCriterion(category2, "C2 val" + i);
	}
	tag.addCriterion(categoryDecimal, -1.54);
	tag.addCriterion(categoryDecimal, "0.0");
	tag.addCriterion(categoryDate, "2010-01-11");
	tag.setCreationDate(cal.getTime());

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertFalse(stored);
    }

    @Test
    public void testTypeInteger() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(categoryInteger, Integer.valueOf(12));

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertTrue(stored);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeIntegerWrongType() {
	tag.addCriterion(categoryInteger, Float.valueOf(1.4f));
    }

    @Test
    public void testTypeDecimal() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(categoryDecimal, Float.valueOf(1.4f));

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertTrue(stored);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongTypeDecimal() {
	tag.addCriterion(categoryDecimal, new Date());
    }

    @Test(expected = RuntimeException.class)
    public void testWrongTypeDecimalNotParsable() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(categoryDecimal, "wrongType");

	ServiceProvider.getStoreService().storeDocument(tag, newDoc);
    }

    @Test
    public void testTypeDate() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(categoryDate, new Date());

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertTrue(stored);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongTypeDate() {
	tag.addCriterion(categoryDate, Integer.valueOf(12));
    }

    @Test
    public void testTypeBoolean() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);
	tag.addCriterion(categoryBoolean, Boolean.TRUE);

	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertTrue(stored);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeBooleanWrongType() {
	tag.addCriterion(categoryBoolean, Integer.valueOf(12));
    }

    @Test
    public void testWrongDocType() {
	String c0 = "Identifier" + UUID.randomUUID();
	tag.addCriterion(category0, c0);

	newDoc = getFile("note.txt", this.getClass());
	boolean stored = ServiceProvider.getStoreService().storeDocument(tag,
		newDoc);

	assertFalse(stored);
    }
}
