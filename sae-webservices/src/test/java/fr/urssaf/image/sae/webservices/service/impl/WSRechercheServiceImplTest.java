/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.cirtil.www.saeservice.MetadonneeCodeType;

/**
 * 
 * 
 */
public class WSRechercheServiceImplTest {

   @Test
   public void testListeNullMDDesired_success() {
      List<String> values = WSRechercheServiceImpl.recupererListMDDesired(null);

      Assert.assertNotNull("la liste retournée est non nulle", values);
      Assert.assertEquals("la liste est vide", 0, values.size());
   }
   
   @Test
   public void testListeVideMDDesired_success() {
      List<String> values = WSRechercheServiceImpl.recupererListMDDesired(new MetadonneeCodeType[]{});

      Assert.assertNotNull("la liste retournée est non nulle", values);
      Assert.assertEquals("la liste est vide", 0, values.size());
   }

}
