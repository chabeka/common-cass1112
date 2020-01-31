package fr.urssaf.astyanaxtest;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Equality;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.CompositeRangeBuilder;
import com.netflix.astyanax.serializers.ObjectSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.astyanaxtest.dao.BasesReferenceCF;
import fr.urssaf.astyanaxtest.dao.BasesReferenceDao;
import fr.urssaf.astyanaxtest.dao.BasesReferenceEntity;
import fr.urssaf.astyanaxtest.dao.IndexReference;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeCF;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeColumn;
import fr.urssaf.astyanaxtest.dao.TermInfoRangeKey;
import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class IterateDocuments {

   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;
   
   /**
    * Facilite le mapping cassandra<->entité
    */
   MappingUtil mapper;

   /**
    * La où on veut dumper
    */
   PrintStream sysout;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";     //GIVN
      servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160"; // Production
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers = "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      //servers = "cer69-saeint3:9160";
      //servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160";     //Préprod
      //servers = "hwi69gincleasaecas1.cer69.recouv:9160,hwi69gincleasaecas2.cer69.recouv:9160";
      //servers = "cnp6saecvecas1.cve.recouv:9160,cnp3saecvecas1.cve.recouv:9160,cnp7saecvecas1.cve.recouv:9160";	// Charge

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root", "regina4932");

      AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("Docubase").forKeyspace("Docubase")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl().setDiscoveryType(
                        NodeDiscoveryType.NONE).setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM)
                        .setDefaultWriteConsistencyLevel(
                              ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9160).setMaxConnsPerHost(1).setSeeds(servers)
                        .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getEntity();
      mapper = new MappingUtil(keyspace, new MappingCache());
      
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      
   }

   
   @SuppressWarnings("unchecked")
   @Test
   /**
    * Exemple d'itération sur des documents en utilisant l'index "numéro de compte externe" (nce)
    */
   public void testIterateOverIndex() throws Exception {
      //UUID baseUUID = UUID.fromString("5871886a-1041-422e-baa7-ebaee6667fcd");         //GIVN
      //UUID baseUUID = UUID.fromString("f573ae93-ac6a-4615-a23b-150fd621b5a0");   // Production
      
      //UUID baseUUID = getBaseUUID("SAE-GIVN");
      UUID baseUUID = getBaseUUID("SAE-PROD");
      String numCompteStart = "827";
      String numCompteEnd =   "ZZZ";
      int blocSize = 100;              // Nombre de document qu'on ramène à la fois de cassandra
      int hardLimitForTest = 200;      // On arrête la boucle une fois ce nombre de documents parcourus
      int rangeId = 34;
      
      TermInfoRangeKey key = new TermInfoRangeKey("nce", baseUUID, rangeId);
      byte[] keyAsBytes = TermInfoRangeCF.keySerializer.toBytes(key);
      System.out.println(ConvertHelper.getReadableUTF8String(keyAsBytes));
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.stringCf)
            .getKey(new TermInfoRangeKey("nce", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(numCompteStart, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(numCompteEnd, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      int compteurComptes = 0;
      int compteurDoc = 0;
      int maxDoc = 0;
      int minDoc = 0;
      String currentCompte = "";
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            
            {
               byte[] bytes = c.getByteArrayValue();
               System.out.println("serialize : " + ConvertHelper.getHexString(bytes));
               HashMap<String,ArrayList<String>> metadatas = (HashMap<String,ArrayList<String>>) c.getValue(ObjectSerializer.get());
               String virtual = getMetadataValue(metadatas, "SM_VIRTUAL");
               String uuid = getMetadataValue(metadatas, "SM_UUID");
               System.out.println("virtual : " + virtual);
               System.out.println("uuid : " + uuid);
               System.out.println("DeseriazedValue : " + metadatas.toString());
            }
            
            String numCompte = colName.getCategoryValueAsString();
            UUID docUUID = colName.getDocumentUUID();
            if (compteurLigne % 10000 == 0) System.out.println(compteurLigne + " - " + numCompte + " - " + docUUID);
            if (!numCompte.equals(currentCompte)) {
               currentCompte = numCompte;
               compteurComptes++;
               if (compteurDoc > maxDoc) maxDoc = compteurDoc;
               if (compteurDoc == 1) minDoc ++;
               
               compteurDoc = 0;
            }
            compteurLigne ++;
            compteurDoc++;
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes affichées");
      System.out.println(compteurComptes + " comptes différents trouvés");
      System.out.println("Nombre moyen de doc par compte : " + (float)compteurLigne / compteurComptes);
      System.out.println("Nombre max de doc : " + maxDoc);
      System.out.println("Nombre min de doc : " + minDoc);
   }

   @Test
   /**
    * Exemple d'itération sur des documents en utilisant l'index "Denomination" (den)
    */
   public void testIterateOverIndexDenomination() throws Exception {
      UUID baseUUID = getBaseUUID("SAE-PROD");
      String start = "e";
      String end =   "z";
      int blocSize = 100;              // Nombre de document qu'on ramène à la fois de cassandra
      int hardLimitForTest = 500;      // On arrête la boucle une fois ce nombre de documents parcourus
      int rangeId = 0;
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.stringCf)
            .getKey(new TermInfoRangeKey("den", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(start, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(end, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      int compteurDoc = 0;
      int maxDoc = 0;
      int minDoc = 0;
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            
            {
               UUID docUUID = colName.getDocumentUUID();
               String denomination = colName.getCategoryValueAsString();
               
               HashMap<String,ArrayList<String>> metadatas = (HashMap<String,ArrayList<String>>) c.getValue(ObjectSerializer.get());
               String uuid = getMetadataValue(metadatas, "SM_UUID");
               System.out.println("uuid : " + uuid);
               System.out.println("denomination : " + denomination);
               System.out.println("DeseriazedValue : " + metadatas.toString());
            }
            
            compteurLigne ++;
            compteurDoc++;
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes affichées");
      System.out.println("Nombre max de doc : " + maxDoc);
      System.out.println("Nombre min de doc : " + minDoc);
   }
   
   @Test
   /**
    * Exemple d'itération sur des documents en utilisant l'index "NumeroCompte" (nce)
    * On tests si on trouve des documents en doublon
    */
   public void testCheckDoublonsIndexCompte() throws Exception {
      UUID baseUUID = getBaseUUID("SAE-PROD");
      String start = "000";
      String end =   "999";
      int blocSize = 500;              	// Nombre de document qu'on ramène à la fois de cassandra
      int hardLimitForTest = 500000;      // On arrête la boucle une fois ce nombre de documents parcourus
      int rangeId = 0;
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.stringCf)
            .getKey(new TermInfoRangeKey("nce", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(start, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(end, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      boolean shouldStop = false;
      String currentCompte = "";
      ArrayList<HashMap<String,ArrayList<String>>> metaList = new ArrayList<HashMap<String,ArrayList<String>>>();
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            
            {
               String compte = colName.getCategoryValueAsString();
               if (!compte.equals(currentCompte)) {
            	   if (!currentCompte.isEmpty()) {
            		   checkForDoublons(compte, metaList);
            		   metaList.clear();
            	   }
            	   currentCompte = compte;
               }
            }
            @SuppressWarnings("unchecked")
			HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            metaList.add(metadatas);
            compteurLigne ++;
            if (compteurLigne % 500 == 0) System.out.println(compteurLigne + "...");
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes parcourues");
   }
   
   
	private void checkForDoublons(String compte, ArrayList<HashMap<String, ArrayList<String>>> metaList) {
		// On commence par voir s'il y a des hashs en doublon
		ArrayList<String> hashs = new ArrayList<String>();
		for (HashMap<String, ArrayList<String>> metadatas : metaList) {
			String hash = getMetadataValue(metadatas, "SM_DIGEST");
			hashs.add(hash);
		}
		Set<String> duplicates = findDuplicates(hashs);
		/*
		for (String hash : duplicates) {
			sysout.println("Compte " + compte + " : hash en doublon : "	+ hash);
			for (HashMap<String, ArrayList<String>> metadatas : metaList) {
				if (getMetadataValue(metadatas, "SM_DIGEST").equals(hash)) {
					sysout.println("Date archivage : " + getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE"));
					sysout.println(metadatas.toString());
				}
			}
			sysout.println();
		}
		*/
		// On regarde s'il y a des métadonnées qui se ressemblent
		ArrayList<String> titles = new ArrayList<String>();
		for (HashMap<String, ArrayList<String>> metadatas : metaList) {
			String title = getMetadataValue(metadatas, "SM_TITLE");
			String creationDate = getMetadataValue(metadatas, "SM_CREATION_DATE").substring(0, 6);
			titles.add(title + " - "  + creationDate);
		}
		duplicates = findDuplicates(titles);
		for (String title : duplicates) {
			sysout.println("Compte " + compte + " : title en doublon : "	+ title);
			for (HashMap<String, ArrayList<String>> metadatas : metaList) {
				String creationDate = getMetadataValue(metadatas, "SM_CREATION_DATE").substring(0, 6);
				if ((getMetadataValue(metadatas, "SM_TITLE") + " - " + creationDate ).equals(title)) {
					sysout.println("Date archivage : " + getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE"));
					sysout.println(metadatas.toString());
				}
			}
			sysout.println();
		}		
	}

	public Set<String> findDuplicates(List<String> listContainingDuplicates) {
		final Set<String> setToReturn = new HashSet<String>();
		final Set<String> set1 = new HashSet<String>();

		for (String yourString : listContainingDuplicates) {
			if (!set1.add(yourString)) {
				setToReturn.add(yourString);
			}
		}
		return setToReturn;
	}  

@Test
   /**
    * Exemple d'itération sur des documents en utilisant l'index "UUID" (SM_UUID).
    * Ça nous permet d'itérer sur l'ensemble des documents du SAE
    */
   public void testIterateOverUUID() throws Exception {
      
      //UUID baseUUID = getBaseUUID("SAE-GIVN");
      //UUID baseUUID = getBaseUUID("SAE-PROD");
      UUID baseUUID = getBaseUUID("SAE-INT");
      
      String uuidStart = "00000000-0000-0000-0000-000000000000";
      String uuidEnd =   "ffffffff-ffff-ffff-ffff-ffffffffffff";
      int blocSize = 100;
      int hardLimitForTest = 20000000;
      int rangeId = 0;
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.uuidCf)
            .getKey(new TermInfoRangeKey("SM_UUID", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(uuidStart, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(uuidEnd, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            UUID docUUID = colName.getDocumentUUID();
            //System.out.println("UUID - " + docUUID);
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            //System.out.println("Siret - " + getMetadataValue(metadatas, "srt"));
            //System.out.println("Size - " + getMetadataValue(metadatas, "SM_SIZE"));
            String domaineCotisant = getMetadataValue(metadatas, "cot");
            if (domaineCotisant != null) {
            	System.out.println(metadatas);            	
            }
            compteurLigne ++;
            if (compteurLigne % 1000 == 0) System.out.println(compteurLigne + " - " + docUUID);
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes affichées");
   }

   @Test
   /**
    * Exemple d'itération sur des documents en utilisant l'index "UUID" (SM_UUID).
    * Ça nous permet de regarder la cohérence des codes organismes
    */
   public void testCheckCodesOrga() throws Exception {
      
      //UUID baseUUID = getBaseUUID("SAE-GIVN");
      //UUID baseUUID = getBaseUUID("SAE-PROD");
      UUID baseUUID = getBaseUUID("SAE-INT");
      String uuidStart = "00000000-0000-0000-0000-000000000000";
      String uuidEnd =   "ffffffff-ffff-ffff-ffff-ffffffffffff";
      int blocSize = 300;
      int hardLimitForTest = 500000000;
      int rangeId = 0;
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.uuidCf)
            .getKey(new TermInfoRangeKey("SM_UUID", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(uuidStart, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(uuidEnd, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      int compteurDiff = 0;
      int compteurNull = 0;
      Writer fileDiff = new OutputStreamWriter(new FileOutputStream("c:/temp/sae_diff_codeOrga.txt"));
      Writer fileNull = new OutputStreamWriter(new FileOutputStream("c:/temp/sae_null.txt"));
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            UUID docUUID = colName.getDocumentUUID();
            //System.out.println("UUID - " + docUUID);
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            String codeOrgaProprietaire = getMetadataValue(metadatas, "cop");
            String codeOrgaGestionnaire = getMetadataValue(metadatas, "cog");
            String nce = getMetadataValue(metadatas, "nce");
            String srt = getMetadataValue(metadatas, "srt");
            String debutNumCompte = "";
            if (nce != null) {
               debutNumCompte = "UR" + nce.substring(0, 3);
            }
            if (!codeOrgaProprietaire.equals(codeOrgaGestionnaire) || 
                  (nce != null && !codeOrgaProprietaire.equals(debutNumCompte))) {
               compteurDiff++;
               fileDiff.write(String.format("codeOrgaProprietaire = %s - codeOrgaGestionnaire = %s - debutNumCompte = %s\n",
                     codeOrgaProprietaire, codeOrgaGestionnaire, nce));
               fileDiff.write("Value - " + metadatas.toString() + "\n");
            }
            /*
            if (nce == null && srt == null) {
               compteurNull++;
               fileNull.write("Value - " + metadatas.toString() + "\n");
            }
            */
            compteurLigne ++;
            if(compteurLigne % 1000 == 0) {
               System.out.println(compteurLigne + " lignes parcourues");
               System.out.println(compteurDiff + " différences");
               //System.out.println(compteurNull + " nulls");
            }
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes parcourues");
      System.out.println(compteurDiff + " différences");
      //System.out.println(compteurNull + " nulls");
      fileDiff.close();
      fileNull.close();
   }
   
   @Test
   /**
    * Exemple d'itération sur des documents par date. On utilise SM_LIFE_CYCLE_REFERENCE_DATE
    * car elle est posée sur chaque document.
    */
   public void testIterateByDate() throws Exception {
      
      //String meta = "SM_LIFE_CYCLE_REFERENCE_DATE";
      //String meta = "SM_FINAL_DATE";
	  //String meta = "SM_MODIFICATION_DATE";
      String meta = "SM_CREATION_DATE";
      //UUID baseUUID = getBaseUUID("SAE-GIVN");
      UUID baseUUID = getBaseUUID("SAE-PROD");
      System.out.println("baseUUID="+ baseUUID.toString());
      //UUID baseUUID = getBaseUUID("SAE-INT");
      String dateStart = "";
      //String dateStart = "20100101000000000";
      String dateEnd =   "20119999999999999";
      //String dateStart = "20121031000000000";
      //String dateEnd =   "20121031500000000";
      int blocSize = 100;
      int hardLimitForTest = 10000;
      //int rangeId = 1;
      //IndexReference ref = new IndexReference();
      //ref.readIndexReference(keyspace, baseUUID, meta, "NOMINAL");
      //int rangeId = ref.metaToRangeId(dateStart);
      int rangeId = 128;
      System.out.println("RangeId="+ rangeId);
      
      /*
      CompositeRangeBuilder columnRange = TermInfoRangeCF.columnSerializer.buildRange().withPrefix("")
            //.greaterThanEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .greaterThanEquals(UUID.fromString("f60b96a7-49bc-46d2-88b0-7575b222fa1a"))
            .lessThanEquals(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"))
            .limit(blocSize);
      */
      CompositeRangeBuilder columnRange = TermInfoRangeCF.columnSerializer.buildRange()
              .greaterThanEquals(ConvertHelper.stringToBytes("20170105000000000"))
              .lessThanEquals(ConvertHelper.stringToBytes("2048"))
              //.reverse()
              .limit(blocSize);
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.dateTimeCf)
            .getKey(new TermInfoRangeKey(meta, baseUUID, rangeId))
            .withColumnRange(columnRange)
            //.withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(dateStart, Equality.EQUAL).toBytes(),
            //      TermInfoRangeCF.columnSerializer.makeEndpoint(dateEnd, Equality.LESS_THAN_EQUALS).toBytes(),
            //      false, blocSize);
            .autoPaginate(true);
            
      
      byte[] keyAsBytes = TermInfoRangeCF.keySerializer.toBytes(new TermInfoRangeKey(meta, baseUUID, rangeId));
      String keyAsString = ConvertHelper.getHexString(keyAsBytes);
      System.out.println("Key :" + keyAsString);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      System.out.println("Début");
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            byte[] colNameAsBytes = TermInfoRangeCF.columnSerializer.toBytes(c.getName());
            String colNameAsHex = ConvertHelper.getHexString(colNameAsBytes);
            sysout.println("colNameAsHex : " + colNameAsHex);
            
            TermInfoRangeColumn colName = c.getName();
            UUID docUUID = colName.getDocumentUUID();
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            //System.out.println("Value - " + metadatas.toString());
            String codeOrgaProprietaire = getMetadataValue(metadatas, "cop");
            String codeOrgaGestionnaire = getMetadataValue(metadatas, "cog");
            String finalDate = getMetadataValue(metadatas, "SM_FINAL_DATE");
            String creationDate = getMetadataValue(metadatas, "SM_CREATION_DATE");
            String modificationDate = getMetadataValue(metadatas, "SM_MODIFICATION_DATE");
            String archivageDate = getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE");
            sysout.println("UUID - " + docUUID + " " + codeOrgaProprietaire + " " + codeOrgaGestionnaire + " " + modificationDate + " " + archivageDate + " "+  creationDate);
            
            //System.out.println("Siret - " + getMetadataValue(metadatas, "srt"));
            //System.out.println("Size - " + getMetadataValue(metadatas, "SM_SIZE"));
            //String title = getMetadataValue(metadatas, "SM_TITLE");
            //if (title == null || title.equals("")) throw new Exception(docUUID + " n'a pas de titre");
            //System.out.println("Title - " + getMetadataValue(metadatas, "SM_TITLE"));
            //System.out.println("Reference Date - " + getMetadataValue(metadatas, "SM_LIFE_CYCLE_REFERENCE_DATE"));
            //System.out.println("Archivage Date - " + getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE"));
            
            compteurLigne ++;
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes affichées");
   }

   @Test
   /**
    * Exemple d'itération
    * Permet de récupérer les 1er et derniers documents sur les différents ranges d'un index
    */
   public void testIterate_checkRanges() throws Exception {
      
      String meta = "SM_MODIFICATION_DATE";
      //String meta = "SM_FINAL_DATE";
      //String meta = "SM_CREATION_DATE";
      //String meta = "cot&cop&SM_DOCUMENT_TYPE&SM_ARCHIVAGE_DATE&";
      ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> cf = TermInfoRangeCF.dateTimeCf;
      //ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> cf = TermInfoRangeCF.stringCf;
      UUID baseUUID = getBaseUUID("SAE-PROD");
      int blocSize = 3;
      int hardLimitForTest = 3;
      int startRange = 0;
      int endRange = 105;
      
      for (int rangeId = startRange; rangeId <= endRange; rangeId++) {
          String firstValue = "";
          String lastValue = "";
          for (boolean reverse : new boolean[] {false, true}) {
             sysout.println();
             sysout.println("RangeId="+ rangeId + " - reverse=" + reverse);
             CompositeRangeBuilder columnRange = TermInfoRangeCF.columnSerializer.buildRange()
                     .greaterThanEquals("")
                     //.lessThanEquals("2058")
                     .limit(blocSize);
             if (reverse) columnRange.reverse();
             RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
                   .prepareQuery(cf)
                   .getKey(new TermInfoRangeKey(meta, baseUUID, rangeId))
                   .withColumnRange(columnRange)
                   .autoPaginate(true);
                   
             
             byte[] keyAsBytes = TermInfoRangeCF.keySerializer.toBytes(new TermInfoRangeKey(meta, baseUUID, rangeId));
             String keyAsString = ConvertHelper.getHexString(keyAsBytes);
             System.out.println("Key :" + keyAsString);
             
             ColumnList<TermInfoRangeColumn> columns;
             int compteurLigne = 0;
             boolean shouldStop = false;
             while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
                for (Column<TermInfoRangeColumn> c : columns) {
                   byte[] colNameAsBytes = TermInfoRangeCF.columnSerializer.toBytes(c.getName());
                   String colNameAsHex = ConvertHelper.getHexString(colNameAsBytes);
                   //sysout.println("colNameAsHex : " + colNameAsHex);
                   
                   TermInfoRangeColumn colName = c.getName();
                   UUID docUUID = colName.getDocumentUUID();
                   @SuppressWarnings("unchecked")
                   HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
                   //System.out.println("Value - " + metadatas.toString());
                   String codeOrgaProprietaire = getMetadataValue(metadatas, "cop");
                   String codeOrgaGestionnaire = getMetadataValue(metadatas, "cog");
                   String value = getMetadataValue(metadatas, meta);
                   sysout.println("UUID - " + docUUID + " " + codeOrgaProprietaire + " " + codeOrgaGestionnaire + " " + value);
                   if (reverse) {
                      if ("".equals(lastValue)) lastValue = value;
                   }
                   else {
                       if ("".equals(firstValue)) firstValue = value;
                   }
                   compteurLigne ++;
                   if (compteurLigne >= hardLimitForTest) {
                      shouldStop = true;
                      break;
                   }
                }
             } // Fin while
             
             if (reverse) {
                 sysout.println();
                 sysout.println(String.format("{\"ID\":%d,\"LOWER_BOUND\":\"%s\",\"UPPER_BOUND\":\"%s\",\"COUNT\":999999,\"STATE\":\"NOMINAL\"}", rangeId, firstValue, lastValue));
             }
             
         }  // fin reverse
     } // fin for ranges

   }


   @Test
   /**
    * Exemple d'itération sur des documents par date d'archivage.
    * Utiliser pour diagnostiquer les erreurs du script d'archivage GED -> SAE
    */
   public void testIterateByDate_RH() throws Exception {
      
      //UUID baseUUID = getBaseUUID("SAE-GIVN");
      //UUID baseUUID = getBaseUUID("SAE-PROD");
      UUID baseUUID = getBaseUUID("SAE-INT");
      String dateStart = "20150615195900000";
      String dateEnd =   "20150615235900000";
      //String dateStart = "20121031000000000";
      //String dateEnd =   "20121031500000000";
      int blocSize = 500;
      int hardLimitForTest = 100000;
      int rangeId = 0;
      
      RowQuery<TermInfoRangeKey, TermInfoRangeColumn> query = keyspace
            .prepareQuery(TermInfoRangeCF.dateTimeCf)
            .getKey(new TermInfoRangeKey("SM_ARCHIVAGE_DATE", baseUUID, rangeId)).autoPaginate(
                  true)
            .withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint(dateStart, Equality.EQUAL).toBytes(),
                  TermInfoRangeCF.columnSerializer.makeEndpoint(dateEnd, Equality.LESS_THAN_EQUALS).toBytes(),
                  false, blocSize);

      ColumnList<TermInfoRangeColumn> columns;
      int compteurLigne = 0;
      boolean shouldStop = false;
      Stopwatch chrono = new Stopwatch();
      chrono.start();
      System.out.println("Début");
      while (!(columns = query.execute().getResult()).isEmpty() && !shouldStop) {
         for (Column<TermInfoRangeColumn> c : columns) {
            TermInfoRangeColumn colName = c.getName();
            UUID docUUID = colName.getDocumentUUID();
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            //System.out.println("Value - " + metadatas.toString());
            String filename = getMetadataValue(metadatas, "SM_FILENAME");
            String extension = getMetadataValue(metadatas, "SM_EXTENSION");
            String date = getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE");
            sysout.println(date + " " + docUUID.toString().toUpperCase() + " " + filename + "." + extension);
            
            compteurLigne ++;
            if (compteurLigne >= hardLimitForTest) {
               shouldStop = true;
               break;
            }
         }
      }
      chrono.stop();
      System.out.println("Temps de traitement : " + chrono.toString());      
      System.out.println(compteurLigne + " lignes affichées");
   }
   
	@Test
	/**
	 * Test de dump des clés de TermInfoRangeString 
	 */
	public void testDumpTermInfoRangeString() throws Exception {
		OperationResult<Rows<byte[], TermInfoRangeColumn>> rows = keyspace
				.prepareQuery(TermInfoRangeCF.stringCfKeyAsBytes).getAllRows()
				.setRowLimit(10)
				// This is the page size
				//.withColumnRange(new RangeBuilder().setLimit(10).build())
				.withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint("10", Equality.EQUAL).toBytes(),
            		TermInfoRangeCF.columnSerializer.makeEndpoint("11", Equality.LESS_THAN_EQUALS).toBytes(),
            		false, 10)				
				.setExceptionCallback(new ExceptionCallback() {
					public boolean onException(ConnectionException e) {
						Assert.fail(e.getMessage());
						return true;
					}
				}).execute();

		int counter = 0;
		for (Row<byte[], TermInfoRangeColumn> row : rows.getResult()) {
			//byte[] key = TermInfoRangeCF.keySerializer.toBytes(row.getKey());
			byte[] key = row.getKey();
			
			sysout.println(counter + " - Key : " + ConvertHelper.getReadableUTF8String(key));
			ColumnList<TermInfoRangeColumn> columns = row.getColumns();
			for (Column<TermInfoRangeColumn> c : columns) {


	            TermInfoRangeColumn colName = c.getName();
				byte[] columnAsBytes = TermInfoRangeCF.columnSerializer.toBytes(colName);
				sysout.println("Column : " + ConvertHelper.getHexString(columnAsBytes));
	            
	            UUID docUUID = colName.getDocumentUUID();
	            String docVersion = colName.getDocumentVersion();
	            @SuppressWarnings("unchecked")
	            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
	            //System.out.println("Value - " + metadatas.toString());
	            String filename = getMetadataValue(metadatas, "SM_FILENAME");
	            String extension = getMetadataValue(metadatas, "SM_EXTENSION");
	            String date = getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE");
	            String siret = getMetadataValue(metadatas, "srt");
	            sysout.println(date + " " + docUUID.toString().toUpperCase() + " " + docVersion + " " + filename + "." + extension + " " + siret);
			}
			sysout.println();
			counter++;
			if (counter > 1000) break;
		}
		System.out.println("Counter=" + counter);
	}
	
	
	@Test
	/**
	 * Lecture d'une ligne de TermInfoRangeString 
	 */
	public void testDumpTermInfoRangeString_one() throws Exception {
		byte[] key = ConvertHelper.hexStringToByteArray("00000000036e6365000010f573ae93ac6a4615a23b150fd621b5a00000010d00");
		//byte[] key = ConvertHelper.hexStringToByteArray("00000000036e6365000010f573ae93ac6a4615a23b150fd621b5a00000010000");
		System.out.println("Key : " + ConvertHelper.getReadableUTF8String(key));
		OperationResult<ColumnList<TermInfoRangeColumn>> cols = keyspace
				.prepareQuery(TermInfoRangeCF.stringCf).getKey(TermInfoRangeCF.keySerializer.fromBytes(key))
				// This is the page size
				//.withColumnRange(new RangeBuilder().setStart(0).setEnd(1).setLimit(10).build())
				.withColumnRange(TermInfoRangeCF.columnSerializer.makeEndpoint("10", Equality.EQUAL).toBytes(),
            		TermInfoRangeCF.columnSerializer.makeEndpoint("20", Equality.LESS_THAN_EQUALS).toBytes(),
            		false, 10)				
				.execute();

		for (Column<TermInfoRangeColumn> c : cols.getResult()) {
            TermInfoRangeColumn colName = c.getName();
            UUID docUUID = colName.getDocumentUUID();
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<String>> metadatas = (HashMap<String, ArrayList<String>>) c.getValue(ObjectSerializer.get());
            //System.out.println("Value - " + metadatas.toString());
            String filename = getMetadataValue(metadatas, "SM_FILENAME");
            String extension = getMetadataValue(metadatas, "SM_EXTENSION");
            String date = getMetadataValue(metadatas, "SM_ARCHIVAGE_DATE");
            sysout.println(date + " " + docUUID.toString().toUpperCase() + " " + filename + "." + extension);
		}
	}
	
   
   /**
    * Récupère la valeur d'une métadonnée mono-valuée
    * @param metadatas     : Hashmap contenant les métadonnée
    * @param metadataCode  : Code de la métadonnée à lire
    * @return
    */
   private String getMetadataValue(HashMap<String, ArrayList<String>> metadatas, String metadataCode) {
      if (metadatas.containsKey(metadataCode)) {
         ArrayList<String> valuesList = metadatas.get(metadataCode);
         // catégorie monovaluée -> on renvoie le 1er élément
         return valuesList.get(0);
      }
      return null;
   }

   
   /**
    * Renvoie l'UUID d'une base DFCE dont le nom est donné
    * @param baseName     : Nom de la base (ex : 'SAE-PROD') 
    * @return UUID de la base
    */
   private UUID getBaseUUID(String baseName) throws Exception {
      BasesReferenceEntity  base = mapper.get(BasesReferenceCF.get(), baseName, BasesReferenceEntity.class);
      if (base == null) return null;
      return UUIDSerializer.get().fromBytes(base.getBaseUUID());
   }
   
}
