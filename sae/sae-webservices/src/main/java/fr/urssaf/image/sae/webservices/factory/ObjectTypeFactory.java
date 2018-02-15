package fr.urssaf.image.sae.webservices.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMRequestType;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationMTOMResponseType;
import fr.cirtil.www.saeservice.ConsultationRequestType;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.ListeMetadonneeCodeType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeDispoType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationTypeChoice_type0;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.cirtil.www.saeservice.UrlConsultationDirecteType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Classe d'instanciation du modèle généré par le web service. <br>
 * le modèle est contenu dans le package {@link fr.cirtil.www.saeservice}<br>
 * le schema XSD est <code>META-INF/SaeService.xsd</code>
 * 
 * 
 */
public final class ObjectTypeFactory {

   private ObjectTypeFactory() {

   }

   /**
    * instanciation de la classe {@link MetadonneeType}
    * 
    * <pre>
    *  &lt;xsd:complexType name="metadonneeType">
    *    ...
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="code" type="sae:metadonneeCodeType">
    *       ...
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="valeur" type="sae:metadonneeValeurType">
    *       ...  
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    *  &lt;/xsd:complexType>
    * </pre>
    * 
    * @param code
    *           valeur <code>metadonneeCodeType</code>
    * @param valeur
    *           valeur <code>metadonneeValeurType</code>
    * @return instance de {@link MetadonneeType}
    */
   public static MetadonneeType createMetadonneeType(String code, String valeur) {

      Assert.notNull(code, "code is required");

      MetadonneeType metaDonnee = new MetadonneeType();
      MetadonneeCodeType codeType = new MetadonneeCodeType();
      metaDonnee.setCode(codeType);

      codeType.setMetadonneeCodeType(code);
      MetadonneeValeurType valeurType = new MetadonneeValeurType();

      valeurType.setMetadonneeValeurType(valeur);
      metaDonnee.setValeur(valeurType);

      return metaDonnee;
   }

   /**
    * 
    * instanciation de la classe {@link ObjetNumeriqueConsultationType}<br>
    * le paramètre est <code>content</code> est transformé en une chaine de
    * caractères en base64
    * 
    * <pre>
    * &lt;xsd:complexType name="objetNumeriqueConsultationType">
    *      ...
    *    &lt;xsd:sequence>
    *       &lt;xsd:choice>
    *          &lt;xsd:element name="url" type="sae:urlConsultationDirecteType"/>
    *          &lt;xsd:element name="contenu" type="xsd:base64Binary"/>
    *       &lt;xsd:choice>
    *    &lt;xsd:sequence>
    * &lt;xsd:complexType>
    * 
    * </pre>
    * 
    * @param content
    *           valeur de <code>contenu</code> doit être non null
    * @return instance de {@link ObjetNumeriqueConsultationType}
    */
   public static ObjetNumeriqueConsultationType createObjetNumeriqueConsultationType(
         DataHandler content) {

      Assert.notNull(content, "content is required");

      ObjetNumeriqueConsultationType objetNumerique = new ObjetNumeriqueConsultationType();
      ObjetNumeriqueConsultationTypeChoice_type0 choice = new ObjetNumeriqueConsultationTypeChoice_type0();
      objetNumerique.setObjetNumeriqueConsultationTypeChoice_type0(choice);

      choice.setContenu(content);

      return objetNumerique;
   }

   /**
    * instanciation de la classe {@link ObjetNumeriqueConsultationType}
    * 
    * <pre>
    * &lt;xsd:complexType name="objetNumeriqueConsultationType">
    *      ...
    *    &lt;xsd:sequence>
    *       &lt;xsd:choice>
    *          &lt;xsd:element name="url" type="sae:urlConsultationDirecteType"/>
    *          &lt;xsd:element name="contenu" type="xsd:base64Binary"/>
    *       &lt;xsd:choice>
    *    &lt;xsd:sequence>
    * &lt;xsd:complexType>
    * 
    * </pre>
    * 
    * @param url
    *           valeur de <code>url</code> doit être non null
    * @return instance de {@link ObjetNumeriqueConsultationType}
    */
   public static ObjetNumeriqueConsultationType createObjetNumeriqueConsultationType(
         URI url) {

      Assert.notNull(url, "url is required");

      ObjetNumeriqueConsultationType objetNumerique = new ObjetNumeriqueConsultationType();
      ObjetNumeriqueConsultationTypeChoice_type0 choice = new ObjetNumeriqueConsultationTypeChoice_type0();
      objetNumerique.setObjetNumeriqueConsultationTypeChoice_type0(choice);

      UrlConsultationDirecteType urlConsultation = new UrlConsultationDirecteType();
      choice.setUrl(urlConsultation);

      urlConsultation.setUrlConsultationDirecteType(ConverterUtil
            .convertToAnyURI(url.toASCIIString()));

      return objetNumerique;
   }

   /**
    * instanciation de la classe {@link UuidType}
    * 
    * <pre>
    * &lt;xsd:simpleType name="uuidType">
    *    ...
    *    &lt;xsd:restriction base="xsd:string">
    *       &lt;xsd:pattern value="[A-Fa-f0-9]{8}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{12}"/>
    *    &lt;/xsd:restriction>
    * &lt;/xsd:simpleType>
    * </pre>
    * 
    * @param uuid
    *           valeur de <code>uuidType</code> doit être renseigné avec le
    *           format d'une URL ECDE
    * @return instance de {@link UuidType}
    */
   public static UuidType createUuidType(UUID uuid) {

      Assert.notNull(uuid, "uuid is required");

      UuidType uuidType = new UuidType();
      uuidType.setUuidType(org.apache.commons.lang.StringUtils.upperCase(uuid
            .toString()));

      return uuidType;
   }

   /**
    * instanciation de la classe {@link ListeMetadonneeType}
    * 
    * <pre>
    * 
    *  &lt;xsd:complexType name="listeMetadonneeType">
    *    ...
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="metadonnee" type="sae:metadonneeType" minOccurs="0" maxOccurs="unbounded">
    *            ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * 
    * </pre>
    * 
    * @return instance de {@link ListeMetadonneeType}
    */
   public static ListeMetadonneeType createListeMetadonneeType() {
      return new ListeMetadonneeType();
   }

   /**
    * instanciation de la classe {@link ResultatRechercheType}
    * 
    * <pre>
    * &lt;xsd:complexType name="resultatRechercheType">
    *       ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ResultatRechercheType}
    */
   public static ResultatRechercheType createResultatRechercheType() {

      return new ResultatRechercheType();
   }

   /**
    * Permet de convertir un objet de la couche WebService de type
    * MetadonneeCodeType[] en une liste List&lt;String&gt;
    * 
    * @param listeMD
    *           liste des codes de métadonnées
    * @return Liste des codes de métadonnées converties de l'objet
    *         MetadonneeCodeType[]
    */
   public static List<String> buildMetaCodeFromWS(MetadonneeCodeType[] listeMD) {
      List<String> listMDDesired = null;

      if (ArrayUtils.isNotEmpty(listeMD)) {
         listMDDesired = new ArrayList<String>();

         for (MetadonneeCodeType metadonneeCodeType : listeMD) {
            String code = metadonneeCodeType.getMetadonneeCodeType();
            listMDDesired.add(code);
         }
      }
      return listMDDesired;
   }

   /**
    * Permet de convertir un objet de la couche WebService de type
    * MetadonneeCodeType[] en une liste List&lt;UntypedMetadata&gt;
    * 
    * @param listeMD
    *           liste des codes de métadonnées
    * @return Liste métadonnées converties de l'objet MetadonneeCodeType[]
    */
   public static List<UntypedMetadata> buildMetaListFromWS(
         MetadonneeType[] listeMD) {
      List<UntypedMetadata> listMDDesired = null;

      if (ArrayUtils.isNotEmpty(listeMD)) {
         listMDDesired = new ArrayList<UntypedMetadata>();

         for (MetadonneeType metadonneeType : listeMD) {
            String code = metadonneeType.getCode().getMetadonneeCodeType();
            String valeur = metadonneeType.getValeur()
                  .getMetadonneeValeurType();
            listMDDesired.add(new UntypedMetadata(code, valeur));
         }
      }
      return listMDDesired;
   }

   /**
    * construit la liste des codes de metadata à partir de la liste fournie
    * 
    * @param metadonnees
    *           liste des metadatas dont il faut récupérer le code
    * @return la liste des codes
    */
   public static List<String> buildMetaCodeFromWS(
         ListeMetadonneeCodeType metadonnees) {

      List<String> datas = null;
      if (metadonnees != null) {
         datas = new ArrayList<String>();

         datas.addAll(buildMetaCodeFromWS(metadonnees.getMetadonneeCode()));
      }

      return datas;

   }

   /**
    * construit la liste des codes de metadata à partir de la liste fournie
    * 
    * @param metadonnees
    *           liste des metadatas dont il faut récupérer le code
    * @return la liste des codes
    */
   public static List<UntypedMetadata> buildMetaFromWS(
         ListeMetadonneeType metadonnees) {

      List<UntypedMetadata> datas = null;
      if (metadonnees != null) {
         datas = new ArrayList<UntypedMetadata>();

         datas.addAll(buildMetaListFromWS(metadonnees.getMetadonnee()));
      }

      return datas;

   }

   /**
    * Construit la liste des codes de metadata à partir de la liste fournie
    * 
    * @param metadonnees
    *           tableau d'objet metadonnees
    * @return la liste des code correspondant
    */
   public static List<String> buildMetaCodeFromWS(
         ListeMetadonneeCodeType[] metadonnees) {

      List<String> datas = null;
      if (ArrayUtils.isNotEmpty(metadonnees)) {
         datas = new ArrayList<String>();

         for (ListeMetadonneeCodeType metadataCT : metadonnees) {
            datas.addAll(buildMetaCodeFromWS(metadataCT));
         }
      }

      return datas;

   }

   /**
    * Méthode permettant de convertir un objet ConsultationMTOM en Consultation
    * 
    * @param consultationMTOM
    *           objet de consultation
    * @return objet Consultation
    */
   public static Consultation convertToConsultation(
         ConsultationMTOM consultationMTOM) {
      Consultation consultation = new Consultation();

      ConsultationMTOMRequestType consultMTOMRT = consultationMTOM
            .getConsultationMTOM();

      ConsultationRequestType consultRT = new ConsultationRequestType();
      UuidType uuidType = consultMTOMRT.getIdArchive();
      consultRT.setIdArchive(uuidType);
      ListeMetadonneeCodeType listeMD = consultMTOMRT.getMetadonnees();
      consultRT.setMetadonnees(listeMD);

      consultation.setConsultation(consultRT);

      return consultation;
   }

   /**
    * Méthode permettant de convertir un objet ConsultationResponse en
    * ConsultationMTOMResponse
    * 
    * @param consultationResponse
    *           objet de response de la consultation
    * @return ConsultationMTOMResponse
    */
   public static ConsultationMTOMResponse convertToConsultRespMTOM(
         ConsultationResponse consultationResponse) {
      ConsultationMTOMResponseType param = new ConsultationMTOMResponseType();
      param.setMetadonnees(consultationResponse.getConsultationResponse()
            .getMetadonnees());

      ConsultationMTOMResponse responseMTOM = new ConsultationMTOMResponse();
      param.setContenu(consultationResponse.getConsultationResponse()
            .getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu());
      responseMTOM.setConsultationMTOMResponse(param);

      return responseMTOM;
   }

   /**
    * instanciation de la classe {@link MetadonneeDispoType}
    * 
    * <pre>
    *  &lt;xsd:complexType name="metadonneeDispoType">
    *    ...
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="codeLong" type="xsd:string">
    *       ...
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="libelle" type="xsd:string">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="description" type="xsd:string">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="format" type="xsd:string">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="formatage" type="xsd:string">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="specifiableArchivage" type="xsd:boolean">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="obligatoireArchivage" type="xsd:boolean">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="tailleMax" type="xsd:int">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="critereRecherche" type="xsd:boolean">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="indexation" type="xsd:boolean">
    *       ...  
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="modifiable" type="xsd:boolean">
    *       ...  
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    *  &lt;/xsd:complexType>
    * </pre>
    * 
    * @param metadataReference
    *           metadonnées de référence
    * @return instance de {@link MetadonneeDispoType}
    */
   public static MetadonneeDispoType createMetadonneeDispoType(
         MetadataReference metadataReference) {

      MetadonneeDispoType metaDonnee = new MetadonneeDispoType();
      metaDonnee.setCodeLong(metadataReference.getLongCode());
      metaDonnee.setLibelle(metadataReference.getLabel());
      metaDonnee.setDescription(metadataReference.getDescription());
      metaDonnee.setFormat(metadataReference.getType());
      metaDonnee.setFormatage(metadataReference.getPattern());
      metaDonnee.setSpecifiableArchivage(metadataReference.isArchivable());
      metaDonnee.setObligatoireArchivage(metadataReference
            .isRequiredForArchival());
      metaDonnee.setTailleMax(metadataReference.getLength());
      metaDonnee.setCritereRecherche(metadataReference.isSearchable());
      metaDonnee.setIndexation(metadataReference.getIsIndexed());
      metaDonnee.setModifiable(metadataReference.isModifiable());

      return metaDonnee;
   }
}
