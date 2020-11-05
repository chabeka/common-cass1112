
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Adr_TypeVoie_Type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="Adr_TypeVoie_Type"&gt;
 *   &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}Adr_TypeVoie_Type"&gt;
 *     &lt;enumeration value="ALL"/&gt;
 *     &lt;enumeration value="AV"/&gt;
 *     &lt;enumeration value="BD"/&gt;
 *     &lt;enumeration value="CAR"/&gt;
 *     &lt;enumeration value="CHE"/&gt;
 *     &lt;enumeration value="CHS"/&gt;
 *     &lt;enumeration value="CITE"/&gt;
 *     &lt;enumeration value="COR"/&gt;
 *     &lt;enumeration value="CRS"/&gt;
 *     &lt;enumeration value="CTRE"/&gt;
 *     &lt;enumeration value="DOM"/&gt;
 *     &lt;enumeration value="DSC"/&gt;
 *     &lt;enumeration value="ECA"/&gt;
 *     &lt;enumeration value="ESP"/&gt;
 *     &lt;enumeration value="FG"/&gt;
 *     &lt;enumeration value="GR"/&gt;
 *     &lt;enumeration value="HAM"/&gt;
 *     &lt;enumeration value="HLE"/&gt;
 *     &lt;enumeration value="IMM"/&gt;
 *     &lt;enumeration value="IMP"/&gt;
 *     &lt;enumeration value="LD"/&gt;
 *     &lt;enumeration value="LOT"/&gt;
 *     &lt;enumeration value="MAR"/&gt;
 *     &lt;enumeration value="MTE"/&gt;
 *     &lt;enumeration value="PAS"/&gt;
 *     &lt;enumeration value="PL"/&gt;
 *     &lt;enumeration value="PLN"/&gt;
 *     &lt;enumeration value="PLT"/&gt;
 *     &lt;enumeration value="PRO"/&gt;
 *     &lt;enumeration value="PRV"/&gt;
 *     &lt;enumeration value="QUA"/&gt;
 *     &lt;enumeration value="QUAI"/&gt;
 *     &lt;enumeration value="RES"/&gt;
 *     &lt;enumeration value="RLE"/&gt;
 *     &lt;enumeration value="ROC"/&gt;
 *     &lt;enumeration value="RPT"/&gt;
 *     &lt;enumeration value="RTE"/&gt;
 *     &lt;enumeration value="RUE"/&gt;
 *     &lt;enumeration value="SEN"/&gt;
 *     &lt;enumeration value="SQ"/&gt;
 *     &lt;enumeration value="TPL"/&gt;
 *     &lt;enumeration value="TRA"/&gt;
 *     &lt;enumeration value="VLA"/&gt;
 *     &lt;enumeration value="VLGE"/&gt;
 *     &lt;enumeration value="ZAC"/&gt;
 *     &lt;enumeration value="ZI"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Adr_TypeVoie_Type")
@XmlEnum
public enum AdrTypeVoieType {


    /**
     * Allée
     * 
     */
    ALL,

    /**
     * Avenue
     * 
     */
    AV,

    /**
     * Boulevard
     * 
     */
    BD,

    /**
     * Carrefour
     * 
     */
    CAR,

    /**
     * Chemin
     * 
     */
    CHE,

    /**
     * Chaussée
     * 
     */
    CHS,

    /**
     * Cité
     * 
     */
    CITE,

    /**
     * Corniche
     * 
     */
    COR,

    /**
     * Cours
     * 
     */
    CRS,

    /**
     * Centre
     * 
     */
    CTRE,

    /**
     * Domaine
     * 
     */
    DOM,

    /**
     * Descente
     * 
     */
    DSC,

    /**
     * Ecart
     * 
     */
    ECA,

    /**
     * Esplanade
     * 
     */
    ESP,

    /**
     * Faubourg
     * 
     */
    FG,

    /**
     * Grand Rue
     * 
     */
    GR,

    /**
     * Hameau
     * 
     */
    HAM,

    /**
     * Halle
     * 
     */
    HLE,

    /**
     * Immeuble
     * 
     */
    IMM,

    /**
     * Impasse
     * 
     */
    IMP,

    /**
     * Lieu-dit
     * 
     */
    LD,

    /**
     * Lotissement
     * 
     */
    LOT,

    /**
     * Marché
     * 
     */
    MAR,

    /**
     * Montée
     * 
     */
    MTE,

    /**
     * Passage
     * 
     */
    PAS,

    /**
     * Place
     * 
     */
    PL,

    /**
     * Plaine
     * 
     */
    PLN,

    /**
     * Plateau
     * 
     */
    PLT,

    /**
     * Promenade
     * 
     */
    PRO,

    /**
     * Parvis
     * 
     */
    PRV,

    /**
     * Quartier
     * 
     */
    QUA,

    /**
     * Quai
     * 
     */
    QUAI,

    /**
     * Résidence
     * 
     */
    RES,

    /**
     * Ruelle
     * 
     */
    RLE,

    /**
     * Rocade
     * 
     */
    ROC,

    /**
     * Rond-point
     * 
     */
    RPT,

    /**
     * Route
     * 
     */
    RTE,

    /**
     * Rue
     * 
     */
    RUE,

    /**
     * Sente et sentier
     * 
     */
    SEN,

    /**
     * Square
     * 
     */
    SQ,

    /**
     * Terre-Plein
     * 
     */
    TPL,

    /**
     * Traverse
     * 
     */
    TRA,

    /**
     * Villa
     * 
     */
    VLA,

    /**
     * Village
     * 
     */
    VLGE,

    /**
     * Zone d’aménagement concerté
     * 
     */
    ZAC,

    /**
     * Zone industrielle
     * 
     */
    ZI;

    public String value() {
        return name();
    }

    public static AdrTypeVoieType fromValue(String v) {
        return valueOf(v);
    }

}
