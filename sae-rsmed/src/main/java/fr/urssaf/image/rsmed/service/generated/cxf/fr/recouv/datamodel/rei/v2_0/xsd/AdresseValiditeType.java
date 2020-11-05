
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour AdresseValidite_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="AdresseValidite_Type"&gt;
 *   &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"&gt;
 *     &lt;maxLength value="4"/&gt;
 *     &lt;enumeration value="PSA"/&gt;
 *     &lt;enumeration value="NPAI"/&gt;
 *     &lt;enumeration value="VAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AdresseValidite_Type")
@XmlEnum
public enum AdresseValiditeType {

    PSA,
    NPAI,
    VAL;

    public String value() {
        return name();
    }

    public static AdresseValiditeType fromValue(String v) {
        return valueOf(v);
    }

}
