/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.serializer.ListSerializer;

/**
 * Service DAO de la famille de colonnes "DroitContratService"
 * 
 */
@Repository
public class ContratServiceDao extends AbstractDao<String, String> {

   public static final String CS_CFNAME = "DroitContratService";

   /** code de l'organisme client lié au contrat de service */
   public static final String CS_LIBELLE = "libelle";

   /** durée maximum de l'habilitation exprimée en secondes */
   public static final String CS_VI_DUREE = "viDuree";

   /** description du contrat de service */
   public static final String CS_DESCRIPTION = "description";

   /** CN de la pki attendue */
   public static final String CS_PKI = "pki";

   /** liste des CN des pki attendues */
   public static final String CS_LISTE_PKI = "listPki";

   /** CN du certificat client attendu */
   public static final String CS_CERT = "cert";

   /** liste des CN des certificats clients attendus */
   public static final String CS_LISTE_CERT = "listeCert";

   /** Vérification controle de nommage */
   public static final String CS_VERIF_NOMMAGE = "verifNommage";

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public ContratServiceDao(Keyspace keyspace) {
      super(keyspace);

   }

   /**
    * ajoute une colonne {@value #CS_LIBELLE}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritLibelle(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_LIBELLE, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_VI_DUREE}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritViDuree(ColumnFamilyUpdater<String, String> updater,
         Long value, long clock) {

      addColumn(updater, CS_VI_DUREE, value, LongSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_DESCRIPTION}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritDescription(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, CS_DESCRIPTION, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_PKI}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritIdPki(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_PKI, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_LISTE_PKI}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritListePki(ColumnFamilyUpdater<String, String> updater,
         List<String> value, long clock) {

      addColumn(updater, CS_LISTE_PKI, value, ListSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_CERT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritCert(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, CS_CERT, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_LISTE_CERT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritListeCert(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {

      addColumn(updater, CS_LISTE_CERT, value, ListSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #CS_VERIF_NOMMAGE}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritFlagControlNommage(
         ColumnFamilyUpdater<String, String> updater, boolean value, long clock) {

      addColumn(updater, CS_VERIF_NOMMAGE, value, BooleanSerializer.get(),
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return CS_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }
}
