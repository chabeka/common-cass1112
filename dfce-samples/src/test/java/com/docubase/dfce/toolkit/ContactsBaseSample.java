package com.docubase.dfce.toolkit;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class ContactsBaseSample extends AbstractTestBase {

   protected static BaseCategory gender;
   protected static BaseCategory firstName;
   protected static BaseCategory secondName;
   protected static BaseCategory age;
   protected static BaseCategory eMail;
   protected static BaseCategory phone;
   protected static BaseCategory address;
   protected static BaseCategory birthday;

   protected static Base contacts;
   protected static final String CONTACTS_BASE_ID = "contacts";

   @BeforeClass
   public static void setUp() {
      connect();
      gender = createBaseCategory("Gender", CategoryDataType.STRING, true, false, 1, 1, false);
      firstName = createBaseCategory("FirstName", CategoryDataType.STRING, true, false, 1, 1, false);
      secondName = createBaseCategory("SecondName", CategoryDataType.STRING, true, false, 1, 10,
            false);
      age = createBaseCategory("Age", CategoryDataType.INTEGER, true, false, 1, 1, false);
      eMail = createBaseCategory("eMail", CategoryDataType.STRING, true, false, 1, 10, true);
      phone = createBaseCategory("Phone", CategoryDataType.STRING, true, false, 1, 10, true);
      address = createBaseCategory("Address", CategoryDataType.STRING, true, false, 1, 10, false);
      birthday = createBaseCategory("Birthday", CategoryDataType.DATE, true, false, 1, 1, false);

      contacts = createBase(CONTACTS_BASE_ID, gender, firstName, secondName, age, eMail, phone,
            address, birthday);

   }

   @AfterClass
   public static void tearDown() {
      serviceProvider.getBaseAdministrationService().deleteBase(contacts);
      disconnect();
   }

}
