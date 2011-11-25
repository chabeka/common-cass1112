package fr.urssaf.image.sae.webdemo.resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Classe de conversion des dates pour le format JSON<br>
 * La classe hérite de {@link JsonSerializer} <br>
 * Exemple de conversion d'un attribut d'une classe modèle:
 * 
 * <pre>
 * <code>@JsonSerialize(using = CustomDateSerializer.class)
 * public final Date getDate() {
 * ....
 * }
 * </code>
 * </pre>
 * 
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

   private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

   /**
    * Convertie la date au format <code>dd/MM/yyyy HH:mm:ss</code> <br>
    *{@inheritDoc}
    */
   @Override
   public final void serialize(Date value, JsonGenerator gen,
         SerializerProvider prov) throws IOException {

      SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale
            .getDefault());
      String formattedDate = formatter.format(value);

      gen.writeString(formattedDate);

   }

}
