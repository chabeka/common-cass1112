package fr.urssaf.image.sae.regionalisation.dao;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;

/**
 * Service contenant les opérations concernant les documents SAE
 * 
 * 
 */
public interface SaeDocumentDao {

   /**
    * Renvoie la base sur laquelle on travaille.<br>
    * Cette base est nécessaire pour l'appel à la méthode
    * {@link SaeDocumentDao#find(Base, UUID)}
    * 
    * @return la base sur laquelle on travaille
    */
   Base getBase();

   /**
    * Renvoie le document dont l'identifiant est passé en argument
    * 
    * @param base
    *           la base sur laquelle travailler
    * @param idDoc
    *           l'identifiant unique du document
    * @return l'objet Document
    */
   Document find(Base base, UUID idDoc);

   /**
    * Mise à jour via DFCE du document passé en paramètre.
    * 
    * @param document
    *           Document à mettre à jour
    */
   void update(Document document);

}
