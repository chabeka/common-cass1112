package fr.urssaf.image.sae.format.referentiel.model;

import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.dao.ReferentielFormatDao;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.exceptions.UnknownParameterException;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * 
 * Classe utilisée pour générer les RuntimeExceptions.
 * 
 */
@Component
public class ReferentielFormatSupportBouchon {

   private final ReferentielFormatDao referentielFormatDao;

   /**
    * Constructeur
    * 
    * @param referentielFormatDao
    *           : DAO
    */
   @Autowired
   public ReferentielFormatSupportBouchon(
         ReferentielFormatDao referentielFormatDao) {
      this.referentielFormatDao = referentielFormatDao;
   }

   /**
    * Utilisé seulement pour les tests
    * 
    * @param idFormat
    *           identifiant du format
    * @return formatFichier
    */
   public final FormatFichier find(String idFormat) {

      if (StringUtils.isBlank(idFormat)) {
         throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
               "erreur.param.obligatoire.null", idFormat));
      }

      FormatFichier refFormat = new FormatFichier();
      refFormat.setIdFormat("test");
      refFormat.setTypeMime("application/test");
      refFormat.setExtension("Test");
      refFormat.setDescription("Test, simplement pour les tests");
      refFormat.setVisualisable(true);
      refFormat.setValidator("TestValidatorImpl");
      refFormat.setIdentificateur("TestIdentifierImpl");

      return refFormat;

   }

   /**
    * Méthode de suppression d’un format de fichier {@link FormatFichier}
    * 
    * @param idFormat
    *           Identifiant du format de fichier {@link FormatFichier} à
    *           supprimer - paramètre obligatoire
    * @param clock
    *           horloge de suppression - paramètre obligatoire
    */
   public final void delete(String idFormat, Long clock) {

      if (idFormat == null)
         throw new IllegalArgumentException(SaeFormatMessageHandler
               .getMessage("erreur.referentielformat.notnull"));

      if (clock == null || clock <= 0)
         throw new IllegalArgumentException(SaeFormatMessageHandler
               .getMessage("erreur.param"));

      FormatFichier refFormatExistant = find(idFormat);

      if (refFormatExistant == null) // le format renseigné n'existe pas en base
         throw new UnknownParameterException(SaeFormatMessageHandler
               .getMessage("erreur.format.delete", idFormat));

      try {
         Mutator<String> mutator = referentielFormatDao.createMutator();
         referentielFormatDao.mutatorSuppressionRefFormat(mutator, idFormat,
               clock);
         mutator.execute();
      } catch (Exception except) {
         throw new ReferentielRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.impossible.delete.format"), except);
      }
   }

}
