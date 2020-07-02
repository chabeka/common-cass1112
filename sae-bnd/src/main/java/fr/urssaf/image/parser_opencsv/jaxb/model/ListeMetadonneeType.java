//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.02.24 à 03:57:27 PM CET 
//


package fr.urssaf.image.parser_opencsv.jaxb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de métadonnées
 * 
 * <p>Classe Java pour listeMetadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeMetadonneeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnee" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}metadonneeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeMetadonneeType", propOrder = {
                                                    "metadonnee"
})
public class ListeMetadonneeType {

   protected List<MetadonneeType> metadonnee;

   /**
    * Gets the value of the metadonnee property.
    * 
    * <p>
    * This accessor method returns a reference to the live list,
    * not a snapshot. Therefore any modification you make to the
    * returned list will be present inside the JAXB object.
    * This is why there is not a <CODE>set</CODE> method for the metadonnee property.
    * 
    * <p>
    * For example, to add a new item, do as follows:
    * <pre>
    *    getMetadonnee().add(newItem);
    * </pre>
    * 
    * 
    * <p>
    * Objects of the following type(s) are allowed in the list
    * {@link MetadonneeType }
    * 
    * 
    */
   public List<MetadonneeType> getMetadonnee() {
      if (metadonnee == null) {
         metadonnee = new ArrayList<>();
      }
      return metadonnee;
   }

   public boolean compareWith(final ListeMetadonneeType metas) {
      boolean equal = false;
      if (metas.getMetadonnee().size() != getMetadonnee().size()) {
         return equal;
      }

      int i = 0;
      for (final MetadonneeType meta : metas.getMetadonnee()) {
         if (getMetadonnee().contains(meta)) {
            i++;
         }
      }

      if (i == getMetadonnee().size()) {
         equal = true;
      }

      return equal;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "ListeMetadonneeType [metadonnee=" + metadonnee + "]";
   }

   /**
    * Convertir la liste des méta sous forme d'une Map Key Value
    * 
    * @param metadonnees
    * @return
    */
   public Map<String, MetadonneeType> convertToMap() {

      final Map<String, MetadonneeType> mapMetas = metadonnee.stream()
            .collect(
                     Collectors.toMap(MetadonneeType::getCode,
                                      metadonnee -> metadonnee));

      return mapMetas;
   }

   /**
    * Met à jour une métadonnées dans une liste de métadonnées
    * 
    * @param key
    *           la clé de la métadonnée
    * @param value
    *           la valeur de la métadonnée
    */
   public void updateMeta(final String key, final String value) {
      for (final MetadonneeType meta : metadonnee) {
         if (meta.getCode().equals(key)) {
            if (metadonnee.contains(meta)) {
               metadonnee.remove(meta);
            }

            final MetadonneeType metaToAdd = new MetadonneeType();
            metaToAdd.setCode(key);
            metaToAdd.setValeur(value);
            metadonnee.add(metaToAdd);
            break;
         }
      }
   }

   /**
    * Modifie un ensemble de métadonnées
    * 
    * @param liste
    */
   public void updateMetas(final List<MetadonneeType> liste) {
      for(final MetadonneeType metaAmodifier : liste) {
         updateMeta(metaAmodifier.getCode(), metaAmodifier.getValeur());
      }
   }

   public String getMetaValue(final String key) {
      return convertToMap().get(key).getValeur();
   }

   public void removeMeta(final String key) {
      convertToMap().remove(key);
   }

}
