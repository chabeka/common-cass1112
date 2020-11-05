
package fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Enum_CD-ORI-DBT.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Enum_CD-ORI-DBT"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="debitNul"/&gt;
 *     &lt;enumeration value="debitCalculeDeclare"/&gt;
 *     &lt;enumeration value="gestionTO"/&gt;
 *     &lt;enumeration value="modificationTauxAT"/&gt;
 *     &lt;enumeration value="modificationTauxTransport"/&gt;
 *     &lt;enumeration value="controle"/&gt;
 *     &lt;enumeration value="inutilise"/&gt;
 *     &lt;enumeration value="sanctionsVirement"/&gt;
 *     &lt;enumeration value="trRegularises"/&gt;
 *     &lt;enumeration value="ETI"/&gt;
 *     &lt;enumeration value="travailDissumule"/&gt;
 *     &lt;enumeration value="redressementContratExoneration"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enum_CD-ORI-DBT", namespace = "http://pivot.datamodel.esb.cirso.fr/1.0")
@XmlEnum
public enum EnumCDORIDBT {

    @XmlEnumValue("debitNul")
    DEBIT_NUL("debitNul"),
    @XmlEnumValue("debitCalculeDeclare")
    DEBIT_CALCULE_DECLARE("debitCalculeDeclare"),
    @XmlEnumValue("gestionTO")
    GESTION_TO("gestionTO"),
    @XmlEnumValue("modificationTauxAT")
    MODIFICATION_TAUX_AT("modificationTauxAT"),
    @XmlEnumValue("modificationTauxTransport")
    MODIFICATION_TAUX_TRANSPORT("modificationTauxTransport"),
    @XmlEnumValue("controle")
    CONTROLE("controle"),
    @XmlEnumValue("inutilise")
    INUTILISE("inutilise"),
    @XmlEnumValue("sanctionsVirement")
    SANCTIONS_VIREMENT("sanctionsVirement"),
    @XmlEnumValue("trRegularises")
    TR_REGULARISES("trRegularises"),
    ETI("ETI"),
    @XmlEnumValue("travailDissumule")
    TRAVAIL_DISSUMULE("travailDissumule"),
    @XmlEnumValue("redressementContratExoneration")
    REDRESSEMENT_CONTRAT_EXONERATION("redressementContratExoneration");
    private final String value;

    EnumCDORIDBT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumCDORIDBT fromValue(String v) {
        for (EnumCDORIDBT c: EnumCDORIDBT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
