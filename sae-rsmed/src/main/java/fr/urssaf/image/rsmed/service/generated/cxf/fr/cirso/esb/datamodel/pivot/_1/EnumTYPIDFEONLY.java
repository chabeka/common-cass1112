
package fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Enum_TYP-IDF_E-ONLY.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Enum_TYP-IDF_E-ONLY"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="numeroCompteExterne"/&gt;
 *     &lt;enumeration value="numeroCompteInterne"/&gt;
 *     &lt;enumeration value="numeroCompteExterneRg"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enum_TYP-IDF_E-ONLY", namespace = "http://pivot.datamodel.esb.cirso.fr/1.0")
@XmlEnum
public enum EnumTYPIDFEONLY {

    @XmlEnumValue("numeroCompteExterne")
    NUMERO_COMPTE_EXTERNE("numeroCompteExterne"),
    @XmlEnumValue("numeroCompteInterne")
    NUMERO_COMPTE_INTERNE("numeroCompteInterne"),
    @XmlEnumValue("numeroCompteExterneRg")
    NUMERO_COMPTE_EXTERNE_RG("numeroCompteExterneRg");
    private final String value;

    EnumTYPIDFEONLY(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTYPIDFEONLY fromValue(String v) {
        for (EnumTYPIDFEONLY c: EnumTYPIDFEONLY.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
