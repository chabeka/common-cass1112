package fr.urssaf.image.sae.webservices.service.factory;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.commons.collections.CollectionUtils;

import fr.urssaf.image.sae.webservices.factory.ObjectModeleFactory;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseAvecHash;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseAvecHashRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJ;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJRequestTypeChoice_type0;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationMTOM;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationMTOMRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.DataFileType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.EcdeUrlSommaireType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.EcdeUrlType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Modification;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ModificationRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RequeteRechercheType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Suppression;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.SuppressionRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.UuidType;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Classe d'instanciation des requêtes pour consommer les différents web
 * services
 * 
 * 
 */
public final class RequestServiceFactory {

   private RequestServiceFactory() {

   }

   /**
    * 
    * @param lucene
    *           La requête de recherche
    * @param codes
    *           La liste des codes des métadonnées voulues
    * @return instance {@link Recherche}
    */
   public static Recherche createRecherche(String lucene, String... codes) {

      Recherche request = new Recherche();
      RechercheRequestType requestType = new RechercheRequestType();

      RequeteRechercheType requete = new RequeteRechercheType();
      requete.setRequeteRechercheType(lucene);
      requestType.setRequete(requete);

      ListeMetadonneeCodeType metadonnees = new ListeMetadonneeCodeType();

      for (String code : codes) {

         MetadonneeCodeType codeType = ObjectModeleFactory
               .createMetadonneeCodeType(code);
         metadonnees.addMetadonneeCode(codeType);
      }

      requestType.setMetadonnees(metadonnees);

      request.setRecherche(requestType);

      return request;
   }

   /**
    * 
    * @param uuid
    *           L'identifiant unique d'archivage de l'archive à consulter
    * @return instance de {@link Consultation}
    */
   public static Consultation createConsultation(String uuid, List<String> listMetaData) {

      Consultation request = new Consultation();

      ConsultationRequestType requestType = new ConsultationRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);

      requestType.setIdArchive(uuidType);

      ListeMetadonneeCodeType listeMD = null;
      
      if (CollectionUtils.isNotEmpty(listMetaData)) {
         listeMD = new ListeMetadonneeCodeType();
         MetadonneeCodeType codeType;
         
         for (String metaData : listMetaData) {
            codeType = new MetadonneeCodeType();
            codeType.setMetadonneeCodeType(metaData);
            listeMD.addMetadonneeCode(codeType);
         }
      }
      
      requestType.setMetadonnees(listeMD);

      request.setConsultation(requestType);

      return request;
   }
   
   /**
    * 
    * @param uuid
    *           L'identifiant unique d'archivage de l'archive à consulter
    * @return instance de {@link Consultation}
    */
   public static ConsultationMTOM createConsultationMTOM(String uuid, List<String> listMetaData) {

      ConsultationMTOM request = new ConsultationMTOM();

      ConsultationMTOMRequestType requestMTOMType = new ConsultationMTOMRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);

      requestMTOMType.setIdArchive(uuidType);

      ListeMetadonneeCodeType listeMD = null;
      
      if (CollectionUtils.isNotEmpty(listMetaData)) {
         listeMD = new ListeMetadonneeCodeType();
         MetadonneeCodeType codeType;
         
         for (String metaData : listMetaData) {
            codeType = new MetadonneeCodeType();
            codeType.setMetadonneeCodeType(metaData);
            listeMD.addMetadonneeCode(codeType);
         }
      }
      
      requestMTOMType.setMetadonnees(listeMD);

      request.setConsultationMTOM(requestMTOMType);

      return request;
   }

   /**
    * 
    * @param url
    *           L'URL ECDE du fichier sommaire.xml décrivant le traitement de
    *           masse
    * @return instance de {@link ArchivageMasse}
    */
   public static ArchivageMasse createArchivageMasse(URI url) {

      ArchivageMasse request = new ArchivageMasse();

      ArchivageMasseRequestType requestType = new ArchivageMasseRequestType();
      request.setArchivageMasse(requestType);

      EcdeUrlSommaireType urlType = new EcdeUrlSommaireType();

      urlType.setEcdeUrlSommaireType(ConverterUtil.convertToAnyURI(url
            .toASCIIString()));
      requestType.setUrlSommaire(urlType);

      return request;

   }

   /**
    * 
    * @param url
    *           l'objet numérique est représenté soit par son URL ECDE
    * @param metadonnees
    *           Les métadonnées.
    * @return instance de {@link ArchivageUnitaire}
    */
   public static ArchivageUnitaire createArchivageUnitaire(URI url,
         Collection<Metadata> metadonnees) {

      ArchivageUnitaire request = createArchivageUnitaire(metadonnees);

      // instanciation de EcdeUrlType
      EcdeUrlType ecdeURL = new EcdeUrlType();
      ecdeURL
            .setEcdeUrlType(ConverterUtil.convertToAnyURI(url.toASCIIString()));
      request.getArchivageUnitaire().setEcdeUrl(ecdeURL);

      return request;

   }

   private static ArchivageUnitaire createArchivageUnitaire(
         Collection<Metadata> metadonnees) {

      ArchivageUnitaire request = new ArchivageUnitaire();

      ArchivageUnitaireRequestType requestType = new ArchivageUnitaireRequestType();

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

      for (Metadata metadonnee : metadonnees) {

         MetadonneeType type = ObjectModeleFactory.createMetadonneeType();

         MetadonneeCodeType codeType = ObjectModeleFactory
               .createMetadonneeCodeType(metadonnee.getCode());
         type.setCode(codeType);

         MetadonneeValeurType valeurType = ObjectModeleFactory
               .createMetadonneeValeurType(metadonnee.getValue());
         type.setValeur(valeurType);

         listeMetadonnee.addMetadonnee(type);
      }

      requestType.setMetadonnees(listeMetadonnee);
      request.setArchivageUnitaire(requestType);

      return request;

   }
   
   /**
    * 
    * @param url
    *           l'objet numérique est représenté soit par son URL ECDE
    * @param metadonnees
    *           Les métadonnées.
    * @return instance de {@link ArchivageUnitaire}
    */
   public static ArchivageUnitairePJ createArchivagePJUnitaire(URI url,
         Collection<Metadata> metadonnees) {

      ArchivageUnitairePJ request = createArchivageUnitairePJ(metadonnees);

      // instanciation de EcdeUrlType
      EcdeUrlType ecdeURL = new EcdeUrlType();
      ecdeURL
            .setEcdeUrlType(ConverterUtil.convertToAnyURI(url.toASCIIString()));
      
      ArchivageUnitairePJRequestTypeChoice_type0 type_0 = new ArchivageUnitairePJRequestTypeChoice_type0();
      type_0.setEcdeUrl(ecdeURL);
      
      ArchivageUnitairePJRequestType archivageUnitairePJ = request.getArchivageUnitairePJ();
      archivageUnitairePJ.setArchivageUnitairePJRequestTypeChoice_type0(type_0);

      return request;

   }
   
   /**
    * 
    * @param fileName
    *           nom du fichier à archiver
    * @param contenu
    *           contenu du fichier à archiver
    * @param metadonnees
    *           Les métadonnées.
    * @return instance de {@link ArchivageUnitaire}
    */
   public static ArchivageUnitairePJ createArchivagePJUnitaire(String fileName, byte[] contenu,
         Collection<Metadata> metadonnees) {

      ArchivageUnitairePJ request = createArchivageUnitairePJ(metadonnees);

      
      ArchivageUnitairePJRequestTypeChoice_type0 type_0 = new ArchivageUnitairePJRequestTypeChoice_type0();
      DataFileType data = new DataFileType();
      data.setFileName(fileName);
      
      
      ByteArrayDataSource byteArray = new ByteArrayDataSource(contenu);
      DataHandler dataHandler = new DataHandler(byteArray);
      data.setFile(dataHandler);
      type_0.setDataFile(data);
      
      ArchivageUnitairePJRequestType archivageUnitairePJ = request.getArchivageUnitairePJ();
      archivageUnitairePJ.setArchivageUnitairePJRequestTypeChoice_type0(type_0);

      return request;

   }
   
   private static ArchivageUnitairePJ createArchivageUnitairePJ(
         Collection<Metadata> metadonnees) {

      ArchivageUnitairePJ request = new ArchivageUnitairePJ();

      ArchivageUnitairePJRequestType requestType = new ArchivageUnitairePJRequestType();

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();

      for (Metadata metadonnee : metadonnees) {

         MetadonneeType type = ObjectModeleFactory.createMetadonneeType();

         MetadonneeCodeType codeType = ObjectModeleFactory
               .createMetadonneeCodeType(metadonnee.getCode());
         type.setCode(codeType);

         MetadonneeValeurType valeurType = ObjectModeleFactory
               .createMetadonneeValeurType(metadonnee.getValue());
         type.setValeur(valeurType);

         listeMetadonnee.addMetadonnee(type);
      }

      requestType.setMetadonnees(listeMetadonnee);
      request.setArchivageUnitairePJ(requestType);

      return request;

   }
   
   /**
    * 
    * @param url
    *           L'URL ECDE du fichier sommaire.xml décrivant le traitement de
    *           masse
    * @param hash
    *            Hash du fichier sommaire.xml
    * @param typeHash
    *            l'ago de hash utilisé
    * @return instance de {@link ArchivageMasse}
    */
   public static ArchivageMasseAvecHash createArchivageMasseAvecHash(URI url, String hash, String typeHash) {

      ArchivageMasseAvecHash request = new ArchivageMasseAvecHash();

      ArchivageMasseAvecHashRequestType requestType = new ArchivageMasseAvecHashRequestType();
      request.setArchivageMasseAvecHash(requestType);

      EcdeUrlSommaireType urlType = new EcdeUrlSommaireType();

      urlType.setEcdeUrlSommaireType(ConverterUtil.convertToAnyURI(url
            .toASCIIString()));
      requestType.setUrlSommaire(urlType);
      requestType.setHash(hash);
      requestType.setTypeHash(typeHash);
      

      return request;

   }
   
   /**
    * 
    * @param uuid
    *           L'identifiant unique d'archivage de l'archive à modifier
    * @param metadonnees
    *           Les métadonnées.
    * @return instance de {@link Modification}
    */
   public static Modification createModification(String uuid, Collection<Metadata> metadonnees) {

      Modification request = new Modification();

      ModificationRequestType requestType = new ModificationRequestType();

      ListeMetadonneeType listeMetadonnee = new ListeMetadonneeType();
      
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);

      for (Metadata metadonnee : metadonnees) {

         MetadonneeType type = ObjectModeleFactory.createMetadonneeType();

         MetadonneeCodeType codeType = ObjectModeleFactory
               .createMetadonneeCodeType(metadonnee.getCode());
         type.setCode(codeType);

         MetadonneeValeurType valeurType = ObjectModeleFactory
               .createMetadonneeValeurType(metadonnee.getValue());
         type.setValeur(valeurType);

         listeMetadonnee.addMetadonnee(type);
      }

      requestType.setMetadonnees(listeMetadonnee);
      requestType.setUuid(uuidType);
      request.setModification(requestType);

      return request;
   }
   
   /**
    * 
    * @param uuid
    *           L'identifiant unique d'archivage de l'archive à consulter
    * @return instance de {@link Suppression}
    */
   public static Suppression createSuppression(String uuid) {

      Suppression request = new Suppression();

      SuppressionRequestType requestType = new SuppressionRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);

      requestType.setUuid(uuidType);

      request.setSuppression(requestType);

      return request;
   }
}
