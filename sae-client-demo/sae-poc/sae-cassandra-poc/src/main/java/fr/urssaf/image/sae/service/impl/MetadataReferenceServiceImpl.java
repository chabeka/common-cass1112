/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.mapping.Result;

import fr.urssaf.image.sae.dao.IMetadataDAO;
import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;
import fr.urssaf.image.sae.service.IMetadataReferenceService;
import fr.urssaf.image.sae.utils.MetadataUtils;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * TODO (AC75095028) Description du type
 */
@Service
public class MetadataReferenceServiceImpl implements IMetadataReferenceService {

  @Autowired
  IMetadataDAO metadatadao;

  /**
   * Récupère toutes les métadonnées recherchables
   *
   * @return la liste des métadonnées recherchables
   */
  @Override
  public List<Metadata> findMetadatasRecherchables() {
    final Result<Metadata> result = metadatadao.findAll();
    final List<Metadata> list = new ArrayList<Metadata>();
    for (final Metadata metadata : result) {
      if (metadata.isSearchable()) {
        list.add(metadata);
      }
    }
    return list;
  }

  /**
   * Récupère la liste des métadonnées consultables
   *
   * @return la liste des métadonnées consultables
   */
  @Override
  public List<Metadata> findMetadatasConsultables() {
    final Result<Metadata> result = metadatadao.findAll();
    final List<Metadata> list = new ArrayList<Metadata>();
    for (final Metadata metadata : result) {
      if (metadata.isConsultable()) {
        list.add(metadata);
      }
    }
    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Result<Metadata> findAll() {
    return metadatadao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Metadata> saveAll(final List<Metadata> metas) {
    return metadatadao.saveAll(metas);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Metadata> findAllMetadata() {
    final ResultSet result = metadatadao.findAllMatadatas();
    final Map<String, Map<String, String>> map = new HashedMap();
    final Iterator iter = result.iterator();
    while (iter.hasNext()) {

      // Extraction de la clé

      String strArrayKey = "";
      String value = "";
      final Row row = (Row) iter.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      final ByteBuffer blobAsBytesKey = row.getBytes("key");
      final byte[] arrayKey = blobAsBytesKey.array();
      try {
        strArrayKey = new String(arrayKey, "UTF-8");
      }
      catch (final UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      // extraction du nom de la colonne
      final String columnName = row.getString("column1");

      // extraction de la value
      final ByteBuffer blobAsBytes = row.getBytes("value");
      final String valueT = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      final byte[] arrayValue = blobAsBytes.array();
      try {
        String str1 = "";
        if (arrayValue.length == 1) {
          final byte firstValue = arrayValue[0];
          str1 = Byte.toString(firstValue);
          value = new String(arrayValue, "UTF-8");
        } else if ("length".equals(columnName)) {
          final ByteBuffer bb = ByteBuffer.wrap(arrayValue);
          bb.order(ByteOrder.LITTLE_ENDIAN);
          final int ss = bb.getInt();
          value = Integer.toString(ss);
        } else {
          value = new String(arrayValue, "UTF-8");

        }

      }
      catch (final UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      if (map.containsKey(strArrayKey)) {
        map.get(strArrayKey).put(columnName, value);
      } else {
        final Map<String, String> ssMap = new HashedMap();
        ssMap.put(columnName, value);
        map.put(strArrayKey, ssMap);
      }
    }
    // liste des metadata
    final List<Metadata> datas = MetadataUtils.getMetadataFromResult(map);
    return datas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Metadata> findAllByGType() {
    final List<GenericType> result = metadatadao.findAllGenericType();
    final Map<String, Map<String, GenericType>> map = new HashedMap();

    final String strKey = "";

    for (final GenericType gtype : result) {
      // Déserialisation de la colonne
      // strKey = StringSerializer.get().fromByteBuffer(gtype.getKey());
      final Map<String, GenericType> ssMap = new HashedMap();
      ssMap.put(gtype.getColumn1(), gtype);
      if (map.containsKey(strKey)) {
        // map.get(strKey).
      } else {
        final List<GenericType> listGType = new ArrayList<>();
        listGType.add(gtype);
        map.put(strKey, ssMap);
      }
    }
    // liste des metadata
    // final List<Metadata> datas = MetadataUtils.mapGenericTypeToMetadata(map);
    // return datas;
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void insertGType(final GenericType gtype, final Object daoType) {
    metadatadao.insert(gtype);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Metadata metadata) {
    metadatadao.delete(metadata);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteById(final String id) {
    metadatadao.deleteById(id); 
  }

}
