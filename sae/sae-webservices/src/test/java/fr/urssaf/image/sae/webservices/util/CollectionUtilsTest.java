package fr.urssaf.image.sae.webservices.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class CollectionUtilsTest {

   @Test
   public void loadlist() {

      List<String> arg = new ArrayList<String>();
      arg.add("toto");
      arg.add("tata");

      assertEquals("la liste doit être identique", arg, CollectionUtils
            .loadListNotNull(arg));

   }

   @Test
   public void loadlist_null() {

      assertEquals("la liste doit être vide", Collections.EMPTY_LIST,
            CollectionUtils.loadListNotNull(null));
   }
}
