/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.JAXBUtils;
import fr.urssaf.image.sae.services.batch.common.model.ErreurTraitement;

/**
 * Implémentation du support {@link ResultatsFileEchecBloquantSupport}
 * 
 */
@Component
public class ResultatsFileEchecBloquantSupportImpl implements
      ResultatsFileEchecBloquantSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileEchecBloquantSupportImpl.class);

   private static final String PREFIX_TRC = "writeResultatsFile()";

   private final ObjectFactory objFactory = new ObjectFactory();

   @Autowired
   private ApplicationContext context;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeResultatsFile(final File ecdeDirectory,
         final ErreurTraitement erreur) {
      LOGGER.debug(
            "{} - Début de création du fichier (resultats.xml en erreur bloquante)",
            PREFIX_TRC);
      if (erreur != null && ecdeDirectory != null) {

      final String pathResultats = ecdeDirectory.getAbsolutePath()
            + File.separator + "resultats.xml";
         final ResultatsType resultatsType = affectResultatsOnError(erreur);
         final JAXBElement<ResultatsType> resultat = objFactory
               .createResultats(resultatsType);

         FileOutputStream outputStream = null;

         URL xsdSchema;

         final Resource classPath = context
               .getResource("classpath:xsd_som_res/resultats.xsd");
         try {
            xsdSchema = classPath.getURL();
            outputStream = new FileOutputStream(pathResultats);
            JAXBUtils.marshal(resultat, outputStream, xsdSchema);
         } catch (FileNotFoundException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (IOException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (JAXBException e) {
            throw new CaptureMasseRuntimeException(e);
         } catch (SAXException e) {
            throw new CaptureMasseRuntimeException(e);
         } finally {
            if (outputStream != null) {
               try {
                  outputStream.close();
               } catch (IOException e) {
                  LOGGER.debug("{} - Erreur de fermeture du flux de "
                        + pathResultats, PREFIX_TRC);
               }
            }
         }
      } else {
         throw new IllegalArgumentException(
               "Les paramètres obligatoires sont manquants pour le traitement : url sommaire ou l'erreur");
      }

      LOGGER.debug("{} - Fin de création du fichier (resultats.xml en erreur bloquante)",
            PREFIX_TRC);
   }

   /**
    * Affectation resultatsOnError
    * 
    * @param sommaireType
    *           representant le fichier sommaire.xml
    * @param sommaireType
    *           sommaire.xml
    * 
    * @return ResultatsType correspondant au resultats.xml
    */
   private ResultatsType affectResultatsOnError(final ErreurTraitement erreur) {

      final ErreurType erreurType = new ErreurType();
      erreurType.setCode(erreur.getCodeErreur());
      erreurType.setLibelle(erreur.getMessageErreur()
            + erreur.getException().getMessage());

      ResultatsType resultats = objFactory.createResultatsType();

      resultats.setErreurBloquanteTraitement(erreurType);

      return resultats;

   }
}
