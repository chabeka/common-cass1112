package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;
import net.docubase.toolkit.service.ged.SearchService.DateFormat;

import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class TypedCategoryTest extends AbstractTestCaseCreateAndPrepareBase {

    private final ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();

    private void assertCriterionsEquals(Document expected, Document actual) {
	ReflectionAssert.assertReflectionEquals(
		((DocumentImpl) expected).getCriterions(),
		((DocumentImpl) actual).getCriterions(),
		ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testTypedBoolean() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory booleanCategory = base.getBaseCategory(catNames[3]);

	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyBooleanTest");
	document.addCriterion(booleanCategory, true);

	Document actual = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String c3FormattedName = booleanCategory.getName();
	assertEquals(1, searchLucene(c3FormattedName + ":true", 5));
	assertEquals(0, searchLucene(c3FormattedName + ":false", 5));
	assertCriterionsEquals(document, actual);

    }

    @Test
    public void testTypedInteger() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory integerCategory = base.getBaseCategory(catNames[4]);
	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyIntegerTest");
	document.addCriterion(integerCategory, 10);

	Document storeDoc = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String c4FormattedName = integerCategory.getName();
	assertEquals(1, searchLucene(c4FormattedName + ":10", 5));
	assertEquals(0, searchLucene(c4FormattedName + ":11", 5));
	assertCriterionsEquals(document, storeDoc);
    }

    @Test
    public void testTypedDecimal() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory decimalBaseCategory = base.getBaseCategory(catNames[5]);
	Document document = toolkitFactory.createDocumentTag(base);
	document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyDecimalTest");
	document.addCriterion(decimalBaseCategory, 3.14);

	Document storeDoc = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String c5FName = decimalBaseCategory.getName();
	assertEquals(1, searchLucene(c5FName + ":3.14", 5));
	assertEquals(0, searchLucene(c5FName + ":3.1459", 5));
	assertCriterionsEquals(document, storeDoc);
    }

    @Test
    public void testTypedDate() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	// Type DATE
	GregorianCalendar calendar = new GregorianCalendar(2010, 12, 24);
	calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
	Date currDate = calendar.getTime();

	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory dateBaseCategory = base.getBaseCategory(catNames[6]);
	String strDate = serviceProvider.getSearchService().formatDate(
		currDate, DateFormat.DATE);

	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyDateTest");
	document.addCriterion(dateBaseCategory, currDate);

	Document storeDoc = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String c6FName = dateBaseCategory.getName();
	assertEquals(1, searchLucene(c6FName + ":" + strDate, 5));
	assertEquals(0, searchLucene(c6FName + ":1975-01-01", 5));
	Criterion dateCriterion = storeDoc.getFirstCriterion(dateBaseCategory);

	assertEquals((new DateTime(currDate.getTime())).toDateMidnight()
		.toDate(),
		new DateTime(((Date) dateCriterion.getWord()).getTime())
			.toDateMidnight().toDate());

	assertCriterionsEquals(document, storeDoc);

    }

    @Test
    public void testTypedDateTime() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	Date currDate = new Date();
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory dateTimeBaseCategory = base.getBaseCategory(catNames[7]);
	String strDateTime = serviceProvider.getSearchService().formatDate(
		currDate, DateFormat.DATETIME);

	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyDateTimeTest");
	document.addCriterion(dateTimeBaseCategory, currDate);

	Document storeDoc = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String c7Name = dateTimeBaseCategory.getName();
	assertEquals(0, searchLucene(c7Name + ":\"1975-01-01 08-32\"", 5));
	assertEquals(1, searchLucene(c7Name + ":\"" + strDateTime + "\"", 5));

	assertCriterionsEquals(document, storeDoc);
    }

    @Test
    public void testMixTyped() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	BaseCategory booleanCategory = base.getBaseCategory(catNames[3]);
	BaseCategory integerCategory = base.getBaseCategory(catNames[4]);
	BaseCategory decimalBaseCategory = base.getBaseCategory(catNames[5]);
	BaseCategory dateBaseCategory = base.getBaseCategory(catNames[6]);
	BaseCategory dateTimeBaseCategory = base.getBaseCategory(catNames[7]);

	String booleanFN = booleanCategory.getName();
	String integerFN = integerCategory.getName();
	String decimalFN = decimalBaseCategory.getName();
	String dateTimeFN = dateTimeBaseCategory.getName();

	Date currDate = new Date();
	String strDateTime = serviceProvider.getSearchService().formatDate(
		currDate, DateFormat.DATETIME);

	Document document = toolkitFactory.createDocumentTag(base);
	document.addCriterion(stringCategory, "MyMixTypedTest");
	document.addCriterion(booleanCategory, false);
	document.addCriterion(integerCategory, 10);
	document.addCriterion(decimalBaseCategory, 3.14);
	document.addCriterion(dateBaseCategory, currDate);
	document.addCriterion(dateTimeBaseCategory, currDate);

	Document storeDoc = storeDocument(document,
		TestUtils.getFile("doc1.pdf"), true);

	String query = "((" + booleanFN + ":true OR " + integerFN + ":10"
		+ " OR " + decimalFN + ":3.14) AND " + dateTimeFN + ":"
		+ strDateTime + ")";

	assertEquals(1, searchLucene(query, 10));

	assertEquals(0, searchLucene(dateTimeFN + ":\"1975-01-01 08-32\"", 5));
	assertEquals(
		1,
		searchLucene(booleanFN + ":false AND " + dateTimeFN + ":"
			+ strDateTime, 5));

	assertCriterionsEquals(document, storeDoc);
    }

    @Test
    public void testWildcard() throws ExceededSearchLimitException,
	    SearchQueryParseException {
	BaseCategory stringCategory = base.getBaseCategory(catNames[0]);
	String c0FName = stringCategory.getName();
	String query = c0FName + ":My*";
	assertEquals(6,
		serviceProvider.getSearchService().search(query, 100, base)
			.getTotalHits());
	//
	// query = c0FName + ":My*Test";
	// assertEquals(6, searchLucene(query, 10));
    }

}
