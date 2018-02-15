/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagm;

/**
 * 
 * 
 */
public class SerializerTest {

   @Test
   public void testOnlyNotNullValuesPagm() throws UnsupportedEncodingException {
      Pagm pagm = new Pagm();
      pagm.setCode("leCode");
      ByteBuffer byteBuffer = PagmSerializer.get().toByteBuffer(pagm);
      String value = new String(byteBuffer.array(), "UTF-8");
      Assert.assertTrue(
            "le champ sérialisé ne doit pas contenir d'autre champ que code",
            value.split(",").length == 1);
      Assert.assertTrue("le champ sérialisé ne doit pas contenir pagmf", !value
            .contains("pagmf"));
   }

   @Test
   public void testAllValuesProfilFormat() throws UnsupportedEncodingException {
      FormatProfil formatProfil = new FormatProfil();
      formatProfil.setFileFormat("fmt");
      ByteBuffer byteBuffer = FormatProfilSerializer.get().toByteBuffer(
            formatProfil);
      String value = new String(byteBuffer.array(), "UTF-8");
      Assert.assertTrue("le champ sérialisé doit contenir tous les champs",
            value.split(",").length == 4);
      Assert.assertTrue(
            "le champ sérialisé doit contenir formatValidationMode", value
                  .contains("formatValidationMode"));
   }

}
