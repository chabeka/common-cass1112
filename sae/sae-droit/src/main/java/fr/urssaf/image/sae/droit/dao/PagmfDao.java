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
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * DAO permettant de réaliser les opérations de base d'écriture, de lecture<br>
 * modification et suppression sans prendre en compte les règles fonctionnelles.
 * */

@Repository
public class PagmfDao extends AbstractDao<String, String> {

   private FormatControlProfilSupport formatControlProfilSupport;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public PagmfDao(Keyspace keyspace, FormatControlProfilSupport formatControlProfilSupport) {
      super(keyspace);
      this.formatControlProfilSupport = formatControlProfilSupport;
   }

   /**
    * Ajoute un nouveau Pagmf {@link Pagmf}.
    * 
    * @param pagmf
    *           Objet contenant les informations sur le PAGMF - obligatoire
    * @param updater
    *           : necessaire pour Cassandra
    * @param clock
    *           : heure d'enregistrement
    * @throws FormatControlProfilNotFoundException : format de controle profil est inexistant          
    */
   public final void addPagmf(ColumnFamilyUpdater<String, String> updater,
         Pagmf pagmf, Long clock) throws FormatControlProfilNotFoundException{

      if (pagmf == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils
               .loadMessage("erreur.param.obligatoire.null"));
      }

      List<String> variable = new ArrayList<String>();

      String codePagmf = pagmf.getCodePagmf();
      String description = pagmf.getDescription();
      String formatProfile = pagmf.getCodeFormatControlProfil();

      if (StringUtils.isBlank(codePagmf)) {
         variable.add("codePagmf");
      }
      if (StringUtils.isBlank(description)) {
         variable.add("description");
      }
      if (StringUtils.isBlank(formatProfile)) {
         variable.add("formatProfile");
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
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "erreur.param.obligatoire.null", variable.toString()));
      }

      FormatControlProfil formatControlProfil = formatControlProfilSupport
            .find(formatProfile);
      if (formatControlProfil == null) {
         /**
          * @exception FormatControlProfilNotFounfException
          *               Le profil de contrôle n'existe pas dans la CF
          *               DroitFormatControlProfil
          */
         throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
               .loadMessage("erreur.format.control.profil.not.found",
                     formatProfile));
      }

      // écrire des colonnes
      writeColumnCodePagmf(updater, codePagmf, clock);
      writeColumnDescription(updater, description, clock);
      writeColumnFormatProfile(updater, formatProfile, clock);

   }

   /**
    * Méthode de suppression d'une ligne {@link Pagmf}
    * 
    * @param mutator
    *           Mutator de {@link Pagmf}
    * @param codePagmf
    *           code du Pagmf à supprimer - param obligatoire
    * @param clock
    *           horloge de la suppression
    */
   public final void deletePagmf(Mutator<String> mutator,
         String codePagmf, long clock) {

      List<String> variable = new ArrayList<String>();
      if (StringUtils.isBlank(codePagmf)) {
         variable.add("codePagmf");
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
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "erreur.param.obligatoire.null", variable.toString()));
      }

      mutator.addDeletion(codePagmf, Constantes.DROIT_PAGMF, clock);

   }

   /**
    * ajoute une colonne {@value PagmfDao#COL_CODEPAGMF}
    * 
    * @param updater
    *           updater de {@link Pagmf}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnCodePagmf(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_CODEPAGMF, value, StringSerializer
            .get(), clock);
   }

   /**
    * ajoute une colonne {@value PagmfDao#COL_DESCRIPTION}
    * 
    * @param updater
    *           updater de {@link Pagmf}
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
    * ajoute une colonne {@value PagmfDao#COL_codeFormatControlProfil}
    * 
    * @param updater
    *           updater de {@link Pagmf}
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   private void writeColumnFormatProfile(
         ColumnFamilyUpdater<String, String> updater, String value, Long clock) {
      addColumn(updater, Constantes.COL_CODEFORMATCONTROLPROFIL, value,
            StringSerializer.get(), clock);
   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return Constantes.DROIT_PAGMF;
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
