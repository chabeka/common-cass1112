/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.PagmaDao;
import fr.urssaf.image.sae.droit.dao.model.Pagma;

/**
 * Classe de support de la classe {@link PagmaDao}
 * 
 */
@Component
public class PagmaSupport {

   private final PagmaDao dao;
   
   /**
    * constructeur
    * @param pagmaDao DAO associée au pagma
    */
   @Autowired
   public PagmaSupport(PagmaDao pagmaDao){
      this.dao = pagmaDao;
   }
   
   
   /**
    * Méthode de création d'un ligne
    * 
    * @param pagma
    *           propriétés du PAGMa à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Pagma pagma, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getPagmaTmpl()
            .createUpdater(pagma.getCode());

      for (String code : pagma.getActionUnitaires()) {
         dao.ecritActionUnitaire(updater, code, clock);
      }
      dao.getPagmaTmpl().update(updater);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PAGMa
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPagma(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PAGMa
    * @return un PAGMa correpodant à l'identifiant passé en paramètre
    */
   public final Pagma find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPagmaTmpl()
            .queryColumns(code);

      Pagma pagma = getPagmaFromResult(result);

      return pagma;

   }

   private Pagma getPagmaFromResult(ColumnFamilyResult<String, String> result) {

      Pagma pagma = null;

      if (result != null && result.hasResults()) {
         pagma = new Pagma();
         pagma.setCode(result.getKey());
         List<String> list = new ArrayList<String>(result.getColumnNames());
         pagma.setActionUnitaires(list);
      }

      return pagma;
   }
}
