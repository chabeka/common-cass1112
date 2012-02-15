package sae.client.demo.webservice.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.Consultation;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlSommaireType;
import sae.client.demo.webservice.modele.SaeServiceStub.EcdeUrlType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ListeMetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeCodeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeValeurType;
import sae.client.demo.webservice.modele.SaeServiceStub.Recherche;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheRequestType;
import sae.client.demo.webservice.modele.SaeServiceStub.RequeteRechercheType;
import sae.client.demo.webservice.modele.SaeServiceStub.UuidType;

/**
 * Construction d'objets du modèle Axis2
 */
public class Axis2ObjectFactory {

   
   public static ArchivageUnitaire contruitParamsEntreeArchivageUnitaire(
         String urlEcdeFichier,
         HashMap<String,String> metadonnees) {
      
      
      ArchivageUnitaire archivageUnitaire = 
         new ArchivageUnitaire();

      ArchivageUnitaireRequestType archivageUnitaireRequest = 
         new ArchivageUnitaireRequestType();  
      
      archivageUnitaire.setArchivageUnitaire(archivageUnitaireRequest);
      
      
      // URL ECDE
      EcdeUrlType ecdeUrl = new EcdeUrlType();
      archivageUnitaireRequest.setEcdeUrl(ecdeUrl);
      URI uriEcdeFichier;
      try {
         uriEcdeFichier = new URI(urlEcdeFichier);
      } catch (MalformedURIException e) {
         throw new DemoRuntimeException(e);
      }
      ecdeUrl.setEcdeUrlType(uriEcdeFichier);
      
      
      // Métadonnées
      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType(); 
      archivageUnitaireRequest.setMetadonnees(listeMetadonnee);
      MetadonneeType metadonnee;
      MetadonneeCodeType metaCode;
      MetadonneeValeurType metaValeur;
      String code;
      String valeur;
      for (Map.Entry<String, String> entry: metadonnees.entrySet()) {
         
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
      
      
      // Renvoie du paramètre d'entrée de l'opération archivageUnitaire
      return archivageUnitaire;
      
   }
   
   
   public static Consultation contruitParamsEntreeConsultation(
         String idArchive) {
      
      Consultation consultation = 
         new Consultation();
      
      ConsultationRequestType consultationRequest = 
         new ConsultationRequestType();
      
      consultation.setConsultation(
            consultationRequest);
      
      // L'identifiant unique de l'archivage
      UuidType uuid = new UuidType();
      uuid.setUuidType(idArchive);
      consultationRequest.setIdArchive(uuid);
      
      // Renvoie du paramètre d'entrée de l'opération consultation
      return consultation;
      
   }
   
   
   
   /**
    * Transformation des objets "pratiques" en objets Axis2 pour un appel de
    * service web 
    * 
    * @param requeteRecherche la requête de recherche
    * @param codesMetasSouhaitess les codes de métadonnées souhaitées
    *                             dans les résultats de recherche.
    * 
    * @return le paramètre d'entrée pour l'opération "recherche"
    */
   public static Recherche contruitParamsEntreeRecherche(
         String requeteRecherche,
         List<String> codesMetasSouhaitess) {
    
      
      Recherche recherche = 
         new Recherche();
      
      RechercheRequestType rechercheRequest = 
         new RechercheRequestType(); 
      
      recherche.setRecherche(rechercheRequest);

      
      // Requête de recherche
      RequeteRechercheType requeteRechercheObj = new RequeteRechercheType();
      requeteRechercheObj.setRequeteRechercheType(requeteRecherche);
      rechercheRequest.setRequete(requeteRechercheObj);
      
      // Codes des métadonnées souhaitées dans les résultats de recherche
      ListeMetadonneeCodeType listeMetadonneeCode = new ListeMetadonneeCodeType();
      rechercheRequest.setMetadonnees(listeMetadonneeCode);
      if ((codesMetasSouhaitess!=null) && (codesMetasSouhaitess.size()>0)) {
         
         MetadonneeCodeType[] arrMetadonneeCode = new MetadonneeCodeType[codesMetasSouhaitess.size()];
         
         MetadonneeCodeType metadonneeCode;
         for(int i=0;i<codesMetasSouhaitess.size();i++) {
            metadonneeCode = new MetadonneeCodeType();
            metadonneeCode.setMetadonneeCodeType(codesMetasSouhaitess.get(i));
            arrMetadonneeCode[i] = metadonneeCode; 
         }
         
         listeMetadonneeCode.setMetadonneeCode(arrMetadonneeCode);
         
      }
      else {
         listeMetadonneeCode.setMetadonneeCode(null);
      }
      
      
      // Renvoie du paramètre d'entrée de l'opération recherche
      return recherche ;
      
   }
   
   
   public static ArchivageMasse contruitParamsEntreeArchivageMasse(
         String urlEcdeSommaire) {
      
      
      ArchivageMasse archivageMasse = 
         new ArchivageMasse() ;
      
      ArchivageMasseRequestType archivageMasseRequest = 
         new ArchivageMasseRequestType();
      
      archivageMasse.setArchivageMasse(
            archivageMasseRequest);
      
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
   
}
