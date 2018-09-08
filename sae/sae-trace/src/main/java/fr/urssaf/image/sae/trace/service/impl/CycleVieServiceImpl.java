package fr.urssaf.image.sae.trace.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.support.CycleVieSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.service.CycleVieService;

/**
 * Classe d'implémentation du support {@link CycleVieService}. Cette classe est
 * un singleton et peut être accessible par le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
public class CycleVieServiceImpl implements CycleVieService {

   private final CycleVieSupport support;

   /**
    * @param support
    *           Classe de support pour les journaux du cycle de vie des archives
    */
   @Autowired
   public CycleVieServiceImpl(CycleVieSupport support) {
      super();
      this.support = support;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<DfceTraceDoc> lecture(Date dateDebut, Date dateFin,
         int limite, boolean reversed) {

      //Date startDate = getGmtDate(dateDebut);
      // startDate = DateUtils.truncate(startDate, Calendar.DATE);
      //Date endDate = getGmtDate(dateFin);
      // endDate = DateUtils.addDays(endDate, 1);
      // endDate = DateUtils.truncate(endDate, Calendar.DATE);

      //return support.findByDates(startDate, endDate, limite, reversed);
      return support.findByDates(dateDebut, dateFin, limite, reversed);

   }

//   private Date getGmtDate(Date date) {
//      long value = DateTimeZone.getDefault().convertLocalToUTC(date.getTime(),
//            true);
//
//      return new Date(value);
//
//   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<DfceTraceDoc> lectureParDocument(UUID docUuid) {

      return support.findByDocUuid(docUuid);
   }

}
