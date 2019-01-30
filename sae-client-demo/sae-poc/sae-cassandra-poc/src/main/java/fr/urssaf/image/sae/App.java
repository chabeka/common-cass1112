package fr.urssaf.image.sae;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Result;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.batch.DBInsertData;
import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;
import fr.urssaf.image.sae.model.testpoc3;
import fr.urssaf.image.sae.service.IMetadataReferenceService;
import fr.urssaf.image.sae.service.IPOCService;
import fr.urssaf.image.sae.service.ITestpoc3Service;
import fr.urssaf.image.sae.service.impl.MetadataReferenceServiceImpl;
import fr.urssaf.image.sae.service.impl.POCServiceImpl;

/**
 * Hello world!
 */

public class App {

  public static void main(final String[] args) {

    System.out.println("start");

    final String[] springConfig = {"applicationContext-cassandra-poc.xml"};
    final ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    final CassandraCQLClientFactory ccf = (CassandraCQLClientFactory) context.getBean("cassandraCQLClientFactory");
    final String[] str = context.getBeanDefinitionNames();
    final Session session = ccf.getSession();

    /* ------------------------- TABLE POC ----------------------- */

    // Creation de la table "POC"
    // DBSchema.createTablePOC(session);

    final IPOCService pocservice = (POCServiceImpl) context.getBean("POCServiceImpl");

    // Ajout d'une liste d'objet "POC"
    // final List<POC> pocsT = DBInsertData.createPOCS();
    // final List<POC> pocsSaved = pocservice.saveAll(pocsT);
    // assert pocsSaved.size() == 2 : "pocdao - saveAll - Probleme d'enregistrement dans la table POC";

    // Lister le contenu de la table "POC"
    // final List<POC> pocs = pocservice.findAll();
    // assert pocs.size() > 0 : "pocdao - saveAll - Probleme d'enregistrement dans la table POC";

    /*
     * final Optional<POC> pocOpt = pocservice.findById(java.util.UUID.fromString("a79e3507-1ede-4982-b701-48cde9862112"));
     * final POC dic = pocOpt.isPresent() ? pocOpt.get() : new POC();
     * assert pocOpt.isPresent() : " pocdao - findById - Problème de lecture dans la table POC";
     */

    /* ---------------- TABLE METADATAREFERENCE ------------------- */

    // Creation de la table Metadata
    // DBSchema.createTableMetadata(session);

    final IMetadataReferenceService metaservice = (MetadataReferenceServiceImpl) context.getBean("metadataReferenceServiceImpl");

    // Ajout d'une liste d'objet MetadataReference
    //final List<Metadata> metas = DBInsertData.insertMetadata();
    //metaservice.saveAll(metas);

    // Lister le contenu de la table MetadataReference
    //final Result<Metadata> listMeta = metaservice.findAll();
    //for (final Metadata meta : listMeta) {
    	//System.out.println(meta.toString());
   // }
    // Lister les metadatas consultable
    // final List<Metadata> listMetaC = metaservice.findMetadatasConsultables();

    // Lister les metadatas recherchable
    // final List<MetadataReference> listMetaS = metaservice.findMetadatasRecherchables();

    // delete de la metadata
    // metaservice.deleteById(id);
    // Mapping manuel

    /*final List<Metadata> listMetadatas1 = metaservice.findAllMetadata();

    final List<Metadata> listMetadatas = metaservice.findAllByGType();
    final GenericType gtype = new GenericType();
    gtype.setColumn1("test3");
    gtype.setKey(ByteBuffer.wrap("test3key".getBytes(Charset.forName("UTF-8"))));
    gtype.setValue(ByteBuffer.wrap("test3value".getBytes(Charset.forName("UTF-8"))));
    metaservice.insertGType(gtype, null);*/

    /* ---------------- TABLE DICTIONARY ------------------- */

    // Creation de la table Metadata
    // DBSchema.createTableDictionary(session);

    // final IDictionaryService dicoservice = (IDictionaryService) context.getBean("dictionaryServiceImpl");

    // Ajout d'une liste d'objet MetadataReference
    // final List<Dictionary> dicos = DBInsertData.createDicos();
    // dicoservice.saveAll(dicos);

    // Lister le contenu de la table MetadataReference
    // final List<Dictionary> listDico = dicoservice.findAll();

    /* ---------------- TABLE Test poc 3 ------------------- */

     final ITestpoc3Service poc3service = (ITestpoc3Service) context.getBean("testpoc3ServiceImpl");
     final List<testpoc3> testpoc3 = poc3service.findAll();

    /* -------------------- */

    System.out.println("end");
  }

}
