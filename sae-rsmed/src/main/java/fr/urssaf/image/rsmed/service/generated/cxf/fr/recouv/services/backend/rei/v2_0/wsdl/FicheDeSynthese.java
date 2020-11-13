package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.3.7
 * 2020-11-05T10:03:07.829+01:00
 * Generated source version: 3.3.7
 *
 */
@WebService(targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", name = "FicheDeSynthese")
@XmlSeeAlso({ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface FicheDeSynthese {

    @WebMethod(operationName = "ConsulterDossierIndividu", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/ConsulterDossierIndividu")
    @WebResult(name = "ConsulterDossierIndividuResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "ConsulterDossierIndividuResponse")
    public ConsulterDossierIndividuResponse consulterDossierIndividu(

        @WebParam(partName = "ConsulterDossierIndividu", name = "ConsulterDossierIndividu", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        ConsulterDossierIndividu consulterDossierIndividu
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherEntreprisesEtablissementsAdressesParIdREI", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherEntreprisesEtablissementsAdressesParIdREI")
    @WebResult(name = "RechercherEntreprisesEtablissementsAdressesParIdREIResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherEntreprisesEtablissementsAdressesParIdREIResponse")
    public RechercherEntreprisesEtablissementsAdressesParIdREIResponse rechercherEntreprisesEtablissementsAdressesParIdREI(

        @WebParam(partName = "RechercherEntreprisesEtablissementsAdressesParIdREI", name = "RechercherEntreprisesEtablissementsAdressesParIdREI", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherEntreprisesEtablissementsAdressesParIdREI rechercherEntreprisesEtablissementsAdressesParIdREI
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherRelations", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherRelations")
    @WebResult(name = "RechercherRelationsResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherRelationsResponse")
    public RechercherRelationsResponse rechercherRelations(

        @WebParam(partName = "RechercherRelationsRequest", name = "RechercherRelationsRequest", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherRelationsRequest rechercherRelationsRequest
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherCollaborateurs", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherCollaborateurs")
    @WebResult(name = "RechercherCollaborateursResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherCollaborateursResponse")
    public RechercherCollaborateursResponse rechercherCollaborateurs(

        @WebParam(partName = "RechercherCollaborateurs", name = "RechercherCollaborateurs", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherCollaborateurs rechercherCollaborateurs
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherDirigeantsEtCollaborateurs", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherDirigeantsEtCollaborateurs")
    @WebResult(name = "RechercherDirigeantsEtCollaborateursResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherDirigeantsEtCollaborateursResponse")
    public RechercherDirigeantsEtCollaborateursResponse rechercherDirigeantsEtCollaborateurs(

        @WebParam(partName = "RechercherDirigeantsEtCollaborateurs", name = "RechercherDirigeantsEtCollaborateurs", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherDirigeantsEtCollaborateurs rechercherDirigeantsEtCollaborateurs
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "ConsulterDossierEtablissement", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/ConsulterDossierEtablissement")
    @WebResult(name = "ConsulterDossierEtablissementResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "ConsulterDossierEtablissementResponse")
    public ConsulterDossierEtablissementResponse consulterDossierEtablissement(

        @WebParam(partName = "ConsulterDossierEtablissement", name = "ConsulterDossierEtablissement", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        ConsulterDossierEtablissement consulterDossierEtablissement
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherDirigeantsEtRelations", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherDirigeantsEtRelations")
    @WebResult(name = "RechercherDirigeantsEtRelationsResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherDirigeantsEtRelationsResponse")
    public RechercherDirigeantsEtRelationsResponse rechercherDirigeantsEtRelations(

        @WebParam(partName = "RechercherDirigeantsEtRelations", name = "RechercherDirigeantsEtRelationsRequest", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherDirigeantsEtRelationsRequest rechercherDirigeantsEtRelations
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "ConsulterDossierEntreprise", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/ConsulterDossierEntreprise")
    @WebResult(name = "ConsulterDossierEntrepriseResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "ConsulterDossierEntrepriseResponse")
    public ConsulterDossierEntrepriseResponse consulterDossierEntreprise(

        @WebParam(partName = "ConsulterDossierEntreprise", name = "ConsulterDossierEntreprise", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        ConsulterDossierEntreprise consulterDossierEntreprise
    ) throws TechnicalFaultMessage, BusinessFaultMessage;
}