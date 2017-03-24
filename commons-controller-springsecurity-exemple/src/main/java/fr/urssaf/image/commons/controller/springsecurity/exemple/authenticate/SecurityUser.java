package fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {

   private static final long serialVersionUID = 1L;

   private final String username;

   private final String password;
   
   private final Collection<GrantedAuthority> authories;

   public SecurityUser(String username, String password,String... roles) {

      this.username = username;
      this.password = password;
      this.authories = AuthorityUtils.createAuthorityList(roles);
      
   }

   @Override
   public Collection<GrantedAuthority> getAuthorities() {
      return authories;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

}
