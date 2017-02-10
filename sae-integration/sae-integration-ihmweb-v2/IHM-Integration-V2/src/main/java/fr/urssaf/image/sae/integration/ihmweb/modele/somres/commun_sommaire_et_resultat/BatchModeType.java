//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.11.17 à 03:49:54 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

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
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public enum BatchModeType {

    TOUT_OU_RIEN,
    PARTIEL;

    public String value() {
        return name();
    }

    public static BatchModeType fromValue(String v) {
        return valueOf(v);
    }

}
