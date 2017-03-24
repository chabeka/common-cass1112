package fr.urssaf.image.commons.spring.batch.service;

import org.easymock.EasyMock;

public final class ServiceMockFactory {

   private ServiceMockFactory() {

   }

   public static ProcessorService createProcessorService() {

      ProcessorService service = EasyMock.createMock(ProcessorService.class);

      return service;
   }

   public static ReaderService createReaderService() {

      ReaderService service = EasyMock.createMock(ReaderService.class);

      return service;
   }

   public static WriterService createWriterService() {

      WriterService service = EasyMock.createMock(WriterService.class);

      return service;
   }
}
