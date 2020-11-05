
package fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Enum_CD-TYP-REV.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Enum_CD-TYP-REV"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="cotisationSociale"/&gt;
 *     &lt;enumeration value="revenusAgricoles"/&gt;
 *     &lt;enumeration value="revenusActivite"/&gt;
 *     &lt;enumeration value="revenusRemplacement"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enum_CD-TYP-REV", namespace = "http://pivot.datamodel.esb.cirso.fr/1.0")
@XmlEnum
public enum EnumCDTYPREV {

    @XmlEnumValue("cotisationSociale")
    COTISATION_SOCIALE("cotisationSociale"),
    @XmlEnumValue("revenusAgricoles")
    REVENUS_AGRICOLES("revenusAgricoles"),
    @XmlEnumValue("revenusActivite")
    REVENUS_ACTIVITE("revenusActivite"),
    @XmlEnumValue("revenusRemplacement")
    REVENUS_REMPLACEMENT("revenusRemplacement");
    private final String value;

    EnumCDTYPREV(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumCDTYPREV fromValue(String v) {
        for (EnumCDTYPREV c: EnumCDTYPREV.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
