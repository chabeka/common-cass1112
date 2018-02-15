package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/applicationContext-security-basic.xml",
      "/security/applicationContext-security-aspect.xml" })
public class FormServiceImplAspectTest extends AbstractTestFormServiceImpl {

}
