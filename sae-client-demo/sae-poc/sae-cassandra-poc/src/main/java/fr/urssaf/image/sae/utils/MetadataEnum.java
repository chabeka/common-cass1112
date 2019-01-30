package fr.urssaf.image.sae.utils;

/**
 *  TODO ("AC75095028) Description du fichier
 */

/**
 * TODO ("AC75095028) Description du type
 */
public enum MetadataEnum {

                          META_SHORT_CODE("sCode"),

                          META_TYPE("type"),

                          META_REQ_ARCH("reqArch"),

                          META_REQ_STOR("reqStor"),

                          META_LENGTH("length"),

                          META_PATTERN("pattern"),

                          META_CONSUL("cons"),

                          META_DEF_CONSUL("defCons"),

                          META_SEARCH("search"),

                          META_INTERNAL("int"),

                          META_ARCH("arch"),

                          META_LABEL("label"),

                          META_DESCR("descr"),

                          META_HAS_DICT("hasDict"),

                          META_DICT_NAME("dictName"),

                          META_INDEXED("index"),

                          META_DISPO("dispo"),

                          META_LEFT_TRIM("leftTrim"),

                          META_RIGHT_TRIM("rightTrim"),

                          METADATA_CFNAME("Metadata"),

                          META_UPDATE("update"),

                          META_TRANSF("transf");

  String name;

  MetadataEnum(final String name) {
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

}
