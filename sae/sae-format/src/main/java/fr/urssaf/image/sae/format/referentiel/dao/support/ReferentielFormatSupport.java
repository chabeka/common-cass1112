package fr.urssaf.image.sae.format.referentiel.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.ReferentielFormatDao;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/***
 * 
 * Classe support permettant d’exploiter le bean {@link FormatFichier} <br>
 * et la DAO {@link ReferentielFormatDao} du référentiel des formats.<br>
 * 
 * Classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "ReferentielFormat" *
 */
@Component
public class ReferentielFormatSupport {

   private final ReferentielFormatDao referentielFormatDao;

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ReferentielFormatSupport.class);

   private static final int MAX_FIND_RESULT = 2000; // limite des résultats des

   // recherches

   /**
    * Constructeur de la classe support
    * 
    * @param referentielFormatDao
    *           la dao
    */
   @Autowired
   public ReferentielFormatSupport(ReferentielFormatDao referentielFormatDao) {
      this.referentielFormatDao = referentielFormatDao;
   }

   /**
    * Ajoute un nouveau format de fichier {@link FormatFichier}.
    * 
    * @param referentielFormat
    *           objet contenant les informations sur le nouveau format de
    *           fichier - paramètre obligatoire
    * @param clock
    *           horloge de la création - paramètre obligatoire
    */
   public final void create(FormatFichier referentielFormat, Long clock) {

      if (referentielFormat == null)
         throw new IllegalArgumentException(SaeFormatMessageHandler
               .getMessage("erreur.param"));

      String description = referentielFormat.getDescription();
      String extension = referentielFormat.getExtension();
      String identification = referentielFormat.getIdentificateur();
      String idFormat = referentielFormat.getIdFormat();
      String typeMime = referentielFormat.getTypeMime();
      String validator = referentielFormat.getValidator();
      Boolean visualisable = referentielFormat.isVisualisable();

      String opeRefFormatPrefix = "ajouterRefFormat";
      LOGGER.debug(DEBUT_LOG, opeRefFormatPrefix);
      LOGGER.debug("{} - Identifiant du referentielFormat : {}", new String[] {
            opeRefFormatPrefix, idFormat });

      ColumnFamilyUpdater<String, String> updater = referentielFormatDao
            .getCfTmpl().createUpdater(idFormat);

      referentielFormatDao.addNewFormat(updater, idFormat, typeMime, extension,
            description, visualisable, validator, identification, clock);

      referentielFormatDao.getCfTmpl().update(updater);

      LOGGER.info("{} - Ajout du format de fichier : {}", new String[] {
            opeRefFormatPrefix, idFormat });
      LOGGER.debug(FIN_LOG, opeRefFormatPrefix);

   }

   /**
    * Méthode de suppression d’un format de fichier {@link FormatFichier}
    * 
    * @param idFormat
    *           Identifiant du format de fichier {@link FormatFichier} à
    *           supprimer - paramètre obligatoire
    * @param clock
    *           horloge de suppression - paramètre obligatoire
    * @throws UnknownFormatException
    *            : le format renseigné n'existe pas en base
    */
   public final void delete(String idFormat, Long clock)
         throws UnknownFormatException {

      if (idFormat == null)
         throw new IllegalArgumentException(SaeFormatMessageHandler
               .getMessage("erreur.referentielformat.notnull"));

      if (clock == null || clock <= 0)
         throw new IllegalArgumentException(SaeFormatMessageHandler
               .getMessage("erreur.param"));

      FormatFichier refFormatExistant = find(idFormat);

      if (refFormatExistant == null) // le format renseigné n'existe pas en base
         throw new UnknownFormatException(SaeFormatMessageHandler.getMessage(
               "erreur.format.delete", idFormat));

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

   /**
    * Récupération des informations associées à un format de fichier
    * {@link FormatFichier}.
    * 
    * Si le format n'est pas trouvé en base, on renvoie null et pas une
    * exception typée pour des contraintes techniques de liées au cache Guava
    * réalisées dans la couche service
    * 
    * @param idFormat
    *           Identifiant du format de fichier à rechercher - paramètre
    *           obligatoire
    * @return Un objet {@link FormatFichier} qui représente le format de fichier
    *         trouvé
    * */
   public final FormatFichier find(String idFormat) {

      if (StringUtils.isBlank(idFormat)) {
         throw new IllegalArgumentException(SaeFormatMessageHandler.getMessage(
               "erreur.param.obligatoire.null", idFormat));
      }

      ColumnFamilyResult<String, String> result = referentielFormatDao
            .getCfTmpl().queryColumns(idFormat);

      FormatFichier refFormat = getRefFormatFromResult(result, idFormat);

      return refFormat;

   }

   /**
    * Construction d'un objet {@link FormatFichier} à  partir du résultat de la
    * requête
    * 
    * @param result
    *           {@link ColumnFamilyResult}
    * @param idFormat
    *           Identifiant du format de fichier à supprimer - paramètre
    *           obligatoire
    * @return {@link FormatFichier}
    * @throws UnknownFormatException
    *            : Erreur levée quand le format demandé n’existe pas au sein du
    *            référentiel
    */
   private FormatFichier getRefFormatFromResult(
         ColumnFamilyResult<String, String> result, String idFormat) {

      // pour un besoin lié au cache GUAVA
      FormatFichier refFormat = null;

      if (result != null && result.hasResults()) {

         refFormat = new FormatFichier();

         // refFormat.setIdFormat(result.getKey());
         refFormat.setIdFormat(idFormat);

         // typeMime et extension peut être null
         String typeMime = result.getString(Constantes.COL_TYPEMIME);
         if (!StringUtils.isBlank(typeMime))
            refFormat.setTypeMime(typeMime);

         String extension = result.getString(Constantes.COL_EXTENSION);
         if (!StringUtils.isBlank(extension))
            refFormat.setExtension(extension);

         refFormat.setDescription(result.getString(Constantes.COL_DESCRIPTION));

         refFormat.setVisualisable(result
               .getBoolean(Constantes.COL_VISUALISABLE));

         refFormat.setValidator(result.getString(Constantes.COL_VALIDATOR));

         refFormat.setIdentificateur(result
               .getString(Constantes.COL_IDENTIFIEUR));

      }
      // Erreur levée quand le format demandé n’existe pas au sein du
      // référentiel

      return refFormat;

   }

   /**
    * Récupération de tous les formats de fichier présents dans le référentiel.
    * 
    * 
    * @return Une liste d’objet {@link FormatFichier} représentant les formats
    *         présents dans le référentiel
    */
   public final List<FormatFichier> findAll() {

      try {
         BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

         RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
               .createRangeSlicesQuery(referentielFormatDao.getKeyspace(),
                     StringSerializer.get(), StringSerializer.get(),
                     bytesSerializer);

         rangeSlicesQuery.setColumnFamily(referentielFormatDao
               .getColumnFamilyName());
         rangeSlicesQuery.setRange("", "", false, MAX_FIND_RESULT);
         QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
               .execute();

         // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
         // l'utiliser
         QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
         ColumnFamilyResultWrapper<String, String> result = converter
               .getColumnFamilyResultWrapper(queryResult, StringSerializer
                     .get(), StringSerializer.get(), bytesSerializer);

         HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
               result);

         List<FormatFichier> list = new ArrayList<FormatFichier>();
         for (ColumnFamilyResult<String, String> row : resultIterator) {
            if (row != null && row.hasResults()) {

               FormatFichier referentielFormat = getRefFormatFromResult(row,
                     null);
               list.add(referentielFormat);
            }
         }

         return list;
      } catch (Exception except) {
         throw new ReferentielRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.impossible.recup.info"), except);
      }
   }

}
