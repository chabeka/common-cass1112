/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;

/**
 * TODO (AC75095028) Description du type
 */
public class MetadataUtils {

  /**
   * Construction d'un objet {@link MetadataReference} à partir du résultat de
   * la requête
   *
   * @param result
   *          {@link ColumnFamilyResult}
   * @return {@link MetadataReference}
   */
  public static List<Metadata> getMetadataFromResult(final Map<String, Map<String, String>> map) {

    final List<Metadata> listmetadats = new ArrayList<>();
    Metadata meta = null;
    if (map != null && !map.isEmpty()) {

      for (final Entry entry : map.entrySet()) {
        final String key = (String) entry.getKey();

        meta = new Metadata();

        meta.setLongCode(key);

        final Map<String, String> ssMap = (Map<String, String>) entry.getValue();

        meta.setDescription(ssMap.get(MetadataEnum.META_DESCR.getName()));
        meta
            .setDictionaryName(ssMap.get(MetadataEnum.META_DICT_NAME.getName()));

        meta.setLabel(ssMap.get(MetadataEnum.META_LABEL));

        // NB: -1 est la valeur signifiant "non renseigné"
        if (StringUtils.isEmpty(ssMap.get(MetadataEnum.META_LENGTH.getName())) || ssMap.get(MetadataEnum.META_LENGTH.getName()) == null) {
          meta.setLength(-1);

        } else if ("-1".equals(ssMap.get(MetadataEnum.META_LENGTH.getName()))) {
          meta.setLength(-1);

        } else {
          meta.setLength(Integer.parseInt(ssMap.get(MetadataEnum.META_LENGTH.getName())));
        }

        meta.setShortCode(ssMap.get(MetadataEnum.META_SHORT_CODE.getName()));
        meta.setPattern(ssMap.get(MetadataEnum.META_PATTERN.getName()));

        final Boolean requredArchiv = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_REQ_ARCH.getName()));

        meta.setRequiredForArchival(requredArchiv);

        final Boolean requiredStor = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_REQ_STOR.getName()));
        meta.setRequiredForStorage(requiredStor);

        final Boolean leftTrim = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_LEFT_TRIM.getName()));
        meta.setLeftTrimable(leftTrim);

        final Boolean rightTrim = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_RIGHT_TRIM.getName()));
        meta.setRightTrimable(rightTrim);

        final Boolean searchable = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_SEARCH.getName()));
        meta.setSearchable(searchable);

        meta.setType(ssMap.get(MetadataEnum.META_TYPE.getName()));

        final Boolean metaArchivable = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_ARCH.getName()));
        meta.setArchivable(metaArchivable);

        final Boolean metaConsultable = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_CONSUL.getName()));
        meta.setConsultable(metaConsultable);

        final Boolean defaultConsultable = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_DEF_CONSUL.getName()));
        meta.setDefaultConsultable(defaultConsultable);

        final Boolean hasDict = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_HAS_DICT.getName()));
        meta.setHasDictionary(hasDict);

        final Boolean internal = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_INTERNAL.getName()));
        meta.setInternal(internal);

        final Boolean indexed = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_INDEXED.getName()));
        meta.setIsIndexed(indexed);

        final Boolean update = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_UPDATE.getName()));
        meta.setModifiable(update);

        final Boolean dispo = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_DISPO.getName()));
        meta.setClientAvailable(dispo);

        final Boolean transferable = Utils.getBooleanValue(ssMap.get(MetadataEnum.META_TRANSF.getName()));
        meta.setTransferable(transferable);
        listmetadats.add(meta);
      }

    }
    return listmetadats;
  }

  /**
   * @param genericTypes
   * @return
   */
  public static List<Metadata> mapGenericTypeToMetadata(final Map<String, List<GenericType>> genericTypes) {

    final List<Metadata> listmetadats = new ArrayList<>();
    final Metadata meta = null;

    if (genericTypes != null && !genericTypes.isEmpty()) {
      for (final Entry entry : genericTypes.entrySet()) {
        final List<GenericType> entries = (List<GenericType>) entry.getValue();
        for (final GenericType gtype : entries) {
          System.out.println(gtype.getColumn1());
        }
      }
    }
    return listmetadats;
  }

  /**
   * @param map
   * @return
   */
  public List<Metadata> getMetadataFromGeneicType(final Map<String, GenericType> map) {
    final List<Metadata> listmetadats = new ArrayList<>();
    Metadata meta = null;
    if (map != null && !map.isEmpty()) {
      for (final Entry entry : map.entrySet()) {
        final String key = (String) entry.getKey();

        meta = new Metadata();
      }
    }
    return listmetadats;
  }
}
