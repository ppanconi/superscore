package domainapp.dom.superscore;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoleRepository;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

import com.google.common.collect.Lists;

import domainapp.dom.simple.SimpleObject;
import domainapp.dom.simple.SimpleObjects;
import domainapp.dom.simple.SimpleObjects.CreateDomainEvent;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Team.class
)
@DomainServiceLayout(
        menuOrder = "8"
)
public class Teams {
	
	//region > title
    public TranslatableString title() {
        return TranslatableString.tr("Teams");
    }
    //endregion

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Team> listAll() {
        return container.allInstances(Team.class);
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<Teams> {
        public CreateDomainEvent(final Teams source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public Team create(
            final @ParameterLayout(named="Player 1") ApplicationUser player1,
            final @ParameterLayout(named="Player 2") ApplicationUser player2
            ) {
    	
    	Team team = container.uniqueMatch(new QueryDefault<>(Team.class, "findByPlayers", "playerA", player1, "playerB", player2));
    	if (team == null) {
	        final Team obj = container.newTransientInstance(Team.class);
	        obj.setPlayer1(player1);
	        obj.setPlayer2(player2);
	        container.persistIfNotAlready(obj);
	        return obj;
    	} else
    		return team;
    }

    //endregion

    public List<ApplicationUser> autoComplete0Create(final String search) {
    	 return getSuperscoreUsers(search);
    }
    
    public List<ApplicationUser> autoComplete1Create(final String search) {
    	return getSuperscoreUsers(search);
    }
    	
    @Programmatic
    public List<ApplicationUser> getSuperscoreUsers(final String search) {
    	List<ApplicationUser> l = Lists.newArrayList();
    	ApplicationRole supercoreRole = applicationRoleRepository.findByName("superscore-regular-user");
    	
    	List<ApplicationUser> users = applicationUserRepository.autoComplete(search);
    	for (ApplicationUser applicationUser : users) {
			if (applicationUser.getRoles().contains(supercoreRole)) 
				l.add(applicationUser);
		}
    	
    	return l;
    }	
    
    public String validateCreate(
            final @ParameterLayout(named="Player 1") ApplicationUser player1,
            final @ParameterLayout(named="Player 2") ApplicationUser player2
            ) {
    	if (player1 == player2)
    		return "Select different player for the team";
    	return null;
    }
    //region > injected services
    
    // {{ autoComplete (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @MemberOrder(sequence = "2")
	public List<Team> findByNames(final String search) {
		if (search != null && search.length() > 0) {
			String[] parts = search.split(" ");
			if (parts.length > 1) {
				return findByNames(parts[0].trim(), parts[1].trim());
			} else {
				return findByName(parts[0].trim());
			}
        }
        return Lists.newArrayList();
	}
	// }}

	@Programmatic
    public List<Team> findByName(final String search) {
        final String regex = String.format("(?i).*%s.*", search.replace("*", ".*").replace("?", "."));
        return container.allMatches(new QueryDefault<>(
                Team.class,
                "findByPlayerName", "regex", regex));
    }
	
	@Programmatic
    public List<Team> findByNames(String name1, String name2) {
		final String regex1 = String.format("(?i).*%s.*", name1.replace("*", ".*").replace("?", "."));
		final String regex2 = String.format("(?i).*%s.*", name2.replace("*", ".*").replace("?", "."));
        return container.allMatches(new QueryDefault<>(
                Team.class,
                "findByPlayersName", "regex1", regex1, "regex2", regex2));
    }

    @javax.inject.Inject 
    DomainObjectContainer container;
    
    @javax.inject.Inject 
    ApplicationUserRepository applicationUserRepository;
    
    @javax.inject.Inject 
    ApplicationRoleRepository applicationRoleRepository;
    //endregion

}
