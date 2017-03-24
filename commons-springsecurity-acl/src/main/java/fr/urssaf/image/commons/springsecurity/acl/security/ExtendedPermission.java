package fr.urssaf.image.commons.springsecurity.acl.security;

import org.apache.log4j.Logger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public final class ExtendedPermission extends BasePermission {

   private static final Logger LOG = Logger.getLogger(ExtendedPermission.class);

   private static final long serialVersionUID = 1L;

   public static final Permission RULE_AUTHOR = new ExtendedPermission(32, 'A');// 32

   private ExtendedPermission(int mask, char code) {
      super(mask, code);
      LOG.debug(this.getPattern() + ":" + this.getMask());

   }

}
