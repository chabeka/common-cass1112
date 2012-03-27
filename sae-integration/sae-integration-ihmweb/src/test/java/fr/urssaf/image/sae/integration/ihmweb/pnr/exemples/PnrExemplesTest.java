package fr.urssaf.image.sae.integration.ihmweb.pnr.exemples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EcdeUrlSommaireType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EcdeUrlType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ObjetNumeriqueConsultationType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Recherche;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheRequestType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RequeteRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.UuidType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.VIHandler;
import fr.urssaf.image.sae.integration.ihmweb.utils.ViUtils;


/**
 * EXEMPLE de code pour attaquer les opérations du service web du SAE<br>
 */
public class PnrExemplesTest {


   // Pour ce code exemple, le code modèle de SaeService.wsdl + SaeService.xsd
   // a été généré avec le plugin Maven pour Axis2
   //
   // Ce code modèle a été généré dans un package nommé :
   //  fr.urssaf.image.sae.integration.ihmweb.saeservice.modele
   
   
   @Ignore
   @Test
   public void archivageUnitaireExemple() throws Exception {
      
      // Pré-requis pour le fichier à archiver :
      //  - Un répertoire de traitement a été créé dans l'ECDE d'intégration
      //    Dans cet exemple :
      //      /ecde/ecde_integration/ATT_PROD_001/20111006/1/
      //     où /ecde/ecde_integration/ est le point de montage NFS de l'ECDE d'intégration
      //     sur le serveur où est hébergée l'application cliente
      //  - Le fichier à archiver a été déposé dans le sous-répertoire "documents"
      //    de ce répertoire de traitement
      //    Dans cet exemple :
      //     /ecde/ecde_integration/ATT_PROD_001/20111006/1/documents/attestation.pdf
      //
      // L'URL ECDE correspondant à ce fichier "attestation.pdf" est :
      //  => ecde://cer69-ecdeint.cer69.recouv/ATT_PROD_001/20111006/1/documents/attestation.pdf
      
      
      // URL des services web du SAE
      String urlServiceWeb = "http://cer69-saeint2.cer69.recouv:8080/sae-integration/services/SaeService";
      
      // URL ECDE du fichier à archiver
      String urlEcdeFichier = "ecde://cer69-ecdeint.cer69.recouv/ATT_PROD_001/20111006/1/documents/attestation.pdf";
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("DateCreation", "2011-10-05");
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeProprietaire", "UR750");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("TypeHash", "SHA-1");
      metadonnees.put("NbPages", "2");
      metadonnees.put("FormatFichier", "fmt/354");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren","123456789");
      metadonnees.put("NniEmployeur","148032541101648");
      metadonnees.put("Denomination","COUTURIER GINETTE ");
      metadonnees.put("NumeroCompteInterne","719900");
      metadonnees.put("NumeroCompteExterne","30148032541101600");
      // ...
      
      
      
      // Instantiation du stub
      SaeServiceStub service = buildSaeServiceStub(urlServiceWeb);
      
      // Construction du paramètre d'entrée de l'opération archivageUnitaire, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitaire paramsEntree = contruitParamsEntreeArchivageUnitaire(
            urlEcdeFichier,metadonnees);
      
      // Appel de l'opération archivageUnitaire
      ArchivageUnitaireResponse reponse = service.archivageUnitaire(paramsEntree);
      
      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getArchivageUnitaireResponse().getIdArchive().toString();
      System.out.println(idUniqueArchivage);
      
   }
   
   
   private SaeServiceStub buildSaeServiceStub(String urlServiceWeb) throws Exception {
      
      // Le fichier de configuration "axis2-sae-integration-ihmweb.xml" contient
      //  la mécanique qui permet de brancher une classe avant l'envoi du message SOAP
      // Cette classe est chargé de générer la partie authentification (VI) et de l'insérer
      //  dans l'en-tête SOAP
      
      ConfigurationContext configContext = 
         ConfigurationContextFactory.createBasicConfigurationContext(
               "axis2-sae-integration-ihmweb.xml");
      
      configContext.setProperty(
            VIHandler.PROP_FICHIER_VI, 
            ViUtils.FIC_VI_OK);
      
      SaeServiceStub service = new SaeServiceStub(
            configContext,
            urlServiceWeb);
      
      return service;
      
   }
   
   
   
   private ArchivageUnitaire contruitParamsEntreeArchivageUnitaire(
         String urlEcdeFichier,
         HashMap<String,String> metadonnees) throws MalformedURIException {
      
      
      ArchivageUnitaire archivageUnitaire = 
         new ArchivageUnitaire();

      ArchivageUnitaireRequestType archivageUnitaireRequest = 
         new ArchivageUnitaireRequestType();  
      
      archivageUnitaire.setArchivageUnitaire(archivageUnitaireRequest);
      
      
      // URL ECDE
      EcdeUrlType ecdeUrl = new EcdeUrlType();
      archivageUnitaireRequest.setEcdeUrl(ecdeUrl);
      URI uriEcdeFichier = new URI(urlEcdeFichier);
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
   

   @Ignore
   @Test
   public void rechercheExemple() throws Exception {
      
      // URL des services web du SAE
      String urlServiceWeb = "http://cer69-saeint2.cer69.recouv:8080/sae-integration/services/SaeService";
           
      // Requête de recherche
      // CodeRND:("2.3.1.1.12" OR "2.3.1.1.8") AND NumeroCompteExterne:"30148032541101600"
      String requeteRecherche = "CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\") AND NumeroCompteExterne:\"30148032541101600\""; 
      
      // Métadonnées souhaitées dans les résultats de recherche
      // Soit on veut les métadonnées dites "consultées par défaut" d'après le
      //  référentiel des métadonnées. Dans ce cas, il faut écrire :
      //  List<String> codesMetasSouhaitess = null;
      // Si on veut choisir les métadonnées récupérées, il faut écrire :
      //  List<String> codesMetasSouhaitess = new ArrayList<String>();
      //  codesMetasSouhaitess.add("Titre");
      //  codesMetasSouhaitess.add("CodeRND");
      //  ...
      
//      List<String> codesMetasSouhaitess = null;
      
      List<String> codesMetasSouhaitess = new ArrayList<String>();
      codesMetasSouhaitess.add("Titre");
      codesMetasSouhaitess.add("CodeRND");
      codesMetasSouhaitess.add("NumeroCompteExterne");
      codesMetasSouhaitess.add("DateCreation");
      codesMetasSouhaitess.add("Siret");
      codesMetasSouhaitess.add("Siren");
      codesMetasSouhaitess.add("NumeroRecours");
      
      
      // Instantiation du stub
      SaeServiceStub service = buildSaeServiceStub(urlServiceWeb);
      
      // Construction du paramètre d'entrée de l'opération recherche, 
      //  avec les objets modèle générés par Axis2.
      Recherche paramsEntree = contruitParamsEntreeRecherche(
            requeteRecherche,codesMetasSouhaitess);
      
      // Appel du service web de recherche
      RechercheResponse reponse = service.recherche(paramsEntree);
      
      // Affichage dans la console du résultat de la recherche
      afficheResultatsRecherche(reponse);
      
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
   private Recherche contruitParamsEntreeRecherche(
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
   
   
   private void afficheResultatsRecherche(
         RechercheResponse reponse)  {
    

      RechercheResponseType rechercheResponse = 
         reponse.getRechercheResponse();
      
      // Affichage dans la console du flag de resultats tronques
      System.out.println(
            "Résultats tronqués => " + 
            rechercheResponse.getResultatTronque());
      
      // Regarde si on a eu au moins 1 résultat de recherche
      if (
            (rechercheResponse.getResultats()==null) || 
            (rechercheResponse.getResultats().getResultat()==null) || 
            (rechercheResponse.getResultats().getResultat().length==0)) {
         
         System.out.println("La recherche n'a pas ramené de résultats");
         
      } else {
         
         ResultatRechercheType[] tabResRecherche = 
            rechercheResponse.getResultats().getResultat();
         
         // Affiche le nombre de résultats
         System.out.println(
               "La recherche a ramené " + 
               tabResRecherche.length + 
               " résultats");
         System.out.println("");
         
         // Boucle sur les résultats de recherche
         ResultatRechercheType resRecherche;
         MetadonneeType[] tabMetas;
         String codeMeta;
         String valeurMeta;
         for(int i=0;i<tabResRecherche.length;i++) {
            
            // Affiche un compteur
            System.out.println("Résultat de recherche #" + (i+1));
            
            // Affiche l'identifiant unique d'archivage
            resRecherche = tabResRecherche[i];
            System.out.println(
                  "Identifiant unique d'archivage : " + 
                  resRecherche.getIdArchive().toString());
            
            // Affiche les métadonnées
            System.out.println("Métadonnées : ");
            tabMetas = resRecherche.getMetadonnees().getMetadonnee();
            for(MetadonneeType metadonnee: tabMetas) {
               
               codeMeta = metadonnee.getCode().getMetadonneeCodeType();
               valeurMeta = metadonnee.getValeur().getMetadonneeValeurType();
               
               System.out.println(codeMeta + "=" + valeurMeta);
               
            }
            
            // Un saut de ligne
            System.out.println("");
            
         }
         
      }
      
   }
   
   
   @Ignore
   @Test
   public void consultationExemple() throws Exception {
      
      // URL des services web du SAE
      String urlServiceWeb = "http://cer69-saeint2.cer69.recouv:8080/sae-integration/services/SaeService";
      
      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      String idArchive = "1D212E87-7CEF-4224-9E47-09CA5FA5DC95";
      
      // Instantiation du stub
      SaeServiceStub service = buildSaeServiceStub(urlServiceWeb);
      
      // Construction du paramètre d'entrée de l'opération consultation, 
      //  avec les objets modèle générés par Axis2.
      Consultation paramsEntree = contruitParamsEntreeConsultation(
            idArchive);
      
      // Appel du service web de consultation
      ConsultationResponse reponse = service.consultation(paramsEntree);
      
      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);
      
      
   }
   
   
   
   private Consultation contruitParamsEntreeConsultation(
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
   
   
   private void afficheResultatConsultation(
         ConsultationResponse reponse) throws IOException {
      
       ConsultationResponseType consultationResponse = 
          reponse.getConsultationResponse();
       
       // Les métadonnées
       
       System.out.println("Métadonnées : ");
       
       MetadonneeType[] tabMetas = 
          consultationResponse.getMetadonnees().getMetadonnee();
       
       String valeurMetaNomFichier = "";
       String codeMeta;
       String valeurMeta;
       for(MetadonneeType metadonnee: tabMetas) {
          
          codeMeta = metadonnee.getCode().getMetadonneeCodeType();
          valeurMeta = metadonnee.getValeur().getMetadonneeValeurType();
          
          System.out.println(codeMeta + "=" + valeurMeta);
          
          // Mémorise la valeur de la métadonnée NomFichier
          // pour l'écriture ultérieure du fichier renvoyée
          if (codeMeta.equals("NomFichier")) {
             valeurMetaNomFichier = valeurMeta;
          }
          
       }
       
       // Le fichier
       ObjetNumeriqueConsultationType objetNumerique = 
          consultationResponse.getObjetNumerique();
       
       // Récupère le flux base64 renvoyé
       DataHandler contenu = 
          objetNumerique.getObjetNumeriqueConsultationTypeChoice_type0().getContenu();
       
       // On va créér un fichier dans le répertoire temporaire de l'OS
       String repTempOs = System.getProperty("java.io.tmpdir");
       File file = new File(repTempOs,valeurMetaNomFichier); 
       
       // Ecrit le flux
       OutputStream outputStream = null ;
       outputStream = new FileOutputStream(file);
       contenu.writeTo(outputStream);
       
       // Ecrit dans la console le chemin complet du fichier créé
       System.out.println("");
       System.out.println("Fichier créé : " + file.getAbsolutePath());
      
   }
   
   
   @Ignore
   @Test
   public void archivageMasseExemple() throws Exception {
      
      // Pré-requis pour la capture de masse :
      //  - Un répertoire de traitement a été créé dans l'ECDE d'intégration
      //    Dans cet exemple :
      //      /ecde/ecde_integration/ATT_PROD_001/20111006/2/
      //    où /ecde/ecde_integration/ est le point de montage NFS de l'ECDE d'intégration
      //     sur le serveur où est hébergée l'application cliente
      //  - Le fichier sommaire.xml a été déposé dans ce répertoire.
      //    Exemple :
      //    /ecde/ecde_integration/ATT_PROD_001/20111006/2/sommaire.xml
      //  - Les fichiers à archiver, référencés dans sommaire.xml, ont été déposés dans
      //    le sous-répertoire "documents" du répertoire de traitement.
      //    Dans cet exemple :
      //     /ecde/ecde_integration/ATT_PROD_001/20111006/2/documents/attestation1.pdf
      //     /ecde/ecde_integration/ATT_PROD_001/20111006/2/documents/attestation2.pdf
      // 
      // L'URL ECDE correspondant au sommaire.xml est :
      //  => ecde://cer69-ecdeint.cer69.recouv/ATT_PROD_001/20111006/2/sommaire.xml 
      

      // URL des services web du SAE
      String urlServiceWeb = "http://cer69-saeint2.cer69.recouv:8080/sae-integration/services/SaeService";
      
      // URL ECDE du fichier sommaire.xml
      String urlEcdeSommaire = "ecde://cer69-ecdeint.cer69.recouv/ATT_PROD_001/20111006/2/sommaire.xml";
      
      // Instantiation du stub
      SaeServiceStub service = buildSaeServiceStub(urlServiceWeb);
      
      // Construction du paramètre d'entrée de l'opération archivageMasse, 
      //  avec les objets modèle générés par Axis2.
      ArchivageMasse paramsEntree = contruitParamsEntreeArchivageMasse(
            urlEcdeSommaire);
      
      // Appel de l'opération archivageMasse
      // => aucun retour attendu, le traitement de masse étant asynchrone, le service web
      //    renvoie la main immédiatement
      service.archivageMasse(paramsEntree);

      // Une petite trace
      System.out.println("La demande de prise en compte de l'archivage de masse a été envoyée");
      System.out.println("URL ECDE du sommaire.xml : " + urlEcdeSommaire);
      
   }
   
   
   private ArchivageMasse contruitParamsEntreeArchivageMasse(
         String urlEcdeSommaire) throws MalformedURIException {
      
      
      ArchivageMasse archivageMasse = 
         new ArchivageMasse() ;
      
      ArchivageMasseRequestType archivageMasseRequest = 
         new ArchivageMasseRequestType();
      
      archivageMasse.setArchivageMasse(
            archivageMasseRequest);
      
      // URL ECDE du sommaire
      EcdeUrlSommaireType ecdeUrlSommaireObj = new EcdeUrlSommaireType();
      archivageMasseRequest.setUrlSommaire(ecdeUrlSommaireObj);
      URI ecdeUriSommaireUri = new URI(urlEcdeSommaire);
      ecdeUrlSommaireObj.setEcdeUrlSommaireType(ecdeUriSommaireUri);
      
      // Renvoie du paramètre d'entrée de l'opération archivageMasse
      return archivageMasse;
      
   }
   

}
