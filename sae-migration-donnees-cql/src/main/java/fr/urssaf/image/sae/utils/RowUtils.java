/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.util.UUID;

import me.prettyprint.hector.api.beans.Row;

/**
 * Gestion des colonnes vides
 */

public class RowUtils {
  public final static int MAX_SIZE_COLUMN = 15598424;

  public final static int BLOCK_SIZE_DEFAULT = 100;

  public final static int BLOCK_SIZE_TRACE_REG_TECHNIQUE = 10;

  public final static int BLOCK_SIZE_TRACE_REG_SECURITE = 100;

  public final static int BLOCK_SIZE_TRACE_REG_EXPLOITATION = 100;

  public final static int BLOCK_SIZE_TRACE_JOURNAL_EVT = 100;

  public final static int BLOCK_SIZE_TRACE_DESTINATAIRE = 100;

  public final static int BLOCK_SIZE_JOB_INSTANCE = 100;

  public final static int BLOCK_SIZE_JOB_INSTANCE_TO_JOB_EXECUTION = 100;

  public final static int BLOCK_SIZE_JOB_INSTANCE_BY_NAME = 100;

  public final static int BLOCK_SIZE_JOB_EXECUTION = 100;

  public final static int BLOCK_SIZE_JOB_EXECUTIONS = 100;

  public final static int BLOCK_SIZE_JOB_EXECUTIONS_RUNNING = 100;

  public final static int BLOCK_SIZE_JOB_STEP = 100;

  public final static int BLOCK_SIZE_JOB_STEPS = 100;

  public final static int BLOCK_SIZE_JOB_HISTORY = 100;

  public final static int BLOCK_SIZE_JOB_QUEUE = 100;

  public final static int BLOCK_SIZE_JOB_REQUEST = 100;


  public static boolean rowLsbHasColumns(final Row<Long, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && !row.getColumnSlice().getColumns().isEmpty();
  }

  public static boolean rowBbbHasColumns(final Row<byte[], byte[], byte[]> row) {
    return row != null && row.getColumnSlice() != null && !row.getColumnSlice().getColumns().isEmpty();
  }

  public static boolean rowSsbHasColumns(final Row<String, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && !row.getColumnSlice().getColumns().isEmpty();
  }

  // UUID
  public static boolean rowUsbHasColumns(final Row<UUID, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && !row.getColumnSlice().getColumns().isEmpty();
  }
}
