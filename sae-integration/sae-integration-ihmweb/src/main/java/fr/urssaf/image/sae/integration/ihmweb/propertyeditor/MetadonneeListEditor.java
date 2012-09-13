package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;


/**
 * Formatage d'un objet de type MetadonneeList pour le passage d'une classe
 * de formulaire à un contrôleur.<br>
 * <br>
 * On s'attend à récupérer du formulaire une chaîne du type :<br>
 * <br>
 * CodeMetadonnee1=ValeurMetadonnee1<br>
 * CodeMetadonnee2=ValeurMetadonnee2<br>
 * <br>
 * Et il faut faire le transtypage en un objet MetadonneeList
 */
public class MetadonneeListEditor extends PropertyEditorSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      MetadonneeValeurList listeMeta = new MetadonneeValeurList();

      if (StringUtils.isNotBlank(text)) {
      
         String[] pairesCleValeur = PropertyEditorUtils.eclateSurRetourCharriot(text);
         
         if (ArrayUtils.isNotEmpty(pairesCleValeur)) {
            for(String cleValeur: pairesCleValeur) {
               addMeta(listeMeta,cleValeur);
            }
         }
         
      } 
      
      setValue(listeMeta);

   }
   
   
   private void addMeta(
         MetadonneeValeurList listeMeta,
         String cleValeur) {
      
      
      // NB : ne pas trimmer, car on peut vouloir laisser des espaces
      // String cleValeurOk = StringUtils.trim(cleValeur);
      String cleValeurOk = cleValeur;
      String[] cleValeurSplit = null;
      
      if(cleValeurOk.contains("JetonDePreuve")){
         cleValeurSplit = cleValeurOk.split("=",2);
      }else {
         cleValeurSplit = StringUtils.split(cleValeurOk,"=");   
      }
      
      
      if (cleValeurSplit.length>2) {
         throw new IntegrationRuntimeException("La syntaxe de métadonnée suivante est incorrecte : " + cleValeur);
      }
      String code = cleValeurSplit[0];
//      String valeur = cleValeurSplit[1];
      String valeur;
      if (cleValeurSplit.length>1) {
         valeur = cleValeurSplit[1];
      } else {
         valeur = StringUtils.EMPTY;
      }
      
      listeMeta.add(new MetadonneeValeur(code,valeur));
      
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      MetadonneeValeurList metadonnees = (MetadonneeValeurList)getValue();
      
      StringBuilder result = new StringBuilder();
      
      if (CollectionUtils.isNotEmpty(metadonnees)) {
         for (MetadonneeValeur metadonnee:metadonnees) {
            result.append(metadonnee.getCode() + "=" + metadonnee.getValeur());
            result.append("\r");
         }
      }
      
      return result.toString();
      
   }

   
}
