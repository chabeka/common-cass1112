/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;

/**
 * @author AC75007648
 */
public class JobHistoryMapper {

  static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;

  public static List<JobHistory> mapJobHistoryCqlToThrift(final JobHistoryCql jobHistoryCql) {
    final List<JobHistory> jobHistorys = new ArrayList<JobHistory>();

    final Set<Entry<UUID, String>> tracesSet = jobHistoryCql.getTrace().entrySet();
    final Iterator<Entry<UUID, String>> taraceIterator = tracesSet.iterator();

    while (taraceIterator.hasNext()) {
      final JobHistory jobHistory = new JobHistory();
      final Entry<UUID, String> trace = taraceIterator.next();
      final long time = getTimeFromUUID(trace.getKey());
      final Date date = new Date(time);
      jobHistory.setDate(date);
      jobHistory.setTrace(trace.getValue());
      jobHistorys.add(jobHistory);
    }
    return jobHistorys;
  }

  public static long getTimeFromUUID(final UUID uuid) {
    return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
  }

  public static List<JobHistory> mapListJobHistoryCqlToListJobHistory(final JobHistoryCql jobHistoryCql) {
    final List<JobHistory> jobsHistory = new ArrayList<>();

    if (jobHistoryCql != null) {
      jobsHistory.addAll(JobHistoryMapper.mapJobHistoryCqlToThrift(jobHistoryCql));
    }
    return jobsHistory;
  }

}
