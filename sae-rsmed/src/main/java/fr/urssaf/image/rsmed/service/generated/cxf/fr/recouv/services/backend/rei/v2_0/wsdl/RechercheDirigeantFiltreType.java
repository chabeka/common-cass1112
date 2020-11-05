
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour RechercheDirigeantFiltre_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="RechercheDirigeantFiltre_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TOUS"/&gt;
 *     &lt;enumeration value="TI"/&gt;
 *     &lt;enumeration value="TI_ACTIF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RechercheDirigeantFiltre_Type")
@XmlEnum
public enum RechercheDirigeantFiltreType {

    TOUS,
    TI,
    TI_ACTIF;

    public String value() {
        return name();
    }

    public static RechercheDirigeantFiltreType fromValue(String v) {
        return valueOf(v);
    }

}
