/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.lotinstallmaj.modele.Pagm;
import fr.urssaf.image.sae.lotinstallmaj.serializer.PagmSerializer;

/**
 * 
 * 
 */
public class InsertionDonnees {

   private static final String DESCRIPTION = "description";
   /**
    * 
    */
   private static final int DEFAULT_CONSERVATION = 7200;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           le keyspace CASSANDRA
    */
   public InsertionDonnees(Keyspace keyspace) {
      this.keyspace = keyspace;
   }

   /**
    * Insertion de données de droits
    */
   public final void addDroits() {
      addActionsUnitaires();
      addPrmd();
      addPagma();
      addPagmp();
      addPagm();
      addContratService();
   }

   private void addActionsUnitaires() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("consultation", "consultation", cfTmpl);
      addActionUnitaire("recherche", "recherche", cfTmpl);
      addActionUnitaire("archivage_masse", "archivage de masse", cfTmpl);
      addActionUnitaire("archivage_unitaire", "archivage unitaire", cfTmpl);

   }

   /**
    * @param string
    * @param string2
    * @param updater
    */
   @SuppressWarnings("unchecked")
   private void addColumn(Object colName, Object value,
         Serializer nameSerializer, Serializer valueSerializer,
         ColumnFamilyUpdater<String, String> updater) {
      HColumn<String, String> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);
      updater.setColumn(column);

   }

   private void addPrmd() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPrmd", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PRMD");
      addColumn(DESCRIPTION, "acces total", StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("bean", "permitAll", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
   }

   private void addPagma() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagma", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMA");
      addColumn("consultation", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("recherche", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("archivage_masse", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("archivage_unitaire", StringUtils.EMPTY,
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   private void addPagmp() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagmp", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMP");
      addColumn(DESCRIPTION, "acces pagmp full", StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("prmd", "ACCES_FULL_PRMD", StringSerializer.get(),
            StringSerializer.get(), updater);

      cfTmpl.update(updater);
   }

   private void addPagm() {
      Pagm pagm = new Pagm();
      pagm.setCode("ACCES_FULL_PAGM");
      pagm.setDescription("Pagm accès total");
      pagm.setPagma("ACCES_FULL_PAGMA");
      pagm.setPagmp("ACCES_FULL_PAGMP");
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagm", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      addColumn("ACCES_FULL_PAGM", pagm, StringSerializer.get(), PagmSerializer
            .get(), updater);
      cfTmpl.update(updater);

   }

   private void addContratService() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitContratService", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      addColumn("libelle", "accès ancien contrat de service", StringSerializer
            .get(), PagmSerializer.get(), updater);
      addColumn(DESCRIPTION, "accès ancien contrat de service",
            StringSerializer.get(), PagmSerializer.get(), updater);
      addColumn("viDuree", Long.valueOf(DEFAULT_CONSERVATION), StringSerializer.get(),
            LongSerializer.get(), updater);
      addColumn("pki", "CN=IGC/A", StringSerializer.get(), StringSerializer
            .get(), updater);

      cfTmpl.update(updater);

   }

   private void addActionUnitaire(String identifiant, String description,
         ColumnFamilyTemplate<String, String> cfTmpl) {
      ColumnFamilyUpdater<String, String> updater = cfTmpl.createUpdater(identifiant);
      HColumn<String, String> column = HFactory.createColumn(DESCRIPTION,
            description, StringSerializer.get(), StringSerializer.get());
      updater.setColumn(column);

      cfTmpl.update(updater);
   }

}
