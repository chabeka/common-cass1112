package fr.urssaf.image.rsmed;


import fr.urssaf.image.rsmed.job.service.impl.XmlReaderServiceImpl;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RsmedConotApplicationTests {


    @Autowired
    private XmlReaderServiceImpl process;

    @Test
    void contextLoads() {
        assertThat(process).isNotNull();
    }


    @Test
    void RechercherEntiteDuCompteShouldReturnEntiteDuCompte() throws TechnicalFaultMessage, BusinessFaultMessage {


        ObjectFactory factory = new ObjectFactory();
        RechercherEntiteDuCompte rechercherEntiteDuCompte = factory.createRechercherEntiteDuCompte();
        rechercherEntiteDuCompte.setNumeroCompteExterne("537000000502080752");


        Redevabilite redevabiliteServiceSOAP = new RedevabiliteService(RedevabiliteService.WSDL_LOCATION, RedevabiliteService.SERVICE).getRedevabiliteServiceSOAP();
        Long idEntite = redevabiliteServiceSOAP.rechercherEntiteDuCompte(rechercherEntiteDuCompte).getIdEntite();

        assertThat(536945L).isEqualTo(idEntite);
    }


    @Test
    void RechercherIndividuParIdReiShouldReturnIndividu() throws TechnicalFaultMessage, BusinessFaultMessage {


        ObjectFactory factory = new ObjectFactory();
        RechercherIndividuParIdRei rechercherIndividuParIdRei = factory.createRechercherIndividuParIdRei();
        rechercherIndividuParIdRei.setIdIndividu(536945L);


        Individu individu = new IndividuService(IndividuService.WSDL_LOCATION, IndividuService.SERVICE).getIndividuServiceSOAP();
        String codeCivilite = individu.rechercherIndividuParIdRei(rechercherIndividuParIdRei).getIndividu().getCivilite().getCodeCivilite();

        assertThat("2").isEqualTo(codeCivilite);
    }


}
