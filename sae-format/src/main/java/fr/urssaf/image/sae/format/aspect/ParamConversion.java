package fr.urssaf.image.sae.format.aspect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 * 
 */
@Aspect
public class ParamConversion {

   private static final String CONVERS_SVC_CONVERTIRFICHIER_FILE = "execution(* fr.urssaf.image.sae.format.conversion.service.ConversionService.convertirFichier(*,*,*,*))"
         + "&& args(idFormat,fichier,numeroPage,nombrePages)";

   private static final String CONVERS_SVC_CONVERTIRFICHIER_BYTE = "execution(* fr.urssaf.image.sae.format.conversion.service.ConversionService.convertirFichier(*,*,*,*))"
         + "&& args(idFormat,fichier,numeroPage,nombrePages)";

   private static final String CONVERTISSEUR_CONVERTIRFICHIER_FILE = "execution(* fr.urssaf.image.sae.format.conversion.convertisseurs.Convertisseur.convertirFichier(*,*,*))"
         + "&& args(fichier,numeroPage,nombrePages)";

   private static final String CONVERTISSEUR_CONVERTIRFICHIER_BYTE = "execution(* fr.urssaf.image.sae.format.conversion.convertisseurs.Convertisseur.convertirFichier(*,*,*))"
         + "&& args(fichier,numeroPage,nombrePages)";

   /**
    * Vérification des paramètres de la méthode "convertirFichier" de la classe
    * ConversionService Vérification du String idFormat donné en paramètre<br>
    * 
    * @param idFormat
    *           identifiant du format souhaité
    * @param fichier
    *           le fichier à identifier
    * @param numeroPage
    *           numero de page de début
    * @param nombrePages
    *           nombre de pages
    */
   @Before(CONVERS_SVC_CONVERTIRFICHIER_FILE)
   public final void validConvertirFichierFileFromConversionService(
         String idFormat, File fichier, Integer numeroPage, Integer nombrePages) {

      genererExceptionFile(idFormat, fichier, true);
   }

   /**
    * Vérification des paramètres de la méthode "convertirFichier" de la classe
    * ConversionService Vérification du String idFormat donné en paramètre<br>
    * 
    * @param idFormat
    *           identifiant du format souhaité
    * @param fichier
    *           le fichier à identifier
    * @param numeroPage
    *           numero de page de début
    * @param nombrePages
    *           nombre de pages
    */
   @Before(CONVERS_SVC_CONVERTIRFICHIER_BYTE)
   public final void validConvertirFichierByteFromConversionService(
         String idFormat, byte[] fichier, Integer numeroPage,
         Integer nombrePages) {

      genererExceptionByte(idFormat, fichier, true);
   }

   /**
    * Vérification des paramètres de la méthode "convertirFichier" de la classe
    * Convertisseur Vérification du File fichier donné en paramètre<br>
    * 
    * @param fichier
    *           le fichier à identifier
    * @param numeroPage
    *           numero de page de début
    * @param nombrePages
    *           nombre de pages
    */
   @Before(CONVERTISSEUR_CONVERTIRFICHIER_FILE)
   public final void convertirFichierFile(File fichier, Integer numeroPage,
         Integer nombrePages) {

      genererExceptionFile(null, fichier, false);
   }

   /**
    * Vérification des paramètres de la méthode "convertirFichier" de la classe
    * Convertisseur Vérification du byte[] fichier donné en paramètre<br>
    * 
    * @param fichier
    *           le fichier à identifier
    * @param numeroPage
    *           numero de page de début
    * @param nombrePages
    *           nombre de pages
    */
   @Before(CONVERTISSEUR_CONVERTIRFICHIER_BYTE)
   public final void convertirFichierByte(byte[] fichier, Integer numeroPage,
         Integer nombrePages) {

      genererExceptionByte(null, fichier, false);
   }

   private void genererExceptionFile(String idFormat, File file,
         boolean idFormatObligatoire) {

      List<String> param = new ArrayList<String>();

      if (idFormatObligatoire && StringUtils.isBlank(idFormat)) {
         param.add(Constantes.IDFORMAT);
      }
      if (file == null || !file.exists()) {
         param.add(Constantes.FICHIER);
      }
      if (!param.isEmpty()) {
         throw new ReferentielRuntimeException(SaeFormatMessageHandler
               .getMessage(Constantes.PARAM_OBLIGATOIRE, param.toString()));
      }
   }

   private void genererExceptionByte(String idFormat, byte[] fichier,
         boolean idFormatObligatoire) {

      List<String> param = new ArrayList<String>();

      if (idFormatObligatoire && StringUtils.isBlank(idFormat)) {
         param.add(Constantes.IDFORMAT);
      }
      if (fichier == null) {
         param.add(Constantes.BYTE);
      }
      if (!param.isEmpty()) {
         throw new ReferentielRuntimeException(SaeFormatMessageHandler
               .getMessage(Constantes.PARAM_OBLIGATOIRE, param.toString()));
      }
   }

}
