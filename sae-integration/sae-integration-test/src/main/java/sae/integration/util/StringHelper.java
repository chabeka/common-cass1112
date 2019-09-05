package sae.integration.util;

import org.slf4j.helpers.MessageFormatter;

public class StringHelper {

   private StringHelper() {
      // Classe statique
   }

   public static String format(final String msg, final Object... objs) {
      return MessageFormatter.arrayFormat(msg, objs).getMessage();
   }
}
