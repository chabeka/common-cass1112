package fr.urssaf.image.sae.lotinstallmaj.invocator;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.InvocationHandler;

public class InvocationHandlerImpl implements InvocationHandler {

   private final Object instance;

   /**
    * 
    */
   public InvocationHandlerImpl(final Object instance) {
      this.instance = instance;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object invoke(final Object arg0, final Method arg1, final Object[] arg2) throws Throwable {
      return null;
   }

}
