/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmfSupportFacade;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmfService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Implémentation de l'interface {@link SaePagmfService} décrivant les <br>
 * proposées par le service Pagmf.
 * (Thrift et Cql)
 */
@Service
public class SaePagmfServiceImpl implements SaePagmfService {


  private final PagmfSupportFacade pagmfSupportFacade;



  /**
   * Constructeur
   * 
   * @param pagmfSup
   *           la classe support
   * @param clockSupport
   *           l'horloge {@link JobClockSupport}
   */
  @Autowired
  public SaePagmfServiceImpl(final PagmfSupportFacade pagmfSupportFacade) {

    this.pagmfSupportFacade = pagmfSupportFacade;
  }

  @Override
  public final void addPagmf(final Pagmf pagmf) {

    // la vérification des paramètres obligatoires est faite en aspect.
    pagmfSupportFacade.create(pagmf);
  }

  @Override
  public final void deletePagmf(final String codePagmf) {

    pagmfSupportFacade.delete(codePagmf);
  }

  @Override
  public final Pagmf getPagmf(final String code) {

    final Pagmf pagmf = pagmfSupportFacade.find(code);

    if (pagmf == null) {
      throw new PagmfNotFoundException(ResourceMessagesUtils.loadMessage(
                                                                         "erreur.no.pagmf.found", code));
    }

    return pagmf;
  }

  @Override
  public final List<Pagmf> getAllPagmf() {

    return pagmfSupportFacade.findAll();

  }

}
