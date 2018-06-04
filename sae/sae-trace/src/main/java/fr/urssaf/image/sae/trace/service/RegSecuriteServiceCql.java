/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;

/**
 * Services du registre de sécurité
 */
public interface RegSecuriteServiceCql extends
                                        RegService<TraceRegSecuriteCql, TraceRegSecuriteIndexCql> {

}
