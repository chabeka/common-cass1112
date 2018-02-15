package fr.urssaf.image.commons.ioc.spring.exemple.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-annotation.xml")
@SuppressWarnings("PMD.LongVariable")
public class MethodeAnnotationServiceTest {

	private MethodeService attributMethodeService;

	@Autowired
	public void setAttributMethodeService(
			@Qualifier("attribut") MethodeService attributMethodeService) {
		this.attributMethodeService = attributMethodeService;
	}

	@Test
	public void byAnnotationAttribut() {

		assertEquals(
		      "Test par annotation attribut",
		      "ExempleByAnnotationAttributMethodeServiceImpl",
				attributMethodeService.methode());

	}

	private MethodeService setterMethodeService;

	@Autowired
	public void setSetterMethodeService(
			@Qualifier("setter") MethodeService setterMethodeService) {
		this.setterMethodeService = setterMethodeService;
	}

	@Test
	public void byAnnotationSetter() {

		assertEquals(
		      "Test par annotation setter",
		      "ExempleByAnnotationSetterMethodeServiceImpl",
				setterMethodeService.methode());

	}
	
	private MethodeService constructeurMethodeService;

	@Autowired
	public void setConstructeurMethodeService(
			@Qualifier("constructeur") MethodeService constructeurMethodeService) {
		this.constructeurMethodeService = constructeurMethodeService;
	}

	@Test
	public void byAnnotationConstructeur() {

		assertEquals(
		      "Test par annotation constructeur",
		      "ExempleByAnnotationConstructeurMethodeServiceImpl",
				constructeurMethodeService.methode());

	}
}
