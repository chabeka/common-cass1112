//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.02.24 à 03:57:27 PM CET 
//


package fr.urssaf.image.parser_opencsv.jaxb.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour batchModeType.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="batchModeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TOUT_OU_RIEN"/>
 *     &lt;enumeration value="PARTIEL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "batchModeType")
@XmlEnum
public enum BatchModeType {

   TOUT_OU_RIEN,
   PARTIEL;

   public String value() {
      return name();
   }

   public static BatchModeType fromValue(final String v) {
      return valueOf(v);
   }

}
