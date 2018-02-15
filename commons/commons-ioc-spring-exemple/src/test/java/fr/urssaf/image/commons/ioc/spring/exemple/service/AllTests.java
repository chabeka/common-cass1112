package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

;

@RunWith(Suite.class)
@SuiteClasses( { MethodeAnnotationServiceTest.class, MethodeServiceTest.class})
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class AllTests {

}
