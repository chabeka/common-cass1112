package fr.urssaf.image.rsmed.job.service;

import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;

public interface IndividuServiceInterface {

    IndividuType getIndividuParIdRei(Long idEntite) throws TechnicalFaultMessage, BusinessFaultMessage;
}
