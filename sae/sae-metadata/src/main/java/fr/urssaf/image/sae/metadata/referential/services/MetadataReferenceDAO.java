package fr.urssaf.image.sae.metadata.referential.services;

import java.util.Map;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Fournit des services de manipulation des métadonnées de référentiel des
 * métadonnées.
 * 
 * @author akenore
 * 
 */
public interface MetadataReferenceDAO {

   /**
    * 
    * @return La liste des métadonnées du référentiel des métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getAllMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées consultables par défaut du référentiel
    *         des métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getDefaultConsultableMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées consultables du référentiel des
    *         métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getConsultableMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées interrogables du référentiel des
    *         métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getSearchableMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées obligatoires pour le stockage du
    *         référentiel des métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getRequiredForStorageMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées obligatoires pour l'archivage du
    *         référentiel des métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getRequiredForArchivalMetadataReferences()
         throws ReferentialException;

   /**
    * 
    * @return La liste des métadonnées archivable du référentiel des
    *         métadonnées.
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, MetadataReference> getArchivableMetadataReferences()
         throws ReferentialException;

   /**
    * @param longCode
    *           : le code long.
    * @return Retourne un objet de type MetadataReference à partir du code long
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   MetadataReference getByLongCode(final String longCode)
         throws ReferentialException;

   /**
    * @param shortCode
    *           : le code court.
    * @return Retourne un objet de type MetadataReference à partir du code court
    * @throws ReferentialException
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   MetadataReference getByShortCode(final String shortCode)
         throws ReferentialException;

   /**
    * Charge les métadonnées modifiables
    * 
    * @return La liste des métadonnées modifiables
    * @throws ReferentialException
    *            Une erreur s'est produite lors du chargement des données
    */
   Map<String, MetadataReference> getModifiableMetadataReferences()
         throws ReferentialException;

}
