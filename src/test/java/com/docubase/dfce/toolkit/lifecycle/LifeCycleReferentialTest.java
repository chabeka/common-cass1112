package com.docubase.dfce.toolkit.lifecycle;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.model.reference.LifeCycleRule;

import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class LifeCycleReferentialTest extends
	AbstractTestCaseCreateAndPrepareBase {

    @Test
    public void testCreateLifeCycleRule() throws ObjectAlreadyExistsException {
	String documentType = "documentType" + UUID.randomUUID().toString();
	LifeCycleRule newLifeCycleRule = serviceProvider
		.getStorageAdministrationService().createNewLifeCycleRule(
			documentType, 12, LifeCycleLengthUnit.YEAR);

	assertEquals(documentType, newLifeCycleRule.getDocumentType());
	assertEquals(12, newLifeCycleRule.getLifeCycleLength());
	assertEquals(LifeCycleLengthUnit.YEAR,
		newLifeCycleRule.getLifeCycleLengthUnit());
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void testCreateLifeCycleRuleAlreadyExists()
	    throws ObjectAlreadyExistsException {
	String documentType = "documentType" + UUID.randomUUID().toString();
	serviceProvider.getStorageAdministrationService()
		.createNewLifeCycleRule(documentType, 12,
			LifeCycleLengthUnit.YEAR);

	serviceProvider.getStorageAdministrationService()
		.createNewLifeCycleRule(documentType, 1,
			LifeCycleLengthUnit.MONTH);
    }

    @Test
    public void testGetLifeCycleRule() throws ObjectAlreadyExistsException {
	String documentType = "documentType" + UUID.randomUUID().toString();
	serviceProvider.getStorageAdministrationService()
		.createNewLifeCycleRule(documentType, 12,
			LifeCycleLengthUnit.MONTH);

	LifeCycleRule lifeCycleRule = serviceProvider
		.getStorageAdministrationService().getLifeCycleRule(
			documentType);

	assertEquals(documentType, lifeCycleRule.getDocumentType());
	assertEquals(12, lifeCycleRule.getLifeCycleLength());
	assertEquals(LifeCycleLengthUnit.MONTH,
		lifeCycleRule.getLifeCycleLengthUnit());
    }

    @Test
    public void testGetAllLifeCycleRules() throws ObjectAlreadyExistsException {
	String documentType = "documentType" + UUID.randomUUID().toString();
	serviceProvider.getStorageAdministrationService()
		.createNewLifeCycleRule(documentType, 12,
			LifeCycleLengthUnit.MONTH);

	Set<LifeCycleRule> allLifeCycleRules = serviceProvider
		.getStorageAdministrationService().getAllLifeCycleRules();

	boolean found = false;
	for (LifeCycleRule lifeCycleRule : allLifeCycleRules) {
	    if (lifeCycleRule.getDocumentType().equals(documentType)) {
		found = true;
	    }
	}
	assertTrue(found);
    }

    @Test
    public void testGetAllTypes() throws ObjectAlreadyExistsException {
	String documentType = "documentType" + UUID.randomUUID().toString();
	serviceProvider.getStorageAdministrationService()
		.createNewLifeCycleRule(documentType, 12,
			LifeCycleLengthUnit.MONTH);

	Set<String> allDocumentTypes = serviceProvider
		.getStorageAdministrationService().getAllDocumentTypes();

	assertTrue(allDocumentTypes.contains(documentType));
    }
}
