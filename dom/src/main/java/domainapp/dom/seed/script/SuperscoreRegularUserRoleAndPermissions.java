package domainapp.dom.seed.script;

import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.seed.scripts.AbstractRoleAndPermissionsFixtureScript;

public class SuperscoreRegularUserRoleAndPermissions extends AbstractRoleAndPermissionsFixtureScript {

	public static final String ROLE_NAME = "superscore-regular-user";
	
    protected SuperscoreRegularUserRoleAndPermissions() {
		super(ROLE_NAME, "Regular user of the superscore application");
	}
	
    @Override
	protected void execute(ExecutionContext executionContext) {
    	newPackagePermissions(
    			ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                "domainapp.dom.superscore"
    	);
    	
	}

}
