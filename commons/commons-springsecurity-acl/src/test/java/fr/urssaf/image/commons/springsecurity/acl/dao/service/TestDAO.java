package fr.urssaf.image.commons.springsecurity.acl.dao.service;

public interface TestDAO<T, I> {

   T findById(I identity);
}
