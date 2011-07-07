package com.docubase.dfce.toolkit.jira;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CTRL37Test {
    public static final String BASE_ID = "RICHGED";

    private static final String ADM_LOGIN = "_ADMIN";
    private static final String ADM_PASSWORD = "DOCUBASE";
    private static final String URL = "http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/";

    @Test
    public void getBaseMultiThread() {
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base = ServiceProvider.getBaseAdministrationService().getBase(
		BASE_ID);

	System.out.println(base + " " + Thread.currentThread().getName());

	Thread t = new Thread() {
	    @Override
	    public void run() {
		Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
		System.out.println(Thread.currentThread().getName());
		ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
		Authentication.closeSession();
	    };
	};
	t.start();
	Authentication.closeSession();
    }

    @Test
    public void getBaseMultiThreadWithAuth() {
	Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
	Base base = ServiceProvider.getBaseAdministrationService().getBase(
		BASE_ID);
	System.out.println(base + " " + Thread.currentThread().getName());

	Thread t = new Thread() {
	    @Override
	    public void run() {
		Authentication.openSession(ADM_LOGIN, ADM_PASSWORD, URL);
		System.out.println(Thread.currentThread().getName());
		ServiceProvider.getBaseAdministrationService().getBase(BASE_ID);
		Authentication.closeSession();
	    };
	};
	t.start();
	Authentication.closeSession();
    }
}
