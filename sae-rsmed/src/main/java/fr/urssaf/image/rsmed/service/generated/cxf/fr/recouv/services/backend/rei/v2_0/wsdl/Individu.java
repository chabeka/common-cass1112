package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * <p>Le service Entreprise et son port
 * 			définissent l'ensemble des opérations REI portant sur
 * 			l'Individu</p>
 * 			<p>Les opérations de consultation et de
 * 			recherche sont ouvertes à
 * 			l'ensemble des partenaires.</p>
 * 			<p>Les opérations de
 * 			création, ajout, mise à jour et suppression
 * 			sont à venir dans les
 * 			paliers ultérieurs </p> 
 *
 * This class was generated by Apache CXF 3.3.7
 * 2020-11-05T10:03:07.854+01:00
 * Generated source version: 3.3.7
 *
 */
@WebService(targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", name = "Individu")
@XmlSeeAlso({ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.cirso.esb.datamodel.pivot._1.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.ObjectFactory.class, fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Individu {

    @WebMethod(operationName = "RechercherCollaborateursDuDirigeant", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherCollaborateursDuDirigeant")
    @WebResult(name = "RechercherCollaborateursDuDirigeantResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherCollaborateursDuDirigeantResponse")
    public RechercherCollaborateursDuDirigeantResponse rechercherCollaborateursDuDirigeant(

        @WebParam(partName = "RechercherCollaborateursDuDirigeant", name = "RechercherCollaborateursDuDirigeant", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherCollaborateursDuDirigeant rechercherCollaborateursDuDirigeant
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherIndividuParIdRei", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividuParIdRei")
    @WebResult(name = "RechercherIndividuParIdReiResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividuParIdReiResponse")
    public RechercherIndividuParIdReiResponse rechercherIndividuParIdRei(

        @WebParam(partName = "RechercherIndividuParIdRei", name = "RechercherIndividuParIdRei", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividuParIdRei rechercherIndividuParIdRei
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherDirigeantsDeEntreprise", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherDirigeantsDeEntreprise")
    @WebResult(name = "RechercherDirigeantsDeEntrepriseResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherDirigeantsDeEntrepriseResponse")
    public RechercherDirigeantsDeEntrepriseResponse rechercherDirigeantsDeEntreprise(

        @WebParam(partName = "RechercherDirigeantsDeEntreprise", name = "RechercherDirigeantsDeEntreprise", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherDirigeantsDeEntreprise rechercherDirigeantsDeEntreprise
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    /**
     * Cette opération permet de consulter l'individu
     * 				correspondant à la redevabilité passée en paramètre (par son
     * 				identifiant).
     * 				<br/> 
     */
    @WebMethod(operationName = "RechercherIndividuParIdRedevabilite", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividuParIdRedevabilite")
    @WebResult(name = "RechercherIndividuParIdRedevabiliteResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividuParIdRedevabiliteResponse")
    public RechercherIndividuParIdRedevabiliteResponse rechercherIndividuParIdRedevabilite(

        @WebParam(partName = "RechercherIndividuParIdRedevabilite", name = "RechercherIndividuParIdRedevabilite", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividuParIdRedevabilite rechercherIndividuParIdRedevabilite
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercheHistoriseeIndividuParNir", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercheHistoriseeIndividuParNir")
    @WebResult(name = "RechercheHistoriseeIndividuParNirResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercheHistoriseeIndividuParNirResponse")
    public RechercheHistoriseeIndividuParNirResponse rechercheHistoriseeIndividuParNir(

        @WebParam(partName = "RechercheHistoriseeIndividuParNir", name = "RechercheHistoriseeIndividuParNir", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercheHistoriseeIndividuParNir rechercheHistoriseeIndividuParNir
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherContactsDeIndividu", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherContactsDeIndividu")
    @WebResult(name = "RechercherContactsDeIndividuResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherContactsDeIndividuResponse")
    public RechercherContactsDeIndividuResponse rechercherContactsDeIndividu(

        @WebParam(partName = "RechercherContactsDeIndividu", name = "RechercherContactsDeIndividu", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherContactsDeIndividu rechercherContactsDeIndividu
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercherAdresseDomicileDeIndividu", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherAdresseDomicileDeIndividu")
    @WebResult(name = "RechercherAdresseDomicileDeIndividuResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherAdresseDomicileDeIndividuResponse")
    public RechercherAdresseDomicileDeIndividuResponse rechercherAdresseDomicileDeIndividu(

        @WebParam(partName = "RechercherAdresseDomicileDeIndividu", name = "RechercherAdresseDomicileDeIndividu", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherAdresseDomicileDeIndividu rechercherAdresseDomicileDeIndividu
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercheHistoriseeIndividuParSirenPersonnel", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercheHistoriseeIndividuParSirenPersonnel")
    @WebResult(name = "RechercheHistoriseeIndividuParSirenPersonnelResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercheHistoriseeIndividuParSirenPersonnelResponse")
    public RechercheHistoriseeIndividuParSirenPersonnelResponse rechercheHistoriseeIndividuParSirenPersonnel(

        @WebParam(partName = "RechercheHistoriseeIndividuParSirenPersonnel", name = "RechercheHistoriseeIndividuParSirenPersonnel", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercheHistoriseeIndividuParSirenPersonnel rechercheHistoriseeIndividuParSirenPersonnel
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercheHistoriseeIndividuParIdRei", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercheHistoriseeIndividuParIdRei")
    @WebResult(name = "RechercheHistoriseeIndividuParIdReiResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercheHistoriseeIndividuParIdReiResponse")
    public RechercheHistoriseeIndividuParIdReiResponse rechercheHistoriseeIndividuParIdRei(

        @WebParam(partName = "RechercheHistoriseeIndividuParIdRei", name = "RechercheHistoriseeIndividuParIdRei", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercheHistoriseeIndividuParIdRei rechercheHistoriseeIndividuParIdRei
    ) throws BusinessFaultMessage, TechnicalFaultMessage;

    @WebMethod(operationName = "RechercheMultipleIndividuParNir", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercheMultipleIndividuParNir")
    @WebResult(name = "RechercheMultipleIndividuParNirResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercheMultipleIndividuParNirResponse")
    public RechercheMultipleIndividuParNirResponse rechercheMultipleIndividuParNir(

        @WebParam(partName = "RechercheMultipleIndividuParNir", name = "RechercheMultipleIndividuParNir", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercheMultipleIndividuParNir rechercheMultipleIndividuParNir
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherIndividuParNir", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividuParNir")
    @WebResult(name = "RechercherIndividuParNirResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividuParNirResponse")
    public RechercherIndividuParNirResponse rechercherIndividuParNir(

        @WebParam(partName = "RechercherIndividuParNir", name = "RechercherIndividuParNir", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividuParNir rechercherIndividuParNir
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercheMultipleIndividuParIdRei", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercheMultipleIndividuParIdRei")
    @WebResult(name = "RechercheMultipleIndividuParIdReiResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercheMultipleIndividuParIdReiResponse")
    public RechercheMultipleIndividuParIdReiResponse rechercheMultipleIndividuParIdRei(

        @WebParam(partName = "RechercheMultipleIndividuParIdRei", name = "RechercheMultipleIndividuParIdRei", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercheMultipleIndividuParIdRei rechercheMultipleIndividuParIdRei
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "EstIndividuEPM", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/EstIndividuEPM")
    @WebResult(name = "EstIndividuEPMResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "EstIndividuEPMResponse")
    public EstIndividuEPMResponse estIndividuEPM(

        @WebParam(partName = "EstIndividuEPM", name = "EstIndividuEPM", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        EstIndividuEPM estIndividuEPM
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherIndividuParSirenPersonnel", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividuParSirenPersonnel")
    @WebResult(name = "RechercherIndividuParSirenPersonnelResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividuParSirenPersonnelResponse")
    public RechercherIndividuParSirenPersonnelResponse rechercherIndividuParSirenPersonnel(

        @WebParam(partName = "RechercherIndividuParSirenPersonnel", name = "RechercherIndividuParSirenPersonnel", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividuParSirenPersonnel rechercherIndividuParSirenPersonnel
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherIdDirigeantDuCollaborateur", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIdDirigeantDuCollaborateur")
    @WebResult(name = "RechercherIdDirigeantDuCollaborateurResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIdDirigeantDuCollaborateurResponse")
    public RechercherIdDirigeantDuCollaborateurResponse rechercherIdDirigeantDuCollaborateur(

        @WebParam(partName = "RechercherIdDirigeantDuCollaborateur", name = "RechercherIdDirigeantDuCollaborateur", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIdDirigeantDuCollaborateur rechercherIdDirigeantDuCollaborateur
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherIndividusParAdresseDomicile", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividusParAdresseDomicile")
    @WebResult(name = "RechercherIndividusParAdresseDomicileResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividusParAdresseDomicileResponse")
    public RechercherIndividusParAdresseDomicileResponse rechercherIndividusParAdresseDomicile(

        @WebParam(partName = "RechercherIndividusParAdresseDomicile", name = "RechercherIndividusParAdresseDomicile", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividusParAdresseDomicile rechercherIndividusParAdresseDomicile
    ) throws TechnicalFaultMessage, BusinessFaultMessage;

    @WebMethod(operationName = "RechercherIndividusParNomPrenom", action = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl/RechercherIndividusParNomPrenom")
    @WebResult(name = "RechercherIndividusParNomPrenomResponse", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", partName = "RechercherIndividusParNomPrenomResponse")
    public RechercherIndividusParNomPrenomResponse rechercherIndividusParNomPrenom(

        @WebParam(partName = "RechercherIndividusParNomPrenom", name = "RechercherIndividusParNomPrenom", targetNamespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl")
        RechercherIndividusParNomPrenom rechercherIndividusParNomPrenom
    ) throws BusinessFaultMessage, TechnicalFaultMessage;
}