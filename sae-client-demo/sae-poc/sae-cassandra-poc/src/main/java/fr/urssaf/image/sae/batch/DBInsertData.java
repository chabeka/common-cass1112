/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.dao.impl.DictionaryDAOImpl;
import fr.urssaf.image.sae.model.Dictionary;
import fr.urssaf.image.sae.model.Metadata;
import fr.urssaf.image.sae.model.POC;

/**
 * TODO (AC75095028) Description du type
 */
public class DBInsertData {

  public static List<Metadata> insertMetadata() {
    final List<Metadata> listData = new ArrayList<>();
    final Metadata ref = new Metadata();
    ref.setShortCode("ATransfererScribe2");
    ref.setClientAvailable(false);
    ref.setDictionaryName("dico0");
    ref.setClientAvailable(true);
    ref.setLongCode("ATransfererScribes");
    ref.setIsIndexed(true);
    ref.setArchivable(false);
    ref.setSearchable(true);
    ref.setConsultable(true);
    listData.add(ref);

    final Metadata ref1 = new Metadata();
    ref1.setShortCode("ATransfererScribe3");
    ref1.setClientAvailable(false);
    ref1.setDictionaryName("dico1");
    ref1.setClientAvailable(true);
    ref1.setLongCode("ATransfererScribes");
    ref1.setIsIndexed(true);
    ref1.setArchivable(false);
    ref1.setSearchable(true);
    ref1.setConsultable(true);
    listData.add(ref1);

    final Metadata ref2 = new Metadata();
    ref2.setShortCode("ATransfererScribe3");
    ref2.setClientAvailable(false);
    ref2.setDictionaryName("dico1");
    ref2.setClientAvailable(true);
    ref2.setLongCode("ATransfererScribes");
    ref2.setIsIndexed(true);
    ref2.setArchivable(false);
    ref2.setSearchable(false);
    ref2.setConsultable(false);
    listData.add(ref2);
    return listData;
  }

  public static List<POC> createPOCS() {
    final List<POC> pocs = new ArrayList<>();
    POC poc = new POC();
    UUID uuid = java.util.UUID.fromString(UUID.randomUUID().toString());
    poc.setId(uuid);
    poc.setValue("poc 1");
    pocs.add(poc);

    poc = new POC();
    uuid = java.util.UUID.fromString(UUID.randomUUID().toString());
    poc.setId(uuid);
    poc.setValue("poc 2");
    pocs.add(poc);
    return pocs;
  }

  public static List<Dictionary> createDicos() {
    final List<Dictionary> dicos = new ArrayList<>();
    Dictionary dico = new Dictionary();
    UUID uuid = java.util.UUID.fromString(UUID.randomUUID().toString());
    dico.setId(uuid);
    dico.setValue("poc 1");
    dicos.add(dico);

    dico = new Dictionary();
    uuid = java.util.UUID.fromString(UUID.randomUUID().toString());
    dico.setId(uuid);
    dico.setValue("poc 2");
    dicos.add(dico);
    return dicos;
  }

  public void testCRUD(final CassandraCQLClientFactory ccf, final DictionaryDAOImpl dicodao) {

    // Save data in dictionary table
    final Dictionary dicoo = new Dictionary();
    final UUID uuid = java.util.UUID.fromString(UUID.randomUUID().toString());
    dicoo.setId(uuid);
    dicoo.setValue("dicodao");
    dicodao.save(dicoo);

    // Delete data in dictionary table
    // dicodao.delete(dicoo);

    // Save all dictionary
    final List<Dictionary> listDict = new ArrayList<>();
    listDict.add(dicoo);
    // dicodao.saveAll(listDict);

    // Find data in Dictionary table with ids
    final List<UUID> listID = Arrays.asList(java.util.UUID.fromString("fc5d7fda-984e-4d33-9eb0-07666becdf6c"),
                                            java.util.UUID.fromString("aec61465-4e92-4e96-bbcf-5eceb62e6c56"),
                                            java.util.UUID.fromString("248a759d-f1b1-4c20-8fab-e263fd8ffaac"),
                                            java.util.UUID.fromString("9db5a745-edef-4da6-bbe3-e46c4035ed7f"),
                                            java.util.UUID.fromString("fd7ded38-1cfc-44cf-a474-ddb5d7a7e0c3"));

    final List<Dictionary> listDicts = dicodao.findAllById(listID);

    final Optional<Dictionary> dico1 = dicodao.findById(java.util.UUID.fromString("fc5d7fda-984e-4d33-9eb0-07666becdf6c"));

    //final List<Dictionary> listDicos = dicodao.findAll();

    // Delete all data in Dictionary table
    // template.truncate(Dictionary.class);
    // dicodao.deleteAll();

    // Insert data with options
    /*final InsertOptions options = InsertOptions.builder()
                                               .consistencyLevel(ConsistencyLevel.ONE)
                                               .ttl(Duration.ofMinutes(1L))
                                               .build();
    */

    // insert data with TTL
    final Dictionary dico = new Dictionary();
    dico.setId(java.util.UUID.fromString(UUID.randomUUID().toString()));
    dico.setValue("ttl");
    // template.insert(dico, options);
  }
}
