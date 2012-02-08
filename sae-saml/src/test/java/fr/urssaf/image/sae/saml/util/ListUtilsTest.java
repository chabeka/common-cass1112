package fr.urssaf.image.sae.saml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

@SuppressWarnings({"PMD.MethodNamingConventions","PMD.JUnitAssertionsShouldIncludeMessage"})
public class ListUtilsTest {

   @Test
   public void filter() {

      assertFilter("bbb,a", Arrays.asList(" ", "", "bbb", null, "a"));
   }

   @Test
   public void filter_empty() {

      List<String> empty = Collections.emptyList();

      assertFilter("", empty);
   }

   @Test
   public void filter_null() {

      assertNull(ListUtils.filter(null));
   }

   private void assertFilter(String expected, List<String> actual) {

      List<String> filter = ListUtils.filter(actual);
      assertEquals(expected, StringUtils.join(filter, ","));
   }

}
