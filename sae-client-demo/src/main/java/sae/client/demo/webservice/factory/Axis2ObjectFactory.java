package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.apache.commons.io.IOUtils;

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
import sae.client.demo.webservice.modele.SaeServiceStub.Deblocage;
import sae.client.demo.webservice.modele.SaeServiceStub.DeblocageRequestType;
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
import sae.client.demo.webservice.modele.SaeServiceStub.Reprise;
import sae.client.demo.webservice.modele.SaeServiceStub.RepriseRequestType;
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
   *          l'URL ECDE du fichier à archiver
   * @param metadonnees
   *          les métadonnées à associer au fichier
   * @return le paramètre d'entrée de l'opération "archivageUnitaire"
   */
  public static ArchivageUnitaire contruitParamsEntreeArchivageUnitaire(
                                                                        final String urlEcdeFichier, final Map<String, String> metadonnees) {

    final ArchivageUnitaire archivageUnitaire = new ArchivageUnitaire();

    final ArchivageUnitaireRequestType archivageUnitaireRequest = new ArchivageUnitaireRequestType();

    archivageUnitaire.setArchivageUnitaire(archivageUnitaireRequest);

    // URL ECDE
    final EcdeUrlType ecdeUrl = buildEcdeUrl(urlEcdeFichier);
    archivageUnitaireRequest.setEcdeUrl(ecdeUrl);

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    archivageUnitaireRequest.setMetadonnees(listeMetadonnee);

    // Renvoie du paramètre d'entrée de l'opération archivageUnitaire
    return archivageUnitaire;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @return le paramètre d'entrée de l'opération "consultation"
   */
  public static Consultation contruitParamsEntreeConsultation(final String idArchive) {

    return contruitParamsEntreeConsultation(idArchive, null);

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @return le paramètre d'entrée de l'opération "consultationGNTGNS"
   */
  public static ConsultationGNTGNS contruitParamsEntreeConsultationGNTGNS(final String idArchive) {

    return contruitParamsEntreeConsultationGNTGNS(idArchive, null);

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @return le paramètre d'entrée de l'opération "consultationMTOM"
   */
  public static ConsultationMTOM contruitParamsEntreeConsultationMTOM(
                                                                      final String idArchive) {

    return contruitParamsEntreeConsultationMTOM(idArchive, null);

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @return le paramètre d'entrée de l'opération "consultationAffichable"
   */
  public static ConsultationAffichable contruitParamsEntreeConsultationAffichable(
                                                                                  final String idArchive) {

    return contruitParamsEntreeConsultationAffichable(idArchive, null);

  }

  private static UuidType buildUuid(final String uuid) {
    final UuidType uuidType = new UuidType();
    uuidType.setUuidType(uuid);
    return uuidType;
  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @param codesMetasSouhaites
   *          la liste des métadonnées que l'on souhaite en retour du service
   *          web
   * @return le paramètre d'entrée de l'opération "consultation"
   */
  public static Consultation contruitParamsEntreeConsultation(
                                                              final String idArchive, final List<String> codesMetasSouhaites) {

    final Consultation consultation = new Consultation();

    final ConsultationRequestType consultationRequest = new ConsultationRequestType();

    consultation.setConsultation(consultationRequest);

    // L'identifiant unique de l'archivage
    consultationRequest.setIdArchive(buildUuid(idArchive));

    // Les codes des métadonnées souhaitées
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
                                                                                               .size()];

      MetadonneeCodeType metadonneeCode;
      for (int i = 0; i < codesMetasSouhaites.size(); i++) {
        metadonneeCode = new MetadonneeCodeType();
        metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
        arrMetadonneeCode[i] = metadonneeCode;
      }

      final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
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
   *          l'identifiant unique du document que l'on veut consulter
   * @param metadonnees
   *          la liste des métadonnées que l'on souhaite modifier avant la copie
   * @return le paramètre d'entrée de l'opération "copie"
   */
  public static Copie contruitParamsEntreeCopie(final String idArchive,
                                                final Map<String, String> metadonnees) {

    final Copie copie = new Copie();

    final CopieRequestType copieRequest = new CopieRequestType();

    copie.setCopie(copieRequest);

    copieRequest.setIdGed(buildUuid(idArchive));

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);

    copieRequest.setMetadonnees(listeMetadonnee);

    return copie;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idArchive
   *          l'identifiant unique du document que l'on veut consulter
   * @param codesMetasSouhaites
   *          la liste des métadonnées que l'on souhaite en retour du service
   *          web
   * @return le paramètre d'entrée de l'opération "consultationMTOM"
   */
  public static ConsultationMTOM contruitParamsEntreeConsultationMTOM(
                                                                      final String idArchive, final List<String> codesMetasSouhaites) {

    final ConsultationMTOM consultation = new ConsultationMTOM();

    final ConsultationMTOMRequestType consultationRequest = new ConsultationMTOMRequestType();

    consultation.setConsultationMTOM(consultationRequest);

    // L'identifiant unique de l'archive
    consultationRequest.setIdArchive(buildUuid(idArchive));

    // Les codes des métadonnées souhaitées
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
                                                                                               .size()];

      MetadonneeCodeType metadonneeCode;
      for (int i = 0; i < codesMetasSouhaites.size(); i++) {
        metadonneeCode = new MetadonneeCodeType();
        metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
        arrMetadonneeCode[i] = metadonneeCode;
      }

      final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
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
   *          l'identifiant unique du document que l'on veut consulter
   * @param codesMetasSouhaites
   *          la liste des métadonnées que l'on souhaite en retour du service
   *          web
   * @return le paramètre d'entrée de l'opération "consultationGNTGNS"
   */
  public static ConsultationGNTGNS contruitParamsEntreeConsultationGNTGNS(
                                                                          final String idArchive, final List<String> codesMetasSouhaites) {

    final ConsultationGNTGNS consultation = new ConsultationGNTGNS();

    final ConsultationGNTGNSRequestType consultationRequest = new ConsultationGNTGNSRequestType();

    consultation.setConsultationGNTGNS(consultationRequest);

    // L'identifiant unique de l'archive
    consultationRequest.setIdArchive(buildUuid(idArchive));

    // Les codes des métadonnées souhaitées
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
                                                                                               .size()];

      MetadonneeCodeType metadonneeCode;
      for (int i = 0; i < codesMetasSouhaites.size(); i++) {
        metadonneeCode = new MetadonneeCodeType();
        metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
        arrMetadonneeCode[i] = metadonneeCode;
      }

      final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
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
   *          l'identifiant unique du document que l'on veut consulter
   * @param codesMetasSouhaites
   *          la liste des métadonnées que l'on souhaite en retour du service
   *          web
   * @return le paramètre d'entrée de l'opération "consultationAffichable"
   */
  public static ConsultationAffichable contruitParamsEntreeConsultationAffichable(
                                                                                  final String idArchive, final List<String> codesMetasSouhaites) {

    final ConsultationAffichable consultation = new ConsultationAffichable();

    final ConsultationAffichableRequestType consultationRequest = new ConsultationAffichableRequestType();

    consultation.setConsultationAffichable(consultationRequest);

    // L'identifiant unique de l'archive
    consultationRequest.setIdArchive(buildUuid(idArchive));

    // Les codes des métadonnées souhaitées
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
                                                                                               .size()];

      MetadonneeCodeType metadonneeCode;
      for (int i = 0; i < codesMetasSouhaites.size(); i++) {
        metadonneeCode = new MetadonneeCodeType();
        metadonneeCode.setMetadonneeCodeType(codesMetasSouhaites.get(i));
        arrMetadonneeCode[i] = metadonneeCode;
      }

      final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
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
   *          la requête de recherche
   * @param codesMetasSouhaites
   *          les codes de métadonnées souhaitées dans les résultats de
   *          recherche.
   * @return le paramètre d'entrée pour l'opération "recherche"
   */
  public static Recherche contruitParamsEntreeRecherche(
                                                        final String requeteRecherche, final List<String> codesMetasSouhaites) {

    final Recherche recherche = new Recherche();

    final RechercheRequestType rechercheRequest = new RechercheRequestType();

    recherche.setRecherche(rechercheRequest);

    // Requête de recherche
    final RequeteRechercheType requeteRechercheObj = new RequeteRechercheType();
    requeteRechercheObj.setRequeteRechercheType(requeteRecherche);
    rechercheRequest.setRequete(requeteRechercheObj);

    // Codes des métadonnées souhaitées dans les résultats de recherche
    final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
    rechercheRequest.setMetadonnees(listeMetadonneeCode);
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
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
   *          la requête de recherche
   * @param codesMetasSouhaites
   *          les codes de métadonnées souhaitées dans les résultats de
   *          recherche.
   * @return le paramètre d'entrée pour l'opération "rechercheNbRes"
   */
  public static RechercheNbRes contruitParamsEntreeRechercheNbRes(
                                                                  final String requeteRecherche, final List<String> codesMetasSouhaites) {

    final RechercheNbRes recherche = new RechercheNbRes();

    final RechercheNbResRequestType rechercheRequest = new RechercheNbResRequestType();

    recherche.setRechercheNbRes(rechercheRequest);

    // Requête de recherche
    final RequeteRechercheNbResType requeteRechercheObj = new RequeteRechercheNbResType();
    requeteRechercheObj.setRequeteRechercheNbResType(requeteRecherche);
    rechercheRequest.setRequete(requeteRechercheObj);

    // Codes des métadonnées souhaitées dans les résultats de recherche
    final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
    rechercheRequest.setMetadonnees(listeMetadonneeCode);
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
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
   *          La liste des métadonnées fixes
   * @param codeMetaVariable
   *          Le code de la méta variable
   * @param valeurMinMetaVar
   *          La valeur min de la méta variable
   * @param valeurMaxMetaVar
   *          La valeur max de la méta variable
   * @param equalFilter
   *          La liste des filtres de type égalité
   * @param notEqualFilter
   *          La liste des filtres de type non égalité
   * @param rangeFilter
   *          La liste des filtres de type range
   * @param notInRangeFilter
   *          La liste des filtres de type not in range
   * @param nombreDocParPage
   *          Le nombre de document par page
   * @param codesMetasSouhaites
   *          La liste des métadonnées souhaitées en retour
   * @param valeurIdentifiantPage
   *          La valeur de l'identifiant de la page
   * @param idArchive
   *          L'identifiant de l'archive
   * @return
   */
  public static RechercheParIterateur contruitParamsEntreeRechercheParIterateur(
                                                                                final Map<String, String> listeMetasFixes, final String codeMetaVariable,
                                                                                final String valeurMinMetaVar, final String valeurMaxMetaVar,
                                                                                final Map<String, String> equalFilter, final Map<String, String> notEqualFilter,
                                                                                final Map<String, String[]> rangeFilter,
                                                                                final Map<String, String[]> notInRangeFilter, final String nombreDocParPage,
                                                                                final List<String> codesMetasSouhaites, final String valeurIdentifiantPage,
                                                                                final String idArchive) {

    final RechercheParIterateur rechercheParIterateur = new RechercheParIterateur();

    final RechercheParIterateurRequestType rechercheParIterateurRequest = new RechercheParIterateurRequestType();

    rechercheParIterateur
                         .setRechercheParIterateur(rechercheParIterateurRequest);

    // Requête principale
    final RechercheParIterateurRequestType requeteParIterateurObj = new RechercheParIterateurRequestType();
    final RequetePrincipaleType requetePrincipaleType = new RequetePrincipaleType();

    // - Liste des métadonnées fixes (facultatif)
    ListeMetadonneeType listeMetadonneeFixes = new ListeMetadonneeType();
    if (listeMetasFixes != null && !listeMetasFixes.isEmpty()) {
      listeMetadonneeFixes = buildListeMeta(listeMetasFixes);
    } else {
      listeMetadonneeFixes.setMetadonnee(null);
    }
    requetePrincipaleType.setFixedMetadatas(listeMetadonneeFixes);

    // - Métadonnée variable (obligatoire)
    final RangeMetadonneeType rangeMetadonnee = new RangeMetadonneeType();
    final MetadonneeCodeType metaCode = new MetadonneeCodeType();
    metaCode.setMetadonneeCodeType(codeMetaVariable);
    rangeMetadonnee.setCode(metaCode);

    final MetadonneeValeurType metaValeurMin = new MetadonneeValeurType();
    metaValeurMin.setMetadonneeValeurType(valeurMinMetaVar);
    rangeMetadonnee.setValeurMin(metaValeurMin);

    final MetadonneeValeurType metaValeurMax = new MetadonneeValeurType();
    metaValeurMax.setMetadonneeValeurType(valeurMaxMetaVar);
    rangeMetadonnee.setValeurMax(metaValeurMax);

    requetePrincipaleType.setVaryingMetadata(rangeMetadonnee);

    requeteParIterateurObj.setRequetePrincipale(requetePrincipaleType);

    // Filtre (facultatif)
    final FiltreType filtreType = new FiltreType();
    if (equalFilter != null && !equalFilter.isEmpty()) {
      final ListeMetadonneeType listeMetaEqual = buildListeMeta(equalFilter);
      filtreType.setEqualFilter(listeMetaEqual);
    } else {
      filtreType.setEqualFilter(new ListeMetadonneeType());
    }

    final FiltreType filtreNotEqualType = new FiltreType();
    if (notEqualFilter != null && !notEqualFilter.isEmpty()) {
      final ListeMetadonneeType listeMetaNotEqual = buildListeMeta(notEqualFilter);
      filtreType.setNotEqualFilter(listeMetaNotEqual);
    } else {
      filtreType.setNotEqualFilter(new ListeMetadonneeType());
    }

    if (rangeFilter != null && !rangeFilter.isEmpty()) {
      final ListeRangeMetadonneeType listeRangeMeta = buildListeRangeMeta(rangeFilter);
      filtreType.setRangeFilter(listeRangeMeta);
    } else {
      filtreType.setRangeFilter(new ListeRangeMetadonneeType());
    }

    if (notInRangeFilter != null && !notInRangeFilter.isEmpty()) {
      final ListeRangeMetadonneeType listeNotInRangeMeta = buildListeRangeMeta(rangeFilter);
      filtreType.setNotInRangeFilter(listeNotInRangeMeta);
    } else {
      filtreType.setNotInRangeFilter(new ListeRangeMetadonneeType());
    }

    requeteParIterateurObj.setFiltres(filtreType);

    // Identifiant de la page
    if (idArchive != null && !idArchive.isEmpty()
        && valeurIdentifiantPage != null
        && !valeurIdentifiantPage.isEmpty()) {
      final IdentifiantPageType identifiantPage = new IdentifiantPageType();
      final UuidType uuidType = new UuidType();
      uuidType.setUuidType(idArchive);
      identifiantPage.setIdArchive(uuidType);
      final MetadonneeValeurType metaValType = new MetadonneeValeurType();
      metaValType.setMetadonneeValeurType(valeurIdentifiantPage);
      identifiantPage.setValeur(metaValType);
      requeteParIterateurObj.setIdentifiantPage(identifiantPage);
    }

    // Codes des métadonnées souhaitées dans les résultats de recherche
    final ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();

    requeteParIterateurObj.setMetadonnees(listeMetadonneeCode);
    if (codesMetasSouhaites != null && !codesMetasSouhaites.isEmpty()) {

      final MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaites
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
   *          l'URL ECDE du fichier sommaire.xml
   * @return le paramètre d'entrée pour l'opération "archivageMasse"
   */
  public static ArchivageMasse contruitParamsEntreeArchivageMasse(
                                                                  final String urlEcdeSommaire) {

    final ArchivageMasse archivageMasse = new ArchivageMasse();

    final ArchivageMasseRequestType archivageMasseRequest = new ArchivageMasseRequestType();

    archivageMasse.setArchivageMasse(archivageMasseRequest);

    // URL ECDE du sommaire
    final EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
    archivageMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
    URI ecdeUriSommaireUri;
    try {
      ecdeUriSommaireUri = new URI(urlEcdeSommaire);
    }
    catch (final MalformedURIException e) {
      throw new DemoRuntimeException(e);
    }
    ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);

    // Renvoie du paramètre d'entrée de l'opération archivageMasse
    return archivageMasse;

  }

  public static TransfertMasse contruitParamsEntreeTransfertMasse(final String urlEcdeSommaire, final String hash, final String typeHash) {

    final TransfertMasse transfertMasse = new TransfertMasse();

    final TransfertMasseRequestType transfertMasseRequest = new TransfertMasseRequestType();

    transfertMasse.setTransfertMasse(transfertMasseRequest);

    // URL ECDE du sommaire
    final EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
    transfertMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
    URI ecdeUriSommaireUri;
    try {
      ecdeUriSommaireUri = new URI(urlEcdeSommaire);
    }
    catch (final MalformedURIException e) {
      throw new DemoRuntimeException(e);
    }
    ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);

    transfertMasseRequest.setHash(hash);

    transfertMasseRequest.setTypeHash(typeHash);

    return transfertMasse;

  }

  public static Deblocage contruitParamsEntreeDeblocage(final String uuidJob) {
    final Deblocage deblocage = new Deblocage();
    final DeblocageRequestType deblocageRequest = new DeblocageRequestType();
    final UuidType uuid = new UuidType();
    uuid.setUuidType(uuidJob);
    deblocageRequest.setUuid(uuid.toString());
    deblocage.setDeblocage(deblocageRequest);
    return deblocage;
  }

  private static EcdeUrlType buildEcdeUrl(final String urlEcde) {

    final EcdeUrlType ecdeUrl = new EcdeUrlType();
    URI uriEcdeFichier;
    try {
      uriEcdeFichier = new URI(urlEcde);
    }
    catch (final MalformedURIException e) {
      throw new DemoRuntimeException(e);
    }
    ecdeUrl.setEcdeUrlType(uriEcdeFichier);

    return ecdeUrl;

  }

  private static ListeMetadonneeType buildListeMeta(
                                                    final Map<String, String> metadonnees) {

    final ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

    MetadonneeType metadonnee;
    MetadonneeCodeType metaCode;
    MetadonneeValeurType metaValeur;
    String code;
    String valeur;
    for (final Map.Entry<String, String> entry : metadonnees.entrySet()) {

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
                                                              final Map<String, String[]> metadonnees) {

    final ListeRangeMetadonneeType listeMetadonnee = new ListeRangeMetadonneeType();

    RangeMetadonneeType metadonnee;
    MetadonneeCodeType metaCode;
    MetadonneeValeurType metaValeurMin;
    MetadonneeValeurType metaValeurMax;
    String code;
    String valeurMin;
    String valeurMax;
    for (final Map.Entry<String, String[]> entry : metadonnees.entrySet()) {

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
   *          l'URL ECDE du fichier à archiver
   * @param metadonnees
   *          les métadonnées à associer au fichier
   * @return le paramètre d'entrée de l'opération "archivageUnitairePJ"
   */
  public static ArchivageUnitairePJ contruitParamsEntreeArchivageUnitairePJavecUrlEcde(
                                                                                       final String urlEcdeFichier, final Map<String, String> metadonnees) {

    final ArchivageUnitairePJ archivageUnitairePJ = new ArchivageUnitairePJ();

    final ArchivageUnitairePJRequestType archivageUnitairePJRequest = new ArchivageUnitairePJRequestType();

    archivageUnitairePJ.setArchivageUnitairePJ(archivageUnitairePJRequest);

    // URL ECDE
    final EcdeUrlType ecdeUrl = buildEcdeUrl(urlEcdeFichier);
    final ArchivageUnitairePJRequestTypeChoice_type0 choice = new ArchivageUnitairePJRequestTypeChoice_type0();
    archivageUnitairePJRequest
                              .setArchivageUnitairePJRequestTypeChoice_type0(choice);
    choice.setEcdeUrl(ecdeUrl);

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    archivageUnitairePJRequest.setMetadonnees(listeMetadonnee);

    // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
    return archivageUnitairePJ;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param nomFichier
   *          le nom du fichier à archiver
   * @param contenu
   *          le flux pointant vers le fichier à archiver
   * @param metadonnees
   *          les métadonnées à associer au fichier
   * @return le paramètre d'entrée de l'opération "archivageUnitairePJ"
   */
  public static ArchivageUnitairePJ contruitParamsEntreeArchivageUnitairePJavecContenu(
                                                                                       final String nomFichier, final InputStream contenu,
                                                                                       final Map<String, String> metadonnees) {

    final ArchivageUnitairePJ archivageUnitairePJ = new ArchivageUnitairePJ();

    final ArchivageUnitairePJRequestType archivageUnitairePJRequest = new ArchivageUnitairePJRequestType();

    archivageUnitairePJ.setArchivageUnitairePJ(archivageUnitairePJRequest);

    // Nom et contenu du fichier
    final DataFileType dataFile = new DataFileType();
    dataFile.setFileName(nomFichier);
    byte[] contenuBytes;
    try {
      contenuBytes = IOUtils.toByteArray(contenu);
    }
    catch (final IOException e) {
      throw new DemoRuntimeException(e);
    }
    final ByteArrayDataSource byteArray = new ByteArrayDataSource(contenuBytes);
    final DataHandler dataHandler = new DataHandler(byteArray);
    dataFile.setFile(dataHandler);
    final ArchivageUnitairePJRequestTypeChoice_type0 choice = new ArchivageUnitairePJRequestTypeChoice_type0();
    archivageUnitairePJRequest
                              .setArchivageUnitairePJRequestTypeChoice_type0(choice);
    choice.setDataFile(dataFile);

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    archivageUnitairePJRequest.setMetadonnees(listeMetadonnee);

    // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
    return archivageUnitairePJ;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param urlEcdeSommaire
   *          l'URL ECDE du sommaire.xml
   * @param typeHash
   *          le type de hash
   * @param hash
   *          le hash
   * @return le paramètre d'entrée de l'opération "archivageMasseAvecHash"
   */
  public static ArchivageMasseAvecHash contruitParamsEntreeArchivageMasseAvecHash(
                                                                                  final String urlEcdeSommaire, final String typeHash, final String hash) {

    final ArchivageMasseAvecHash archivageMasseAvecHash = new ArchivageMasseAvecHash();

    final ArchivageMasseAvecHashRequestType archivageMasseAvecHashRequest = new ArchivageMasseAvecHashRequestType();

    archivageMasseAvecHash
                          .setArchivageMasseAvecHash(archivageMasseAvecHashRequest);

    // URL ECDE du sommaire
    final EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
    archivageMasseAvecHashRequest.setUrlSommaire(ecdeUrlSommaireObj);
    URI ecdeUriSommaireUri;
    try {
      ecdeUriSommaireUri = new URI(urlEcdeSommaire);
    }
    catch (final MalformedURIException e) {
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
   *          l'identifiant du document à supprimer
   * @return le paramètre d'entrée de l'opération "suppression"
   */
  public static Suppression contruitParamsEntreeSuppression(final String idArchive) {

    final Suppression suppression = new Suppression();

    final SuppressionRequestType suppressionRequest = new SuppressionRequestType();

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
   *          l'identifiant du document à transférer
   * @return le paramètre d'entrée de l'opération "transfert"
   */
  public static Transfert contruitParamsEntreeTransfert(final String idArchive) {

    final Transfert transfert = new Transfert();

    final TransfertRequestType transfertRequest = new TransfertRequestType();

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
   *          l'identifiant unique du document à modifier
   * @param metadonnees
   *          les modifications de métadonnées
   * @return le paramètre d'entrée de l'opération "modification"
   */
  public static Modification contruitParamsEntreeModification(
                                                              final String idArchive, final Map<String, String> metadonnees) {

    final Modification modification = new Modification();

    final ModificationRequestType modificationRequest = new ModificationRequestType();

    modification.setModification(modificationRequest);

    // Identifiant de l'archive
    modificationRequest.setUuid(buildUuid(idArchive));

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    modificationRequest.setMetadonnees(listeMetadonnee);

    // Renvoie du paramètre d'entrée de l'opération modification
    return modification;

  }

  public static AjoutNote contruitParamsEntreeAjoutNote(final String idArchive,
                                                        final String contenuNote) {
    final AjoutNote ajoutNote = new AjoutNote();
    final AjoutNoteRequestType ajoutNoteRequest = new AjoutNoteRequestType();
    ajoutNote.setAjoutNote(ajoutNoteRequest);

    // Identifiant de l'archive
    ajoutNoteRequest.setUuid(buildUuid(idArchive));

    // Contenu de la note à ajouter au document
    final NoteTxtType paramNote = new NoteTxtType();
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
   *          l'URL ECDE du fichier à archiver
   * @param urlEcdeFichierFormatOrigine
   *          L'URL ECDE du fichier au format d'originie à rattacher
   * @param metadonnees
   *          les métadonnées à associer au fichier
   * @return le paramètre d'entrée de l'opération "stockageUnitaire"
   */
  public static StockageUnitaire contruitParamsEntreeStockageUnitaireAvecUrlEcde(
                                                                                 final String urlEcdeFichier, final String urlEcdeFichierFormatOrigine,
                                                                                 final Map<String, String> metadonnees) {

    final StockageUnitaire stockageUnitaire = new StockageUnitaire();
    final StockageUnitaireRequestType stockageUnitaireRequest = new StockageUnitaireRequestType();
    stockageUnitaire.setStockageUnitaire(stockageUnitaireRequest);

    // URL ECDE du document parent
    final EcdeUrlType ecdeUrlFichier = buildEcdeUrl(urlEcdeFichier);
    final StockageUnitaireRequestTypeChoice_type0 choice0 = new StockageUnitaireRequestTypeChoice_type0();
    stockageUnitaireRequest
                           .setStockageUnitaireRequestTypeChoice_type0(choice0);
    choice0.setUrlEcdeDoc(ecdeUrlFichier);

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    stockageUnitaireRequest.setMetadonnees(listeMetadonnee);

    // URL ECDE du document au format d'origine
    final EcdeUrlType ecdeUrlFichierFormatOrigine = buildEcdeUrl(urlEcdeFichierFormatOrigine);
    final StockageUnitaireRequestTypeChoice_type1 choice1 = new StockageUnitaireRequestTypeChoice_type1();
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
   *          le nom du fichier à archiver
   * @param contenu
   *          le flux pointant vers le fichier à archiver
   * @param nomFichier
   *          le nom du fichier au format d'origine à rattacher
   * @param contenu
   *          le flux pointant vers le fichier au format d'origine à rattacher
   * @param metadonnees
   *          les métadonnées à associer au fichier
   * @return le paramètre d'entrée de l'opération "stockageUnitaire"
   */
  public static StockageUnitaire contruitParamsEntreeStockageUnitaireavecContenu(
                                                                                 final String nomFichier, final InputStream contenu,
                                                                                 final String nomFichierFormatOrigine, final InputStream contenuFormatOrigine,
                                                                                 final Map<String, String> metadonnees) {

    final StockageUnitaire stockageUnitaire = new StockageUnitaire();
    final StockageUnitaireRequestType stockageUnitaireRequest = new StockageUnitaireRequestType();
    stockageUnitaire.setStockageUnitaire(stockageUnitaireRequest);

    // Nom et contenu du fichier
    final DataFileType dataFile = new DataFileType();
    dataFile.setFileName(nomFichier);
    byte[] contenuBytes;
    try {
      contenuBytes = IOUtils.toByteArray(contenu);
    }
    catch (final IOException e) {
      throw new DemoRuntimeException(e);
    }
    final ByteArrayDataSource byteArray = new ByteArrayDataSource(contenuBytes);
    final DataHandler dataHandler = new DataHandler(byteArray);
    dataFile.setFile(dataHandler);
    final StockageUnitaireRequestTypeChoice_type0 choice0 = new StockageUnitaireRequestTypeChoice_type0();
    stockageUnitaireRequest
                           .setStockageUnitaireRequestTypeChoice_type0(choice0);
    choice0.setDataFileDoc(dataFile);

    // Nom et contenu du fichier au format d'origine
    final DataFileType dataFileFormatOrigine = new DataFileType();
    dataFileFormatOrigine.setFileName(nomFichierFormatOrigine);
    byte[] contenuBytesFormatOrigine;
    try {
      contenuBytesFormatOrigine = IOUtils.toByteArray(contenuFormatOrigine);
    }
    catch (final IOException e) {
      throw new DemoRuntimeException(e);
    }
    final ByteArrayDataSource byteArrayFormatOrigine = new ByteArrayDataSource(
                                                                               contenuBytesFormatOrigine);
    final DataHandler dataHandlerFormatOrigine = new DataHandler(
                                                                 byteArrayFormatOrigine);
    dataFileFormatOrigine.setFile(dataHandlerFormatOrigine);
    final StockageUnitaireRequestTypeChoice_type1 choice1 = new StockageUnitaireRequestTypeChoice_type1();
    stockageUnitaireRequest
                           .setStockageUnitaireRequestTypeChoice_type1(choice1);
    choice1.setDataFileAttached(dataFileFormatOrigine);

    // Métadonnées
    final ListeMetadonneeType listeMetadonnee = buildListeMeta(metadonnees);
    stockageUnitaireRequest.setMetadonnees(listeMetadonnee);

    // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
    return stockageUnitaire;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param uuidDocParent
   *          L'UUID du document dont on cherche le document au format
   *          d'origine
   * @return le paramètre d'entrée de l'opération "getDocFormatOrigine"
   */
  public static GetDocFormatOrigine contruitParamsEntreeGetDocFormatOrigine(
                                                                            final UUID uuidDocParent) {

    final GetDocFormatOrigine getDocFormatOrigine = new GetDocFormatOrigine();
    final GetDocFormatOrigineRequestType getDocFormatOrigineRequest = new GetDocFormatOrigineRequestType();
    getDocFormatOrigine.setGetDocFormatOrigine(getDocFormatOrigineRequest);

    // UUID du document parent
    final UuidType uuidType = new UuidType();
    uuidType.setUuidType(uuidDocParent.toString());
    getDocFormatOrigineRequest.setIdDoc(uuidType);

    return getDocFormatOrigine;

  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param requete
   *          La requête de suppression des documents
   * @return le paramètre d'entrée de l'opération "suppressionMasse"
   */
  public static SuppressionMasse contruitParamsEntreeSuppressionMasse(
                                                                      final String requete) {
    final SuppressionMasse suppressionMasse = new SuppressionMasse();
    final SuppressionMasseRequestType suppressionMasseRequest = new SuppressionMasseRequestType();
    suppressionMasse.setSuppressionMasse(suppressionMasseRequest);

    // Requete de suppression
    final RequeteRechercheType requeteType = new RequeteRechercheType();
    requeteType.setRequeteRechercheType(requete);
    suppressionMasseRequest.setRequete(requeteType);

    return suppressionMasse;
  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idTraitementSuppression
   *          L'identifiant du traitement de suppression de masse à restorer
   * @return le paramètre d'entrée de l'opération "restoreMasse"
   */
  public static RestoreMasse contruitParamsEntreeRestoreMasse(
                                                              final String idTraitementSuppression) {
    final RestoreMasse restoreMasse = new RestoreMasse();
    final RestoreMasseRequestType restoreMasseRequest = new RestoreMasseRequestType();
    restoreMasse.setRestoreMasse(restoreMasseRequest);

    final UuidType requeteType = new UuidType();
    requeteType.setUuidType(idTraitementSuppression);
    restoreMasseRequest.setUuid(requeteType);

    return restoreMasse;
  }

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param listeUuid
   *          La liste des uuid des traitements de masse
   * @return le paramètre d'entrée de l'opération "etatTraitementsMasse"
   */
  public static EtatTraitementsMasse contruitParamsEntreeEtatTraitementsMasse(
                                                                              final List<String> listeUuid) {

    final EtatTraitementsMasse etatTraitementsMasse = new EtatTraitementsMasse();
    final EtatTraitementsMasseRequestType etatTraitementsMasseRequest = new EtatTraitementsMasseRequestType();
    etatTraitementsMasse.setEtatTraitementsMasse(etatTraitementsMasseRequest);

    final ListeUuidType listeUuidType = new ListeUuidType();
    for (final String uuid : listeUuid) {
      final UuidType uuidType = new UuidType();
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
   *          l'URL ECDE du sommaire.xml
   * @param typeHash
   *          le type de hash
   * @param hash
   *          le hash
   * @return le paramètre d'entrée de l'opération "ModificationMasse"
   */
  public static ModificationMasse contruitParamsEntreeModificationMasse(
                                                                        final String urlEcdeSommaire, final String typeHash, final String hash,
                                                                        final String codeTraitement) {

    final ModificationMasse modificationMasse = new ModificationMasse();

    final ModificationMasseRequestType modificationMasseRequest = new ModificationMasseRequestType();

    modificationMasse.setModificationMasse(modificationMasseRequest);

    // URL ECDE du sommaire
    final EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
    modificationMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
    URI ecdeUriSommaireUri;
    try {
      ecdeUriSommaireUri = new URI(urlEcdeSommaire);
    }
    catch (final MalformedURIException e) {
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

  /**
   * Transformation des objets "pratiques" en objets Axis2 pour un appel de
   * service web
   * 
   * @param idJob
   *          l'identifiant unique du job que l'on veut relancer en mode
   *          "Reprise".
   * @return le paramètre d'entrée de l'opération "copie"
   */
  public static Reprise contruitParamsEntreeReprise(final String idJob) {

    final Reprise reprise = new Reprise();

    final RepriseRequestType repriseRequest = new RepriseRequestType();

    reprise.setReprise(repriseRequest);

    repriseRequest.setUuid(buildUuid(idJob).toString());

    return reprise;

  }

}
