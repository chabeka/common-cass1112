package fr.urssaf.image.sae.droit.dao;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.EnumValidationMode;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * DAO permettant de réaliser les opérations de base d'écriture, de lecture<br>
 * modification et suppression sans prendre en compte les règles fonctionnelles.
 * */

@Repository
public class FormatControlProfilDao extends AbstractDao<String, String> {

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public FormatControlProfilDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Ajoute un nouveau {@link FormatControlProfil}.
    * 
    * @param formatControlProfil
    *           Objet contenant les informations sur le profil de controle -
    *           obligatoire
    * @param updater
    *           : necessaire pour Cassandra
    * @param clock
    *           : heure d'enregistrement
    */
   public final void addFormatControlProfil(
         ColumnFamilyUpdater<String, String> updater,
         FormatControlProfil formatControlProfil, Long clock) {

      if (formatControlProfil == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils
               .loadMessage("erreur.param.obligatoire.null"));
      }

      List<String> variable = new ArrayList<String>();

      String formatCode = formatControlProfil.getFormatCode();

      String description = formatControlProfil.getDescription();

      if (StringUtils.isBlank(formatCode)) {
         variable.add("formatCode");
      }
      if (StringUtils.isBlank(description)) {
         variable.add("description");
      }
      if (!variable.isEmpty()) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "erreur.param.obligatoire.null", variable.toString()));
      }

      FormatProfil formatProfil = formatControlProfil.getControlProfil();
      if (formatProfil == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "erreur.control.profil.obligatoire", variable.toString()));
      }

      if (formatProfil != null) {
         boolean validation = formatProfil.isFormatValidation();
         String validationMode = formatProfil.getFormatValidationMode();
         if (validation) {
            if (!StringUtils.isBlank(validationMode)
                  && !EnumValidationMode.contains(validationMode)) {

               throw new IllegalArgumentException(ResourceMessagesUtils
                     .loadMessage("erreur.param.format.valid.mode.obligatoire"));
            }
         } else {
            if (!StringUtils.isBlank(validationMode)
                  && !(Constantes.AUCUN.equalsIgnoreCase(validationMode) || Constantes.NONE
                        .equalsIgnoreCase(validationMode))) {
               variable.add(Constantes.FORMAT_VALIDATION_MODE);
            }
         }
      }

      // écrire des colonnes

      // TODO Valider la mise en commentaire. Normalement, il s'agit de la clé
      // de la ligne
      // writeColumnFormatCode(updater, formatCode, clock);
      writeColumnFormatDescription(updater, description, clock);

      writeColumnControlProfil(updater, formatProfil, clock);

   }

   /**
    * ajoute une colonne {@value FormatControlProfilDao#COL_CONTROLPROFIL}
    * 
    * @param updater
    *           updater de {@link SaePagmf}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnControlProfil(
         ColumnFamilyUpdater<String, String> updater, FormatProfil value,
         Long clock) {
      addColumn(updater, Constantes.COL_CONTROLPROFIL, value,
            FormatProfilSerializer.get(), clock);
   }

   // /**
   // * ajoute une colonne {@value FormatControlProfilDao#COL_CODEPROFIL}
   // *
   // * @param updater
   // * updater de {@link FormatControlProfil}
   // * @param value
   // * valeur de la colonne
   // * @param clock
   // * horloge de la colonne
   // */
   // private void writeColumnFormatCode(
   // ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
   // addColumn(updater, Constantes.COL_CODEPROFIL, value, StringSerializer
   // .get(), clock);
   // }

   /**
    * ajoute une colonne {@value FormatControlProfilDao#COL_DESCRIPTION}
    * 
    * @param updater
    *           updater de {@link FormatControlProfil}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnFormatDescription(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_DESCRIPTION, value, StringSerializer
            .get(), clock);
   }

   /**
    * Méthode de suppression d'une ligne {@link FormatControlProfil}
    * 
    * @param mutator
    *           Mutator de {@link FormatControlProfil}
    * @param codeFormatProfil
    *           code du FormatControlProfil à supprimer - param obligatoire
    * @param clock
    *           horloge de la suppression
    */
   public final void deleteFormatControlProfil(Mutator<String> mutator,
         String codeFormatProfil, long clock) {

      try {
         List<String> variable = new ArrayList<String>();
         if (StringUtils.isBlank(codeFormatProfil)) {
            variable.add("codeFormatProfil");
         }
         if (!variable.isEmpty()) {
            /**
             * @exception IllegalArgumentException
             *               <ul>
             *               <li>Il existe une erreur dans les paramètres qui
             *               ont été fournis à la méthode</li>
             *               <li>La valeur d’un ou plusieurs paramètres
             *               obligatoires est nulle ou vide.</li>
             *               </ul>
             */
            throw new IllegalArgumentException(ResourceMessagesUtils
                  .loadMessage("erreur.param.obligatoire.null", variable
                        .toString()));
         }

         mutator.addDeletion(codeFormatProfil, Constantes.DROIT_FORMAT_CONTROL,
               clock);
      } catch (Exception except) {
         throw new DroitRuntimeException(ResourceMessagesUtils
               .loadMessage("erreur.delete.format.control"), except);
      }

   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return Constantes.DROIT_FORMAT_CONTROL;
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
