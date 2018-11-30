package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.LienHttp;
import fr.urssaf.image.sae.integration.ihmweb.modele.LienHttpList;
import fr.urssaf.image.sae.integration.ihmweb.utils.Base64Utils;


/**
 * Formatage d'un objet de type LienHttpList pour le passage d'une classe
 * de formulaire à un contrôleur.<br>
 * <br>
 * On utilise la représentation en chaîne de caractères suivante:<br>
 * <br>
 * lien1_texte[SEP1]lien1_url[SEP2]lien2_texte[SEP1]lien2_url<br>
 * <br>
 * [SEP1] est le séparateur entre le texte et l'url<br>
 * [SEP2] est le séparateur entre les liens<br>
 * <br>
 * De plus, on encode le tout en base64
 */
public class LienHttpListEditor extends PropertyEditorSupport {

   
   private static final String SEPAR_PROPS = "[SEP1]";
   private static final String SEPAR_ITEMS = "[SEP2]";
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final void setAsText(String text) {

      LienHttpList liens = new LienHttpList();
      
      
      if (StringUtils.isNotBlank(text)) {
         
         // Décode la base 64
//         byte[] bytes = Base64.decodeBase64(text);
//         String text2 = org.apache.commons.codec.binary.StringUtils.newStringUtf8(bytes); 
         String text2 = Base64Utils.decode(text);
         
         // Eclate à l'aide des séparateurs
         text2 = StringUtils.trim(text2);
         String[] lesLiens = StringUtils.splitByWholeSeparator(text2,SEPAR_ITEMS);
         String[] parties ;
         if (ArrayUtils.isNotEmpty(lesLiens)) {
            for(String lienAsString: lesLiens) {
               parties = StringUtils.splitByWholeSeparator(lienAsString,SEPAR_PROPS);
               liens.add(new LienHttp(parties[0], parties[1]));
            }
         }
            
      }
      
      setValue(liens);
      
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      LienHttpList liens = (LienHttpList)getValue();
      
      String resultAsString = StringUtils.EMPTY;
      
      if (CollectionUtils.isNotEmpty(liens)) {
         
         StringBuilder sBuilder = new StringBuilder();
         
         for(LienHttp lien: liens) {

            sBuilder.append(lien.getTexte());
            sBuilder.append(SEPAR_PROPS);
            sBuilder.append(lien.getUrl());
            sBuilder.append(SEPAR_ITEMS);
            
         }
         
         resultAsString = sBuilder.toString();
         
         resultAsString = StringUtils.removeEnd(resultAsString, SEPAR_ITEMS);
         
         // Conversion en base64
//         byte[] bytes = org.apache.commons.codec.binary.StringUtils.getBytesUtf8(resultAsString);
//         byte[] base64 = Base64.encodeBase64(bytes, false);
//         resultAsString = org.apache.commons.codec.binary.StringUtils.newStringUtf8(base64);
         resultAsString = Base64Utils.encode(resultAsString);
         
      }
      
      return resultAsString;
      
   }

   
}
