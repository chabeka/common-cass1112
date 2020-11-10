package fr.urssaf.image.rsmed;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import fr.urssaf.image.rsmed.bean.CurrentDocumentBean;
import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import fr.urssaf.image.rsmed.bean.xsd.generated.MetadonneeType;
import fr.urssaf.image.rsmed.exception.FunctionalException;
import fr.urssaf.image.rsmed.job.Validation;
import fr.urssaf.image.rsmed.job.service.IndividuServiceInterface;
import fr.urssaf.image.rsmed.job.service.RedevabiliteServiceInterface;
import fr.urssaf.image.rsmed.job.service.XmlReaderServiceInterface;
import fr.urssaf.image.rsmed.job.service.impl.EnrichissementDocumentServiceImpl;
import fr.urssaf.image.rsmed.job.service.impl.XmlReaderServiceImpl;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.*;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;
import fr.urssaf.image.rsmed.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@Scope("test")
@ActiveProfiles({"test"})
public class ReadXmlAndConstructBeanTests {


    private static MockedStatic<FileUtils> fileUtilsMockedStatic;
    @Autowired
    EnrichissementDocumentServiceImpl enrichissementDocumentService;
    @Autowired
    PropertiesBean properties;
    @Autowired
    XmlReaderServiceInterface xmlReaderService;
    XMLInputFactory xmlInputFactory;
    @MockBean
    private IndividuServiceInterface individuService;
    @MockBean
    private RedevabiliteServiceInterface redevabiliteService;

    @Before
    public void setup() throws TechnicalFaultMessage, BusinessFaultMessage {

        when(redevabiliteService.getIdEntiteDuCompte(anyString()))
                .thenReturn(0L);

        when(individuService.getIndividuParIdRei(anyLong()))
                .thenReturn(getIndividuTypeForTest());

        when(redevabiliteService.getCodeUrssafParNumCptExterne(anyString()))
                .thenReturn("TU code urssaf");

        if (fileUtilsMockedStatic == null) {
            fileUtilsMockedStatic = mockStatic(FileUtils.class);
            fileUtilsMockedStatic.when(() -> FileUtils.getHash(anyString())).thenReturn("TU Hash");
        }

        xmlInputFactory = XMLInputFactory.newInstance();
    }


    @Test
    public void testReadXmlToCurrentDocumentBean() {
        Arrays.asList("/fd30ado1_IN/fichier_1_in.xml", "/fd30ado2_IN/fichier_2_in.xml").stream().forEach(xmlFile -> {

                    Map<String, String> correspondances = new HashMap<>();
                    correspondances.put("file1_idv2", "1170000015577462760088862507");
                    correspondances.put("file_1_pdf", "doc_2C15525198118_RSI.pdf");
                    correspondances.put("file2_idv2", "2470000017614720730061213465");
                    correspondances.put("file_2_pdf", "NR_2C15524056457.pdf");


                    try {
                        int i = Integer.parseInt(xmlFile.split("_")[2]);
                        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(properties.getWorkdirDirectory() + File.separator + xmlFile));
                        XmlReaderServiceImpl.setReader(reader);


                        CurrentDocumentBean currentDocumentBean = xmlReaderService.getNextDocument();


                        Assertions.assertThat(currentDocumentBean)
                                .as("currentDocumentBean")
                                .isNotNull();

                        Assertions.assertThat(currentDocumentBean.getIdV2())
                                .as("idV2 file_" + i)
                                .isEqualTo(correspondances.get("file" + i + "_idv2"));

                        Assertions.assertThat(currentDocumentBean.getPdf())
                                .as("pdf name")
                                .isEqualTo("src/test/resources/xml" + File.separator + correspondances.get("file_" + i + "_pdf"));
                        i++;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

        );

    }


    @Test
    public void testInvalidIdV2ShouldThrowFunctionalException() {

        CurrentDocumentBean currentDocumentBean = getCurrentDocumentValid();
        currentDocumentBean.setIdV2("");
        Assertions.assertThatThrownBy(() ->
                enrichissementDocumentService.addMetadatasToCurrentDocument(currentDocumentBean))
                .isInstanceOf(FunctionalException.class)
                .hasMessageContaining("Le champ ID_V2 n'est pas valide");
    }

    @Test
    public void testRequiredMetadonneesShouldThrowFunctionnelException() {

        try {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream("src/test/resources/xml/invalid_IN/fichier_1_in.xml"));

            XmlReaderServiceImpl.setReader(reader);


            CurrentDocumentBean currentDocumentBean = xmlReaderService.getNextDocument();
            currentDocumentBean.setListeMetadonneeType(new ListeMetadonneeType());

            Assertions.assertThatThrownBy(() -> Validation.validateMetadonnees(currentDocumentBean.getListeMetadonneeType()))
                    .isInstanceOf(FunctionalException.class)
                    .hasMessageContaining("Le champ Titre n'est pas valide");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @Test
    public void testAjoutMetadonneesOK() {


        Arrays.asList("fd30ado1_IN" + File.separator + "fichier_1_in.xml", "fd30ado2_IN" + File.separator + "fichier_2_in.xml").forEach(xmlFile -> {


                    try {
                        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(properties.getWorkdirDirectory() + File.separator + xmlFile));

                        XmlReaderServiceImpl.setReader(reader);


                        CurrentDocumentBean currentDocumentBean = xmlReaderService.getNextDocument();
                        currentDocumentBean.setTitre("TU titre");
                        currentDocumentBean.setCodeRND("TU codeRND");
                        enrichissementDocumentService.addMetadatasToCurrentDocument(currentDocumentBean);


                        List<MetadonneeType> metadonnees = currentDocumentBean.getListeMetadonneeType().getMetadonnee();
                        Assertions.assertThat(metadonnees)
                                .size()
                                .as("metadonnées size")
                                .isEqualTo(20);

                        metadonnees.stream().forEach(
                                m -> {
                                    switch (m.getCode()) {
                                        case "Hash":
                                            Assertions.assertThat(m.getValeur()).as("valeur de la metadata hash").isEqualTo("TU Hash");
                                            break;
                                        case "ApplicationProductrice":
                                            Assertions.assertThat(m.getValeur()).as("valeur de la metadata  ApplicationProductrice").isEqualTo("RSMED_CONOT");
                                            break;
                                        case "ApplicationTraitement":
                                            Assertions.assertThat(m.getValeur()).as("valeur de la metadata  ApplicationTraitement").isEqualTo("SATURNE");
                                            break;
                                    }
                                }
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }


    private CurrentDocumentBean getCurrentDocumentValid() {
        CurrentDocumentBean currentDocumentBean = new CurrentDocumentBean();
        currentDocumentBean.setIdV2("1170000015577462760088862507");
        currentDocumentBean.setTitre("TU titre");
        currentDocumentBean.setTitre("TU codeRND");
        return currentDocumentBean;
    }

    private IndividuType getIndividuTypeForTest() {
        IndividuType individuType = new IndividuType();
        SIRENAlphanumeriqueCertificationType siren = new SIRENAlphanumeriqueCertificationType();
        siren.setCodeStatut(1);
        siren.setValeur("TU siren");
        individuType.setSirenPersonnel(siren);

        NIRNumeroCertificationType nir = new NIRNumeroCertificationType();
        nir.setCodeStatut(1);
        nir.setValeur("TU NIR");
        individuType.setNir(nir);

        NIRCleCertificationType cleNir = new NIRCleCertificationType();
        cleNir.setValeur("TU cle nir");
        individuType.setCleNir(cleNir);

        NomCertificationType nom = new NomCertificationType();
        nom.setValeur("TU nom &");
        individuType.setNomPatronymique(nom);

        PrenomsPatronymiquesCertificationType prenom = new PrenomsPatronymiquesCertificationType();
        prenom.setValeur("TU prenom");
        individuType.setPrenomsPatronymiques(prenom);

        DateCertificationType date = new DateCertificationType();
        date.setValeur(XMLGregorianCalendarImpl.createDate(2020, 11, 1, 0));
        individuType.setDateNaissance(date);

        NoRIBACertificationType noRiba = new NoRIBACertificationType();
        noRiba.setValeur("TU noRiba");
        individuType.setNoRiba(noRiba);

        return individuType;
    }
}