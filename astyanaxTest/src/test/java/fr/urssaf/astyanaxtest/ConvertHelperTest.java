package fr.urssaf.astyanaxtest;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class ConvertHelperTest {

	@Test
	public void test() {
		String value = "toto&TITI&tété&";
		String newValue =  ConvertHelper.normalizeMetaValue(value);
		System.out.println("newValue=" + newValue);
		Assert.assertEquals("toto&titi&tete&", newValue);
	}
}
