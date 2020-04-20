package fr.urssaf.image.sae.webservices.service.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.ListeResultatRechercheNbResType;
import fr.cirtil.www.saeservice.ListeResultatRechercheType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheNbResResponseType;
import fr.cirtil.www.saeservice.RechercheParIterateurResponse;
import fr.cirtil.www.saeservice.RechercheParIterateurResponseType;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RechercheResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;

/**
 * Classe d'instanciation de :
 * <ul>
 * <li>{@link RechercheResponse}</li>
 * </ul>
 */
public final class ObjectRechercheFactory {

   private ObjectRechercheFactory() {
   }

   /**
    * instanciation de {@link RechercheResponse}.<br>
    * Implementation de {@link RechercheResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="rechercheResponseType">
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="resultats" type="sae:listeResultatRechercheType">
    *       ...       
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="resultatTronque" type="xsd:boolean">
    *       ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @param untypedDocuments
    *           valeur de <code>listeResultatRechercheType</code>
    * @param resultatTronque
    *           valeur de <code>resultatTronque</code>
    * @return instance de {@link RechercheResponse}
    */
   public static RechercheResponse createRechercheResponse(
                                                           final List<UntypedDocument> untypedDocuments, final boolean resultatTronque) {

      final RechercheResponse response = new RechercheResponse();
      final RechercheResponseType responseType = new RechercheResponseType();

      final ListeResultatRechercheType resultatsType = new ListeResultatRechercheType();

      resultatsType.setResultat(new ResultatRechercheType[] {});

      if (CollectionUtils.isNotEmpty(untypedDocuments)) {
         int taille = untypedDocuments.size();
         if (resultatTronque) {
            taille = untypedDocuments.size() - 1;
         }
         for (int i = 0; i < taille; i++) {
            final UntypedDocument storageDocument = untypedDocuments.get(i);
            final ResultatRechercheType resultatRecherche = ObjectTypeFactory
                                                                             .createResultatRechercheType();

            resultatRecherche.setIdArchive(ObjectTypeFactory
                                                            .createUuidType(storageDocument.getUuid()));
            final ListeMetadonneeType listeMetadonnee = ObjectTypeFactory
                                                                         .createListeMetadonneeType();

            final List<MetadonneeType> metadonnees = createListMetadonneeType(storageDocument);

            if (CollectionUtils.isNotEmpty(metadonnees)) {
               for (final MetadonneeType metaDonnee : metadonnees) {
                  listeMetadonnee.addMetadonnee(metaDonnee);
               }
            }
            resultatRecherche.setMetadonnees(listeMetadonnee);
            resultatsType.addResultat(resultatRecherche);
         }
      }
      responseType.setResultats(resultatsType);
      responseType.setResultatTronque(resultatTronque);
      response.setRechercheResponse(responseType);
      return response;
   }

   /**
    * instanciation de {@link RechercheParIterateurResponse}.<br>
    * Implementation de {@link RechercheParIterateurResponseType}
    * 
    * @param untypedDocuments
    *           valeur de <code>listeResultatRechercheType</code>
    * @param isMetaVariableAjoute
    *           pour savoir si il faut laisser ou non la métadonnée variable
    * @param nomMetaVariable
    *           Le nom de la métadonnée variable
    * @return instance de {@link RechercheResponse}
    */
   public static RechercheParIterateurResponse createRechercheParIterateurResponse(
                                                                                   final List<UntypedDocument> untypedDocuments) {

      final RechercheParIterateurResponse response = new RechercheParIterateurResponse();
      final RechercheParIterateurResponseType responseType = new RechercheParIterateurResponseType();
      final ListeResultatRechercheType resultatsType = new ListeResultatRechercheType();

      resultatsType.setResultat(new ResultatRechercheType[] {});

      if (CollectionUtils.isNotEmpty(untypedDocuments)) {
         final int taille = untypedDocuments.size();
         for (int i = 0; i < taille; i++) {
            final UntypedDocument untypedDocument = untypedDocuments.get(i);
            final ResultatRechercheType resultatRecherche = ObjectTypeFactory
                                                                             .createResultatRechercheType();

            resultatRecherche.setIdArchive(ObjectTypeFactory
                                                            .createUuidType(untypedDocument.getUuid()));
            final ListeMetadonneeType listeMetadonnee = ObjectTypeFactory
                                                                         .createListeMetadonneeType();

            final List<MetadonneeType> metadonnees = createListMetadonneeType(untypedDocument);

            if (CollectionUtils.isNotEmpty(metadonnees)) {
               for (final MetadonneeType metaDonnee : metadonnees) {
                  listeMetadonnee.addMetadonnee(metaDonnee);
               }
            }
            resultatRecherche.setMetadonnees(listeMetadonnee);
            resultatsType.addResultat(resultatRecherche);
         }
      }
      responseType.setResultats(resultatsType);
      response.setRechercheParIterateurResponse(responseType);

      return response;
   }

   /**
    * instanciation de {@link RechercheNbResResponse}.<br>
    * Implementation de {@link RechercheNbResResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="rechercheNbResResponseType">
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="resultats" type="sae:listeResultatRechercheType">
    *       ...       
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="resultatTronque" type="xsd:boolean">
    *       ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @param untypedDocuments
    *           valeur de <code>listeResultatRechercheType</code>
    * @param resultatTronque
    *           valeur de <code>resultatTronque</code>
    * @return instance de {@link RechercheResponse}
    */
   public static RechercheNbResResponse createRechercheNbResResponse(
                                                                     final List<UntypedDocument> untypedDocuments, final boolean resultatTronque,
                                                                     final int maxResult) {

      final RechercheNbResResponse response = new RechercheNbResResponse();
      final RechercheNbResResponseType responseType = new RechercheNbResResponseType();
      final ListeResultatRechercheNbResType resultatsType = new ListeResultatRechercheNbResType();

      resultatsType.setResultat(new ResultatRechercheType[] {});

      if (CollectionUtils.isNotEmpty(untypedDocuments)) {
         int taille = untypedDocuments.size();
         // on sauvegarde le nombre de resultat avant de tronquer la liste
         responseType.setNbResultats(taille);
         if (resultatTronque) {
            taille = maxResult;
         }
         for (int i = 0; i < taille; i++) {
            final UntypedDocument storageDocument = untypedDocuments.get(i);
            final ResultatRechercheType resultatRecherche = ObjectTypeFactory
                                                                             .createResultatRechercheType();

            resultatRecherche.setIdArchive(ObjectTypeFactory
                                                            .createUuidType(storageDocument.getUuid()));
            final ListeMetadonneeType listeMetadonnee = ObjectTypeFactory
                                                                         .createListeMetadonneeType();

            final List<MetadonneeType> metadonnees = createListMetadonneeType(storageDocument);

            if (CollectionUtils.isNotEmpty(metadonnees)) {
               for (final MetadonneeType metaDonnee : metadonnees) {
                  listeMetadonnee.addMetadonnee(metaDonnee);
               }
            }
            resultatRecherche.setMetadonnees(listeMetadonnee);
            resultatsType.addResultat(resultatRecherche);
         }
      }
      responseType.setResultats(resultatsType);
      responseType.setNbResultats(untypedDocuments.size());
      responseType.setResultatTronque(resultatTronque);
      response.setRechercheNbResResponse(responseType);
      return response;
   }

   /**
    * instanciation d'une liste de {@link MetadonneeType} à partir des
    * {@link fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata} contenu dans
    * une instance de
    * {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocument}
    * <ul>
    * <li>{@link MetadonneeType#setCode} :
    * {@link fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata#getLongCode()}
    * </li>
    * <li>{@link MetadonneeType#setValeur} :
    * {@link fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata#getValue()}</li>
    * </ul>
    * 
    * @param untypedDocument
    *           instance de
    *           {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocument}
    *           doit être non null
    * @return collection d'instance de {@link MetadonneeType}
    */
   public static List<MetadonneeType> createListMetadonneeType(
                                                               final UntypedDocument untypedDocument) {

      final List<MetadonneeType> metadonnees = new ArrayList<>();
      String valeur = null;
      for (final UntypedMetadata untypedMetadata : untypedDocument.getUMetadatas()) {
         final String code = untypedMetadata.getLongCode();
         if (untypedMetadata.getValue() == null) {
            valeur = StringUtils.EMPTY;
         } else {
            valeur = untypedMetadata.getValue().toString();
         }
         final MetadonneeType metadonnee = ObjectTypeFactory.createMetadonneeType(
                                                                                  code,
                                                                                  valeur);
         metadonnees.add(metadonnee);
      }
      return metadonnees;
   }
}
