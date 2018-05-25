/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;

/**
 * Services du registre de surveillance technique
 */
public interface IRegTechniqueServiceCql extends
                                         RegService<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> {
}
