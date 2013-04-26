package fr.urssaf.image.sae.rnd.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
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

   private RndSupport rndSupport;

   /**
    * @param dureeCache
    *           la durée du cache définie dans le fichier sae-config Construit
    *           un objet de type {@link RndServiceImpl}
    */
   @Autowired
   public RndServiceImpl(@Value("${sae.rnd.cache}") int dureeCache) {

      cacheRnd = CacheBuilder.newBuilder().refreshAfterWrite(dureeCache,
            TimeUnit.MINUTES).build(new CacheLoader<String, TypeDocument>() {

         @Override
         public TypeDocument load(String codeRnd)
               throws CodeRndInexistantException {
            return rndSupport.getRnd(codeRnd);
         }

      });
   }

   @Override
   public final String getCodeActivite(String codeRnd)
         throws CodeRndInexistantException {

      TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getCodeActivite();

   }

   @Override
   public final String getCodeFonction(String codeRnd)
         throws CodeRndInexistantException {

      TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getCodeFonction();

   }

   @Override
   public final int getDureeConservation(String codeRnd)
         throws CodeRndInexistantException {

      TypeDocument typeDoc = cacheRnd.getUnchecked(codeRnd);
      return typeDoc.getDureeConservation();

   }

   @Override
   public final TypeDocument getTypeDocument(String codeRnd)
         throws CodeRndInexistantException {

      return cacheRnd.getUnchecked(codeRnd);

   }

}
