package com.docubase.dfce.toolkit.document.iterator;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentQueryIteratorMultiBaseTest extends
	AbstractTestCaseCreateAndPrepareBase {
    private static BaseCategory c0;
    private static BaseCategory c1;
    private static BaseCategory c2;
    private static BaseCategory c4;

    private static String RANDOM_STRING = UUID.randomUUID().toString();
    private static Base base2;

    public static void createBase2() {
	ToolkitFactory factory = ToolkitFactory.getInstance();

	base2 = factory.createBase(UUID.randomUUID().toString());
	BaseCategory c1Base2 = factory.createBaseCategory(c1.getCategory(),
		true);

	base2.addBaseCategory(c1Base2);

	try {
	    serviceProvider.getBaseAdministrationService().createBase(base2);
	} catch (ObjectAlreadyExistsException e) {
	    throw new RuntimeException(e);
	}
    }

    @BeforeClass
    public static void setupAll() throws NoSuchJobException,
	    JobExecutionAlreadyRunningException, JobRestartException,
	    JobInstanceAlreadyCompleteException, JobParametersInvalidException,
	    JobParametersNotFoundException, UnexpectedJobExecutionException {
	Document document;

	c0 = base.getBaseCategory(catNames[0]);
	c1 = base.getBaseCategory(catNames[1]);
	c2 = base.getBaseCategory(catNames[2]);
	c4 = base.getBaseCategory(catNames[4]);

	createBase2();
	for (int i = 0; i < 50; i++) {
	    document = ToolkitFactory.getInstance().createDocumentTag(base);

	    // C0 unique
	    document.addCriterion(c0, "testfilter" + i);

	    // C1 soit Enfant, soit Adulte
	    String c1Val = null;
	    if (i < 10) { // Les enfants d'abord
		c1Val = "enfant";
	    } else {
		c1Val = "adulte";
	    }
	    // document.addCriterion(c1, c1Val);

	    document.addCriterion(c1, RANDOM_STRING);
	    document.addCriterion(c1, RANDOM_STRING + i);

	    // C2. 2 valeurs, une qui varie très peu, une qui est unique.
	    document.addCriterion(c2, "personne" + i);
	    document.addCriterion(c2, i % 2 == 0 ? "masculin" : "feminin");
	    document.addCriterion(c4, 10);

	    // stockage
	    storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
	}

	for (int i = 0; i < 50; i++) {
	    document = ToolkitFactory.getInstance().createDocumentTag(base2);

	    document.addCriterion(c1.getName(), RANDOM_STRING);
	    document.addCriterion(c1.getName(), RANDOM_STRING + i);

	    storeDocument(document, TestUtils.getFile("doc1.pdf"), true);
	}

	serviceProvider.getStorageAdministrationService()
		.updateAllIndexesUsageCount();
    }

    @AfterClass
    public static void afterClass() {
	serviceProvider.getBaseAdministrationService().deleteBase(base2);
    }

    @Test
    public void testCreateDocumentIterator_Multibase_C1() {
	SearchQuery query = ToolkitFactory.getInstance().createMultibaseQuery(
		c1.getName() + ":" + RANDOM_STRING);
	query.setSearchLimit(7);
	Iterator<Document> iterator = serviceProvider.getSearchService()
		.createDocumentIterator(query);

	int count = 0;
	while (iterator.hasNext()) {
	    iterator.next();
	    count++;
	}

	assertEquals(100, count);
    }

    @Test
    public void testCreateDocumentIterator_Multibase_C1_Starred() {
	SearchQuery query = ToolkitFactory.getInstance().createMultibaseQuery(
		c1.getName() + ":" + RANDOM_STRING + 1 + "*");
	query.setSearchLimit(7);
	Iterator<Document> iterator = serviceProvider.getSearchService()
		.createDocumentIterator(query);

	int count = 0;
	while (iterator.hasNext()) {
	    iterator.next();
	    count++;
	}

	assertEquals(22, count);
    }
}
