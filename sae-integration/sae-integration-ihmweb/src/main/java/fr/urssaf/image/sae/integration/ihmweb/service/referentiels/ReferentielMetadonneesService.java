package fr.urssaf.image.sae.integration.ihmweb.service.referentiels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeDefinition;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.refmeta.ListeMetadonneesType;
import fr.urssaf.image.sae.integration.ihmweb.modele.refmeta.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.utils.JAXBUtils;


/**
 * Service pour le référentiel des métadonnées<br>
 * <br>
 * Ce référentiel est stocké sous la forme d'un fichier XML,
 * dans src/main/resources/ReferentielMetadonnees/ReferentielMetadonnees.xml<br>
 * <br>
 * Ce fichier est une représentation technique du document "Metadonnees.X.Y.xls" 
 */
@Service
public class ReferentielMetadonneesService {

   
   private final List<MetadonneeDefinition> refMetadonnees = new ArrayList<MetadonneeDefinition>() ;
   
   
   /**
    * Constructeur
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public ReferentielMetadonneesService() {
      
      try {
         
         // Lecture du fichier ReferentielMetadonnees.xml
         ListeMetadonneesType listeDefMeta = JAXBUtils.unmarshalResource(
               ListeMetadonneesType.class,
               "/ReferentielMetadonnees/ReferentielMetadonnees.xml",
               "/ReferentielMetadonnees/ReferentielMetadonnees.xsd");
         
         // Construction de la liste privée defMetadonnees
         Iterator<MetadonneeType> iterator = listeDefMeta.getMetadonnee().iterator();
         MetadonneeType metadonneeXml;
         MetadonneeDefinition metaDef;
         while (iterator.hasNext())
         {
            
            // Récupère l'objet MetadonneeType
            metadonneeXml = iterator.next();
            
            // Construction d'une MetadonneeDefinition maison
            metaDef = new MetadonneeDefinition();
            refMetadonnees.add(metaDef);
            metaDef.setCodeLong(metadonneeXml.getCodeLong()) ;
            metaDef.setCodeCourt(metadonneeXml.getCodeCourt()) ;
            metaDef.setArchivablePossible(metadonneeXml.isArchivablePossible()) ;
            metaDef.setArchivableObligatoire(metadonneeXml.isArchivableObligatoire()) ;
            metaDef.setConsulteeParDefaut(metadonneeXml.isConsulteeParDefaut()) ;
            metaDef.setConsultable(metadonneeXml.isConsultable()) ;
            metaDef.setCritereRecherche(metadonneeXml.isCritereRecherche()) ;
            metaDef.setClient(metadonneeXml.isClient()) ;
            metaDef.setObligatoireAuStockage(metadonneeXml.isObligatoireAuStockage()) ;
            metaDef.setTypeDfce(metadonneeXml.getTypeDfce());
            
         }
         
         
      } catch (Exception e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }
   
   
   /**
    * Le référentiel des métadonnées
    * 
    * @return Le référentiel des métadonnées
    */
   public final List<MetadonneeDefinition> getReferentielMetadonnees() {
      return this.refMetadonnees;
   }
   
      
   
   /**
    * Renvoie la liste des métadonnées dites "consultée par défaut"
    * 
    * @return la liste des métadonnées dites "consultée par défaut"
    */
   public final List<MetadonneeDefinition> listeMetadonneesConsulteeParDefaut() {
      
      // Construction de l'objet résultat
      List<MetadonneeDefinition> liste = new ArrayList<MetadonneeDefinition>() ;
      
      // Parcours de la liste des métadonnées, et on ajoute dans la liste résultat
      // celles qui ont le flag "consultée par défaut" à true
      // NB : on pourrait faire un clone des objets MetadonneeDefinition
      for(MetadonneeDefinition metaDef: this.refMetadonnees) {
         if (metaDef.isConsulteeParDefaut()) {
            liste.add(metaDef);
         }
      }
      
      // Renvoie du résulta
      return liste;
      
   }

   
   /**
    * Recherche une métadonnée à partir de son code long dans une liste de métadonnées
    * 
    * @param codeLong le code long de la métadonnée à chercher
    * @param metadonnees la liste des métadonnées dans laquelle chercher
    * @return la métadonnée si on l'a retrouvée, ou null 
    */
   public final MetadonneeDefinition findMeta(
         String codeLong,
         List<MetadonneeDefinition> metadonnees) {
      
      // Initialise le résultat
      MetadonneeDefinition result = null;
      
      // Boucle sur la liste des métadonnées
      if (!CollectionUtils.isEmpty(metadonnees)) {
         for (MetadonneeDefinition metaDef:metadonnees) {
            if (codeLong.equals(metaDef.getCodeLong())) {
               result = metaDef;
               break;
            }
         }
      }
      
      // Renvoie du résultat
      return result;
      
   }
   
   
   /**
    * Recherche une métadonnée à partir de son code long dans le référentiel des métadonnées
    * 
    * @param codeLong le code long de la métadonnée à chercher
    * @return la métadonnée si on l'a retrouvée, ou null 
    */
   public final MetadonneeDefinition findMeta(
         String codeLong) {
      return findMeta(codeLong,this.refMetadonnees);
   }
   
   
   /**
    * Renvoie une liste de métadonnées "exemples" pour la capture<br>
    * <br>
    * Cela permet par exemple d'initialiser les IHM avec
    * un modèle de liste de métadonnées. On aide ainsi
    * l'utilisateur à la saisie des métadonnées.
    * 
    * @return une liste de métadonnées "exemple"
    */
   public static final MetadonneeValeurList getMetadonneesExemplePourCapture() {
      
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      
      metadonnees.add(new MetadonneeValeur("Hash","A RENSEIGNER"));
      
      metadonnees.add(new MetadonneeValeur("Titre","Attestation de vigilance"));
      metadonnees.add(new MetadonneeValeur("DateCreation","2011-09-01"));
      metadonnees.add(new MetadonneeValeur("ApplicationProductrice","ADELAIDE"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeProprietaire","AC750"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeGestionnaire","CER69"));
      metadonnees.add(new MetadonneeValeur("CodeRND","2.3.1.1.12"));
      metadonnees.add(new MetadonneeValeur("TypeHash","SHA-1"));
      metadonnees.add(new MetadonneeValeur("NbPages","2"));
      metadonnees.add(new MetadonneeValeur("FormatFichier","fmt/354"));
      
      return metadonnees;
      
   }
   
   
   /**
    * Renvoie une liste de métadonnées "exemples" pour la recherche<br>
    * <br>
    * Cela permet par exemple d'initialiser les IHM avec
    * un modèle de liste de métadonnées. On aide ainsi
    * l'utilisateur à la saisie des métadonnées.
    * 
    * @return une liste de métadonnées "exemple"
    */
   public static final CodeMetadonneeList getMetadonneesExemplePourRecherche() {
      CodeMetadonneeList metadonnees = new CodeMetadonneeList();
      metadonnees.add("Titre");
      metadonnees.add("DateCreation");
      metadonnees.add("CodeOrganismeProprietaire");
      metadonnees.add("CodeOrganismeGestionnaire");
      
      return metadonnees;
   }
   
   /**
    * Renvoie une liste de métadonnées "exemples" pour la recherche<br>
    * sur l'isolatin des données
    * <br>
    * Cela permet par exemple d'initialiser les IHM avec
    * un modèle de liste de métadonnées. On aide ainsi
    * l'utilisateur à la saisie des métadonnées.
    * 
    * @return une liste de métadonnées "exemple"
    */
   public static final CodeMetadonneeList getMetadonneesExempleRechercheIsolation() {
      CodeMetadonneeList metadonnees = new CodeMetadonneeList();
      metadonnees.add("Titre");
      metadonnees.add("DomaineRH");
      metadonnees.add("DomaineCotisant");
      metadonnees.add("DomaineTechnique");
      metadonnees.add("DomaineComptable");
      metadonnees.add("DateCreation");
      
      return metadonnees;
   }
   
   
   /**
    * Renvoie le code court correspondant au code long passé en paramètre
    * 
    * @param codeLong le code long dont il faut renvoyer le code court correspondant
    * @return le code court correspondant au code long passé en paramètre
    */
   public final String getCodeCourt(String codeLong) {
      
      MetadonneeDefinition metadonnee = this.findMeta(codeLong);
      if (metadonnee==null) {
         throw new IntegrationRuntimeException("La métadonnée au code long \"" + codeLong + "\" n'a pas été retrouvée dans le référentiel des métadonnées");
      } else {
         return metadonnee.getCodeCourt();
      }
      
   }
   
   
   /**
    * Construit une liste de définition de métadonnées à partir d'une liste de codes longs
    * 
    * @param codesLongs la liste des codes longs
    * @return la liste de définition des métadonnées correspondants aux codes longs passés en paramètre
    */
   public final List<MetadonneeDefinition> construitListeMetadonnee(
         CodeMetadonneeList codesLongs) {
      
      // Création de l'objet résultat
      List<MetadonneeDefinition> result = new ArrayList<MetadonneeDefinition>();
      
      // Recherche chaque code dans le référentiel des métadonnées
      MetadonneeDefinition metaDef;
      for (String codeLong: codesLongs) {
         
         // Recherche la métadonnée à partir de son code long dans le référentiel des métadonnées
         metaDef = findMeta(codeLong);
         if (metaDef==null) {
            throw new IntegrationRuntimeException(
                  "La métadonnée " + codeLong + " n'a pas été retrouvée dans le référentiel des métadonnées");
         }
         
         // Ajout la métadonnée à la liste résultat
         result.add(metaDef);
         
      }
      
      // Renvoie de la liste résultat
      return result;
      
   }
   
   
   
   /**
    * Renvoie la liste des codes des métadonnées "consultables"
    * 
    * @return liste des codes des métadonnées "consultables"
    */
   public final CodeMetadonneeList listeMetadonneesConsultables() {
      
      CodeMetadonneeList result = new CodeMetadonneeList();
      
      for(MetadonneeDefinition metaDef: this.refMetadonnees) {
         if (metaDef.isConsultable()) {
            result.add(metaDef.getCodeLong());
         }
      }
      
      Collections.sort(result);
      
      return result;
      
   }
   
   
}
