package fr.urssaf.image.commons.springsecurity.acl.dao.util;

import static org.junit.Assert.assertEquals;
import fr.urssaf.image.commons.springsecurity.acl.model.Publication;

public final class AssertPublication {

   private AssertPublication(){
      
   }
   
   public static void assertPublication(Publication publication,
         Integer idExpected, String titleExpected) {

      assertEquals("id non attendu", idExpected, publication.getId());
      assertEquals("title non attendu", titleExpected, publication.getTitle());

   }
}
