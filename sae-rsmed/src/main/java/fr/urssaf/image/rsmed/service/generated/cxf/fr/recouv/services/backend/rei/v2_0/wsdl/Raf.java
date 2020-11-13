package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.3.7
 * 2020-11-05T10:03:07.827+01:00
 * Generated source version: 3.3.7
 *
 */
@WebService(targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", name = "Raf")
@XmlSeeAlso({ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Raf {

    @WebMethod(operationName = "RechercherEtablissementsParEntreprise", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherEtablissementsParEntreprise")
    @WebResult(name = "RechercherEtablissementsParEntrepriseResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherEtablissementsParEntrepriseResponse")
    public RechercherEtablissementsParEntrepriseResponse rechercherEtablissementsParEntreprise(

        @WebParam(partName = "RechercherEtablissementsParEntreprise", name = "RechercherEtablissementsParEntreprise", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherEtablissementsParEntreprise rechercherEtablissementsParEntreprise
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherEtablissementCompteParNoCompteExterne", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherEtablissementCompteParNoCompteExterne")
    @WebResult(name = "RechercherEtablissementCompteParNoCompteExterneResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherEtablissementCompteParNoCompteExterneResponse")
    public RechercherEtablissementCompteParNoCompteExterneResponse rechercherEtablissementCompteParNoCompteExterne(

        @WebParam(partName = "RechercherEtablissementCompteParNoCompteExterneRequest", name = "RechercherEtablissementCompteParNoCompteExterneRequest", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherEtablissementCompteParNoCompteExterneRequest rechercherEtablissementCompteParNoCompteExterneRequest
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    /**
     * Cette opération permet de consulter les données
     * 				d'une entreprise à partir du SIREN de cette dernière.<br/>
     * 				Le
     * 				SIREN étant une donnée caractérisant l'entrprise de manière unique,
     * 				cette opération renvoie une seule entreprise en sortie.<br/>
     * 				Il est également possible de préciser que seules les entreprises
     * 				actives intéressent l'appelant. <br/>
     * 				<p>
     * 				Si ce filtre est
     * 				utilisé, le service renvoie <br/>
     * 				- L'entreprise dont le SIREN
     * 				est celui précisé en entrée, si cette entreprise existe et qu'elle
     * 				est active<br/>
     * 				- zéro résultats si <br/>
     * 				- Aucune
     * 				entreprise correspondant à ce SIREN n'existe
     * 				- L'entreprise
     * 				correspondant à ce SIREN n'est pas active
     * 				<p>
     * 				Cas
     * 				d'envoi d'une
     * 				Business Fault <br/>
     * 				- SIREN incorrect<br/>
     * 				- Plusieurs
     * 				entreprises correspondant à ce SIREN existent dans le
     * 				REI. Dans ce
     * 				cas rare, l'appelant est invité à s'appuyer sur les
     * 				autres recherches
     * 				(par dénomination ou par adresse avec options de recherche afin de
     * 				cibler l'entreprise qui l'intéresse)
     * 				</p>
     * 				<p>
     * 				Cas d'envoi
     * 				d'une Technical Fault<br/>
     * 				- Indisponibilité du REI<br/>
     * 				- Indisponibilité d'un composant du SI sur lequel le REI s'appuie
     * 				pour fournir la réponse
     * 				</p> 
     */
    @WebMethod(operationName = "RechercherEntrepriseParSiren", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherEntrepriseParSiren")
    @WebResult(name = "RechercherEntrepriseParSirenResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherEntrepriseParSirenResponse")
    public RechercherEntrepriseParSirenResponse rechercherEntrepriseParSiren(

        @WebParam(partName = "RechercherEntrepriseParSiren", name = "RechercherEntrepriseParSiren", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherEntrepriseParSiren rechercherEntrepriseParSiren
    ) throws TechnicalFaultMessage, BusinessFaultMessage;
}