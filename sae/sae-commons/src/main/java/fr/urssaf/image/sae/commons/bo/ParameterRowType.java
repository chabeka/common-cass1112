package fr.urssaf.image.sae.commons.bo;

/**
 * Ligne de paramètre de configuration
 * 
 */
public enum ParameterRowType {


  /** Nom correspondant à la ligne contenant les paramètres de tracabilite */

  TRACABILITE {
    @Override
    public String toString() {
      return "parametresTracabilite";
    }
  },
  /** Nom correspondant à la ligne contenant les paramètres de purge de la corbeille */

  CORBEILLE {
    @Override
    public String toString() {
      return "parametresCorbeille";
    }
  },
  /**
   * Nom correspondant à la ligne contenant les paramètres du RND
   */
  RND {
    @Override
    public String toString() {
      return "parametresRnd";
    }
                              },

                              /**
                               * Nom correspondant à la ligne contenant les paramètres du RND
                               */
                              PARAMETERS {
                                @Override
                                public String toString() {
                                  return "parameters";
                                }
  };

  // parametresRnd;

  /**
   * @return le nom
   */
  public String getValue() {
    return name();
  }

  public static ParameterRowType getLabel(final String value) {
    for (final ParameterRowType e : values()) {
      if (e.toString().equals(value)) {
        return e;
      }
    }
    return null;
  }
}
