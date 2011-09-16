package com.docubase.dfce.toolkit.client.administration;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.exception.ObjectAlreadyExistsException;
import net.docubase.toolkit.model.user.User;
import net.docubase.toolkit.service.Authentication;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractBaseTestCase;
import com.docubase.dfce.toolkit.client.AbstractDFCEToolkitClientTest;

public class UserAdministrationClientTest extends AbstractDFCEToolkitClientTest {
    private User user;
    String userNameRandom = "userName" + UUID.randomUUID();
    String password = "password";

    @Before
    public void before() {
	Authentication.closeSession();
	Authentication.openSession(AbstractBaseTestCase.ADM_LOGIN,
		AbstractBaseTestCase.ADM_PASSWORD,
		AbstractBaseTestCase.SERVICE_URL);
    }

    @Test
    public void testCreateUser() throws ObjectAlreadyExistsException {
	Set<String> roles = new HashSet<String>();
	roles.add("ROLE_USER");

	User user = ServiceProvider.getUserAdministrationService().createUser(
		userNameRandom, password, roles);

	assertNotNull(user);
	assertEquals(userNameRandom, user.getLogin());
	Authentication.openSession(userNameRandom, password,
		AbstractBaseTestCase.SERVICE_URL);
	assertTrue(Authentication.isSessionActive());
    }

    @Test
    public void testDisableUser() throws ObjectAlreadyExistsException {
	user = ServiceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    Set<String> roles = new HashSet<String>();
	    roles.add("ROLE_ADMIN");
	    user = ServiceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, roles);
	}
	User disableUser = ServiceProvider.getUserAdministrationService()
		.disableUser(user);

	assertFalse(disableUser.isEnabled());
    }

    @Test
    public void testEnableUser() throws ObjectAlreadyExistsException {
	user = ServiceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    Set<String> roles = new HashSet<String>();
	    roles.add("ROLE_ADMIN");
	    user = ServiceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, roles);
	}
	User disableUser = ServiceProvider.getUserAdministrationService()
		.enableUser(user);

	assertTrue(disableUser.isEnabled());
    }

    @Test
    public void testLoadUser() throws ObjectAlreadyExistsException {
	Set<String> roles = new HashSet<String>();
	roles.add("ROLE_ADMIN");
	String userName = UUID.randomUUID().toString();
	user = ServiceProvider.getUserAdministrationService().createUser(
		userName, password, roles);
	User loadUser = ServiceProvider.getUserAdministrationService()
		.loadUser(userName);

	assertNotNull(loadUser);
    }

    @Test
    public void testUpdateUserPassword() throws ObjectAlreadyExistsException {
	user = ServiceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    Set<String> roles = new HashSet<String>();
	    roles.add("ROLE_ADMIN");
	    user = ServiceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, roles);
	}
	ServiceProvider.getUserAdministrationService().updateUserPassword(user,
		"newPassword");
	Authentication.closeSession();
	Authentication.openSession(userNameRandom, "newPassword",
		AbstractBaseTestCase.SERVICE_URL);
	assertTrue(Authentication.isSessionActive());
    }
}
