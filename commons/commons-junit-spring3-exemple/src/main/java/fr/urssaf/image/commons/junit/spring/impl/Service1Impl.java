package fr.urssaf.image.commons.junit.spring.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.junit.spring.service.Service1;
import fr.urssaf.image.commons.junit.spring.service.Service2;

@Service
public class Service1Impl implements Service1 {

   @Autowired
   private Service2 service2;

   @Override
   public String service(String arg) {

      return service2.service(arg);

   }

}
