package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import java.util.Iterator;
import java.util.List;

import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationMTOM;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationMTOMRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EcdeUrlSommaireType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EcdeUrlType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RequeteRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.UuidType;

/**
 * Factory d'objets pour la couche WebService SaeService
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class SaeServiceObjectFactory {

   
   private SaeServiceObjectFactory() {
      
   }
   
   
   /**
    * Construit un objet URL ECDE pour la couche WebService à partir d'une String
    * 
    * @param urlEcde l'URL ECDE sous la forme d'une String
    * @return l'URL ECDE sous la forme attendu par la couche WebService
    * @throws MalformedURIException en cas d'URI incorrect
    */
   public static EcdeUrlType buildEcdeUrl(String urlEcde) throws MalformedURIException {
      EcdeUrlType ecdeUrlType = new EcdeUrlType();
      ecdeUrlType.setEcdeUrlType(new URI(urlEcde));
      return ecdeUrlType;
   }
   
   
   /**
    * Construit un objet URL ECDE pour la couche WebService à partir d'une String
    * 
    * @param urlEcdeSommaire l'URL ECDE d'un sommaire.xml sous la forme d'une String
    * @return l'URL ECDE sous la forme attendu par la couche WebService
    * @throws MalformedURIException en cas d'URI incorrect
    */
   public static EcdeUrlSommaireType buildEcdeUrlSommaire(String urlEcdeSommaire) throws MalformedURIException {
      EcdeUrlSommaireType ecdeUrl = new EcdeUrlSommaireType();
      ecdeUrl.setEcdeUrlSommaireType(new URI(urlEcdeSommaire));
      return ecdeUrl;
   }
   

   /**
    * Construit un objet "Liste de métadonnées" pour la couche WebService
    * 
    * @param metadonnees la liste des métadonnées à utiliser
    * @return l'objet pour la couche WebService
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static ListeMetadonneeType buildListeMetadonnes(MetadonneeValeurList metadonnees) {
      
      ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();
      
      MetadonneeType[] tabMetadonneeType = new MetadonneeType[metadonnees.size()];
      listeMetadonneeType.setMetadonnee(tabMetadonneeType);
      
      int indice = 0 ;
      MetadonneeType metadonneeType;
      MetadonneeCodeType codeType;
      MetadonneeValeurType valeurType;
      for(MetadonneeValeur meta: metadonnees) {
         
         metadonneeType = new MetadonneeType();
         tabMetadonneeType[indice] = metadonneeType; 
         indice++;
         
         // Le code
         codeType = new MetadonneeCodeType();
         codeType.setMetadonneeCodeType(meta.getCode());
         metadonneeType.setCode(codeType);
         
         // La valeur
         valeurType = new MetadonneeValeurType();
         valeurType.setMetadonneeValeurType(meta.getValeur());
         metadonneeType.setValeur(valeurType);
         
      }
      
      return listeMetadonneeType;
      
   }
   
   
   /**
    * Construit un objet "Requete Lucune" pour la couche WebService
    * 
    * @param requeteLucene la requête LUCENE sous forme de chaîne de caractères
    * @return l'objet pour la couche WebService
    */ 
   public static RequeteRechercheType buildRequeteLucene(String requeteLucene) {
      
      RequeteRechercheType requeteRechercheType = new RequeteRechercheType() ;
      
      requeteRechercheType.setRequeteRechercheType(requeteLucene);
      
      return requeteRechercheType;
      
   }
   
   
   
   /**
    * Construit un objet "Liste de codes de métadonnées" pour la couche WebService
    * 
    * @param codesMetadonnees la liste des codes des métadonnées, sous la forme d'une List
    * @return l'objet pour la couche WebService
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static ListeMetadonneeCodeType buildListeCodesMetadonnes(
         List<String> codesMetadonnees) {
      
      ListeMetadonneeCodeType listeMetadonneeCodeType = new ListeMetadonneeCodeType();
      
      MetadonneeCodeType[] tabMetadonneeCodeType = 
         new MetadonneeCodeType[codesMetadonnees.size()];
      listeMetadonneeCodeType.setMetadonneeCode(tabMetadonneeCodeType);
      
      int indice = 0 ;
      MetadonneeCodeType metadonneeCodeType;
      
      Iterator<String> iterator = codesMetadonnees.iterator();
      while ( iterator.hasNext() ){
         
         metadonneeCodeType = new MetadonneeCodeType();
         tabMetadonneeCodeType[indice] = metadonneeCodeType; 
         indice++;
         
         metadonneeCodeType.setMetadonneeCodeType(iterator.next());
         
      }
      
      return listeMetadonneeCodeType;
      
   }
   
   
   /**
    * Construit un objet "UUID" pour la couche WebService
    * 
    * @param uuid l'UUID sous la forme d'une chaîne de caractères
    * @return l'objet pour la couche WebService
    */
   public static UuidType buildUuid(String uuid) {
      
      UuidType uuidType = new UuidType();
      
      uuidType.setUuidType(uuid);
      
      return uuidType;
      
   }
   
   
   /**
    * Construit un objet de requête pour l'opération "archivageUnitaire"
    * 
    * @param urlEcde l'URL ECDE du document à archiver
    * @param metadonnees les métadonnées associés au document
    * @return l'objet pour la couche web service
    */
   public static ArchivageUnitaire buildArchivageUnitaireRequest(
         String urlEcde,
         MetadonneeValeurList metadonnees) {
      
      ArchivageUnitaire archivageUnitaire = new ArchivageUnitaire();
      
      ArchivageUnitaireRequestType archivageUnitaireReqType = 
         new ArchivageUnitaireRequestType();
      
      archivageUnitaire.setArchivageUnitaire(archivageUnitaireReqType);
      
      // L'URL ECDE
      try {
         archivageUnitaireReqType.setEcdeUrl(buildEcdeUrl(urlEcde));
      } catch (MalformedURIException e) {
         throw new IntegrationRuntimeException(e);
      }
      
      // Les métadonnées
      ListeMetadonneeType listeMetadonneeType = buildListeMetadonnes(metadonnees);
      archivageUnitaireReqType.setMetadonnees(listeMetadonneeType);
      
      // fin
      return archivageUnitaire;
      
   }
   
   

   /**
    * Renvoie un objet de requête pour le service web archivageMasse
    * 
    * @param urlSommaire l'URL du fichier sommaire.xml
    * @return l'objet pour la couche WebService
    */
   public static ArchivageMasse buildArchivageMasseRequest(
         String urlSommaire) {
      
      ArchivageMasse archivageMasse = new ArchivageMasse();
      
      ArchivageMasseRequestType archivageMasseReqType = 
         new ArchivageMasseRequestType();
      
      archivageMasse.setArchivageMasse(archivageMasseReqType);
      
      // URL de sommaire.xml
      EcdeUrlSommaireType urlEcdeSommaire;
      try {
         urlEcdeSommaire = buildEcdeUrlSommaire(urlSommaire);
      } catch (MalformedURIException e) {
         throw new IntegrationRuntimeException(e);
      }
      archivageMasseReqType.setUrlSommaire(urlEcdeSommaire);
      
      // fin
      return archivageMasse;
      
   }
   
   
   /**
    * Construit un objet de requête pour le service web "consultation"
    * 
    * @param idArchivage l'identifiant d'archivage
    * @param la liste des codes des métadonnées souhaitées
    * @return l'objet pour la couche WebService
    */
   public static Consultation buildConsultationRequest(
         String idArchivage,
         CodeMetadonneeList codeMetadonnees) {
      
      Consultation consultation = new Consultation();
      
      ConsultationRequestType consultationReqType = 
         new ConsultationRequestType();
      
      consultation.setConsultation(consultationReqType);
      
      
      // UUID
      UuidType uuid = SaeServiceObjectFactory.buildUuid(idArchivage);
      consultationReqType.setIdArchive(uuid);
      
      // Les codes des métadonnées souhaitées
      // Les métadonnées ne sont ajoutées que SI au moins 1 métadonnée est demandée
      if (!CollectionUtils.isEmpty(codeMetadonnees)) {
         ListeMetadonneeCodeType codesMetadonnees = 
            SaeServiceObjectFactory.buildListeCodesMetadonnes(codeMetadonnees);
         consultationReqType.setMetadonnees(codesMetadonnees);
      }
      
      // fin
      return consultation;
      
   }
   
   
   
   /**
    * Construit un objet de requête pour le service web "consultationMTOM"
    * 
    * @param idArchivage l'identifiant d'archivage
    * @param la liste des codes des métadonnées souhaitées
    * @return l'objet pour la couche WebService
    */
   public static ConsultationMTOM buildConsultationMTOMRequest(
         String idArchivage,
         CodeMetadonneeList codeMetadonnees) {
      
      ConsultationMTOM consultation = new ConsultationMTOM();
      
      ConsultationMTOMRequestType consultationReqType = 
         new ConsultationMTOMRequestType();
      
      consultation.setConsultationMTOM(consultationReqType);
      
      
      // UUID
      UuidType uuid = SaeServiceObjectFactory.buildUuid(idArchivage);
      consultationReqType.setIdArchive(uuid);
      
      // Les codes des métadonnées souhaitées
      // Les métadonnées ne sont ajoutées que SI au moins 1 métadonnée est demandée
      if (!CollectionUtils.isEmpty(codeMetadonnees)) {
         ListeMetadonneeCodeType codesMetadonnees = 
            SaeServiceObjectFactory.buildListeCodesMetadonnes(codeMetadonnees);
         consultationReqType.setMetadonnees(codesMetadonnees);
      }
      
      // fin
      return consultation;
      
   }
   
   
   /**
    * Construit un objet de requête pour le service web "recherche"
    * 
    * @param requeteLucene la requête LUCENE
    * @param codeMetadonnees la liste des codes des métadonnées que l'on veut
    * dans les résultats de recherche
    * @return l'objet pour la couche WebService
    */
   public static Recherche buildRechercheRequest(
         String requeteLucene,
         CodeMetadonneeList codeMetadonnees) {
      
       Recherche recherche = new Recherche();
       
       RechercheRequestType rechercheReqType = 
          new RechercheRequestType();
       
       recherche.setRecherche(rechercheReqType);
       
       // La requête Lucene
       RequeteRechercheType requeteLuceneWs = SaeServiceObjectFactory.buildRequeteLucene(
             requeteLucene);
       rechercheReqType.setRequete(requeteLuceneWs);
       
       // Les codes des métadonnées souhaitées
       ListeMetadonneeCodeType codesMetadonnees = 
          SaeServiceObjectFactory.buildListeCodesMetadonnes(codeMetadonnees);
       rechercheReqType.setMetadonnees(codesMetadonnees);
   
       // fin
       return recherche;
      
   }
   
   
}
