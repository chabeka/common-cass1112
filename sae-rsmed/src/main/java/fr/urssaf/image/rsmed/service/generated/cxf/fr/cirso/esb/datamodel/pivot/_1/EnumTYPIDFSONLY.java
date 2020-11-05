
package fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Enum_TYP-IDF_S-ONLY.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Enum_TYP-IDF_S-ONLY"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="numeroPartenaire"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enum_TYP-IDF_S-ONLY", namespace = "http://pivot.datamodel.esb.cirso.fr/1.0")
@XmlEnum
public enum EnumTYPIDFSONLY {

    @XmlEnumValue("numeroPartenaire")
    NUMERO_PARTENAIRE("numeroPartenaire");
    private final String value;

    EnumTYPIDFSONLY(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTYPIDFSONLY fromValue(String v) {
        for (EnumTYPIDFSONLY c: EnumTYPIDFSONLY.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
