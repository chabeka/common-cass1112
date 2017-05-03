package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.modele.SaeServiceStub.AjoutNote;
import sae.client.demo.webservice.modele.SaeServiceStub.AjoutNoteRequestType;
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
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationAffichable;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationAffichableRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNS;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNSRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOM;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOMRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.Copie;
import sae.client.demo.webservice.modele.SaeServiceStub.CopieRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.DataFileType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlSommaireType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlType;
import sae.client.demo.webservice.modele.SaeServiceStub.EtatTraitementsMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.EtatTraitementsMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.FiltreType;
import sae.client.demo.webservice.modele.SaeServiceStub.GetDocFormatOrigine;
import sae.client.demo.webservice.modele.SaeServiceStub.GetDocFormatOrigineRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.IdentifiantPageType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeRangeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeUuidType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeValeurType;
import sae.client.demo.webservice.modele.SaeServiceStub.Modification;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.NoteTxtType;
import sae.client.demo.webservice.modele.SaeServiceStub.RangeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.Recherche;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbRes;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbResRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateur;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateurRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.RequetePrincipaleType;
import sae.client.demo.webservice.modele.SaeServiceStub.RequeteRechercheNbResType;
import sae.client.demo.webservice.modele.SaeServiceStub.RequeteRechercheType;
import sae.client.demo.webservice.modele.SaeServiceStub.RestoreMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.RestoreMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireRequestTypeChoice_type0;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireRequestTypeChoice_type1;
import sae.client.demo.webservice.modele.SaeServiceStub.Suppression;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.SuppressionRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.Transfert;
import sae.client.demo.webservice.modele.SaeServiceStub.TransfertMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.TransfertMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.TransfertRequestType;
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
    * @return le paramètre d'entrée de l'opération "consultationGNTGNS"
    */
   public static ConsultationGNTGNS contruitParamsEntreeConsultationGNTGNS(String idArchive) {

      return contruitParamsEntreeConsultationGNTGNS(idArchive, null);

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

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @return le paramètre d'entrée de l'opération "consultationAffichable"
    */
   public static ConsultationAffichable contruitParamsEntreeConsultationAffichable(
         String idArchive) {

      return contruitParamsEntreeConsultationAffichable(idArchive, null);

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
    * @param metadonnees
    *           la liste des métadonnées que l'on souhaite modifier avant la copie
    * @return le paramètre d'entrée de l'opération "copie"
    */
   public static Copie contruitParamsEntreeCopie(String idArchive,
         Map<String, String> metadonnees) {

      Copie copie = new Copie();

      CopieRequestType copieRequest = new CopieRequestType();

      copie.setCopie(copieRequest);

      copieRequest.setIdGed(buildUuid(idArchive));

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);

      copieRequest.setMetadonnees(listeMetadonnee);

      return copie;

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
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @param codesMetasSouhaites
    *           la liste des métadonnées que l'on souhaite en retour du service
    *           web
    * @return le paramètre d'entrée de l'opération "consultationGNTGNS"
    */
   public static ConsultationGNTGNS contruitParamsEntreeConsultationGNTGNS(
         String idArchive, List<String> codesMetasSouhaites) {

      ConsultationGNTGNS consultation = new ConsultationGNTGNS();

      ConsultationGNTGNSRequestType consultationRequest = new ConsultationGNTGNSRequestType();

      consultation.setConsultationGNTGNS(consultationRequest);

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
    * @param idArchive
    *           l'identifiant unique du document que l'on veut consulter
    * @param codesMetasSouhaites
    *           la liste des métadonnées que l'on souhaite en retour du service
    *           web
    * @return le paramètre d'entrée de l'opération "consultationAffichable"
    */
   public static ConsultationAffichable contruitParamsEntreeConsultationAffichable(
         String idArchive, List<String> codesMetasSouhaites) {

      ConsultationAffichable consultation = new ConsultationAffichable();

      ConsultationAffichableRequestType consultationRequest = new ConsultationAffichableRequestType();

      consultation.setConsultationAffichable(consultationRequest);

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
    * @param requeteRechercheNbRes
    *           la requête de recherche
    * @param codesMetasSouhaites
    *           les codes de métadonnées souhaitées dans les résultats de
    *           recherche.
    * 
    * @return le paramètre d'entrée pour l'opération "rechercheNbRes"
    */
   public static RechercheNbRes contruitParamsEntreeRechercheNbRes(
         String requeteRecherche, List<String> codesMetasSouhaites) {

      RechercheNbRes recherche = new RechercheNbRes();

      RechercheNbResRequestType rechercheRequest = new RechercheNbResRequestType();

      recherche.setRechercheNbRes(rechercheRequest);

      // Requête de recherche
      RequeteRechercheNbResType requeteRechercheObj = new RequeteRechercheNbResType();
      requeteRechercheObj.setRequeteRechercheNbResType(requeteRecherche);
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
    * @param listeMetasFixes
    *           La liste des métadonnées fixes
    * @param codeMetaVariable
    *           Le code de la méta variable
    * @param valeurMinMetaVar
    *           La valeur min de la méta variable
    * @param valeurMaxMetaVar
    *           La valeur max de la méta variable
    * @param equalFilter
    *           La liste des filtres de type égalité
    * @param notEqualFilter
    *           La liste des filtres de type non égalité
    * @param rangeFilter
    *           La liste des filtres de type range
    * @param notInRangeFilter
    *           La liste des filtres de type not in range
    * @param nombreDocParPage
    *           Le nombre de document par page
    * @param codesMetasSouhaites
    *           La liste des métadonnées souhaitées en retour
    * @param valeurIdentifiantPage
    *           La valeur de l'identifiant de la page
    * @param idArchive
    *           L'identifiant de l'archive
    * @return
    */
   public static RechercheParIterateur contruitParamsEntreeRechercheParIterateur(
         Map<String, String> listeMetasFixes, String codeMetaVariable,
         String valeurMinMetaVar, String valeurMaxMetaVar,
         Map<String, String> equalFilter, Map<String, String> notEqualFilter,
         Map<String, String[]> rangeFilter,
         Map<String, String[]> notInRangeFilter, String nombreDocParPage,
         List<String> codesMetasSouhaites, String valeurIdentifiantPage,
         String idArchive) {

      RechercheParIterateur rechercheParIterateur = new RechercheParIterateur();

      RechercheParIterateurRequestType rechercheParIterateurRequest = new RechercheParIterateurRequestType();

      rechercheParIterateur
            .setRechercheParIterateur(rechercheParIterateurRequest);

      // Requête principale
      RechercheParIterateurRequestType requeteParIterateurObj = new RechercheParIterateurRequestType();
      RequetePrincipaleType requetePrincipaleType = new RequetePrincipaleType();

      // - Liste des métadonnées fixes (facultatif)
      ListeMetadonneeType listeMetadonneeFixes = new ListeMetadonneeType();
      if ((listeMetasFixes != null) && (!listeMetasFixes.isEmpty())) {
         listeMetadonneeFixes = buildListeMeta(listeMetasFixes);
      } else {
         listeMetadonneeFixes.setMetadonnee(null);
      }
      requetePrincipaleType.setFixedMetadatas(listeMetadonneeFixes);

      // - Métadonnée variable (obligatoire)
      RangeMetadonneeType rangeMetadonnee = new RangeMetadonneeType();
      MetadonneeCodeType metaCode = new MetadonneeCodeType();
      metaCode.setMetadonneeCodeType(codeMetaVariable);
      rangeMetadonnee.setCode(metaCode);

      MetadonneeValeurType metaValeurMin = new MetadonneeValeurType();
      metaValeurMin.setMetadonneeValeurType(valeurMinMetaVar);
      rangeMetadonnee.setValeurMin(metaValeurMin);

      MetadonneeValeurType metaValeurMax = new MetadonneeValeurType();
      metaValeurMax.setMetadonneeValeurType(valeurMaxMetaVar);
      rangeMetadonnee.setValeurMax(metaValeurMax);

      requetePrincipaleType.setVaryingMetadata(rangeMetadonnee);

      requeteParIterateurObj.setRequetePrincipale(requetePrincipaleType);

      // Filtre (facultatif)
      FiltreType filtreType = new FiltreType();
      if ((equalFilter != null) && (!equalFilter.isEmpty())) {
         ListeMetadonneeType listeMetaEqual = buildListeMeta(equalFilter);
         filtreType.setEqualFilter(listeMetaEqual);
      } else {
         filtreType.setEqualFilter(new ListeMetadonneeType());
      }

      FiltreType filtreNotEqualType = new FiltreType();
      if ((notEqualFilter != null) && (!notEqualFilter.isEmpty())) {
         ListeMetadonneeType listeMetaNotEqual = buildListeMeta(notEqualFilter);
         filtreType.setNotEqualFilter(listeMetaNotEqual);
      } else {
         filtreType.setNotEqualFilter(new ListeMetadonneeType());
      }

      if ((rangeFilter != null) && (!rangeFilter.isEmpty())) {
         ListeRangeMetadonneeType listeRangeMeta = buildListeRangeMeta(rangeFilter);
         filtreType.setRangeFilter(listeRangeMeta);
      } else {
         filtreType.setRangeFilter(new ListeRangeMetadonneeType());
      }

      if ((notInRangeFilter != null) && (!notInRangeFilter.isEmpty())) {
         ListeRangeMetadonneeType listeNotInRangeMeta = buildListeRangeMeta(rangeFilter);
         filtreType.setNotInRangeFilter(listeNotInRangeMeta);
      } else {
         filtreType.setNotInRangeFilter(new ListeRangeMetadonneeType());
      }

      requeteParIterateurObj.setFiltres(filtreType);

      // Identifiant de la page
      if (idArchive != null && !idArchive.isEmpty()
            && valeurIdentifiantPage != null
            && !valeurIdentifiantPage.isEmpty()) {
         IdentifiantPageType identifiantPage = new IdentifiantPageType();
         UuidType uuidType = new UuidType();
         uuidType.setUuidType(idArchive);
         identifiantPage.setIdArchive(uuidType);
         MetadonneeValeurType metaValType = new MetadonneeValeurType();
         metaValType.setMetadonneeValeurType(valeurIdentifiantPage);
         identifiantPage.setValeur(metaValType);
         requeteParIterateurObj.setIdentifiantPage(identifiantPage);
      }

      // Codes des métadonnées souhaitées dans les résultats de recherche
      ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();

      requeteParIterateurObj.setMetadonnees(listeMetadonneeCode);
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

      requeteParIterateurObj.setNbDocumentsParPage(Integer
            .parseInt(nombreDocParPage));

      rechercheParIterateur.setRechercheParIterateur(requeteParIterateurObj);
      // Renvoie du paramètre d'entrée de l'opération recherche
      return rechercheParIterateur;

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
   
   public static TransfertMasse contruitParamsEntreeTransfertMasse(String urlEcdeSommaire, String hash, String typeHash){
      
      TransfertMasse transfertMasse = new TransfertMasse();
      
      TransfertMasseRequestType transfertMasseRequest = new TransfertMasseRequestType();
      
      transfertMasse.setTransfertMasse(transfertMasseRequest);
      
   // URL ECDE du sommaire
      EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
      transfertMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
      URI ecdeUriSommaireUri;
      try {
         ecdeUriSommaireUri = new URI(urlEcdeSommaire);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);
      
      transfertMasseRequest.setHash(hash);
      
      transfertMasseRequest.setTypeHash(typeHash);
      
      return transfertMasse;    
      
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

   private static ListeRangeMetadonneeType buildListeRangeMeta(
         Map<String, String[]> metadonnees) {

      ListeRangeMetadonneeType listeMetadonnee = new ListeRangeMetadonneeType();

      RangeMetadonneeType metadonnee;
      MetadonneeCodeType metaCode;
      MetadonneeValeurType metaValeurMin;
      MetadonneeValeurType metaValeurMax;
      String code;
      String valeurMin;
      String valeurMax;
      for (Map.Entry<String, String[]> entry : metadonnees.entrySet()) {

         code = entry.getKey();
         valeurMin = entry.getValue()[0];
         valeurMax = entry.getValue()[1];

         metadonnee = new RangeMetadonneeType();

         metaCode = new MetadonneeCodeType();
         metaCode.setMetadonneeCodeType(code);
         metadonnee.setCode(metaCode);

         metaValeurMin = new MetadonneeValeurType();
         metaValeurMin.setMetadonneeValeurType(valeurMin);
         metadonnee.setValeurMin(metaValeurMin);

         metaValeurMax = new MetadonneeValeurType();
         metaValeurMax.setMetadonneeValeurType(valeurMax);
         metadonnee.setValeurMax(metaValeurMax);

         listeMetadonnee.addRangeMetadonnee(metadonnee);

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
         String nomFichier, InputStream contenu, Map<String, String> metadonnees) {

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
    *           l'identifiant du document à transférer
    * @return le paramètre d'entrée de l'opération "transfert"
    */
   public static Transfert contruitParamsEntreeTransfert(String idArchive) {

      Transfert transfert = new Transfert();

      TransfertRequestType transfertRequest = new TransfertRequestType();

      transfert.setTransfert(transfertRequest);

      // L'identifiant unique de l'archive
      transfertRequest.setUuid(buildUuid(idArchive));

      // Renvoie du paramètre d'entrée de l'opération transfert
      return transfert;

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

      // Renvoie du paramètre d'entrée de l'opération modification
      return modification;

   }

   public static AjoutNote contruitParamsEntreeAjoutNote(String idArchive,
         String contenuNote) {
      AjoutNote ajoutNote = new AjoutNote();
      AjoutNoteRequestType ajoutNoteRequest = new AjoutNoteRequestType();
      ajoutNote.setAjoutNote(ajoutNoteRequest);

      // Identifiant de l'archive
      ajoutNoteRequest.setUuid(buildUuid(idArchive));

      // Contenu de la note à ajouter au document
      NoteTxtType paramNote = new NoteTxtType();
      paramNote.setNoteTxtType(contenuNote);
      ajoutNoteRequest.setNote(paramNote);

      // Renvoie du paramètre d'entrée de l'opération ajoutNote
      return ajoutNote;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param urlEcdeFichier
    *           l'URL ECDE du fichier à archiver
    * @param urlEcdeFichierFormatOrigine
    *           L'URL ECDE du fichier au format d'originie à rattacher
    * @param metadonnees
    *           les métadonnées à associer au fichier
    * @return le paramètre d'entrée de l'opération "stockageUnitaire"
    */
   public static StockageUnitaire contruitParamsEntreeStockageUnitaireAvecUrlEcde(
         String urlEcdeFichier, String urlEcdeFichierFormatOrigine,
         Map<String, String> metadonnees) {

      StockageUnitaire stockageUnitaire = new StockageUnitaire();
      StockageUnitaireRequestType stockageUnitaireRequest = new StockageUnitaireRequestType();
      stockageUnitaire.setStockageUnitaire(stockageUnitaireRequest);

      // URL ECDE du document parent
      EcdeUrlType ecdeUrlFichier = buildEcdeUrl(urlEcdeFichier);
      StockageUnitaireRequestTypeChoice_type0 choice0 = new StockageUnitaireRequestTypeChoice_type0();
      stockageUnitaireRequest
            .setStockageUnitaireRequestTypeChoice_type0(choice0);
      choice0.setUrlEcdeDoc(ecdeUrlFichier);

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      stockageUnitaireRequest.setMetadonnees(listeMetadonnee);

      // URL ECDE du document au format d'origine
      EcdeUrlType ecdeUrlFichierFormatOrigine = buildEcdeUrl(urlEcdeFichierFormatOrigine);
      StockageUnitaireRequestTypeChoice_type1 choice1 = new StockageUnitaireRequestTypeChoice_type1();
      stockageUnitaireRequest
            .setStockageUnitaireRequestTypeChoice_type1(choice1);
      choice1.setUrlEcdeDocOrigine(ecdeUrlFichierFormatOrigine);

      return stockageUnitaire;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param nomFichier
    *           le nom du fichier à archiver
    * @param contenu
    *           le flux pointant vers le fichier à archiver
    * @param nomFichier
    *           le nom du fichier au format d'origine à rattacher
    * @param contenu
    *           le flux pointant vers le fichier au format d'origine à rattacher
    * @param metadonnees
    *           les métadonnées à associer au fichier
    * @return le paramètre d'entrée de l'opération "stockageUnitaire"
    */
   public static StockageUnitaire contruitParamsEntreeStockageUnitaireavecContenu(
         String nomFichier, InputStream contenu,
         String nomFichierFormatOrigine, InputStream contenuFormatOrigine,
         Map<String, String> metadonnees) {

      StockageUnitaire stockageUnitaire = new StockageUnitaire();
      StockageUnitaireRequestType stockageUnitaireRequest = new StockageUnitaireRequestType();
      stockageUnitaire.setStockageUnitaire(stockageUnitaireRequest);

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
      StockageUnitaireRequestTypeChoice_type0 choice0 = new StockageUnitaireRequestTypeChoice_type0();
      stockageUnitaireRequest
            .setStockageUnitaireRequestTypeChoice_type0(choice0);
      choice0.setDataFileDoc(dataFile);

      // Nom et contenu du fichier au format d'origine
      DataFileType dataFileFormatOrigine = new DataFileType();
      dataFileFormatOrigine.setFileName(nomFichierFormatOrigine);
      byte[] contenuBytesFormatOrigine;
      try {
         contenuBytesFormatOrigine = IOUtils
               .getStreamAsByteArray(contenuFormatOrigine);
      } catch (IOException e) {
         throw new DemoRuntimeException(e);
      }
      ByteArrayDataSource byteArrayFormatOrigine = new ByteArrayDataSource(
            contenuBytesFormatOrigine);
      DataHandler dataHandlerFormatOrigine = new DataHandler(
            byteArrayFormatOrigine);
      dataFileFormatOrigine.setFile(dataHandlerFormatOrigine);
      StockageUnitaireRequestTypeChoice_type1 choice1 = new StockageUnitaireRequestTypeChoice_type1();
      stockageUnitaireRequest
            .setStockageUnitaireRequestTypeChoice_type1(choice1);
      choice1.setDataFileAttached(dataFileFormatOrigine);

      // Métadonnées
      ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
      stockageUnitaireRequest.setMetadonnees(listeMetadonnee);

      // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
      return stockageUnitaire;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param uuidDocParent
    *           L'UUID du document dont on cherche le document au format
    *           d'origine
    * @return le paramètre d'entrée de l'opération "getDocFormatOrigine"
    */
   public static GetDocFormatOrigine contruitParamsEntreeGetDocFormatOrigine(
         UUID uuidDocParent) {

      GetDocFormatOrigine getDocFormatOrigine = new GetDocFormatOrigine();
      GetDocFormatOrigineRequestType getDocFormatOrigineRequest = new GetDocFormatOrigineRequestType();
      getDocFormatOrigine.setGetDocFormatOrigine(getDocFormatOrigineRequest);

      // UUID du document parent
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuidDocParent.toString());
      getDocFormatOrigineRequest.setIdDoc(uuidType);

      return getDocFormatOrigine;

   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param requete
    *           La requête de suppression des documents
    * @return le paramètre d'entrée de l'opération "suppressionMasse"
    */
   public static SuppressionMasse contruitParamsEntreeSuppressionMasse(
         String requete) {
      SuppressionMasse suppressionMasse = new SuppressionMasse();
      SuppressionMasseRequestType suppressionMasseRequest = new SuppressionMasseRequestType();
      suppressionMasse.setSuppressionMasse(suppressionMasseRequest);

      // Requete de suppression
      RequeteRechercheType requeteType = new RequeteRechercheType();
      requeteType.setRequeteRechercheType(requete);
      suppressionMasseRequest.setRequete(requeteType);

      return suppressionMasse;
   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param idTraitementSuppression
    *           L'identifiant du traitement de suppression de masse à restorer
    * @return le paramètre d'entrée de l'opération "restoreMasse"
    */
   public static RestoreMasse contruitParamsEntreeRestoreMasse(
         String idTraitementSuppression) {
      RestoreMasse restoreMasse = new RestoreMasse();
      RestoreMasseRequestType restoreMasseRequest = new RestoreMasseRequestType();
      restoreMasse.setRestoreMasse(restoreMasseRequest);

      UuidType requeteType = new UuidType();
      requeteType.setUuidType(idTraitementSuppression);
      restoreMasseRequest.setUuid(requeteType);

      return restoreMasse;
   }

   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web
    * 
    * @param listeUuid
    *           La liste des uuid des traitements de masse
    * @return le paramètre d'entrée de l'opération "etatTraitementsMasse"
    */
   public static EtatTraitementsMasse contruitParamsEntreeEtatTraitementsMasse(
         List<String> listeUuid) {

      EtatTraitementsMasse etatTraitementsMasse = new EtatTraitementsMasse();
      EtatTraitementsMasseRequestType etatTraitementsMasseRequest = new EtatTraitementsMasseRequestType();
      etatTraitementsMasse.setEtatTraitementsMasse(etatTraitementsMasseRequest);

      ListeUuidType listeUuidType = new ListeUuidType();
      for (String uuid : listeUuid) {
         UuidType uuidType = new UuidType();
         uuidType.setUuidType(uuid);
         listeUuidType.addUuid(uuidType);
      }
      etatTraitementsMasseRequest.setListeUuid(listeUuidType);

      return etatTraitementsMasse;
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
    * @return le paramètre d'entrée de l'opération "ModificationMasse"
    */
   public static ModificationMasse contruitParamsEntreeModificationMasse(
         String urlEcdeSommaire, String typeHash, String hash,
         String codeTraitement) {

      ModificationMasse modificationMasse = new ModificationMasse();

      ModificationMasseRequestType modificationMasseRequest = new ModificationMasseRequestType();

      modificationMasse.setModificationMasse(modificationMasseRequest);

      // URL ECDE du sommaire
      EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
      modificationMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
      URI ecdeUriSommaireUri;
      try {
         ecdeUriSommaireUri = new URI(urlEcdeSommaire);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);

      // Le hash et le type de hash
      modificationMasseRequest.setTypeHash(typeHash);
      modificationMasseRequest.setHash(hash);

      // Code traitement
      modificationMasseRequest.setCodeTraitement(codeTraitement);

      // Renvoie du paramètre d'entrée de l'opération archivageMasse
      return modificationMasse;

   }

}
