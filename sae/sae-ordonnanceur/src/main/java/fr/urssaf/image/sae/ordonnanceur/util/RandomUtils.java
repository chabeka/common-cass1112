package fr.urssaf.image.sae.ordonnanceur.util;

import java.security.SecureRandom;

import org.apache.commons.lang.Validate;

/**
 * Classe utilitaire pour les nombres aléatoires
 * 
 * 
 */
public final class RandomUtils {

  private RandomUtils() {

  }

  /**
   * Retourne un nombre aléatoire dans une intervalle
   * 
   * @param min
   *           début de l'intervalle
   * @param max
   *           fin de l'intervalle
   * @return nombre aléatoire compris entre les valeurs min et max incluses
   */
  public static int random(final int min, final int max) {

    Validate.isTrue(max > min, "max doit être supérieur à min");

    final SecureRandom random = new SecureRandom();

    final int randomNumber = random.nextInt(max - min + 1) + min;

    return randomNumber;

  }
}
