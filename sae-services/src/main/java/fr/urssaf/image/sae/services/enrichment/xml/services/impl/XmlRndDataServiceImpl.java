package fr.urssaf.image.sae.services.enrichment.xml.services.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.enrichment.xml.model.RndReference;
import fr.urssaf.image.sae.services.enrichment.xml.model.TypeDocument;
import fr.urssaf.image.sae.services.enrichment.xml.services.XmlRndDataService;
import fr.urssaf.image.sae.services.util.XStreamHelper;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;

/**
 * Fournit des méthodes de désérialisation
 * 
 * @author rhofir
 * 
 */
@Service
@Qualifier("xmlRndDataService")
public class XmlRndDataServiceImpl implements XmlRndDataService {

   /**
    * Construit le composant XStream utilisé en lecture.
    * 
    * @param clazz
    *           : classe à séraialiser.
    * @return le composant xstream
    */
   private XStream buildReadingXStream(final Class<?> clazz) {

      return XStreamHelper.newXStream(clazz);
   }

   /**
    * {@inheritDoc}
    */
   public final Map<String, TypeDocument> rndReferenceReader(
         final InputStream xmlInputStream) throws FileNotFoundException {
      final RndReference dataFromXml = getRndReferential(xmlInputStream);
      final Map<String, TypeDocument> referential = new HashMap<String, TypeDocument>();
      for (TypeDocument typeDocument : Utils.nullSafeIterable(dataFromXml
            .getTypeDocuments())) {
         referential.put(typeDocument.getRndCode(), typeDocument);
      }
      return referential;
   }

   /**
    * 
    * 
    * @throws FileNotFoundException
    *            Lorsque le fichier n'existe pas
    */
   private RndReference getRndReferential(final InputStream xmlInputStream)
         throws FileNotFoundException {
      return XStreamHelper.parse(new InputStreamReader(xmlInputStream),
            Constants.ENCODING, RndReference.class,
            buildReadingXStream(RndReference.class));
   }
}
