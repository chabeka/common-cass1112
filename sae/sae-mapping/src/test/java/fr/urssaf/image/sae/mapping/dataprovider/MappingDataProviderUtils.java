package fr.urssaf.image.sae.mapping.dataprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.constants.Constants;
import fr.urssaf.image.sae.mapping.test.model.MappingMockMetadata;
import fr.urssaf.image.sae.mapping.test.model.MappingMockMetadatas;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.metadata.utils.XStreamHelper;

/**
 * Classe fournissant les services de lecture des fichiers xml de test.
 */
public final class MappingDataProviderUtils {

   /**
    * Retourne une liste de métadonnée de type {@link UntypedMetadata} à partir
    * d'une structure XML
    * 
    * @param xmlFile
    *           : Le nom du fichier xml
    * @return ne liste de métadonnée à partir d'une structure XML
    * @throws FileNotFoundException
    *            Exception levée lorsque le fichier n'existe pas.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static List<UntypedMetadata> getUntypedMetadata(final File xmlFile)
         throws FileNotFoundException {
      final List<UntypedMetadata> untyped = new ArrayList<UntypedMetadata>();
      final MappingMockMetadatas dataFromXml = getMetadataFormTestFile(xmlFile);
      for (MappingMockMetadata metadata : Utils.nullSafeIterable(dataFromXml
            .getMetadatas())) {
         untyped.add(new UntypedMetadata(metadata.getLongCode(), metadata
               .getValue()));
      }
      return untyped;
   }

   /**
    * 
    * 
    * @throws FileNotFoundException
    *            Lorsque le fichier n'existe pas
    */
   private static MappingMockMetadatas getMetadataFormTestFile(
         final File xmlFile) throws FileNotFoundException {
      return XStreamHelper.parse(xmlFile, Constants.ENCODING,
            MappingMockMetadatas.class,
            buildReadingXStream(MappingMockMetadatas.class));
   }

   /**
    * Construit le composant XStream utilisé en lecture.
    * 
    * @param xstrClass
    *           : La classe à désérialiser
    * @return le composant xstream
    */
   private static XStream buildReadingXStream(final Class<?> xstrClass) {
      return XStreamHelper.newXStream(xstrClass);
   }

   /**
    * Cette classe n'est pas faite pour être instanciée.
    */
   private MappingDataProviderUtils() {
      assert false;

   }

}
