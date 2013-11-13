package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseAvecHash;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseAvecHashRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJ;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJRequestTypeChoice_type0;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.Consultation;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOM;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOMRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.DataFileType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlSommaireType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeValeurType;
import sae.client.demo.webservice.modele.SaeServiceStub.Modification;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.Recherche;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.RequeteRechercheType;
import sae.client.demo.webservice.modele.SaeServiceStub.Suppression;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.UuidType;

/**
 * Construction d'objets du modèle Axis2
 */
public final class Axis2ObjectFactory {

   private Axis2ObjectFactory() {
      // constructeur privé
   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param urlEcdeFichier
    *           l'URL ECDE du fichier à archiver
    * @param metadonnees
    *           les métadonnées à associer au fichier
    * @return le paramètre d'entrée de l'opération "archivageUnitaire"
    */
   public static ArchivageUnitaire contruitParamsEntreeArchivageUnitaire(
         String urlEcdeFichier, Map<String, String> metadonnees) {

      ArchivageUnitaire archivageUnitaire = new ArchivageUnitaire();

      ArchivageUnitaireRequestType archivageUnitaireRequest = new ArchivageUnitaireRequestType();

      archivageUnitaire.setArchivageUnitaire(archivageUnitaireRequest);

      // URL ECDE
      EcdeUrlType ecdeUrl = buildEcdeUrl(urlEcdeFichier);
      archivageUnitaireRequest.setEcdeUrl(ecdeUrl);

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      archivageUnitaireRequest.setMetadonnees(listeMetadonnee);

      // Renvoie du paramètre d'entrée de l'opération archivageUnitaire
      return archivageUnitaire;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @return le paramètre d'entrée de l'opération "consultation"
    */
   public static Consultation contruitParamsEntreeConsultation(String idArchive) {

      return contruitParamsEntreeConsultation(idArchive, null);

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @return le paramètre d'entrée de l'opération "consultationMTOM"
    */
   public static ConsultationMTOM contruitParamsEntreeConsultationMTOM(
         String idArchive) {

      return contruitParamsEntreeConsultationMTOM(idArchive, null);

   }

   private static UuidType buildUuid(String uuid) {
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);
      return uuidType;
   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @param codesMetasSouhaites
    *           la liste des métadonnées que l'on souhaite en retour du service
    *           web
    * @return le paramètre d'entrée de l'opération "consultation"
    */
   public static Consultation contruitParamsEntreeConsultation(
         String idArchive, List<String> codesMetasSouhaites) {

      Consultation consultation = new Consultation();

      ConsultationRequestType consultationRequest = new ConsultationRequestType();

      consultation.setConsultation(consultationRequest);

      // L'identifiant unique de l'archivage
      consultationRequest.setIdArchive(buildUuid(idArchive));

      // Les codes des métadonnées souhaitées
      if ((codesMetasSouhaites != null) && (!codesMetasSouhaites.isEmpty())) {

         MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
               .size()];

         MetadonneeCodeType metadonneeCode;
         for (int i = 0; i < codesMetasSouhaites.size(); i++) {
            metadonneeCode = new MetadonneeCodeType();
            metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
            arrMetadonneeCode[i] = metadonneeCode;
         }

         ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
         consultationRequest.setMetadonnees(listeMetadonneeCode);
         listeMetadonneeCode.setMetadonneeCode(arrMetadonneeCode);

      }

      // Renvoie du paramètre d'entrée de l'opération consultation
      return consultation;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @param codesMetasSouhaites
    *           la liste des métadonnées que l'on souhaite en retour du service
    *           web
    * @return le paramètre d'entrée de l'opération "consultationMTOM"
    */
   public static ConsultationMTOM contruitParamsEntreeConsultationMTOM(
         String idArchive, List<String> codesMetasSouhaites) {

      ConsultationMTOM consultation = new ConsultationMTOM();

      ConsultationMTOMRequestType consultationRequest = new ConsultationMTOMRequestType();

      consultation.setConsultationMTOM(consultationRequest);

      // L'identifiant unique de l'archive
      consultationRequest.setIdArchive(buildUuid(idArchive));

      // Les codes des métadonnées souhaitées
      if ((codesMetasSouhaites != null) && (!codesMetasSouhaites.isEmpty())) {

         MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
               .size()];

         MetadonneeCodeType metadonneeCode;
         for (int i = 0; i < codesMetasSouhaites.size(); i++) {
            metadonneeCode = new MetadonneeCodeType();
            metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
            arrMetadonneeCode[i] = metadonneeCode;
         }

         ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
         consultationRequest.setMetadonnees(listeMetadonneeCode);
         listeMetadonneeCode.setMetadonneeCode(arrMetadonneeCode);

      }

      // Renvoie du paramètre d'entrée de l'opération consultation
      return consultation;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param requeteRecherche
    *           la requête de recherche
    * @param codesMetasSouhaites
    *           les codes de métadonnées souhaitées dans les résultats de
    *           recherche.
    * 
    * @return le paramètre d'entrée pour l'opération "recherche"
    */
   public static Recherche contruitParamsEntreeRecherche(
         String requeteRecherche, List<String> codesMetasSouhaites) {

      Recherche recherche = new Recherche();

      RechercheRequestType rechercheRequest = new RechercheRequestType();

      recherche.setRecherche(rechercheRequest);

      // Requête de recherche
      RequeteRechercheType requeteRechercheObj = new RequeteRechercheType();
      requeteRechercheObj.setRequeteRechercheType(requeteRecherche);
      rechercheRequest.setRequete(requeteRechercheObj);

      // Codes des métadonnées souhaitées dans les résultats de recherche
      ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
      rechercheRequest.setMetadonnees(listeMetadonneeCode);
      if ((codesMetasSouhaites != null) && (!codesMetasSouhaites.isEmpty())) {

         MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
               .size()];

         MetadonneeCodeType metadonneeCode;
         for (int i = 0; i < codesMetasSouhaites.size(); i++) {
            metadonneeCode = new MetadonneeCodeType();
            metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
            arrMetadonneeCode[i] = metadonneeCode;
         }

         listeMetadonneeCode.setMetadonneeCode(arrMetadonneeCode);

      } else {
         listeMetadonneeCode.setMetadonneeCode(null);
      }

      // Renvoie du paramètre d'entrée de l'opération recherche
      return recherche;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param urlEcdeSommaire
    *           l'URL ECDE du fichier sommaire.xml
    * @return le paramètre d'entrée pour l'opération "archivageMasse"
    */
   public static ArchivageMasse contruitParamsEntreeArchivageMasse(
         String urlEcdeSommaire) {

      ArchivageMasse archivageMasse = new ArchivageMasse();

      ArchivageMasseRequestType archivageMasseRequest = new ArchivageMasseRequestType();

      archivageMasse.setArchivageMasse(archivageMasseRequest);

      // URL ECDE du sommaire
      EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
      archivageMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
      URI ecdeUriSommaireUri;
      try {
         ecdeUriSommaireUri = new URI(urlEcdeSommaire);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);

      // Renvoie du paramètre d'entrée de l'opération archivageMasse
      return archivageMasse;

   }

   private static EcdeUrlType buildEcdeUrl(String urlEcde) {

      EcdeUrlType ecdeUrl = new EcdeUrlType();
      URI uriEcdeFichier;
      try {
         uriEcdeFichier = new URI(urlEcde);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrl.setEcdeUrlType(uriEcdeFichier);

      return ecdeUrl;

   }

   private static ListeMetadonneeType buildListeMeta(
         Map<String, String> metadonnees) {

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

      MetadonneeType metadonnee;
      MetadonneeCodeType metaCode;
      MetadonneeValeurType metaValeur;
      String code;
      String valeur;
      for (Map.Entry<String, String> entry : metadonnees.entrySet()) {

         code = entry.getKey();
         valeur = entry.getValue();

         metadonnee = new MetadonneeType();

         metaCode = new MetadonneeCodeType();
         metaCode.setMetadonneeCodeType(code);
         metadonnee.setCode(metaCode);

         metaValeur = new MetadonneeValeurType();
         metaValeur.setMetadonneeValeurType(valeur);
         metadonnee.setValeur(metaValeur);

         listeMetadonnee.addMetadonnee(metadonnee);

      }

      return listeMetadonnee;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param urlEcdeFichier
    *           l'URL ECDE du fichier à archiver
    * @param metadonnees
    *           les métadonnées à associer au fichier
    * @return le paramètre d'entrée de l'opération "archivageUnitairePJ"
    */
   public static ArchivageUnitairePJ contruitParamsEntreeArchivageUnitairePJavecUrlEcde(
         String urlEcdeFichier, Map<String, String> metadonnees) {

      ArchivageUnitairePJ archivageUnitairePJ = new ArchivageUnitairePJ();

      ArchivageUnitairePJRequestType archivageUnitairePJRequest = new ArchivageUnitairePJRequestType();

      archivageUnitairePJ.setArchivageUnitairePJ(archivageUnitairePJRequest);

      // URL ECDE
      EcdeUrlType ecdeUrl = buildEcdeUrl(urlEcdeFichier);
      ArchivageUnitairePJRequestTypeChoice_type0 choice = new ArchivageUnitairePJRequestTypeChoice_type0();
      archivageUnitairePJRequest
            .setArchivageUnitairePJRequestTypeChoice_type0(choice);
      choice.setEcdeUrl(ecdeUrl);

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      archivageUnitairePJRequest.setMetadonnees(listeMetadonnee);

      // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
      return archivageUnitairePJ;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param nomFichier
    *           le nom du fichier à archiver
    * @param contenu
    *           le flux pointant vers le fichier à archiver
    * @param metadonnees
    *           les métadonnées à associer au fichier
    * @return le paramètre d'entrée de l'opération "archivageUnitairePJ"
    */
   public static ArchivageUnitairePJ contruitParamsEntreeArchivageUnitairePJavecContenu(
         String nomFichier, InputStream contenu,
         Map<String, String> metadonnees) {

      ArchivageUnitairePJ archivageUnitairePJ = new ArchivageUnitairePJ();

      ArchivageUnitairePJRequestType archivageUnitairePJRequest = new ArchivageUnitairePJRequestType();

      archivageUnitairePJ.setArchivageUnitairePJ(archivageUnitairePJRequest);

      // Nom et contenu du fichier
      DataFileType dataFile = new DataFileType();
      dataFile.setFileName(nomFichier);
      byte[] contenuBytes;
      try {
         contenuBytes = IOUtils.getStreamAsByteArray(contenu);
      } catch (IOException e) {
         throw new DemoRuntimeException(e);
      }
      ByteArrayDataSource byteArray = new ByteArrayDataSource(contenuBytes);
      DataHandler dataHandler = new DataHandler(byteArray);
      dataFile.setFile(dataHandler);
      ArchivageUnitairePJRequestTypeChoice_type0 choice = new ArchivageUnitairePJRequestTypeChoice_type0();
      archivageUnitairePJRequest
            .setArchivageUnitairePJRequestTypeChoice_type0(choice);
      choice.setDataFile(dataFile);

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      archivageUnitairePJRequest.setMetadonnees(listeMetadonnee);

      // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
      return archivageUnitairePJ;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param urlEcdeSommaire
    *           l'URL ECDE du sommaire.xml
    * @param typeHash
    *           le type de hash
    * @param hash
    *           le hash
    * @return le paramètre d'entrée de l'opération "archivageMasseAvecHash"
    */
   public static ArchivageMasseAvecHash contruitParamsEntreeArchivageMasseAvecHash(
         String urlEcdeSommaire, String typeHash, String hash) {

      ArchivageMasseAvecHash archivageMasseAvecHash = new ArchivageMasseAvecHash();

      ArchivageMasseAvecHashRequestType archivageMasseAvecHashRequest = new ArchivageMasseAvecHashRequestType();

      archivageMasseAvecHash
            .setArchivageMasseAvecHash(archivageMasseAvecHashRequest);

      // URL ECDE du sommaire
      EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
      archivageMasseAvecHashRequest.setUrlSommaire(ecdeUrlSommaireObj);
      URI ecdeUriSommaireUri;
      try {
         ecdeUriSommaireUri = new URI(urlEcdeSommaire);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);

      // Le hash et le type de hash
      archivageMasseAvecHashRequest.setTypeHash(typeHash);
      archivageMasseAvecHashRequest.setHash(hash);

      // Renvoie du paramètre d'entrée de l'opération archivageMasse
      return archivageMasseAvecHash;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant du document à supprimer
    * @return le paramètre d'entrée de l'opération "suppression"
    */
   public static Suppression contruitParamsEntreeSuppression(String idArchive) {

      Suppression suppression = new Suppression();

      SuppressionRequestType suppressionRequest = new SuppressionRequestType();

      suppression.setSuppression(suppressionRequest);

      // L'identifiant unique de l'archive
      suppressionRequest.setUuid(buildUuid(idArchive));

      // Renvoie du paramètre d'entrée de l'opération suppression
      return suppression;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document à modifier
    * @param metadonnees
    *           les modifications de métadonnées
    * @return le paramètre d'entrée de l'opération "modification"
    */
   public static Modification contruitParamsEntreeModification(
         String idArchive, Map<String, String> metadonnees) {

      Modification modification = new Modification();

      ModificationRequestType modificationRequest = new ModificationRequestType();

      modification.setModification(modificationRequest);

      // Identifiant de l'archive
      modificationRequest.setUuid(buildUuid(idArchive));

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      modificationRequest.setMetadonnees(listeMetadonnee);

      // Renvoie du paramètre d'entrée de l'opération archivageUnitaire
      return modification;

   }

}
