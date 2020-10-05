package fr.urssaf.image.sae.storage.dfce.utils;



import org.junit.Assert;
import org.junit.Test;

public class IntegerUtilsTest {


  @Test
  public void testIntSuccess() {
    Assert.assertTrue(IntegerUtils.tryParse("10"));
    Assert.assertTrue(IntegerUtils.tryParse("1000000000"));
  }

  @Test
  public void testIntFail() {
    Assert.assertFalse(IntegerUtils.tryParse("1A"));
    Assert.assertFalse(IntegerUtils.tryParse("12.2"));
    Assert.assertFalse(IntegerUtils.tryParse("Pas entier"));
  }

}
