/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.PagmDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;

/**
 * Classe de support de la classe {@link PagmDao}
 * 
 */
@Component
public class PagmSupport {

   private final PagmSerializer pagmSerializer = PagmSerializer.get();

   @Autowired
   private PagmDao dao;

   /**
    * Méthode de création d'un ligne
    * 
    * @param idClient
    *           identifiant du client
    * @param pagm
    *           propriétés du PAGM à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(String idClient, Pagm pagm, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getPagmTmpl()
            .createUpdater(idClient);

      dao.ecritPagm(updater, pagm, clock);

      dao.getPagmTmpl().update(updater);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PAGM
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPagm(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PAGM
    * @return la liste des PAGM correspondant à l'identifiant passé en paramètre
    */
   public final List<Pagm> find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPagmTmpl()
            .queryColumns(code);

      Collection<String> colNames = result.getColumnNames();

      List<Pagm> pagms = null;
      
      if (CollectionUtils.isNotEmpty(colNames)) {
         pagms = new ArrayList<Pagm>(colNames.size());
      }
      
      for (String name : colNames) {

         byte[] bResult = result.getByteArray(name);
         Pagm pagm = pagmSerializer.fromBytes(bResult);

         pagms.add(pagm);
      }

      return pagms;

   }

}
