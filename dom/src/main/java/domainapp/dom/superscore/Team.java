package domainapp.dom.superscore;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.IsisApplibModule.ActionDomainEvent;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import domainapp.dom.simple.SimpleObject;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Team"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
//        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name = "findByPlayers", language = "JDOQL",
            value = "SELECT "
                    + "FROM domainapp.dom.superscore.Team "
                    + "WHERE (player1 == :playerA || player1 == :playerB) "
                    + "&& (player2 == :playerA || player2 == :playerB)"),
    
    @javax.jdo.annotations.Query(
            name = "findByPlayerName", language = "JDOQL",
            value = "SELECT "
                    + "FROM domainapp.dom.superscore.Team "
                    + "WHERE player1.username.matches(:regex)"
                    + " || player1.familyName.matches(:regex)"
                    + " || player1.givenName.matches(:regex)"
                    + " || player1.knownAs.matches(:regex)"
                    + " || player1.emailAddress.matches(:regex)"
                    + " || player2.username.matches(:regex)"
                    + " || player2.familyName.matches(:regex)"
                    + " || player2.givenName.matches(:regex)"
                    + " || player2.knownAs.matches(:regex)"
                    + " || player2.emailAddress.matches(:regex)"),
    
    @javax.jdo.annotations.Query(
            name = "findByPlayersName", language = "JDOQL",
            value = "SELECT "
                    + "FROM domainapp.dom.superscore.Team "
                    + "WHERE "
                    + " (player1.username.matches(:regex1)"
                    + " || player1.familyName.matches(:regex1)"
                    + " || player1.givenName.matches(:regex1)"
                    + " || player1.knownAs.matches(:regex1)"
                    + " || player1.emailAddress.matches(:regex1)"
                    + " || player2.username.matches(:regex1)"
                    + " || player2.familyName.matches(:regex1)"
                    + " || player2.givenName.matches(:regex1)"
                    + " || player2.knownAs.matches(:regex1)"
                    + " || player2.emailAddress.matches(:regex1) )"
                    + " && "
                    + " (player1.username.matches(:regex2)"
                    + " || player1.familyName.matches(:regex2)"
                    + " || player1.givenName.matches(:regex2)"
                    + " || player1.knownAs.matches(:regex2)"
                    + " || player1.emailAddress.matches(:regex2)"
                    + " || player2.username.matches(:regex2)"
                    + " || player2.familyName.matches(:regex2)"
                    + " || player2.givenName.matches(:regex2)"
                    + " || player2.knownAs.matches(:regex2)"
                    + " || player2.emailAddress.matches(:regex2) )"
                    )
    })
@DomainObject(editing=Editing.DISABLED,
	autoCompleteRepository=Teams.class,
	autoCompleteAction="findByNames"
	)
public class Team implements Comparable<Team>{

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPlayer1().getUsername() + "," + getPlayer2().getUsername());
		return buf.toString();
	}
	
	// {{ player1 (property)
	private ApplicationUser player1;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public ApplicationUser getPlayer1() {
		return player1;
	}

	public void setPlayer1(final ApplicationUser player1) {
		this.player1 = player1;
	}
	// }}
	
	// {{ player2 (property)
	private ApplicationUser player2;

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	public ApplicationUser getPlayer2() {
		return player2;
	}

	public void setPlayer2(final ApplicationUser player2) {
		this.player2 = player2;
	}
	// }}

	// {{ Win (Collection)
	@Persistent(mappedBy = "winners", dependentElement = "true")
	private SortedSet<Match> win = new TreeSet<Match>();

	@MemberOrder(sequence = "3")
	public SortedSet<Match> getWin() {
		return win;
	}

	public void setWin(final SortedSet<Match> win) {
		this.win = win;
	}
	// }}

	// {{ lose (Collection)
	@Persistent(mappedBy = "losers", dependentElement = "true")
	private SortedSet<Match> lose = new TreeSet<Match>();

	@MemberOrder(sequence = "1")
	public SortedSet<Match> getLose() {
		return lose;
	}

	public void setLose(final SortedSet<Match> lose) {
		this.lose = lose;
	}
	// }}

	// {{ delete (action)
	public static class DeleteDomainEvent extends ActionDomainEvent<Team> {}

    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT
    )	
	@MemberOrder(sequence = "5")
	public List<Team> delete(@Parameter(optionality = Optionality.OPTIONAL)
    						 @ParameterLayout(named="Are you sure?")
    						 final Boolean areYouSure) {
    	container.removeIfNotAlready(this);
        container.flush();
        
        return teamRepository.listAll();
	}
    
    public String validateDelete(final Boolean areYouSure) {
        return not(areYouSure) ? "Please confirm this action": null;
    }
    public Boolean default0Delete() {
        return Boolean.FALSE;
    }
    
    static boolean not(final Boolean areYouSure) {
        return areYouSure == null || !areYouSure;
    }
	// }}
    
    // {{ isMatchable (action)
	@Programmatic
	public boolean isMatchable(final Team team) {
		return ! getPlayer1().equals(team.getPlayer1()) &&
				! getPlayer2().equals(team.getPlayer1()) &&
				! getPlayer1().equals(team.getPlayer2()) &&
				! getPlayer2().equals(team.getPlayer2());
		
	}
	// }}

    
    // {{ SectionName
    @javax.inject.Inject
    DomainObjectContainer container;

    @javax.inject.Inject
    Teams teamRepository;
	// }}

    /**
     * version (derived property)
     */
    @Property(hidden=Where.EVERYWHERE)
    public java.sql.Timestamp getVersionSequence() {
        return (java.sql.Timestamp) JDOHelper.getVersion(this);
    }


    @Override
    public int compareTo(final Team other) {
        return ObjectContracts.compare(this, other, "player1", "player2");
    }
}
