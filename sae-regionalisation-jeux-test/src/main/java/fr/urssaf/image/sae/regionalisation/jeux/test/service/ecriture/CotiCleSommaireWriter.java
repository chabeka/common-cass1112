/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.jeux.test.service.ecriture;

import java.io.IOException;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 */
public class CotiCleSommaireWriter {

   public static void ecrireNoeud(FileWriterWithEncoding writer,
         String objNumCheminEtNomFichier, String hash, String denomination,
         String siren, boolean sirenAleatoire, int indiceDoc,
         String applicationTraitement, boolean avecNumeroRecours,
         String dateCreation, String numeroCompteExterne, String codeOrganisme)
         throws IOException {
      writer.write("      <somres:document>");
      writer.write("\r\n");

      // Objet numérique
      writer.write("         <somres:objetNumerique>");
      writer.write("\r\n");
      writer.write("            <somres:cheminEtNomDuFichier>"
            + objNumCheminEtNomFichier + "</somres:cheminEtNomDuFichier>");
      writer.write("\r\n");
      writer.write("         </somres:objetNumerique>");
      writer.write("\r\n");

      // Début métadonnées
      writer.write("         <somres:metadonnees>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>Titre</somres:code>");
      writer.write("\r\n");
      writer
            .write("               <somres:valeur>Attestation de vigilance</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>DateCreation</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>" + dateCreation
            + "</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer
            .write("               <somres:code>ApplicationProductrice</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>ADELAIDE</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer
            .write("               <somres:code>CodeOrganismeProprietaire</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>UR750</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer
            .write("               <somres:code>CodeOrganismeGestionnaire</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>" + codeOrganisme
            + "</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>CodeRND</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>2.3.1.1.12</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>Hash</somres:code>");
      writer.write("\r\n");
      writer
            .write("               <somres:valeur>" + hash + "</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>TypeHash</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>SHA-1</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>NbPages</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>2</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      writer.write("            <somres:metadonnee>");
      writer.write("\r\n");
      writer.write("               <somres:code>FormatFichier</somres:code>");
      writer.write("\r\n");
      writer.write("               <somres:valeur>fmt/354</somres:valeur>");
      writer.write("\r\n");
      writer.write("            </somres:metadonnee>");
      writer.write("\r\n");

      // Siren, en option
      // Peut être une valeur fixe, ou généré aléatoirement
      if (sirenAleatoire) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer.write("               <somres:code>Siren</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + buildSirenAleatoire()
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      } else if (StringUtils.isNotBlank(siren)) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer.write("               <somres:code>Siren</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + siren
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      }

      // ApplicationTraitement, en option
      if (StringUtils.isNotBlank(applicationTraitement)) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer
               .write("               <somres:code>ApplicationTraitement</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + applicationTraitement
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      }

      // Denomination, en option
      // Surtout utilisé pour les tests d'intégration interne
      if (StringUtils.isNotBlank(denomination)) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer.write("               <somres:code>Denomination</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + denomination
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      }

      // NumeroRecours, en option
      // Surtout utilisé pour les tests d'intégration interne
      if (avecNumeroRecours) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer
               .write("               <somres:code>NumeroRecours</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + indiceDoc
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      }

      if (StringUtils.isNotBlank(numeroCompteExterne)) {
         writer.write("            <somres:metadonnee>");
         writer.write("\r\n");
         writer
               .write("               <somres:code>NumeroCompteExterne</somres:code>");
         writer.write("\r\n");
         writer.write("               <somres:valeur>" + numeroCompteExterne
               + "</somres:valeur>");
         writer.write("\r\n");
         writer.write("            </somres:metadonnee>");
         writer.write("\r\n");
      }

      // Fin des métadonnées
      writer.write("         </somres:metadonnees>");
      writer.write("\r\n");

      // Fin du document
      writer
            .write("         <somres:numeroPageDebut>1</somres:numeroPageDebut>");
      writer.write("\r\n");
      writer.write("         <somres:nombreDePages>1</somres:nombreDePages>");
      writer.write("\r\n");
      writer.write("      </somres:document>");
      writer.write("\r\n");
   }

   private static String buildSirenAleatoire() {
      return RandomStringUtils.randomNumeric(10);
   }

}