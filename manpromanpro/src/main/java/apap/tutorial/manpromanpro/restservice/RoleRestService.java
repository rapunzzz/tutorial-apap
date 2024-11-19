package apap.tutorial.manpromanpro.restservice;

import java.util.List;

import apap.tutorial.manpromanpro.model.Role;

public interface RoleRestService {
    List<Role> getAllRoles();
    Role getRoleByRoleName(String name);
}
