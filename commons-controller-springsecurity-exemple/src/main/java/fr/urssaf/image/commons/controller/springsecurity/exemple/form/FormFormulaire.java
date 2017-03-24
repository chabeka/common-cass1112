package fr.urssaf.image.commons.controller.springsecurity.exemple.form;

import org.hibernate.validator.constraints.NotEmpty;

public class FormFormulaire {

   private String title;

   private String text;

   public void setTitle(String title) {
      this.title = title;
   }

   @NotEmpty
   public String getTitle() {
      return title;
   }

   public void setText(String text) {
      this.text = text;
   }

   @NotEmpty
   public String getText() {
      return text;
   }

}
