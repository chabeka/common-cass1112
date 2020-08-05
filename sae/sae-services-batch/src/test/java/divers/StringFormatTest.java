package divers;

import org.junit.Assert;
import org.junit.Test;


public class StringFormatTest {
  /**
   * Test pour vérifier que %n remplace bien System.lineSeparator()
   * dans String.format
   */

  @Test
  public void testRC() {
    final String textA = String.format("%s%n%s%n", "test1", "test2");
    final String textB = "test1" + System.lineSeparator() +
        "test2" + System.lineSeparator();
    final String[] tabA = textA.split(System.lineSeparator());
    final String[] tabB = textB.split(System.lineSeparator());
    Assert.assertArrayEquals(tabA, tabB);
  }

}
