/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.util.UUID;

import me.prettyprint.hector.api.beans.Row;

/**
 * TODO (AC75095351) Description du type
 *
 */
public class RowUtils {

  public static boolean rowLsbHasColumns(final Row<Long, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && row.getColumnSlice().getColumns().isEmpty();
  }

  public static boolean rowBbbHasColumns(final Row<byte[], byte[], byte[]> row) {
    return row != null && row.getColumnSlice() != null && row.getColumnSlice().getColumns().isEmpty();
  }

  public static boolean rowSsbHasColumns(final Row<String, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && row.getColumnSlice().getColumns().isEmpty();
  }

  // UUID
  public static boolean rowUsbHasColumns(final Row<UUID, String, byte[]> row) {
    return row != null && row.getColumnSlice() != null && row.getColumnSlice().getColumns().isEmpty();
  }
}
