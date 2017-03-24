package fr.urssaf.image.commons.controller.spring3.jndi.exemple.service;

import org.springframework.stereotype.Component;

@Component
public class ParametersComponent {

   private static String title = "No initialized title";

   public void setTitle(String title) {
      ParametersComponent.title = title;
   }

   public static String getTitle() {
      return title;
   }
}
