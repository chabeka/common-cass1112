package fr.urssaf.image.sae.rnd.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.sae.rnd.dao.support.facade.RndSupportFacade;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.service.RndService;

/**
 * Service de récupération des propriétés d'un type de document
 * 
 * 
 */
@Service
public class RndServiceImpl implements RndService {

  /**
   * Cache Rnd
   */
  private final LoadingCache<String, TypeDocument> cacheRnd;

  @Autowired
  private RndSupportFacade rndSupport;

  /**
   * @param dureeCache
   *           la durée du cache définie dans le fichier sae-config Construit
   *           un objet de type {@link RndServiceImpl}
   */
  @Autowired
  public RndServiceImpl(@Value("${sae.rnd.cache}") final int dureeCache) {

    cacheRnd = CacheBuilder.newBuilder().refreshAfterWrite(dureeCache,
                                                           TimeUnit.MINUTES).build(new CacheLoader<String, TypeDocument>() {

                                                             @Override
                                                             public TypeDocument load(final String codeRnd) {
                                                               return rndSupport.getRnd(codeRnd);
                                                             }

                                                           });
  }

  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public final String getCodeActivite(final String codeRnd)
      throws CodeRndInexistantException {
    try {
      final TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getCodeActivite();
    } catch (final InvalidCacheLoadException e) {
      throw new CodeRndInexistantException("Le code RND " + codeRnd
                                           + " n'est pas autorisé à l'archivage (code inexistant).", e
                                           .getCause());
    }

  }

  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public final String getCodeFonction(final String codeRnd)
      throws CodeRndInexistantException {
    try {
      final TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getCodeFonction();
    } catch (final InvalidCacheLoadException e) {
      throw new CodeRndInexistantException("Le code RND " + codeRnd
                                           + " n'est pas autorisé à l'archivage (code inexistant).", e
                                           .getCause());
    }
  }

  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public final int getDureeConservation(final String codeRnd)
      throws CodeRndInexistantException {
    try {
      final TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getDureeConservation();
    } catch (final InvalidCacheLoadException e) {
      throw new CodeRndInexistantException("Le code RND " + codeRnd
                                           + " n'est pas autorisé à l'archivage (code inexistant).", e
                                           .getCause());
    }

  }

  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public final TypeDocument getTypeDocument(final String codeRnd)
      throws CodeRndInexistantException {
    try {
      return cacheRnd.getUnchecked(codeRnd);
    } catch (final InvalidCacheLoadException e) {
      throw new CodeRndInexistantException("Le code RND " + codeRnd
                                           + " n'est pas autorisé à l'archivage (code inexistant).", e
                                           .getCause());
    }
  }

  @Override
  @SuppressWarnings("PMD.PreserveStackTrace")
  public final boolean isCloture(final String codeRnd)
      throws CodeRndInexistantException {
    try {
      final TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.isCloture();
    } catch (final InvalidCacheLoadException e) {
      throw new CodeRndInexistantException("Le code RND " + codeRnd
                                           + " n'est pas autorisé à l'archivage (code inexistant).", e
                                           .getCause());
    }
  }

}
