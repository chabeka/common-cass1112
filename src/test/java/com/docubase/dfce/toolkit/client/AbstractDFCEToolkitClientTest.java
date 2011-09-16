package com.docubase.dfce.toolkit.client;

import net.docubase.toolkit.service.Authentication;

import org.junit.BeforeClass;

import com.docubase.dfce.toolkit.base.AbstractBaseTestCase;

public abstract class AbstractDFCEToolkitClientTest {
    @BeforeClass
    public static void beforeClass() {
	Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN,
		AbstractBaseTestCase.ADM_PASSWORD,
		AbstractBaseTestCase.SERVICE_URL);
    }
}
