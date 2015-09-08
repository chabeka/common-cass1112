package fr.urssaf.image.sae.vi.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import fr.urssaf.image.sae.saml.util.ListUtils;

@SuppressWarnings("PMD.MethodNamingConventions")
public class FilterUtilsTest {

   @Test
   public void filter() {

      assertFilter("bbb,a", Arrays.asList(null, null, "bbb", null, "a"));
   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void filter_null() {

      assertNull(ListUtils.filter(null));
   }

   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   private void assertFilter(String expected, Collection<String> actual) {

      Collection<String> filter = FilterUtils.filter(actual);
      assertEquals(expected, StringUtils.join(filter, ","));
   }
}
