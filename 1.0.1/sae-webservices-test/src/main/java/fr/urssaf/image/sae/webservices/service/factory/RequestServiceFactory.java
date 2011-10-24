package fr.urssaf.image.sae.webservices.service.factory;

import java.net.URI;
import java.util.Collection;

import org.apache.axis2.databinding.utils.ConverterUtil;

import fr.urssaf.image.sae.webservices.factory.ObjectModeleFactory;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.EcdeUrlSommaireType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.EcdeUrlType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RequeteRechercheType;
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
   public static Consultation createConsultation(String uuid) {

      Consultation request = new Consultation();

      ConsultationRequestType requestType = new ConsultationRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(uuid);

      requestType.setIdArchive(uuidType);

      request.setConsultation(requestType);

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

}
