package fr.urssaf.image.rsmed.job.service;

import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;

public interface RedevabiliteServiceInterface {
    Long getIdEntiteDuCompte(String numeroCompteExterne) throws TechnicalFaultMessage, BusinessFaultMessage;

    String getCodeUrssafParNumCptExterne(String numeroCompteExterne) throws TechnicalFaultMessage, BusinessFaultMessage;
}
