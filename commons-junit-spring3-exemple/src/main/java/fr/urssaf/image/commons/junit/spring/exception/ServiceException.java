package fr.urssaf.image.commons.junit.spring.exception;

public class ServiceException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public ServiceException() {
      super("une exception est levée");
   }
}
