package fr.urssaf.image.sae.webdemo.resource;

import java.util.Date;

import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import fr.urssaf.image.commons.web.resource.DateConverter;
import fr.urssaf.image.commons.web.resource.DateFormatter;

/**
 * Classe de formatage des types pour l'ensemble de l'application
 * <ul>
 * <li>{@link Date} : <code>dd/MM/yyyy</code></li>
 * </ul>
 * Le paramètrage s'effectue dans le bean
 * 
 * <pre>
 * &lt;mvc:annotation-driven conversion-service="formattingField" />
 * &lt;bean id="formattingField" class="fr.urssaf.image.sae.webdemo.resource.FormattingField" />
 * </pre>
 * 
 */
public class FormattingField extends FormattingConversionServiceFactoryBean {

   private static final String DATE_FORMAT = "dd/MM/yyyy";

   @Override
   public final void afterPropertiesSet() {
      super.afterPropertiesSet();

      // formatage des dates en dd/MM/yyyy
      this.getObject().addFormatterForFieldType(Date.class,
            new DateFormatter(DATE_FORMAT));
      // conversion des dates en dd/MM/yyyy
      this.getObject().addConverter(new DateConverter(DATE_FORMAT));

   }
}
