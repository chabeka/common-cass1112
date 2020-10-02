/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.utils;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;

/**
 * (AC75095351) Classe de conversion pagm thrift<->cql
 */
public class PagmUtils {
  /**
   * Conversion d'une entité Pagm en PagmCql
   * 
   * @param idClient
   * @param pagm
   * @return PagmCql
   */
  public static PagmCql convertPagmToPagmCql(final String idClient, final Pagm pagm) {

    final PagmCql pagmCql = new PagmCql();
    pagmCql.setIdClient(idClient);
    pagmCql.setCode(pagm.getCode());
    pagmCql.setDescription(pagm.getDescription());
    pagmCql.setPagma(pagm.getPagma());
    pagmCql.setPagmp(pagm.getPagmp());
    pagmCql.setPagmf(pagm.getPagmf());
    pagmCql.setParametres(pagm.getParametres());
    pagmCql.setCompressionPdfActive(pagm.getCompressionPdfActive());
    pagmCql.setSeuilCompressionPdf(pagm.getSeuilCompressionPdf());

    return pagmCql;
  }

  /**
   * Conversion d'une entité PagmCql en Pagm
   * 
   * @param pagmCql
   * @return Pagm
   */
  public static Pagm convertPagmCqlToPagm(final PagmCql pagmCql) {

    final Pagm pagm = new Pagm();

    pagm.setCode(pagmCql.getCode());
    pagm.setDescription(pagmCql.getDescription());
    pagm.setPagma(pagmCql.getPagma());
    pagm.setPagmp(pagmCql.getPagmp());
    pagm.setPagmf(pagmCql.getPagmf());
    pagm.setParametres(pagmCql.getParametres());
    pagm.setCompressionPdfActive(pagmCql.getCompressionPdfActive());
    pagm.setSeuilCompressionPdf(pagmCql.getSeuilCompressionPdf());

    return pagm;
  }

  /**
   * Conversion liste PagmCql en Pagm
   * 
   * @param liste
   *          PagmCql
   * @return liste Pagm
   */
  public static List<Pagm> convertListPagmCqlToListPagm(final List<PagmCql> listPagmCql) {

    final List<Pagm> listPagm = new ArrayList<>();

    for (final PagmCql pagmCql : listPagmCql) {
      listPagm.add(convertPagmCqlToPagm(pagmCql));
    }

    return listPagm;
  }

  /**
   * Conversion liste Pagm en PagmCql
   * 
   * @param liste
   *          PagmCql
   * @return liste Pagm
   */
  public static List<PagmCql> convertListPagmToListPagmCql(final List<Pagm> listPagm, final String idClient) {

    final List<PagmCql> listPagmCql = new ArrayList<>();

    for (final Pagm pagm : listPagm) {
      listPagmCql.add(convertPagmToPagmCql(idClient, pagm));
    }

    return listPagmCql;
  }

}
