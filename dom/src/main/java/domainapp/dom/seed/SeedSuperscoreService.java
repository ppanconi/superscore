package domainapp.dom.seed;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import domainapp.dom.seed.script.SeedSuperscoreUsersAndRolesFixtureScript;

@DomainService(
        nature = NatureOfService.DOMAIN
)
@DomainServiceLayout(
        menuOrder = "1100" // not visible, but determines the order initialized
)
public class SeedSuperscoreService {
    // //////////////////////////////////////

    //region > init
    @Programmatic
    @PostConstruct
    public void init() {
        fixtureScripts.runFixtureScript(new SeedSuperscoreUsersAndRolesFixtureScript(), null);
    }
    //endregion

    //region  >  (injected)
    @Inject
    FixtureScripts fixtureScripts;
    //endregion

}
