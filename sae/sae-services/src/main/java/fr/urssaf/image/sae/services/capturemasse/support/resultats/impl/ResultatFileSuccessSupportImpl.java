/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport;
import fr.urssaf.image.sae.services.util.JAXBUtils;

/**
 * Implémentation du support {@link ResultatFileSuccessSupport}
 * 
 */
@Component
public class ResultatFileSuccessSupportImpl implements
      ResultatFileSuccessSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatFileSuccessSupportImpl.class);

   private static final String PREFIX_TRC = "writeResultatsFile()";

   @Autowired
   private ApplicationContext context;

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeResultatsFile(final File ecdeDirectory,
         final List<CaptureMasseIntegratedDocument> intDocuments,
         final int documentsCount) {

      LOGGER.debug(
            "{} - Début de création du fichier (resultats.xml en réussite)",
            PREFIX_TRC);

      final ResultatsType resultatsType = creerResultat(documentsCount,
            intDocuments.size());

      final ObjectFactory factory = new ObjectFactory();
      final JAXBElement<ResultatsType> resultat = factory
            .createResultats(resultatsType);

      ecrireResultat(resultat, ecdeDirectory);

      LOGGER.debug(
            "{} - Fin de création du fichier (resultats.xml en réussite)",
            PREFIX_TRC);
   }

   /**
    * Création du résultat
    * 
    * @param documentsCount
    *           nombre de documents initial
    * @param intDocCount
    *           nombre de documents intégrés
    * @return le résultat sous forme d'objet
    */
   private ResultatsType creerResultat(final int documentsCount,
         final int intDocCount) {

      final ResultatsType resultatsType = new ResultatsType();
      resultatsType.setBatchMode(BatchModeType.TOUT_OU_RIEN);
      resultatsType.setInitialDocumentsCount(documentsCount);
      resultatsType.setInitialVirtualDocumentsCount(0);
      resultatsType.setIntegratedDocumentsCount(intDocCount);
      resultatsType.setNonIntegratedDocumentsCount(0);
      resultatsType.setIntegratedVirtualDocumentsCount(0);
      resultatsType.setNonIntegratedVirtualDocumentsCount(0);
      resultatsType
            .setNonIntegratedDocuments(new ListeNonIntegratedDocumentsType());
      resultatsType
            .setNonIntegratedVirtualDocuments(new ListeDocumentsVirtuelsType());
      resultatsType.setErreurBloquanteTraitement(null);

      return resultatsType;
   }

   /**
    * Ecriture du fichier de resultat
    * 
    * @param resultat
    *           objet représentant le résultat
    * @param ecdeDirectory
    *           répertoire de traitement
    */
   private void ecrireResultat(final JAXBElement<ResultatsType> resultat,
         final File ecdeDirectory) {

      final String pathResultats = ecdeDirectory.getAbsolutePath()
            + File.separator + "resultats.xml";
      final File resultats = new File(pathResultats);

      try {
         FileOutputStream output = new FileOutputStream(resultats);

         final Resource classPath = getContext().getResource(
               "classpath:xsd_som_res/resultats.xsd");
         URL xsdSchema;

         try {
            xsdSchema = classPath.getURL();
            JAXBUtils.marshal(resultat, output, xsdSchema);
         } catch (IOException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (JAXBException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (SAXException e) {
            throw new CaptureMasseRuntimeException(e);
         } finally {
            try {
               output.close();
            } catch (IOException e) {
               LOGGER.info("erreur de fermeture de flux", e);
            }
         }

      } catch (FileNotFoundException e) {
         throw new CaptureMasseRuntimeException(e);

      }

   }
}
