package fr.urssaf.image.rsmed.job.service.impl;

import fr.urssaf.image.rsmed.job.service.RedevabiliteServiceInterface;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.*;
import org.springframework.stereotype.Service;

@Service
public class RedevabiliteServiceImpl implements RedevabiliteServiceInterface {
    @Override
    public Long getIdEntiteDuCompte(String numeroCompteExterne) throws TechnicalFaultMessage, BusinessFaultMessage {
        ObjectFactory factory = new ObjectFactory();

        RechercherEntiteDuCompte rechercherEntiteDuCompte = factory.createRechercherEntiteDuCompte();
        rechercherEntiteDuCompte.setNumeroCompteExterne(numeroCompteExterne);
        Redevabilite redevabiliteServiceSOAP = new RedevabiliteService(RedevabiliteService.WSDL_LOCATION, RedevabiliteService.SERVICE).getRedevabiliteServiceSOAP();

        return redevabiliteServiceSOAP.rechercherEntiteDuCompte(rechercherEntiteDuCompte).getIdEntite();
    }

    @Override
    public String getCodeUrssafParNumCptExterne(String numeroCompteExterne) throws TechnicalFaultMessage, BusinessFaultMessage {
        ObjectFactory factory = new ObjectFactory();

        RechercherRedevabiliteParNumCptExterne rechercherRedevabiliteParNumCptExterne = factory.createRechercherRedevabiliteParNumCptExterne();
        rechercherRedevabiliteParNumCptExterne.setNumCptExterne(numeroCompteExterne);
        Redevabilite redevabiliteServiceSOAP = new RedevabiliteService(RedevabiliteService.WSDL_LOCATION, RedevabiliteService.SERVICE).getRedevabiliteServiceSOAP();

        return redevabiliteServiceSOAP.rechercherRedevabiliteParNumCptExterne(rechercherRedevabiliteParNumCptExterne).getRedevabilite().getUrssafGestionnaire();

    }
}
