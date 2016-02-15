package domainapp.app.services.registration;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoleRepository;

@DomainService(nature=NatureOfService.DOMAIN)
public class AppUserRegistrationService 
	extends org.isisaddons.module.security.userreg.SecurityModuleAppUserRegistrationServiceAbstract {

	@Override
	protected Set<ApplicationRole> getAdditionalInitialRoles() {
		return Collections.emptySet();
	}

	@Override
	protected ApplicationRole getInitialRole() {
		return findRole("isis-module-security-regular-user");
	}

	private ApplicationRole findRole(final String roleName) {
        return applicationRoleRepository.findByName(roleName);
    }
    @Inject
    private ApplicationRoleRepository applicationRoleRepository;
}
