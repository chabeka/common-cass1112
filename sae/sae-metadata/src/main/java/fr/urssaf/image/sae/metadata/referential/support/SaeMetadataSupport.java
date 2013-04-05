package fr.urssaf.image.sae.metadata.referential.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.referential.dao.SaeMetadataDao;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Metadata"
 */
@Component
public class SaeMetadataSupport {

   
   private SaeMetadataDao saeMetadataDao;
   
   @Autowired
   public SaeMetadataSupport(SaeMetadataDao saeMetadataDao){
      
   }
   
   /**
    * Créé ou modifie la métadonné
    * @param metadata
    * @param clock
    */
   public void create(MetadataReference metadata, long clock){
      
   }
   
   /**
    * Retourne la liste des métadonnées
    * @return Liste des métadonnées
    */
   
   public List<MetadataReference> findAll(){
      return null;
   }
}
