package fr.urssaf.image.sae.rnd.factory;

import java.util.ArrayList;
import java.util.List;

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
      String codeRnd = rndTypeDoc.get_reference();
      typeDoc.setCode(codeRnd);

      String tabRnd[] = codeRnd.split("\\.");

      // Code fonction et code activité
      // On ne récupère pas ceux fournis pas le WS car ils peuvent être null ou
      // incorrects (ex : code activité = 1.2 pour le code RND 1.2.A.X.X)

      // String fonction = rndTypeDoc.get_refFonction();
      String fonction = tabRnd[0];
      typeDoc.setCodeFonction(fonction);

      // String activite = rndTypeDoc.get_refActivite();
      String activite = tabRnd[1];
      // Si ce que l'on récupère est numérique alors cela correspond bien à
      // l'activité
      int taille = activite.length();
      boolean isNumerique = true;
      for (int i = 0; i < taille; i++) {
         if (!Character.isDigit(activite.charAt(i))) {
            isNumerique = false;
         }
      }
      if (isNumerique) {
         typeDoc.setCodeActivite(activite);
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
