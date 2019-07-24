package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.MetadonneeDispoType;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.webservices.exception.ErreurInterneAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSMetadataService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectRecuperationMetadonneesFactory;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

/**
 * Classe d'implémentation du service {@link WSMetadataService}. Cette classe
 * est un singleton et peut être accédée via l'annotation @Autowired.
 */
@Service
public class WSMetadataServiceImpl implements WSMetadataService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSMetadataServiceImpl.class);

   /**
    * Service accédant aux métadonnées.
    */
   @Autowired
   private MetadataService metadataService;

   /**
    * {@inheritDoc}
    */
   @Override
   public RecuperationMetadonneesResponse recupererMetadonnees()
         throws ErreurInterneAxisFault {
      // Traces debug - entrée méthode
      String prefixeTrc = "recupererMetadonnees()";
      LOG.debug("{} - Début", prefixeTrc);

      List<MetadataReference> metadatas = metadataService
            .getClientAvailableMetadata();

      List<MetadonneeDispoType> metadataDispos = convertListeMetasServiceToWebService(metadatas);
      RecuperationMetadonneesResponse response = ObjectRecuperationMetadonneesFactory
            .createRecuperationMetadonneesResponse(metadataDispos);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // renvoie la response contenant la liste des métadonnées mise à
      // disposition du client
      return response;
   }

   /**
    * Methode permettant de convertir la liste des métadonnées de référence en
    * métadonnées disponible.
    * 
    * @param listeMetasService
    *           liste des métadonnées mise à disposition du client
    * @return List<MetadonneeDispoType> : liste des métadonnées disponible pour
    *         le client
    */
   private List<MetadonneeDispoType> convertListeMetasServiceToWebService(
         List<MetadataReference> listeMetasService) {

      List<MetadonneeDispoType> metadatas = new ArrayList<MetadonneeDispoType>();

      for (MetadataReference metadata : CollectionUtils
            .loadListNotNull(listeMetasService)) {

         MetadonneeDispoType metadonnee = ObjectTypeFactory
               .createMetadonneeDispoType(metadata);

         metadatas.add(metadonnee);
      }

      return metadatas;
   }
}
