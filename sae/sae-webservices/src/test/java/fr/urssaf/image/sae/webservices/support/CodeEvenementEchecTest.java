package fr.urssaf.image.sae.webservices.support;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ibm.wsdl.PortTypeImpl;
import com.ibm.wsdl.xml.WSDLReaderImpl;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteRequestType;
import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieRequestType;
import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageRequestType;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.cirtil.www.saeservice.EtatTraitementsMasse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseRequestType;
import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineRequestType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.ListeUuidType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationMasse;
import fr.cirtil.www.saeservice.ModificationMasseRequestType;
import fr.cirtil.www.saeservice.ModificationRequestType;
import fr.cirtil.www.saeservice.NoteTxtType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.cirtil.www.saeservice.Reprise;
import fr.cirtil.www.saeservice.RepriseRequestType;
import fr.cirtil.www.saeservice.RestoreMasse;
import fr.cirtil.www.saeservice.StockageUnitaire;
import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionRequestType;
import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertMasse;
import fr.cirtil.www.saeservice.TransfertMasseRequestType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.webservices.constantes.TracesConstantes;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;
import fr.urssaf.image.sae.webservices.service.WSMetadataService;
import fr.urssaf.image.sae.webservices.service.WSTransfertService;
import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeletonInterface;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test2.xml" })
public class CodeEvenementEchecTest {

	   
	private static final Date DATE = new Date();
	private static final int MAX_COUNT = 10;
	
   @Autowired
   private SaeServiceSkeletonInterface skeleton;
   
   @Autowired
   private  TraceDestinataireSupport destSupport;
   
   @Autowired
   private TraceRegTechniqueSupport techSupport;
   
   @Autowired
   private WSMetadataService wsMetadataService;
   
   @Autowired
   private MetadataService metadataService;
   
   @Autowired
   SAETransfertService transfertService;
   
   @Autowired
   private SAEDocumentService documentService;
   
   @Autowired
   WSTransfertService wsTransfert;

   private  List<String> codes = new ArrayList<>();
   private  MessageContext ctx;

	@Before
	public  void init(){
		
		ctx = new MessageContext();
		
		if(codes.isEmpty()){
			List<TraceDestinataire> allTraceDes = destSupport.findAll();
			assertEquals(true, (allTraceDes.size() > 0));
			
			//
			codes = new ArrayList<>();
			for(TraceDestinataire trace: allTraceDes){
				if(trace.getCodeEvt().contains("WS_")){
					codes.add(trace.getCodeEvt());
				}
			}
			// on enlève les codes d'evt qui ne sont pas concernés par les appels
			// de webservice
			codes.remove(TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE);
			codes.remove(TracesConstantes.CODE_EVT_CHARGE_CRL);
			codes.remove(TracesConstantes.CODE_EVT_ECHEC_CHARGE_CRL);
			codes.remove(TracesConstantes.CODE_EVT_WS_PINGSECURE_KO);
		}
	}

	@Test
	public void appel_web_service_avec_exception() throws Exception {
	  
		
		 //  le test
		 //  Vérifier que toutes les méthodes exposées sur le webservices, 
		 //  tracent une erreur dans le registre de surveillance technique. Chaque erreur est lié à un code
		 //  d'evenement d'echec qui se trouve dans la table traceDestinataire
		 //  1 - on recupère tous les codes d'erreus dans la table traceDestinataire
		 //  2 - Pour chaque  méthode exposées sur le webservices on simule un appel du webservice avec 
		 //  	  lévé Exception. La lévé de l'Exception écrit une trace dans dans le registre de surveillance technique
		 //  3 - On effectue un appel dans la table RegTechnique en recuperant les dernier enrgistrements
		 //  4 - Le code d'erreur concernant le webservice appelé doit être parmit la liste des données recuperées
		 // En cas de l'ajout d'un nouveau service avec nouveau code d'erreur, il faudra modifier le test
		
		for(String opName : getListWebServiceName()){
		  initContextMessage(opName);
	      try {
	    	  
	    	  if("archivageUnitaire".equals(opName)){
	    		  // Appel à la au web service avec exception levée
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/archivageUnitaire_failure_metadonnees_vide.xml");
	    		  
	    		  ArchivageUnitaire request = ArchivageUnitaire.Factory.parse(reader);
	    		  skeleton.archivageUnitaireSecure(request).getArchivageUnitaireResponse();
	    		 
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  } else if("archivageUnitairePJ".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/archivageUnitairePJ_file_success.xml");
	    		  ArchivageUnitairePJ request = ArchivageUnitairePJ.Factory.parse(reader);
	    	      skeleton.archivageUnitairePJSecure(request)
	    	            .getArchivageUnitairePJResponse();
	    	      Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if("stockageUnitaire".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/stockageUnitaire_failure.xml");
	    		  StockageUnitaire request = StockageUnitaire.Factory.parse(reader);
	    	      skeleton.stockageUnitaireSecure(request);
	    	      Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("archivageMasse".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/archivageMasse_success.xml");
	    		  ArchivageMasse request = ArchivageMasse.Factory.parse(reader);
	    		  skeleton.archivageMasseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ( "archivageMasseAvecHash".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/archivageMasseAvecHash_FailureNoParam.xml");
	    		  ArchivageMasseAvecHash request = ArchivageMasseAvecHash.Factory.parse(reader);
	    		  skeleton.archivageMasseAvecHashSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("suppressionMasse".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/suppressionMasse_success.xml");
	    		  SuppressionMasse request = SuppressionMasse.Factory.parse(reader);
	    		  skeleton.suppressionMasseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("restoreMasse".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/restoreMasse_success.xml");
	    		  RestoreMasse request = RestoreMasse.Factory.parse(reader);
	    		  skeleton.restoreMasseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("consultation".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/consultation_success.xml");
	    		  Consultation request = Consultation.Factory.parse(reader);
	    		  skeleton.consultationSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("consultationMTOM".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/consultationMTOM_success.xml");
	    		  ConsultationMTOM request = ConsultationMTOM.Factory.parse(reader);
	    		  skeleton.consultationMTOMSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("consultationAffichable".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/consultation_affichable_failure.xml");
	    		  ConsultationAffichable request = ConsultationAffichable.Factory.parse(reader);
	    		  skeleton.consultationAffichableSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("rechercheParIterateur".equals(opName)){
	    		  
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/recherche_par_iterateur_failure.xml");
	    		  RechercheParIterateur request = RechercheParIterateur.Factory.parse(reader);
	    		  skeleton.rechercheParIterateurSecure(request);
	    		  	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("recherche".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/recherche_success.xml");
	    		  Recherche request = Recherche.Factory.parse(reader);
	    		  skeleton.rechercheSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("rechercheNbRes".equals(opName)){
	    		  XMLStreamReader reader = createConsultationResponseType("src/test/resources/request/recherche_success.xml");
	    		  Recherche request = Recherche.Factory.parse(reader);
	    		  skeleton.rechercheSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("transfert".equals(opName)){
	    		  Transfert request = new Transfert();
	    		  skeleton.transfertSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("suppression".equals(opName)){
	    		  
	    		  Suppression request = new Suppression();
	    	      SuppressionRequestType type = new SuppressionRequestType();
	    	      // pour force la lévé d'une exception lors de l'appel de suppression
	    	      type.setUuid(null);
	    	      request.setSuppression(type);
	    		  skeleton.suppressionSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("modification".equals(opName)){
	    		  Modification request = new Modification();
	    		  ModificationRequestType type = new ModificationRequestType();
	    	      UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    	      type.setUuid(uuidType);
	    	      request.setModification(type);
	    		  skeleton.modificationSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if("recuperationMetadonnees".equals(opName)){	    		  
	    		  mockRecuperationMetadonnees();
	    	  }
	    	  else if ("ajoutNote".equals(opName)){
	    		  AjoutNote request = new AjoutNote();
	    		  AjoutNoteRequestType type = new AjoutNoteRequestType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    	      type.setUuid(uuidType);
	    		  request.setAjoutNote(type);
	    		  NoteTxtType note = new NoteTxtType();
	    		  note.setNoteTxtType("note");
	    		  request.getAjoutNote().setNote(note);
	    		  skeleton.ajoutNoteSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("getDocFormatOrigine".equals(opName)){
	    		  GetDocFormatOrigine request = new GetDocFormatOrigine();
	    		  GetDocFormatOrigineRequestType type = new GetDocFormatOrigineRequestType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    	      type.setIdDoc(uuidType);
	    		  request.setGetDocFormatOrigine(type);    	      
	    		  skeleton.getDocFormatOrigineSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }else if ("etatTraitementsMasse".equals(opName)){
	    		  EtatTraitementsMasse request = new EtatTraitementsMasse();
	    		  EtatTraitementsMasseRequestType type = new EtatTraitementsMasseRequestType();
	    		  ListeUuidType listUuid = new ListeUuidType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    	      listUuid.addUuid(uuidType);
	    	      type.setListeUuid(listUuid);
	    		  request.setEtatTraitementsMasse(type);
	    	      
	    		  skeleton.etatTraitementsMasse(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }else if ("transfertMasse".equals(opName)){
	    		  TransfertMasse request = new TransfertMasse();
	    		  TransfertMasseRequestType type = new TransfertMasseRequestType();
	    		  type.setHash("");
	    		  type.setTypeHash("SHA-1");
	    		  EcdeUrlSommaireType urlEcde = new EcdeUrlSommaireType();
	    		  URI uri = new URI("ecde://cer69-ecde.cer69.recouv/DCL001/19991231/3/sommaire.xml");
	    		  urlEcde.setEcdeUrlSommaireType(uri);
	    		  type.setUrlSommaire(urlEcde);
	    		  
	    		  request.setTransfertMasse(type);
	    	      
	    		  skeleton.transfertMasseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }else if ("modificationMasse".equals(opName)){
	    		  ModificationMasse request = new ModificationMasse();
	    		  ModificationMasseRequestType type = new ModificationMasseRequestType();
	    		  type.setHash("");
	    		  type.setTypeHash("SHA-1");
	    		  EcdeUrlSommaireType urlEcde = new EcdeUrlSommaireType();
	    		  URI uri = new URI("ecde://cer69-ecde.cer69.recouv/DCL001/19991231/3/sommaire.xml");
	    		  urlEcde.setEcdeUrlSommaireType(uri);
	    		  type.setUrlSommaire(urlEcde);
	    		  
	    		  request.setModificationMasse(type);
	    	      
	    		  skeleton.modificationMasseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("deblocage".equals(opName)){
	    		  Deblocage request = new Deblocage();
	    		  DeblocageRequestType type = new DeblocageRequestType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    		  type.setUuid(uuidType);
	    		  request.setDeblocage(type);
	    	      
	    		  skeleton.deblocageSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }else if ("reprise".equals(opName)){
	    		  Reprise request = new Reprise();
	    		  RepriseRequestType type = new RepriseRequestType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    		  type.setUuid(uuidType);
	    		  request.setReprise(type);
	    	      
	    		  skeleton.repriseSecure(request, "127.0.0.1");
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else if ("copie".equals(opName)){
	    		  Copie request = new Copie();
	    		  CopieRequestType type = new CopieRequestType();
	    		  UuidType uuidType = new UuidType();
	    	      uuidType.setUuidType(UUID.randomUUID().toString());
	    		  type.setIdGed(uuidType);
	    		  request.setCopie(type);
	    	      type.setMetadonnees(getLocalMetadonnees());
	    		  skeleton.copieSecure(request);
	    		  
	    		  Assert.fail("Une Exception doit être levé pour l'appel du webserve "+ opName);
	    	  }
	    	  else {
	    		  Assert.fail("L'operation " + opName + " n'est pas encore prise compte");
	    	  }
	
	      } catch (AxisFault axisFault) {
	    	  
	    	  if("archivageUnitaire".equals(opName)
	    			  || "archivageUnitairePJ".equals(opName)
	    			  || "stockageUnitaire".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, false);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_ARCHIVAGE_UNITAIRE_KO);
	    	  }
	    	  else if("archivageMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, false);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_ARCHIVAGE_MASSE_KO);
	    	  }
	    	  else if("archivageMasseAvecHash".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, false);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_ARCHIVAGE_MASSE_KO);
	    	  }
	    	  else if("suppressionMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, false);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_SUPPRESSION_MASSE_KO);
	    	  }
	    	  else if("restoreMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_RESTORE_MASSE_KO);
	    	  }
	    	  else if("consultation".equals(opName)
	    			  || "consultationMTOM".equals(opName)
	    			  || "consultationAffichable".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_CONSULTATION_KO);
	    	  }
	    	  else if("recherche".equals(opName) 
	    			   || "rechercheNbRes".equals(opName)
	    			   || "rechercheParIterateur".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_RECHERCHE_KO);
	    	  }
	    	  else if("transfert".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_TRANSFERT_KO);
	    	  }
	    	  else if("suppression".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_SUPPRESSION_KO);
	    	  }
	    	  else if("modification".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_MODIFICATION_KO);
	    	  }
	    	  else if("recuperationMetadonnees".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_RECUPERATION_METAS_KO);
	    	  }
	    	  else if("ajoutNote".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_AJOUT_NOTE_KO);
	    	  }
	    	  else if("getDocFormatOrigine".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_GET_DOC_FORMAT_ORIGINE);
	    	  }
	    	  else if("etatTraitementsMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_ETAT_TRAIT_MASSE);
	    	  }
	    	  else if("transfertMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_TRANSFERT_MASSE_KO);
	    	  }
	    	  else if("modificationMasse".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_MODIFICATION_MASSE_KO);
	    	  }
	    	  else if("deblocage".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_DEBLOCAGE_KO);
	    	  }
	    	  else if("reprise".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_REPRISE_KO);
	    	  }
	    	  else if("copie".equals(opName)){	    		  
	    		  //On verifie que une entrée a été ecrit dans le registre de surveillance technique
	    		  List<TraceRegTechniqueIndex> listeTReg = techSupport.findByDates(DateUtils.addHours(DATE, -1), DateUtils
	    		            .addHours(DATE, 1), MAX_COUNT, true);
	    		  Assert.assertTrue("La liste ne doit pas être vide", !listeTReg.isEmpty());
	    		  checkOperationCodeName(opName, listeTReg, TracesConstantes.CODE_EVT_WS_COPIE_KO);
	    	  }	  
	      }
		}
		
		Assert.assertTrue("La liste des codes ne doit être vide", codes.isEmpty());
	}

	
   private void mockRecuperationMetadonnees() throws AxisFault {
	   
	      List<MetadataReference> retour = new ArrayList<>();//construireListeMeta(10);
	      EasyMock.expect(metadataService.getClientAvailableMetadata()).andReturn(retour).times(2);
	      
	      EasyMock.expect(wsMetadataService.recupererMetadonnees()).andThrow(new ErreurInterneAxisFault(new Exception())).anyTimes();	     	  
	      
	      EasyMock.replay(wsMetadataService);
	      EasyMock.replay(metadataService);
	      
	   	  RecuperationMetadonnees request = new RecuperationMetadonnees();
	   	  skeleton.recuperationMetadonneesSecure(request); 	  
	   	 
	   }
 
   /**
    * verifie que le code de l'evenement d'erreur concernant l'operation
    * courante est bien dans la liste des données recupérer dans la base
    */
   private void checkOperationCodeName(String OpName, List<TraceRegTechniqueIndex> listeTReg, String codeName){
		String code= "";
		for(TraceRegTechniqueIndex reg : listeTReg){
			if(reg.getCodeEvt().equals(codeName)){
				code = reg.getCodeEvt();
			}
		}
		Assert.assertTrue("Le code " + codeName + " de l'événement d'échec du WS " + OpName + " doit être dans la liste", !code.isEmpty());
		// on retire le code de la liste
		codes.remove(code);
	}
   
   /**
    * Liste des métadonnées
    * @return
    */
   private ListeMetadonneeType getLocalMetadonnees(){
	  ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();
	  MetadonneeType metadonneeType = new MetadonneeType();
      MetadonneeCodeType codeType = new MetadonneeCodeType();
      codeType.setMetadonneeCodeType("apr");
      MetadonneeValeurType valeurType = new MetadonneeValeurType();
      valeurType.setMetadonneeValeurType(null);
      metadonneeType.setCode(codeType);
      metadonneeType.setValeur(valeurType);
      listeMetadonneeType
            .setMetadonnee(new MetadonneeType[] { metadonneeType });
      return listeMetadonneeType;
   }
   
   /**
    *  Initialise un context pour l'appel des web services
    */
	private void initContextMessage(String operationName){

		QName qname = new QName(operationName);
		AxisOperation aOper = new InOutAxisOperation(qname);
		
		ctx.setAxisOperation(aOper);
		StringWriter strW = new StringWriter();
		strW.write("text unitaire");
		ctx.setProperty("soapRequestMessage", strW);
	    MessageContext.setCurrentMessageContext(ctx);			      
	}
		
	private XMLStreamReader createConsultationResponseType(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return reader;

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

	}
	
	/**
	 * Recupère la liste de tous les service web declarer dans le wsdl et qui 
	 * doivent ecrire une trace dans le RegTechnique en cas d'execption lors de
	 * son appel.<br>
	 * URL WSDL: src/main/webapp/WEB-INF/services/SaeService/META-INF/SaeService.wsdl
	 */
	private List<String> getListWebServiceName(){
	  
		List<Operation> listOp = getPortTypeOperations("src/main/webapp/WEB-INF/services/SaeService/META-INF/SaeService.wsdl");
		// On enleve le service de Ping et PinSecure car ne peut lévé d'Exception
		// le service documentExistant n'a aucun code associé
		// consultationGNTGNS faut-il le mettre dans la categorie des consultation
	    List<String> opList = new ArrayList<String>();
	    for(Operation op : listOp){
	    	if (!"Ping".equals(op.getName()) && !"PingSecure".equals(op.getName()) 
	    			&& !"documentExistant".equals(op.getName()) && !"consultationGNTGNS".equals(op.getName())){
	    		opList.add(op.getName());
	    	}
	    }
	    return opList;
	}
	/**
	 * Recupère la liste de toutes les oprération exposée par le web service
	 * URL WSDL: src/main/webapp/WEB-INF/services/SaeService/META-INF/SaeService.wsdl
	 */ 
   private static List<Operation> getPortTypeOperations(String wsdlUrl) {
	    List<Operation> operationList = new ArrayList();
	    try {
	        WSDLReader reader = new WSDLReaderImpl();
	        reader.setFeature("javax.wsdl.verbose", false);
	        Definition definition = reader.readWSDL(wsdlUrl.toString());
	        Map<String, PortTypeImpl> defMap = definition.getAllPortTypes();
	        Collection<PortTypeImpl> collection = defMap.values();
	        for (PortTypeImpl portType : collection) {
	            operationList.addAll(portType.getOperations());
	        }
	    } catch (WSDLException e) {
	        System.out.println("get wsdl operation fail.");
	        e.printStackTrace();
	    }
	   
	    return operationList;
	}
}
