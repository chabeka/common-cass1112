/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.commons;

/**
 * TODO (AC75095028) Description du type
 */
public enum TraceFieldsName {
                             /** Date de création de la trace */
                             COL_TIMESTAMP("timestamp"),

                             /** code de l'événement */
                             COL_CODE_EVT("codeEvt"),

                             /** code du contrat de service */
                             COL_CONTRAT_SERVICE("cs"),

                             /** identifiant utilisateur */
                             COL_LOGIN("login"),

                             /** Le ou les PAGMS */
                             COL_PAGMS("pagms"),

                             /** informations supplémentaires */
                             COL_INFOS("infos"),

                             /** Contexte de l'événement */
                             COL_CONTEXTE("contexte"),

                             /** trace d'erreur */
                             COL_STACKTRACE("stacktrace"),

                             /** nom de la Column Family */
                             REG_TECHNIQUE_CFNAME("TraceRegTechnique"),

                             /** action */
                             ACTION("action");

  private String name;

  /**
   * @param name
   * @param ordinal
   */
  TraceFieldsName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
