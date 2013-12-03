package fr.urssaf.image.sae.format.referentiel.dao;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * DAO permettant de réaliser les opérations de base d’écriture, lecture,
 * modification et suppression sans prendre en compte les règles fonctionnelles.
 * 
 */
@Repository
public class ReferentielFormatDao extends AbstractDao<String, String> {

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public ReferentielFormatDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Ajoute un nouveau format de fichier {@link FormatFichier}.
    * 
    * @param idFormat
    *           Identifiant CIRTIL du format de fichier - obligatoire
    * @param typeMime
    *           Le type-mime du format de fichier - non obligatoire
    * @param extension
    *           L’extension du fichier - non obligatoire
    * @param description
    *           Une description générale du format de fichier - obligatoire
    * @param visualisable
    *           Indicateur d’affichage à l’écran - obligatoire
    * @param validator
    *           Nom de la fonction de validation à appeler si le format doit
    *           être validé - obligatoire
    * @param identification
    *           Nom de la fonction d’identification à appeler si le format doit
    *           être identifié - obligatoire
    * @param updater
    *           : necessaire pour Cassandra
    * @param clock
    *           : heure d'enregistrement
    */
   public final void addNewFormat(ColumnFamilyUpdater<String, String> updater,
         String idFormat, String typeMime, String extension,
         String description, Boolean visualisable, String validator,
         String identification, Long clock) {

      List<String> variable = new ArrayList<String>();

      if (StringUtils.isBlank(idFormat)) {
         variable.add("idFormat");
      }
      if (StringUtils.isBlank(description)) {
         variable.add("description");
      }
      if (visualisable == null) {
         variable.add("visualisable");
      }
      if (StringUtils.isBlank(validator)) {
         variable.add("validator");
      }
      if (StringUtils.isBlank(identification)) {
         variable.add("identification");
      }
      if (clock == null || clock <= 0) {
         variable.add("clock");
      }

      if (!variable.isEmpty()) {
         /**
          * @exception IllegalArgumentException
          *               <ul>
          *               <li>Il existe une erreur dans les paramètres qui ont
          *               été fournis à la méthode</li>
          *               <li>La valeur d’un ou plusieurs paramètres
          *               obligatoires est nulle ou vide.</li>
          *               </ul>
          */
         throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
               "erreur.param.obligatoire.null", variable.toString()));
      }

      // écrire des colonnes
      writeColumnIdFormat(updater, idFormat, clock);
      writeColumnTypeMime(updater, typeMime, clock);
      writeColumnExtension(updater, extension, clock);
      writeColumnDescription(updater, description, clock);
      writeColumnVisualisable(updater, visualisable, clock);
      writeColumnValidator(updater, validator, clock);
      writeColumnIdentification(updater, identification, clock);

   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_IDFORMAT}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnIdFormat(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_IDFORMAT, value,
            StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_TYPEMIME}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnTypeMime(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_TYPEMIME, value,
            StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_EXTENSION}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnExtension(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_EXTENSION, value, StringSerializer
            .get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_DESCRIPTION}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnDescription(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_DESCRIPTION, value, StringSerializer
            .get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_VISUALISABLE}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnVisualisable(
         ColumnFamilyUpdater<String, String> updater, boolean value, Long clock) {
      addColumn(updater, Constantes.COL_VISUALISABLE, value, BooleanSerializer
            .get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_VALIDATOR}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnValidator(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_VALIDATOR, value, StringSerializer
            .get(), clock);
   }

   /**
    * ajoute une colonne {@value ReferentielFormatDao#COL_IDENTIFIEUR}
    * 
    * @param updater
    *           updater de {@link FormatFichier}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnIdentification(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_IDENTIFIEUR, value, StringSerializer
            .get(), clock);
   }

   /**
    * Méthode de suppression d'une ligne {@link FormatFichier}
    * 
    * @param mutator
    *           Mutator de {@link FormatFichier}
    * @param idFormat
    *           identifiant du format
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionRefFormat(Mutator<String> mutator,
         String idFormat, long clock) {

      mutator.addDeletion(idFormat, Constantes.REFERENTIEL_FORMAT, clock);

   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return Constantes.REFERENTIEL_FORMAT;
   }

   /**
    * @return le sérializer d'une colonne
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * @return le sérializer de la clé d'une ligne
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

}
