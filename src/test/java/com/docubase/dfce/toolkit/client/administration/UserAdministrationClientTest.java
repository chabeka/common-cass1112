package com.docubase.dfce.toolkit.client.administration;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.model.user.User;
import net.docubase.toolkit.model.user.UserPermission;

import org.junit.Before;
import org.junit.Test;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.docubase.dfce.toolkit.AbstractTestBase;

public class UserAdministrationClientTest extends AbstractTestBase {
    private String GROUP_NAME = "groupName" + UUID.randomUUID();
    private User user;
    private final String userNameRandom = "userName" + UUID.randomUUID();
    private final String password = "password";
    private final Set<String> baseNames = new HashSet<String>();

    @Before
    public void before() throws ObjectAlreadyExistsException {
	serviceProvider.disconnect();
	serviceProvider.connect(AbstractTestBase.ADM_LOGIN,
		AbstractTestBase.ADM_PASSWORD, AbstractTestBase.SERVICE_URL);

	Set<UserPermission> permissions = new HashSet<UserPermission>();
	permissions.add(UserPermission.BASE_CREATE);
	GROUP_NAME = "groupName" + UUID.randomUUID();
	serviceProvider.getUserAdministrationService().createUserGroup(
		GROUP_NAME, permissions, baseNames);
    }

    @Test
    public void testCreateUser() throws ObjectAlreadyExistsException {
	User user = serviceProvider.getUserAdministrationService().createUser(
		userNameRandom, password, GROUP_NAME);

	assertNotNull(user);
	assertEquals(userNameRandom, user.getLogin());
	serviceProvider.connect(userNameRandom, password,
		AbstractTestBase.SERVICE_URL);
	assertTrue(serviceProvider.isSessionActive());
    }

    @Test
    public void testDisableUser() throws ObjectAlreadyExistsException {
	user = serviceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    user = serviceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, GROUP_NAME);
	}
	User disableUser = serviceProvider.getUserAdministrationService()
		.disableUser(user);

	assertFalse(disableUser.isEnabled());
    }

    @Test
    public void testEnableUser() throws ObjectAlreadyExistsException {
	user = serviceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    user = serviceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, GROUP_NAME);
	}
	User disableUser = serviceProvider.getUserAdministrationService()
		.enableUser(user);

	assertTrue(disableUser.isEnabled());
    }

    @Test
    public void testLoadUser() throws ObjectAlreadyExistsException {
	String userName = UUID.randomUUID().toString();
	user = serviceProvider.getUserAdministrationService().createUser(
		userName, password, GROUP_NAME);
	User loadUser = serviceProvider.getUserAdministrationService()
		.loadUser(userName);

	assertNotNull(loadUser);
    }

    @Test
    public void testUpdateUserPassword() throws ObjectAlreadyExistsException {
	user = serviceProvider.getUserAdministrationService().loadUser(
		userNameRandom);
	if (user == null) {
	    user = serviceProvider.getUserAdministrationService().createUser(
		    userNameRandom, password, GROUP_NAME);
	}
	serviceProvider.getUserAdministrationService().updateUserPassword(user,
		"newPassword");
	serviceProvider.disconnect();
	serviceProvider.connect(userNameRandom, "newPassword",
		AbstractTestBase.SERVICE_URL);
	assertTrue(serviceProvider.isSessionActive());
    }
}
