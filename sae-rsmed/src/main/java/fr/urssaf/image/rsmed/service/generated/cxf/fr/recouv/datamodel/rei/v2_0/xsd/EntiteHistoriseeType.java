
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour EntiteHistorisee_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="EntiteHistorisee_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Individu"/&gt;
 *     &lt;enumeration value="Dirigeant"/&gt;
 *     &lt;enumeration value="Etablissement"/&gt;
 *     &lt;enumeration value="Entreprise"/&gt;
 *     &lt;enumeration value="EIRL"/&gt;
 *     &lt;enumeration value="Redevabilite"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EntiteHistorisee_Type")
@XmlEnum
public enum EntiteHistoriseeType {

    @XmlEnumValue("Individu")
    INDIVIDU("Individu"),
    @XmlEnumValue("Dirigeant")
    DIRIGEANT("Dirigeant"),
    @XmlEnumValue("Etablissement")
    ETABLISSEMENT("Etablissement"),
    @XmlEnumValue("Entreprise")
    ENTREPRISE("Entreprise"),
    EIRL("EIRL"),
    @XmlEnumValue("Redevabilite")
    REDEVABILITE("Redevabilite");
    private final String value;

    EntiteHistoriseeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EntiteHistoriseeType fromValue(String v) {
        for (EntiteHistoriseeType c: EntiteHistoriseeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
