package com.docubase.dfce.toolkit.client.administration;

import static org.junit.Assert.*;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.administration.BaseAdministrationService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public class BaseAdministrationClientIT extends AbstractTestBase {
   private final BaseAdministrationService baseAdministrationService = serviceProvider
         .getBaseAdministrationService();

   @BeforeClass
   public static void setUp() {
      connect();
   }

   @AfterClass
   public static void tearDown() {
      disconnect();
   }

   @Test
   public void testCreateBase() {
      Base base = baseAdministrationService.getBase("base");
      if (base != null) {
         baseAdministrationService.deleteBase(base);
      }
      base = ToolkitFactory.getInstance().createBase("base");
      try {
         baseAdministrationService.createBase(base);
      } catch (ObjectAlreadyExistsException e) {
         e.printStackTrace();
         fail("base : " + base.getBaseId() + " already exists");
      }
   }
}
