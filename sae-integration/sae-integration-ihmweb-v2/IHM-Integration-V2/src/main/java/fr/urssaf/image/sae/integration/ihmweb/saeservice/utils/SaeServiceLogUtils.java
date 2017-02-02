package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;


import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.GetDocFormatOrigineResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.RecuperationMetadonneeResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeDispoType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeDispoType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;


/**
 * Méthodes utilitaires pour logger des éléments liés aux tests du service web
 * SaeService
 */
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods",
      "PMD.ExcessiveImports" })
public final class SaeServiceLogUtils {

   private SaeServiceLogUtils() {

   }

   /**
    * Ajoute, dans le log du résultat du test, une liste de métadonnées
    * 
    * @param log
    *           le log à mettre à jour
    * @param metadonnees
    *           les métadonnées à logguer
    */
   public static void logMetadonnees(ResultatTestLog log,
         ListeMetadonneeType metadonnees) {

      if (metadonnees == null) {
         log.appendLogLn("La liste des métadonnées est null");
      } else {

         MetadonneeType[] tabMetaTypes = metadonnees.getMetadonnee();

         if (tabMetaTypes == null) {
            log.appendLogLn("La liste des métadonnées est null");
         } else {

            log.appendLogLn("Nombre de métadonnées : " + tabMetaTypes.length);
            log.appendLogLn("Liste des métadonnées :");

            String code;
            String valeur;

            // for(MetadonneeType metaType: tabMetaTypes) {
            // code = metaType.getCode().getMetadonneeCodeType();
            // valeur = metaType.getValeur().getMetadonneeValeurType();
            // log.appendLogLn(code + "=" + valeur);
            // }

            List<String> listeMetas = new ArrayList<String>();
            for (MetadonneeType metaType : tabMetaTypes) {
               code = metaType.getCode().getMetadonneeCodeType();
               valeur = metaType.getValeur().getMetadonneeValeurType();
               //CMO : Retrait de la limite de taille des métadonnées
               listeMetas.add(code + "=" + valeur);
               //if (valeur.length() > 50) {
               //   listeMetas.add(code + "="
               //         + StringUtils.substring(valeur, 0, 50) + "...");
               //} else {
               //   listeMetas.add(code + "=" + valeur);
               //}

            }
            Collections.sort(listeMetas);
            for (String uneMeta : listeMetas) {
               log.appendLogLn(uneMeta);
            }

         }

      }

   }
   
   public static void logMetadonneesDispo(ResultatTestLog log,
         ListeMetadonneeDispoType metadonnees) {

      if (metadonnees == null) {
         log.appendLogLn("La liste des métadonnées est null");
      } else {

         MetadonneeDispoType[] tabMetaTypes = metadonnees.getMetadonnee();

         if (tabMetaTypes == null) {
            log.appendLogLn("La liste des métadonnées est null");
         } else {

            log.appendLogLn("Nombre de métadonnées : " + tabMetaTypes.length);
            log.appendLogLn("Liste des métadonnées :");

            String code;
            String valeur;

            // for(MetadonneeType metaType: tabMetaTypes) {
            // code = metaType.getCode().getMetadonneeCodeType();
            // valeur = metaType.getValeur().getMetadonneeValeurType();
            // log.appendLogLn(code + "=" + valeur);
            // }

            List<String> listeMetas = new ArrayList<String>();
            for (MetadonneeDispoType metaType : tabMetaTypes) {
               code = metaType.getCodeLong();
               valeur = metaType.getDescription();
               //CMO : Retrait de la limite de taille des métadonnées
               listeMetas.add(code + "=" + valeur);
               //if (valeur.length() > 50) {
               //   listeMetas.add(code + "="
               //         + StringUtils.substring(valeur, 0, 50) + "...");
               //} else {
               //   listeMetas.add(code + "=" + valeur);
               //}

            }
            Collections.sort(listeMetas);
            for (String uneMeta : listeMetas) {
               log.appendLogLn(uneMeta);
            }

         }

      }

   }

   /**
    * Ajoute, dans le log du résultat du test, une liste de métadonnées
    * 
    * @param log
    *           le log à mettre à jour
    * @param metadonnees
    *           les métadonnées à logguer
    */
   public static void logMetadonnees(ResultatTestLog log,
         MetadonneeValeurList metadonnees) {

      if (metadonnees == null) {
         log.appendLogLn("La liste des métadonnées est null");
      } else {
         log.appendLogLn("Nombre de métadonnées : " + metadonnees.size());
         log.appendLogLn("Liste :");
         for (MetadonneeValeur meta : metadonnees) {
            if (meta.getValeur().length() > 50) {
               log.appendLogLn(meta.getCode() + "="
                     + meta.getValeur().substring(0, 50) + "...");
            } else {
               log.appendLogLn(meta.getCode() + "=" + meta.getValeur());
            }
         }
      }
   }

   /**
    * Ajoute, dans le log du résultat du test, un résultat de l'opération
    * "consultation" ou "consultationMTOM" ou "consultationAffichable"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param resultat
    *           la réponse de l'opération "consultation"
    */
   public static void logResultatConsultation(ResultatTest resultatTest,
         ConsultationResultat resultat) {

      // Le contenu
      logConsultationDataHandler(resultatTest, resultat.getContenu());

      // Les métadonnées
      resultatTest.getLog().appendLogNewLine();
      resultatTest.getLog().appendLogLn("Métadonnées :");
      logMetadonnees(resultatTest.getLog(), resultat.getMetadonnees());

   }
   
   public static void logResultatRecuperationMetadonnee(ResultatTest resultatTest,
         RecuperationMetadonneeResultat resultat) {

      // Les métadonnées
      resultatTest.getLog().appendLogNewLine();
      resultatTest.getLog().appendLogLn("Métadonnées :");
      logMetadonneesDispo(resultatTest.getLog(), resultat.getMetadonnees());

   }

   /**
    * Ajoute, dans le log du résultat du test, un résultat de l'opération
    * "getDocFormatOrigine"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param resultat
    *           la réponse de l'opération "getDocFormatOrigine"
    */
   public static void logResultatGetDocFormatOrigine(ResultatTest resultatTest,
         GetDocFormatOrigineResultat resultat) {

      // Le contenu
      logGetDocFormatOrigineDataHandler(resultatTest, resultat.getContenu());

      // Les métadonnées
      resultatTest.getLog().appendLogNewLine();
      resultatTest.getLog().appendLogLn("Métadonnées :");
      logMetadonnees(resultatTest.getLog(), resultat.getMetadonnees());

   }


   private static void logConsultationDataHandler(ResultatTest resultatTest,
         DataHandler contenu) {

      ResultatTestLog log = resultatTest.getLog();
      if (contenu == null) {

         log.appendLogLn("Le contenu est null");

      } else {

         log.appendLog("Le contenu est renseigné : ");

         // Création d'un fichier temporaire
         File file;
         try {
            file = File.createTempFile("SAE_Integration_", null);
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }
         // LOG.debug("Création d'un fichier temporaire nommé " +
         // file.getAbsolutePath());
         OutputStream outputStream = null;
         try {
            outputStream = new FileOutputStream(file);
         } catch (FileNotFoundException e) {
            throw new IntegrationRuntimeException(e);
         }
         try {
            contenu.writeTo(outputStream);
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }

         // Ajout du lien de téléchargement dans le fichier résultat
         String nomFichierComplet = file.getAbsolutePath();
         String nomFichier = FilenameUtils.getName(nomFichierComplet);
         int idLien = resultatTest.getLiens().ajouteLien("objet numérique",
               "download.do?filename=" + nomFichier);

         // Ajout le log du lien
         log.appendLog("[Voir Lien n°" + idLien + "]");

         // Log du SHA-1
         String sha1 = null;
         try {
            sha1 = DigestUtils.shaHex(contenu.getInputStream());
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }
         log.appendLogLn("SHA-1 = " + sha1);

         // Type MIME
         log.appendLogLn("Type MIME = " + contenu.getContentType());

      }

   }


   private static void logGetDocFormatOrigineDataHandler(ResultatTest resultatTest,
         DataHandler contenu) {

      ResultatTestLog log = resultatTest.getLog();
      if (contenu == null) {

         log.appendLogLn("Le contenu est null");

      } else {

         log.appendLog("Le contenu est renseigné : ");

         // Création d'un fichier temporaire
         File file;
         try {
            file = File.createTempFile("SAE_Integration_", null);
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }
         // LOG.debug("Création d'un fichier temporaire nommé " +
         // file.getAbsolutePath());
         OutputStream outputStream = null;
         try {
            outputStream = new FileOutputStream(file);
         } catch (FileNotFoundException e) {
            throw new IntegrationRuntimeException(e);
         }
         try {
            contenu.writeTo(outputStream);
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }

         // Ajout du lien de téléchargement dans le fichier résultat
         String nomFichierComplet = file.getAbsolutePath();
         String nomFichier = FilenameUtils.getName(nomFichierComplet);
         int idLien = resultatTest.getLiens().ajouteLien("objet numérique",
               "download.do?filename=" + nomFichier);

         // Ajout le log du lien
         log.appendLog("[Voir Lien n°" + idLien + "]");

         // Log du SHA-1
         String sha1 = null;
         try {
            sha1 = DigestUtils.shaHex(contenu.getInputStream());
         } catch (IOException e) {
            throw new IntegrationRuntimeException(e);
         }
         log.appendLogLn("SHA-1 = " + sha1);

         // Type MIME
         log.appendLogLn("Type MIME = " + contenu.getContentType());

      }

   }
   
}
