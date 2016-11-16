package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.hector.api.Keyspace;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;
import fr.urssaf.image.sae.droit.dao.PrmdDao;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;

/**
 * Classe de manipulation des droits
 */
@Service
public final class DroitService {

   private static final Logger LOG = LoggerFactory
         .getLogger(DroitService.class);

   /**
    * Lot 160600 : Mise en place des expression régulières dans les PRMD Il faut
    * donc échapper tous les . déjà présents dans les valeur de métadonnée comme
    * pour les RND
    * 
    * Méthode à ne par réutiliser telle qu'elle dans un prochain lot
    * 
    * @param keyspace
    *           le Keyspace Cassandra
    */
   public void majPrmdExpReguliere160600(Keyspace keyspace) {

      LOG.info("Mise à jour des contrats de service pour gérer les expressions régulières");

      JobClockConfiguration clock = new JobClockConfiguration();
      clock.setMaxTimeSynchroError(10000000);
      clock.setMaxTimeSynchroWarn(2000000);
      JobClockSupport jobClock = new JobClockSupportImpl(keyspace, clock);

      PrmdSupport prmdSupport = new PrmdSupport(new PrmdDao(keyspace));
      List<Prmd> listePrmd = prmdSupport.findAll(500);

      Prmd prmdEchappe = new Prmd();

      for (Prmd prmd : listePrmd) {
         LOG.info("Mise à jour du PRMD " + prmd.getCode());
         prmdEchappe.setBean(prmd.getBean());
         prmdEchappe.setCode(prmd.getCode());
         prmdEchappe.setDescription(prmd.getDescription());
         prmdEchappe.setLucene(prmd.getLucene());

         Map<String, List<String>> listeMeta = prmd.getMetadata();
         Map<String, List<String>> listeMetaEchappe = new HashMap<String, List<String>>();

         if (MapUtils.isNotEmpty(listeMeta)) {

            Iterator<Entry<String, List<String>>> iterator = listeMeta
                  .entrySet().iterator();
            String key;
            while (iterator.hasNext()) {

               key = iterator.next().getKey();
               List<String> listeValeur = listeMeta.get(key);
               List<String> listeValeurEchappe = new ArrayList<String>();
               for (String valeur : listeValeur) {
                  // Au cas ou on souhaite rejouer le script, on remplace
                  // d'abord les \. par des .
                  // Si c'est la 1ère execution, ca ne fera rien
                  valeur = valeur.replace("\\.", ".");

                  // Ensuite on remplace les . par des \. afin que le . ne soit
                  // pas pris comme un caractère spécial d'une expression
                  // régulière
                  valeur = valeur.replace(".", "\\.");

                  listeValeurEchappe.add(valeur);

               }
               listeMetaEchappe.put(key, listeValeurEchappe);
            }
         }

         prmdEchappe.setMetadata(listeMetaEchappe);
         prmdSupport.create(prmdEchappe, jobClock.currentCLock());

      }

      LOG.info("Fin mise à jour des contrats de service");

   }

}
