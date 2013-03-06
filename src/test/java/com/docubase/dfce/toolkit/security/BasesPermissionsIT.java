package com.docubase.dfce.toolkit.security;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.user.UserGroup;
import net.docubase.toolkit.model.user.UserPermission;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class BasesPermissionsIT extends AbstractTestCaseCreateAndPrepareBase {
   private static final String NEW_BASE_ID = "newBase";
   private static Base newBase;

   @BeforeClass
   public static void beforeTest() throws ObjectAlreadyExistsException {
      newBase = serviceProvider.getBaseAdministrationService().getBase(NEW_BASE_ID);
      if (newBase == null) {
         newBase = ToolkitFactory.getInstance().createBase(NEW_BASE_ID);
         serviceProvider.getBaseAdministrationService().createBase(newBase);
      }

      UserGroup userGroup = serviceProvider.getUserAdministrationService().loadUserGroup(
            "newBaseGroup");
      if (userGroup == null) {
         Set<String> baseIds = new HashSet<String>();
         baseIds.add(NEW_BASE_ID);
         Set<UserPermission> permissions = new HashSet<UserPermission>();
         permissions.add(UserPermission.GET_BASE);
         permissions.add(UserPermission.DOCUMENT_STORE);

         userGroup = serviceProvider.getUserAdministrationService().createUserGroup("newBaseGroup",
               permissions, baseIds);
         serviceProvider.getUserAdministrationService().createUser("newBaseUser", "password",
               userGroup.getGroupName());

      }

      serviceProvider.connect("newBaseUser", "password", SERVICE_URL);
   }

   @AfterClass
   public static void afterTest() {
      serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
   }

   @Test
   public void testGetBaseNotRestricted() throws TagControlException {
      Base newBase = serviceProvider.getBaseAdministrationService().getBase(NEW_BASE_ID);
      Document document = ToolkitFactory.getInstance().createDocumentTag(newBase);

      InputStream inputStream = new ByteArrayInputStream("doc1.pdf".getBytes());
      Document storeDocument = serviceProvider.getStoreService().storeDocument(document,
            "doc1.pdf", "pdf", inputStream);

      assertNotNull(storeDocument);
   }

   @Test(expected = RuntimeException.class)
   public void testGetBaseRestricted() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(category0, UUID.randomUUID().toString());

      InputStream inputStream = new ByteArrayInputStream("doc1.pdf".getBytes());
      serviceProvider.getStoreService().storeDocument(document, "doc1.pdf", "pdf", inputStream);
   }

}
