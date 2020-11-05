package fr.urssaf.image.rsmed.job.service.impl;

import fr.urssaf.image.rsmed.job.service.IndividuServiceInterface;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.*;
import org.springframework.stereotype.Service;

@Service
public class IndividuServiceImpl implements IndividuServiceInterface {
    @Override
    public IndividuType getIndividuParIdRei(Long idEntite) throws TechnicalFaultMessage, BusinessFaultMessage {
        ObjectFactory factory = new ObjectFactory();
        RechercherIndividuParIdRei rechercherIndividuParIdRei = factory.createRechercherIndividuParIdRei();
        rechercherIndividuParIdRei.setIdIndividu(idEntite);
        Individu individuServiceSOAP = new IndividuService(IndividuService.WSDL_LOCATION, IndividuService.SERVICE).getIndividuServiceSOAP();
        return  individuServiceSOAP.rechercherIndividuParIdRei(rechercherIndividuParIdRei).getIndividu();
    }
}
