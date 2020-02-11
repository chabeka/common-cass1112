package fr.urssaf.image.sae.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

public class Dumper {

  Keyspace keyspace;

  PrintStream sysout;

  /**
   * On n'affiche que les maxValueLenght 1er caractères
   */
  public int maxValueLenght = 200;

  /**
   * Si on affiche le nom de colonne en mode "composite", on peut indiquer dans
   * ce tableau pour chaque élément du composite s'il faut l'afficher en
   * hexadécimal
   */
  public boolean[] compositeDisplayTypeMapper;

  public Dumper(final Keyspace k, final PrintStream p) {
    keyspace = k;
    sysout = p;
  }

  /**
   * Compte le nombre total de clés d'une CF
   *
   * @param CFName
   * @return
   * @throws Exception
   */
  public int getKeysCount(final String CFName) throws Exception {
    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(keyspace,
                                stringSerializer,
                                stringSerializer,
                                bytesSerializer);
    final List<String> keys = new ArrayList<>();
    rangeSlicesQuery.setColumnFamily(CFName);
    final int blockSize = RowUtils.BLOCK_SIZE_DEFAULT;
    String startKey = "";
    int total = 1;
    int count;
    int nbTotal = 0;

    do {
      rangeSlicesQuery.setRange("", "", false, 1);
      rangeSlicesQuery.setKeys(startKey, "");
      rangeSlicesQuery.setRowCount(blockSize);
      rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
          .execute();

      final OrderedRows<String, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();
      // On enlève 1, car sinon à chaque itération, la startKey serait
      // comptée deux fois.
      total += count - 1;
      nbTotal = total;
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final Row<String, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      for (final Row<String, String, byte[]> row : orderedRows) {
        keys.add(row.getKey());
        // sysout.print("\n key : " + row.getKey());
        final ColumnSlice<String, byte[]> columnSlice = row.getColumnSlice();
        final List<HColumn<String, byte[]>> columns = columnSlice.getColumns();

        nbTotal += columns.size();
        // System.out.println("Nombre de colonnes : " + columns.size());
      }
    } while (count == blockSize);
    // sysout.print("\n Total" + nbTotal);
    // sysout.print("\n Total keys" + keys.size());

    return total;
  }

}
