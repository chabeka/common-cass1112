//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2020.10.27 à 11:48:04 AM CET 
//


package fr.urssaf.image.rsmed.bean.xsd.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour batchModeType.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="batchModeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TOUT_OU_RIEN"/&gt;
 *     &lt;enumeration value="PARTIEL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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

    public static BatchModeType fromValue(String v) {
        return valueOf(v);
    }

}
