package fr.urssaf.image.rsmed.job.service;

import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;

import java.io.IOException;


public interface DocumentConstructorServiceInterface {

    void addMetadatasToCurrentDocument(CurrentDocumentBean currentDocumentBean) throws BusinessFaultMessage, IOException, TechnicalFaultMessage;

}
