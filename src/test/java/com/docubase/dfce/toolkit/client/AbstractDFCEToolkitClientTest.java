package com.docubase.dfce.toolkit.client;

import net.docubase.toolkit.service.Authentication;

import org.junit.BeforeClass;

public abstract class AbstractDFCEToolkitClientTest {
   protected static final String URL = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";
   protected static final String USER = "_ADMIN";
   protected static final String PASSWORD = "DOCUBASE";

   @BeforeClass
   public static void beforeClass() {
      Authentication.openSession(USER, PASSWORD, URL);
   }
}
