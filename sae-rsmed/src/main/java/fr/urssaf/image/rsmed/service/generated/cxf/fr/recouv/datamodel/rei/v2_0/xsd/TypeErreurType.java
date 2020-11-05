
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour TypeErreur_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TypeErreur_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TOUS"/&gt;
 *     &lt;enumeration value="TECHNIQUE"/&gt;
 *     &lt;enumeration value="FONCTIONNELLE"/&gt;
 *     &lt;enumeration value="NIVEAU_3"/&gt;
 *     &lt;enumeration value="LIASSES_EN_ATTENTE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TypeErreur_Type")
@XmlEnum
public enum TypeErreurType {

    TOUS,
    TECHNIQUE,
    FONCTIONNELLE,
    NIVEAU_3,
    LIASSES_EN_ATTENTE;

    public String value() {
        return name();
    }

    public static TypeErreurType fromValue(String v) {
        return valueOf(v);
    }

}
