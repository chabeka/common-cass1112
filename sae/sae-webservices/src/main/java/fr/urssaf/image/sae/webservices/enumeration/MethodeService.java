/**
 * 
 */
package fr.urssaf.image.sae.webservices.enumeration;

/**
 * @author CER6990937
 *
 */
public enum MethodeService {

   ARCHIVAGE_UNITAIRE_PJ("archivageUnitairePJ",
         fr.cirtil.www.saeservice.ArchivageUnitairePJ.class,
         fr.cirtil.www.saeservice.ArchivageUnitairePJResponse.class), RECHERCHE(
         "recherche", fr.cirtil.www.saeservice.Recherche.class,
         fr.cirtil.www.saeservice.RechercheResponse.class), PING("ping",
         fr.cirtil.www.saeservice.PingRequest.class,
         fr.cirtil.www.saeservice.PingResponse.class), ARCHIVAGE_UNITAIRE(
         "archivageUnitaire", fr.cirtil.www.saeservice.ArchivageUnitaire.class,
         fr.cirtil.www.saeservice.ArchivageUnitaireResponse.class), PING_SECURE(
         "pingSecure", fr.cirtil.www.saeservice.PingSecureRequest.class,
         fr.cirtil.www.saeservice.PingSecureResponse.class), CONSULTATION(
         "consultation", fr.cirtil.www.saeservice.Consultation.class,
         fr.cirtil.www.saeservice.ConsultationResponse.class), CONSULTATION_MTOM(
         "consultationMTOM", fr.cirtil.www.saeservice.ConsultationMTOM.class,
         fr.cirtil.www.saeservice.ConsultationMTOMResponse.class), ARCHIVAGE_MASSE(
         "archivageMasse", fr.cirtil.www.saeservice.ArchivageMasse.class,
         fr.cirtil.www.saeservice.ArchivageMasseResponse.class), ARCHIVAGE_MASSE_HASH(
         "archivageMasseAvecHash",
         fr.cirtil.www.saeservice.ArchivageMasseAvecHash.class,
         fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse.class), MODIFICATION(
         "modification", fr.cirtil.www.saeservice.Modification.class,
         fr.cirtil.www.saeservice.ModificationResponse.class), SUPPRESSION(
         "suppression", fr.cirtil.www.saeservice.Suppression.class,
         fr.cirtil.www.saeservice.SuppressionResponse.class), RECUPERATION_METADONNEES(
         "recuperationMetadonnees",
         fr.cirtil.www.saeservice.RecuperationMetadonnees.class,
         fr.cirtil.www.saeservice.RecuperationMetadonneesResponse.class), TRANSFERT(
         "transfert", fr.cirtil.www.saeservice.Transfert.class,
         fr.cirtil.www.saeservice.TransfertResponse.class), CONSULTATION_AFFICHABLE(
         "consultationAffichable",
         fr.cirtil.www.saeservice.ConsultationAffichable.class,
         fr.cirtil.www.saeservice.ConsultationAffichableResponse.class), RECHERCHE_NB_RES(
         "rechercheNbRes", fr.cirtil.www.saeservice.RechercheNbRes.class,
         fr.cirtil.www.saeservice.RechercheNbResResponse.class), RECHERCHE_PAR_ITERATEUR(
         "rechercheParIterateur",
         fr.cirtil.www.saeservice.RechercheParIterateur.class,
         fr.cirtil.www.saeservice.RechercheParIterateurResponse.class), AJOUT_NOTE(
         "ajoutNote", fr.cirtil.www.saeservice.AjoutNote.class,
         fr.cirtil.www.saeservice.AjoutNoteResponse.class), STOCKAGE_UNITAIRE(
         "stockageUnitaire", fr.cirtil.www.saeservice.StockageUnitaire.class,
         fr.cirtil.www.saeservice.StockageUnitaireResponse.class), GET_DOC_FORMAT_ORIG(
         "getDocFormatOrigine",
         fr.cirtil.www.saeservice.GetDocFormatOrigine.class,
         fr.cirtil.www.saeservice.GetDocFormatOrigineResponse.class), RESTORE_MASSE(
         "restoreMasse", fr.cirtil.www.saeservice.RestoreMasse.class,
         fr.cirtil.www.saeservice.RestoreMasseResponse.class), SUPPRESSION_MASSE(
         "suppressionMasse", fr.cirtil.www.saeservice.SuppressionMasse.class,
         fr.cirtil.www.saeservice.SuppressionMasseResponse.class), ETAT_TRAITEMENT_MASSE(
         "etatTraitementsMasse",
         fr.cirtil.www.saeservice.EtatTraitementsMasse.class,
         fr.cirtil.www.saeservice.EtatTraitementsMasseResponse.class), COPIE(
         "copie", fr.cirtil.www.saeservice.Copie.class,
         fr.cirtil.www.saeservice.CopieResponse.class), DOCUMENT_EXISTANT(
         "documentExistant", fr.cirtil.www.saeservice.DocumentExistant.class,
         fr.cirtil.www.saeservice.DocumentExistantResponse.class), CONSULTATION_GNT_GNS(
         "consultationGNTGNS",
         fr.cirtil.www.saeservice.ConsultationGNTGNS.class,
         fr.cirtil.www.saeservice.ConsultationGNTGNSResponse.class), MODIFICATION_MASSE(
         "modificationMasse", fr.cirtil.www.saeservice.ModificationMasse.class,
         fr.cirtil.www.saeservice.ModificationMasseResponse.class), TRANSFERT_MASSE(
         "transfertMasse", fr.cirtil.www.saeservice.TransfertMasse.class,
         fr.cirtil.www.saeservice.TransfertMasseResponse.class), DEBLOCAGE(
         "deblocage", fr.cirtil.www.saeservice.Deblocage.class,
         fr.cirtil.www.saeservice.DeblocageResponse.class), REPRISE("reprise",
         fr.cirtil.www.saeservice.Reprise.class,
         fr.cirtil.www.saeservice.RepriseResponse.class);

   /**
    * Nom methode
    */
   private String nomMethode;

   /**
    * Classe representant la methode de requete
    */
   private Class classeMethodeRequest;

   /**
    * Classe representant la methode de reponse
    */
   private Class classeMethodeResponse;

   /**
    * Constructeur
    * 
    * @param nomMethode
    */
   private MethodeService(String nomMethode, Class classeMethodeRequest,
         Class classeMethodeResponse) {
      this.nomMethode = nomMethode;
      this.classeMethodeRequest = classeMethodeRequest;
      this.classeMethodeResponse = classeMethodeResponse;
   }

   /**
    * @return the nomMethode
    */
   public String getNomMethode() {
      return nomMethode;
   }

   /**
    * @return the classeMethodeResponse
    */
   public Class getClasseMethodeResponse() {
      return classeMethodeResponse;
   }

   /**
    * @return the classeMethodeRequest
    */
   public Class getClasseMethodeRequest() {
      return classeMethodeRequest;
   }

}
