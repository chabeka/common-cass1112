package fr.urssaf.image.commons.junit.spring.service.impl;

import org.easymock.EasyMock;

import fr.urssaf.image.commons.junit.spring.service.Service2;

public final class ServiceManager {

   private ServiceManager() {

   }

   public static Service2 createService2() {

      return EasyMock.createMock(Service2.class);
   }

}
