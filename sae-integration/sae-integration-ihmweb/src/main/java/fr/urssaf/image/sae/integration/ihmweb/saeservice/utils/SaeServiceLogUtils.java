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
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ObjetNumeriqueConsultationType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ObjetNumeriqueConsultationTypeChoice_type0;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.UrlConsultationDirecteType;
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
               listeMetas.add(code + "=" + valeur);
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
            log.appendLogLn(meta.getCode() + "=" + meta.getValeur());
         }
      }
   }

   /**
    * Ajoute, dans le log du résultat du test, un résultat de l'opération
    * "consultation"
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param consultResponse
    *           la réponse de l'opération "consultation"
    */
   public static void logResultatConsultation(ResultatTest resultatTest,
         ConsultationResponseType consultResponse) {

      // Vérifie le null
      if (consultResponse == null) {
         resultatTest.getLog().appendLogLn(
               "Le résultat de la consultation est null");
      } else {

         // L'objet numérique
         logObjetNumeriqueConsultation(resultatTest, consultResponse
               .getObjetNumerique());

         // Les métadonnées
         resultatTest.getLog().appendLogNewLine();
         resultatTest.getLog().appendLogLn(
               "Métadonnées associées à l'objet numérique :");
         logMetadonnees(resultatTest.getLog(), consultResponse.getMetadonnees());

      }

   }

   /**
    * Ajoute, dans le log du résultat du test, un objet numérique
    * 
    * @param resultatTest
    *           les résultats du test à mettre à jour
    * @param objNum
    *           l'objet numérique
    */
   public static void logObjetNumeriqueConsultation(ResultatTest resultatTest,
         ObjetNumeriqueConsultationType objNum) {

      ResultatTestLog log = resultatTest.getLog();

      if (objNum == null) {

         log.appendLogLn("L'objet numérique consultation est null");

      } else if (objNum.getObjetNumeriqueConsultationTypeChoice_type0() == null) {

         log.appendLogLn("L'objet numérique consultation est null");

      } else {

         ObjetNumeriqueConsultationTypeChoice_type0 typeIntermediaire = objNum
               .getObjetNumeriqueConsultationTypeChoice_type0();

         // URL de consultation directe ?
         UrlConsultationDirecteType urlConsult = typeIntermediaire.getUrl();
         if (urlConsult == null) {
            log
                  .appendLogLn("L'objet numérique consultation ne contient pas d'URL de consultation directe.");
         } else {
            log
                  .appendLogLn("L'objet numérique consultation contient une URL de consultation directe :");
            log.appendLogLn(urlConsult.getUrlConsultationDirecteType()
                  .toString());
         }

         // Contenu en base 64 ?
         DataHandler contenu = typeIntermediaire.getContenu();
         if (contenu == null) {

            log
                  .appendLogLn("L'objet numérique consultation ne contient pas de contenu en base 64.");

         } else {

            log
                  .appendLog("L'objet numérique consultation contient un contenu en base 64 : ");

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
            log.appendLogLn(" (SHA-1 = " + sha1 + ")");

         }

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

               Collections.sort(resultatsTries,
                     new ResultatRechercheComparator(triDesResultats));

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

               uuid = SaeServiceTypeUtils.extractUuid(resRechType
                     .getIdArchive());

               log.appendLogLn("IdArchive = " + uuid);

               logMetadonnees(log, resRechType.getMetadonnees());

               log.appendLogNewLine();

            }

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
      log.appendLogLn("Paramètres :");
      log.appendLogLn("URL ECDE :" + formulaire.getUrlEcde());
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
      log.appendLogLn("Paramètres :");
      log.appendLogLn("Id archivage : " + formulaire.getIdArchivage());
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
   public static void logAppelRecherche(ResultatTestLog log,
         RechercheFormulaire formulaire) {
      log.appendLogLn("Appel de l'opération recherche");
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
         ResultatTest resultatTest, ConsultationResponseType repConsult,
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
    * @param captUnitResponse
    *           la réponse de l'opération "archivageUnitaire"
    */
   public static void logResultatCaptureUnitaire(ResultatTest resultatTest,
         ArchivageUnitaireResponseType captUnitResponse) {

      // Initialise
      ResultatTestLog log = resultatTest.getLog();

      // Vérifie le null
      if (captUnitResponse == null) {
         log.appendLogLn("Le résultat de la capture unitaire est null");
      } else {

         // L'identifiant d'archivage
         String uuid = SaeServiceTypeUtils.extractUuid(captUnitResponse
               .getIdArchive());
         log.appendLog("Identifiant d'archivage : ");
         log.appendLog(uuid);
         log.appendLog(" (en minuscule pour Cassandra : ");
         log.appendLog(uuid.toLowerCase());
         log.appendLogLn(")");

      }

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

      log.appendLogLn(StringUtils.EMPTY);
      log.appendLogLn("contenu du fichier debut_traitement.flag");
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

}
