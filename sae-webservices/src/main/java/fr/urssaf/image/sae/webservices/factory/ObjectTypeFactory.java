package fr.urssaf.image.sae.webservices.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.ListeMetadonneeCodeType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationType;
import fr.cirtil.www.saeservice.ObjetNumeriqueConsultationTypeChoice_type0;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.cirtil.www.saeservice.UrlConsultationDirecteType;
import fr.cirtil.www.saeservice.UuidType;

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
         byte[] content) {

      Assert.notNull(content, "content is required");

      ObjetNumeriqueConsultationType objetNumerique = new ObjetNumeriqueConsultationType();
      ObjetNumeriqueConsultationTypeChoice_type0 choice = new ObjetNumeriqueConsultationTypeChoice_type0();
      objetNumerique.setObjetNumeriqueConsultationTypeChoice_type0(choice);

      DataHandler contenu = ConverterUtil.convertToDataHandler(StringUtils
            .newStringUtf8(Base64.encodeBase64(content, false)));

      choice.setContenu(contenu);

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

}
