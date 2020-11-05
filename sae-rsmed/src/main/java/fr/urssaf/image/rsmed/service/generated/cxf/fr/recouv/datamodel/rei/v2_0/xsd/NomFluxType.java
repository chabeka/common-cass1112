
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour NomFlux_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="NomFlux_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CFE"/&gt;
 *     &lt;enumeration value="RSI"/&gt;
 *     &lt;enumeration value="INSEE"/&gt;
 *     &lt;enumeration value="COHERENCE"/&gt;
 *     &lt;enumeration value="TELEDEP"/&gt;
 *     &lt;enumeration value="DEBRAYAGE"/&gt;
 *     &lt;enumeration value="TRANSACTIONNEL"/&gt;
 *     &lt;enumeration value="AUTRES"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NomFlux_Type")
@XmlEnum
public enum NomFluxType {

    CFE,
    RSI,
    INSEE,
    COHERENCE,
    TELEDEP,
    DEBRAYAGE,
    TRANSACTIONNEL,
    AUTRES;

    public String value() {
        return name();
    }

    public static NomFluxType fromValue(String v) {
        return valueOf(v);
    }

}
