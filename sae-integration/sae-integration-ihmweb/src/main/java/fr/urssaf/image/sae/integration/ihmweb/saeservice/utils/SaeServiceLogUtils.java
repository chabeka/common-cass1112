package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.AjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationAffichableFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.GetDocFormatOrigineFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheParIterateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RestoreMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.StockageUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.SuppressionMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureMasseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.GetDocFormatOrigineResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.IdentifiantPageType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheNbResResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheParIterateurResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RestoreMasseResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasseResponseType;
import fr.urssaf.image.sae.integration.ihmweb.utils.Base64Utils;

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
   
   /**
    * Ajoute, dans le log du résultat du test, une réponse de l'opération
    * "recherche"
    * 
    * @param log
    *           le log à mettre à jour
    * @param rechercheResponse
    *           la réponse de l'opération "recherche"
    * @param triDesResultats
    *           ordre de tri des résultats de recherche. Passer null pour ne pas
    *           trier
    */
   public static void logResultatRecherche(ResultatTestLog log,
         RechercheResponseType rechercheResponse,
         TypeComparaison triDesResultats) {

      // Le flag tronqué
      log.appendLogLn("Flag des résultats tronqués : "
            + Boolean.toString(rechercheResponse.getResultatTronque()));

      // Les résultats de la recherche
      if (rechercheResponse.getResultats() == null) {
         log.appendLogLn("Les résultats de recherche sont null");
      } else {

         ResultatRechercheType[] tabResRechTypes = rechercheResponse
               .getResultats().getResultat();

         // -- on log les résultat
         logRestultats(log, tabResRechTypes, triDesResultats);
      }
   }

   /**
    * Ajoute, dans le log du résultat du test, une réponse de l'opération
    * "recherche"
    * 
    * @param log
    *           le log à mettre à jour
    * @param rechercheResponse
    *           la réponse de l'opération "rechercheAvecNbRes"
    * @param triDesResultats
    *           ordre de tri des résultats de recherche. Passer null pour ne pas
    *           trier
    */
   public static void logResultatRechercheAvecNbRes(ResultatTestLog log,
         RechercheNbResResponseType rechercheResponse,
         TypeComparaison triDesResultats) {

      // Le flag tronqué
      log.appendLogLn("Flag des résultats tronqués : "
            + Boolean.toString(rechercheResponse.getResultatTronque()));

      // Les résultats de la recherche
      if (rechercheResponse.getResultats() == null) {
         log
               .appendLogLn("Les résultats de recherche avec nombre de résultats sont null");
      } else {
         ResultatRechercheType[] tabResRechTypes = rechercheResponse
               .getResultats().getResultat();

         // -- on log les résultat
         logRestultats(log, tabResRechTypes, triDesResultats);
      }
   }

   /**
    * Ajoute, dans le log du résultat du test, une réponse de l'opération
    * "rechercheParIterateur"
    * 
    * @param log
    *           le log à mettre à jour
    * @param rechercheResponse
    *           la réponse de l'opération "rechercheParIterateur"
    * @param triDesResultats
    *           ordre de tri des résultats de recherche. Passer null pour ne pas
    *           trier
    */
   public static void logResultatRechercheParIterateur(ResultatTestLog log,
         RechercheParIterateurResponseType rechercheResponse,
         TypeComparaison triDesResultats) {

      // Le flage dernière page
      log.appendLogLn("Flag dernière page : "
            + Boolean.toString(rechercheResponse.getDernierePage()));

      // L'identifiant de la page
      IdentifiantPageType idPage = rechercheResponse
            .getIdentifiantPageSuivante();
      if (idPage == null) {
         log.appendLogLn("L'identifiant de la page est null");
      } else {
         log.appendLogLn("Identifiant page : "
               + idPage.getIdArchive().toString() + "&"
               + idPage.getValeur().getMetadonneeValeurType());
      }

      // Les résultats de la recherche
      if (rechercheResponse.getResultats() == null) {
         log
               .appendLogLn("Les résultats de recherche par itérateur sont null");
      } else {
         ResultatRechercheType[] tabResRechTypes = rechercheResponse
               .getResultats().getResultat();

         // -- on log les résultat
         logRestultats(log, tabResRechTypes, triDesResultats);
      }
   }

   /**
    * Méthode utilitaire, factorisation code des méthodes logResultatRecherche
    * et logResultatRechercheAvecNbRes
    * 
    * @param log
    *           : le log à mettre à jour
    * 
    * @param tabResRechTypes
    *           : la réponse de l'opération "recherche"
    * 
    * @param triDesResultats
    *           : ordre de tri des résultats de recherche. Passer null pour ne
    *           pas trier
    */
   private static void logRestultats(ResultatTestLog log,
         ResultatRechercheType[] tabResRechTypes,
         TypeComparaison triDesResultats) {

      if (tabResRechTypes == null) {
         log.appendLogLn("Les résultats de recherche sont null");
      } else {

         log.appendLogLn("Nombre de résultats de recherche : "
               + tabResRechTypes.length);
         log.appendLogLn("Liste des résultats de recherche :");
         log.appendLogNewLine();

         // Tri des résultats de recherche si demandé
         ResultatRechercheType[] tabResRechTypesOk;
         if (triDesResultats != null) {

            List<ResultatRechercheType> resultatsTries = Arrays
                  .asList(tabResRechTypes);

            Collections.sort(resultatsTries, new ResultatRechercheComparator(
                  triDesResultats));

            tabResRechTypesOk = (ResultatRechercheType[]) resultatsTries
                  .toArray();

         } else {
            tabResRechTypesOk = tabResRechTypes;
         }

         String uuid;
         ResultatRechercheType resRechType;
         for (int i = 0; i < tabResRechTypesOk.length; i++) {

            log.appendLogLn("Résultat #" + (i + 1));
            resRechType = tabResRechTypesOk[i];
            uuid = SaeServiceTypeUtils.extractUuid(resRechType.getIdArchive());

            log.appendLogLn("IdArchive = " + uuid);
            logMetadonnees(log, resRechType.getMetadonnees());
            log.appendLogNewLine();
         }
      }
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "archivageUnitaire"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelArchivageUnitaire(ResultatTestLog log,
         CaptureUnitaireFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération archivageUnitaire");
      log.appendLogLn("Mode d'appel : " + formulaire.getModeCapture());
      log.appendLogLn("Paramètres :");
      log.appendLogLn("URL ECDE : " + formulaire.getUrlEcde());
      log.appendLogLn("Nom du fichier : " + formulaire.getNomFichier());
      log.appendLogLn("Métadonnées :");
      logMetadonnees(log, formulaire.getMetadonnees());
      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "stockageUnitaire"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelStockageUnitaire(ResultatTestLog log,
         StockageUnitaireFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération stockageUnitaire");
      log.appendLogLn("Mode d'appel : " + formulaire.getModeStockage());
      log.appendLogLn("Paramètres :");
      log.appendLogLn("URL ECDE : " + formulaire.getUrlEcde());
      log.appendLogLn("Nom du fichier : " + formulaire.getNomFichier());
      log.appendLogLn("Métadonnées :");
      logMetadonnees(log, formulaire.getMetadonnees());
      log.appendLogNewLine();
   }
   
   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "archivageMasse"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelArchivageMasse(ResultatTestLog log,
         CaptureMasseFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération archivageMasse");
      log.appendLogLn("Paramètres :");
      log.appendLogLn("URL du sommaire :" + formulaire.getUrlSommaire());
      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "consultation"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelConsultation(ResultatTestLog log,
         ConsultationFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération consultation");
      log.appendLogLn("Mode d'appel : " + formulaire.getModeConsult());
      log.appendLogLn("Paramètres :");
      log.appendLogLn("Id archivage : " + formulaire.getIdArchivage());
      log.appendLogLn("Métadonnées :");

      if (CollectionUtils.isEmpty(formulaire.getCodeMetadonnees())) {
         log.appendLogLn("non spécifiées");
      } else {
         log
               .appendLogLn(StringUtils.join(formulaire.getCodeMetadonnees(),
                     ','));
      }

      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "getDocFormatOrigine"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelGetDocFormatOrigine(ResultatTestLog log,
         GetDocFormatOrigineFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération getDocFormatOrigine");
      log.appendLogLn("Paramètres :");
      log.appendLogLn("Id archivage : " + formulaire.getIdArchivage());
//      log.appendLogLn("Métadonnées :");
//      
//      if (CollectionUtils.isEmpty(formulaire.getCodeMetadonnees())) {
//         log.appendLogLn("non spécifiées");
//      } else {
//         log
//               .appendLogLn(StringUtils.join(formulaire.getCodeMetadonnees(),
//                     ','));
//      }

      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "consultationAffichable"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelConsultationAffichable(ResultatTestLog log,
         ConsultationAffichableFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération consultationAffichable");
      log.appendLogLn("Paramètres : ");
      log.appendLogLn("Id archivage : " + formulaire.getIdArchivage());
      log.appendLogLn("Métadonnées : ");

      if (CollectionUtils.isEmpty(formulaire.getCodeMetadonnees())) {
         log.appendLogLn("non spécifiées");
      } else {
         log
               .appendLogLn(StringUtils.join(formulaire.getCodeMetadonnees(),
                     ','));
      }
      log.appendLogLn("Numero page : " + formulaire.getNumeroPage());
      log.appendLogLn("Nombre de pages : " + formulaire.getNombrePages());

      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "recherche"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelRechercheSimple(ResultatTestLog log,
         RechercheFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération recherche");
      logAppelRecherche(log, formulaire);
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "recherche avec nombre de résultats"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelRechercheAvecNbRes(ResultatTestLog log,
         RechercheFormulaire formulaire) {
      log
            .appendLogLn("Appel de l'opération recherche avec nombre de résultats");
      logAppelRecherche(log, formulaire);
   }

   /**
    * Méthode factrisant le code des méthodes : logAppelRechercheSimple &
    * logAppelRechercheAvecNbRes
    * 
    * @param log
    *           : le log
    * @param formulaire
    *           : l'objet formulaire contenant les propriétés d'appel
    */
   private static void logAppelRecherche(ResultatTestLog log,
         RechercheFormulaire formulaire) {
      log.appendLogLn("Paramètres :");
      log.appendLogLn("Requête LUCENE : " + formulaire.getRequeteLucene());
      log.appendLog("Codes des métadonnées : ");
      if (CollectionUtils.isEmpty(formulaire.getCodeMetadonnees())) {
         log.appendLogLn("non spécifiés");
      } else {
         log
               .appendLogLn(StringUtils.join(formulaire.getCodeMetadonnees(),
                     ','));
      }
      log.appendLogNewLine();
   }

   /**
    * Log recherche par itérateur
    * 
    * @param log
    *           : le log
    * @param formulaire
    *           : l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelRechercheParIterateur(ResultatTestLog log,
         RechercheParIterateurFormulaire formulaire) {
      log.appendLogLn("Paramètres :");
      log.appendLogLn("Meta(s) fixe(s) : ");
      if (CollectionUtils.isEmpty(formulaire.getMetaFixes())) {
         log.appendLogLn("non spécifiés");
      } else {
         log.appendLogLn(formulaire.getMetaFixes().toString());
      }

      log.appendLogLn("Meta variable : ");
      log.appendLogLn(formulaire.getMetaVariable().toString());

      log.appendLogLn("Filtre de type equal : "
            + formulaire.getEqualFilter().toString());
      log.appendLogLn("Filtre de type not equal : "
            + formulaire.getNotEqualFilter().toString());

      log.appendLogLn("Filtre de type range : "
            + formulaire.getRangeFilter().toString());
      log.appendLogLn("Nb docs par page : " + formulaire.getNbDocParPage());
      log.appendLogLn("Identifiant page : "
            + formulaire.getIdPage().getIdArchive() + "&"
            + formulaire.getIdPage().getValeur());

      log.appendLog("Codes des métadonnées : ");
      if (CollectionUtils.isEmpty(formulaire.getCodeMetadonnees())) {
         log.appendLogLn("non spécifiés");
      } else {
         log
               .appendLogLn(StringUtils.join(formulaire.getCodeMetadonnees(),
                     ','));
      }
      log.appendLogNewLine();
   }

   /**
    * Log le fait que l'on ait obtenue une archive en résultat de l'opération
    * "consultation" alors qu'on s'attendait à récupérer une SoapFault
    * 
    * @param resultatTest
    *           le résultat du test
    * @param repConsult
    *           la réponse de l'opération "consultation"
    * @param faultAttendue
    *           la SoapFault attendue
    * @param argsMsgSoapFault
    *           les arguments pour le String.format du message de la SoapFault
    *           attendue
    */
   public static void logConsultationReponseAuLieuDeSoapFault(
         ResultatTest resultatTest, ConsultationResultat repConsult,
         SoapFault faultAttendue, Object[] argsMsgSoapFault) {

      ResultatTestLog log = resultatTest.getLog();

      log.appendLog("On s'attendait à recevoir une SoapFault ");
      log.appendLog(faultAttendue.codeToString());
      log.appendLog(" avec le message \"");
      log
            .appendLog(String.format(faultAttendue.getMessage(),
                  argsMsgSoapFault));
      log.appendLogLn("\".");

      log.appendLogLn("A la place, on a obtenu l'archive suivante : ");
      log.appendLogNewLine();
      SaeServiceLogUtils.logResultatConsultation(resultatTest, repConsult);

   }

   /**
    * Ajoute, dans le log du résultat du test, la réponse de l'opération
    * "archivageUnitaire"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param idArchivage
    *           l'identifiant unique d'archivage renvoyé par la capture unitaire
    */
   public static void logResultatCaptureUnitaire(ResultatTest resultatTest,
         String idArchivage) {

      // Initialise
      ResultatTestLog log = resultatTest.getLog();

      // L'identifiant d'archivage
      log.appendLog("Identifiant d'archivage : ");
      log.appendLog(idArchivage);
      log.appendLog(" (en minuscule pour Cassandra : ");
      log.appendLog(StringUtils.lowerCase(idArchivage));
      log.appendLogLn(")");

   }


   /**
    * Ajoute, dans le log du résultat du test, la réponse de l'opération
    * "stockageUnitaire"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param idArchivage
    *           l'identifiant unique d'archivage renvoyé par le stockage unitaire
    */
   public static void logResultatStockageUnitaire(ResultatTest resultatTest,
         String idArchivage) {

      // Initialise
      ResultatTestLog log = resultatTest.getLog();

      // L'identifiant d'archivage
      log.appendLog("Identifiant d'archivage : ");
      log.appendLog(idArchivage);
      log.appendLog(" (en minuscule pour Cassandra : ");
      log.appendLog(StringUtils.lowerCase(idArchivage));
      log.appendLogLn(")");

   }
   
   /**
    * Log le résumé du contenu d'un fichier resultats.xml
    * 
    * @param resultatTest
    *           l'objet contenant le résultat du test. On en a besoin pour
    *           ajouter un lien de téléchargement vers le fichier resultats.xml
    * @param objResultatXml
    *           l'objet représentant le fichier resultats.xml
    * @param cheminFichierResultatsXml
    *           le chemin complet du fichier resultats.xml
    */
   public static void logResultatsXml(ResultatTest resultatTest,
         ResultatsType objResultatXml, String cheminFichierResultatsXml,
         String cheminFichierDebutFlag) {

      // Ajoute un lien de téléchargement du fichier
      String cheminBase64 = Base64Utils.encode(cheminFichierResultatsXml);
      resultatTest.getLiens().ajouteLien(
            SaeIntegrationConstantes.NOM_FIC_RESULTATS,
            "download.do?ecdefilename=" + cheminBase64);

      // Log les compteurs
      ResultatTestLog log = resultatTest.getLog();
      log.appendLogLn("Résumé du contenu du fichier resultats.xml :");
      log.appendLog("Nombre de documents envoyés en capture : ");
      log
            .appendLogLn(String.valueOf(objResultatXml
                  .getInitialDocumentsCount()));
      log.appendLog("Nombre de documents capturés : ");
      log.appendLogLn(String.valueOf(objResultatXml
            .getIntegratedDocumentsCount()));
      log.appendLog("Nombre de documents non capturés : ");
      log.appendLogLn(String.valueOf(objResultatXml
            .getNonIntegratedDocumentsCount()));
      log.appendLog("Nombre de documents virtuels envoyés en capture : ");
      log.appendLogLn(String.valueOf(objResultatXml
            .getInitialVirtualDocumentsCount()));
      log.appendLog("Nombre de documents virtuels capturés : ");
      log.appendLogLn(String.valueOf(objResultatXml
            .getIntegratedVirtualDocumentsCount()));
      log.appendLog("Nombre de documents virtuels non capturés : ");
      log.appendLogLn(String.valueOf(objResultatXml
            .getNonIntegratedVirtualDocumentsCount()));

      // Regarde si le fichier resultats.xml contient les id d'archivage
      log.appendLog("Présence des identifiants d'archivage : ");
      if ((objResultatXml.getIntegratedDocuments() != null)
            && (!CollectionUtils.isEmpty(objResultatXml
                  .getIntegratedDocuments().getIntegratedDocument()))) {
         log.appendLogLn("OUI");
      } else {
         log.appendLogLn("NON");
      }

      // Lecture du fichier debut_traitement.flag
      log.appendLogLn(StringUtils.EMPTY);
      log.appendLogLn("Contenu du fichier debut_traitement.flag");
      File file = new File(cheminFichierDebutFlag);
      try {
         List<String> lines = FileUtils.readLines(file);
         for (String line : lines) {
            log.appendLogLn(line);
         }
      } catch (IOException e) {
         log.appendLogLn("impossible de lire le fichier");
      }

   }

   /**
    * Ajoute, dans le log du résultat du test, un résultat de l'opération
    * "archivageMasse" ou "archivageMasseAvecHash"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param resultat
    *           la réponse de l'opération "consultation"
    */
   public static void logResultatCaptureMasse(ResultatTest resultatTest,
         CaptureMasseResultat resultat) {

      ResultatTestLog log = resultatTest.getLog();

      if (resultat.isAppelAvecHashSommaire()) {
         log.appendLogLn("Identifiant du traitement : "
               + resultat.getIdTraitement());
      } else {
         log.appendLogLn("Le service n'a pas renvoyé d'erreur");
      }

   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "modification"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelModification(ResultatTestLog log,
         ModificationFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération modification");
      log.appendLogLn("Id du document : " + formulaire.getIdDocument());
      log.appendLogLn("Métadonnées :");
      logMetadonnees(log, formulaire.getMetadonnees());
      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "suppression"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelSuppression(ResultatTestLog log,
         SuppressionFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération suppression");
      log.appendLogLn("Id du document : " + formulaire.getIdDocument());
      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "transfert"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelTransfert(ResultatTestLog log,
         TransfertFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération transfert");
      log.appendLogLn("Id du document : " + formulaire.getIdDocument());
      log.appendLogNewLine();
   }
   
   /**
    * Ajoute, dans le log du résultat du test, les paramètres d'appel à
    * l'opération "ajoutNote"
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */
   public static void logAppelAjoutNote(ResultatTestLog log, AjoutNoteFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération ajoutNote");
      log.appendLogLn("Id du document : " + formulaire.getIdArchivage());
      log.appendLogNewLine();
   }

   /**
    * Ajoute, dans le log de l'appel au test
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */

   public static void logAppelSuppressionMasseSimple(ResultatTestLog log,
         SuppressionMasseFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération SuppressionMasse");
            
   }
   
   /**
    * Ajoute, dans le log du résultat du test cf l'id du job
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */

   public static void logResultatSuppressionMasse(ResultatTestLog log,
         SuppressionMasseResponseType suppressionMasseResponse) {
      log.appendLogLn("Stockage dans la pile de l'appel de l'opération SuppressionMasse");      
      log.appendLogNewLine();
   }


   /**
    * Ajoute, dans le log de l'appel au test
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */

   public static void logAppelRestoreMasseSimple(ResultatTestLog log,
         RestoreMasseFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération RestoreMasse");
            
   }
   
   /**
    * Ajoute, dans le log du résultat du test 
    * 
    * @param log
    *           le log
    * @param formulaire
    *           l'objet formulaire contenant les propriétés d'appel
    */

   public static void logResultatRestoreMasse(ResultatTestLog log,
         RestoreMasseResponseType RestoreMasseResponse) {
      log.appendLogLn("Restore de l'opération SuppressionMasse dont l'UUID a été spécifié");      
      log.appendLogLn("Consulter la pile pour voir le résultat (succès et nombre de documents restorés)");
      log.appendLogNewLine();
   }
   

}
