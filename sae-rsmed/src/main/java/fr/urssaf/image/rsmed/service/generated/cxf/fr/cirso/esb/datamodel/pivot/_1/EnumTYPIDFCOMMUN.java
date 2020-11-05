
package fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Enum_TYP-IDF_COMMUN.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Enum_TYP-IDF_COMMUN"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SIREN"/&gt;
 *     &lt;enumeration value="numeroEtablissement"/&gt;
 *     &lt;enumeration value="numeroPersonne"/&gt;
 *     &lt;enumeration value="NNI"/&gt;
 *     &lt;enumeration value="RIB"/&gt;
 *     &lt;enumeration value="telephone"/&gt;
 *     &lt;enumeration value="fax"/&gt;
 *     &lt;enumeration value="numeroStructure"/&gt;
 *     &lt;enumeration value="numeroChambreMetier"/&gt;
 *     &lt;enumeration value="numeroEcartNegatif"/&gt;
 *     &lt;enumeration value="numeroPraticien"/&gt;
 *     &lt;enumeration value="AGESSA_MDA"/&gt;
 *     &lt;enumeration value="numeroCAF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Enum_TYP-IDF_COMMUN", namespace = "http://pivot.datamodel.esb.cirso.fr/1.0")
@XmlEnum
public enum EnumTYPIDFCOMMUN {

    SIREN("SIREN"),
    @XmlEnumValue("numeroEtablissement")
    NUMERO_ETABLISSEMENT("numeroEtablissement"),
    @XmlEnumValue("numeroPersonne")
    NUMERO_PERSONNE("numeroPersonne"),
    NNI("NNI"),
    RIB("RIB"),
    @XmlEnumValue("telephone")
    TELEPHONE("telephone"),
    @XmlEnumValue("fax")
    FAX("fax"),
    @XmlEnumValue("numeroStructure")
    NUMERO_STRUCTURE("numeroStructure"),
    @XmlEnumValue("numeroChambreMetier")
    NUMERO_CHAMBRE_METIER("numeroChambreMetier"),
    @XmlEnumValue("numeroEcartNegatif")
    NUMERO_ECART_NEGATIF("numeroEcartNegatif"),
    @XmlEnumValue("numeroPraticien")
    NUMERO_PRATICIEN("numeroPraticien"),
    AGESSA_MDA("AGESSA_MDA"),
    @XmlEnumValue("numeroCAF")
    NUMERO_CAF("numeroCAF");
    private final String value;

    EnumTYPIDFCOMMUN(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumTYPIDFCOMMUN fromValue(String v) {
        for (EnumTYPIDFCOMMUN c: EnumTYPIDFCOMMUN.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
