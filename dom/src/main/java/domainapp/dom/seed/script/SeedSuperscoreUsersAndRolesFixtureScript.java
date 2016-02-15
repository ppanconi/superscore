/**
 * 
 */
package domainapp.dom.seed.script;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.isisaddons.module.security.seed.scripts.IsisModuleSecurityRegularUserRoleAndPermissions;

/**
 * @author panks
 *
 */
public class SeedSuperscoreUsersAndRolesFixtureScript extends FixtureScript {

	@Override
	protected void execute(ExecutionContext executionContext) {
		executionContext.executeChild(this, new SuperscoreRegularUserRoleAndPermissions());
	}

}
