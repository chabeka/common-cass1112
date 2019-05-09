/**
 * 
 */
package fr.urssaf.image.sae.trace.service.support;

import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.utils.StaxUtils;

/**
 * Classe de support pour la création des fichiers de traces
 * 
 */
@Component
public class TraceFileSupport {

   private static final String NS_RES = "http://www.cirtil.fr/sae/journalSae";
   private static final String PX_RES = "";
   private static final String TYPE_HASH = "SHA-1";

   /**
    * Ecriture de l'entete du fichier
    * 
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireEntete(StaxUtils staxUtils) throws XMLStreamException {
      // entete XML
      staxUtils.startDocument();

      // debut de document
      staxUtils.addStartElement("journal", PX_RES, NS_RES);
      staxUtils.addDefaultPrefix(NS_RES);
   }

   /**
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @param idJournalPrecedent
    *           identifiant du journal précédent
    * @param hashJournalPrecedent
    *           hash du journal précédent
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireInfosJournalPrecedent(StaxUtils staxUtils,
         String idJournalPrecedent, String hashJournalPrecedent)
         throws XMLStreamException {

      staxUtils.addStartElement("journalPrecedent", PX_RES, NS_RES);

      staxUtils.createTag("id", idJournalPrecedent, PX_RES, NS_RES);
      staxUtils.createTag("hash", hashJournalPrecedent, PX_RES, NS_RES);
      staxUtils.createTag("typeHash", TYPE_HASH, PX_RES, NS_RES);

      staxUtils.addEndTag("journalPrecedent", PX_RES, NS_RES);

   }

   /**
    * Ecrire la balise concernant la date
    * 
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @param date
    *           valeur de la date
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireDate(StaxUtils staxUtils, Date date)
         throws XMLStreamException {
      staxUtils.createTag("dateJournee", DateFormatUtils.ISO_DATE_FORMAT
            .format(date), PX_RES, NS_RES);

   }

   /**
    * Ecrire la balise concernant la date
    * 
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireBaliseDebutTraces(StaxUtils staxUtils)
         throws XMLStreamException {

      staxUtils.addStartElement("traces", PX_RES, NS_RES);
   }

   /**
    * Ecrire la balise concernant la date
    * 
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @param trace
    *           trace dont il faut écrire les valeurs
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireTrace(StaxUtils staxUtils, TraceJournalEvt trace)
         throws XMLStreamException {

      staxUtils.addStartElement("trace", PX_RES, NS_RES);

      staxUtils.createTag("id", trace.getIdentifiant().toString(), PX_RES, NS_RES);
      staxUtils.createTag("timestamp",
            DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(trace
                  .getTimestamp()), PX_RES, NS_RES);
      staxUtils.createTag("codeEvt", trace.getCodeEvt(), PX_RES, NS_RES);
      staxUtils.createTag("contexte", trace.getContexte(), PX_RES, NS_RES);
      staxUtils.createTag("cs", trace.getContratService(), PX_RES, NS_RES);

      staxUtils.addStartElement("pagms", PX_RES, NS_RES);
      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         for (String pagm : trace.getPagms()) {
            staxUtils.createTag("pagm", pagm, PX_RES, NS_RES);
         }
      }
      staxUtils.addEndElement("pagms", PX_RES, NS_RES);

      staxUtils.createTag("login", trace.getLogin(), PX_RES, NS_RES);

      staxUtils.addStartElement("infos", PX_RES, NS_RES);
      if (MapUtils.isNotEmpty(trace.getInfos())) {
         for (String key : trace.getInfos().keySet()) {

            staxUtils.addStartElement("info", PX_RES, NS_RES);
            staxUtils.createTag("code", key, PX_RES, NS_RES);
            staxUtils.createTag("valeur", trace.getInfos().get(key).toString(),
                  PX_RES, NS_RES);
            staxUtils.addEndElement("info", PX_RES, NS_RES);
         }
      }
      staxUtils.addEndElement("infos", PX_RES, NS_RES);

      staxUtils.addEndElement("trace", PX_RES, NS_RES);
   }

   /**
    * Ecrire la balise concernant la date
    * 
    * @param staxUtils
    *           objet servant à l'écriture du flux
    * @throws XMLStreamException
    *            exception levée en cas d'erreur d'écriture
    */
   public final void ecrireBalisesFin(StaxUtils staxUtils) throws XMLStreamException {

      staxUtils.addEndElement("traces", PX_RES, NS_RES);
      staxUtils.addEndElement("journal", PX_RES, NS_RES);
   }

}
