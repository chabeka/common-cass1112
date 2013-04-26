package fr.urssaf.image.sae.rnd.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument;

/**
 * Classe de conversion
 * 
 * 
 */
public class ConvertFactory {

   /**
    * Transforme un objet de type RNDTypeDocument en TypeDocument
    * 
    * @param rndTypeDoc
    *           L'objet à transformer
    * @return l'objet transformé
    */
   public final TypeDocument wsToTypeDocument(RNDTypeDocument rndTypeDoc) {
      TypeDocument typeDoc = new TypeDocument();

      // Code RND
      typeDoc.setCode(rndTypeDoc.get_reference());

      // Code Fonction
      String fonction = rndTypeDoc.get_refFonction();
      typeDoc.setCodeFonction(fonction);

      // Code Activité
      // Si l'activité est nulle
      if (StringUtils.isBlank(rndTypeDoc.get_refActivite())) {
         char activite = typeDoc.toString().charAt(2);
         // le caractère à la 3ème position du code type est numérique alors il
         // correspond à l'activité
         if (Character.isDigit(activite)) {
            typeDoc.setCodeActivite(Character.toString(activite));
         }
      } else {
         typeDoc.setCodeActivite(rndTypeDoc.get_refActivite());
      }

      // Libellé du code RND
      typeDoc.setLibelle(rndTypeDoc.get_label());

      // Durée d'archivage et type de code RND
      // Documents archivables (correspond aux docs qui vont actuellement au
      // CNA)
      if (rndTypeDoc.get_dureeArchivage() > 0) {
         int dureeConservation = rndTypeDoc.get_dureeArchivage() * 365;
         typeDoc.setDureeConservation(dureeConservation);
         typeDoc.setType(TypeCode.ARCHIVABLE_AED);
      } else {
         // Documents non archivables (correspond aux docs qui vont actuellement
         // uniquement en GED)
         typeDoc.setDureeConservation(3 * 365);
         typeDoc.setType(TypeCode.NON_ARCHIVABLE_AED);
      }

      // Code RND cloturé
      typeDoc.setCloture(rndTypeDoc.is_etat());

      return typeDoc;
   }

   /**
    * Transforme une liste d’objet de type RNDTypeDocument en liste d’objet de
    * type TypeDocument
    * 
    * @param listeRndTypeDocs
    *           La liste à transformer
    * @return La liste transformée
    */
   public final List<TypeDocument> wsToDocumentsType(
         RNDTypeDocument listeRndTypeDocs[]) {
      List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
      for (RNDTypeDocument rndTypeDocument : listeRndTypeDocs) {
         TypeDocument typeDoc = wsToTypeDocument(rndTypeDocument);
         listeTypeDocs.add(typeDoc);
      }
      return listeTypeDocs;
   }

}
