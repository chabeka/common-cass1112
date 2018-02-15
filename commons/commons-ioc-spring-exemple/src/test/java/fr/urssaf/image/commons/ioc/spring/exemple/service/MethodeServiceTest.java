package fr.urssaf.image.commons.ioc.spring.exemple.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class MethodeServiceTest {

	@Test
	public void byConstructeur() {

		assertEquals(
		      "Test par constructeur",
		      "ExempleByConstructeurMethodeServiceImpl",
		      ((MethodeService) (new XmlBeanFactory(new ClassPathResource(
		            "applicationContext.xml"))).getBean("byConstructeur")).methode());

	}
	
	@Test
	public void bySetter() {

		assertEquals(
		      "Test par setter",
		      "ExempleBySetterMethodeServiceImpl",
		      ((MethodeService) (new XmlBeanFactory(new ClassPathResource(
		            "applicationContext.xml"))).getBean("bySetter")).methode());

	}
	
}
