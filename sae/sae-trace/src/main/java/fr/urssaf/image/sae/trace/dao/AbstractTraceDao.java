package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

/**
 * Classe abstraite
 */
public abstract class AbstractTraceDao extends AbstractDao<UUID, String> {

  /** Date de création de la trace */
  public static final String COL_TIMESTAMP = "timestamp";

  /** code de l'événement */
  public static final String COL_CODE_EVT = "codeEvt";

  /** code du contrat de service */
  public static final String COL_CONTRAT_SERVICE = "cs";

  /** identifiant utilisateur */
  public static final String COL_LOGIN = "login";

  /** Le ou les PAGMS */
  public static final String COL_PAGMS = "pagms";

  /** informations supplémentaires */
  public static final String COL_INFOS = "infos";

  /**
   * Constructeur
   * 
   * @param keyspace
   *          keyspace utilisé
   */
  public AbstractTraceDao(final Keyspace keyspace) {
    super(keyspace);
  }

  /**
   * @return le sérializer d'une colonne
   */
  @Override
  public final Serializer<String> getColumnKeySerializer() {
    return StringSerializer.get();
  }

  /**
   * @return le sérializer de la clé d'une ligne
   */
  @Override
  public final Serializer<UUID> getRowKeySerializer() {
    return UUIDSerializer.get();
  }

  /**
   * ajoute une colonne {@value #COL_CODE_EVT}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnCodeEvt(
                                       final ColumnFamilyUpdater<UUID, String> updater, final String value, final long clock) {
    addColumn(updater, COL_CODE_EVT, value, StringSerializer.get(), clock);
  }

  /**
   * ajoute une colonne {@value #COL_CONTRAT_SERVICE}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnContratService(
                                              final ColumnFamilyUpdater<UUID, String> updater, final String value, final long clock) {
    addColumn(updater,
              COL_CONTRAT_SERVICE,
              value,
              StringSerializer.get(),
              clock);
  }

  /**
   * ajoute une colonne {@value #COL_PAGMS}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnPagms(
                                     final ColumnFamilyUpdater<UUID, String> updater, final List<String> value,
                                     final long clock) {
    addColumn(updater, COL_PAGMS, value, ListSerializer.get(), clock);
  }

  /**
   * ajoute une colonne {@value #COL_LOGIN}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnLogin(
                                     final ColumnFamilyUpdater<UUID, String> updater, final String value, final long clock) {
    addColumn(updater, COL_LOGIN, value, StringSerializer.get(), clock);
  }

  /**
   * ajoute une colonne {@value #COL_INFOS}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnInfos(
                                     final ColumnFamilyUpdater<UUID, String> updater, final Map<String, Object> value,
                                     final long clock) {
    addColumn(updater, COL_INFOS, value, MapSerializer.get(), clock);
  }

  /**
   * ajoute une colonne {@value #COL_TIMESTAMP}
   * 
   * @param updater
   *          updater
   * @param value
   *          valeur de la colonne
   * @param clock
   *          horloge de la colonne
   */
  public final void writeColumnTimestamp(
                                         final ColumnFamilyUpdater<UUID, String> updater, final Date value, final long clock) {
    addColumn(updater, COL_TIMESTAMP, value, DateSerializer.get(), clock);
  }

}
