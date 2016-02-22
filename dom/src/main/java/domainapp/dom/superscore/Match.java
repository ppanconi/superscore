package domainapp.dom.superscore;

import java.util.Date;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.IsisApplibModule.ActionDomainEvent;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;


@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Match"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@DomainObject
public class Match implements Comparable<Match> {
	
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append("Match");
//		buf.append(getWinners().title() + " vs " + getLosers().title() + ":" + getWinnersScore() + "-" + getLosersScore());
		return buf.toString();
	}
	
	// {{ when (property)
	private Date when;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Date getWhen() {
		return when;
	}

	public void setWhen(final Date when) {
		this.when = when;
	}
	// }}

	// {{ winners (property)
	private Team winners;

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	@Persistent(mappedBy = "win")
	public Team getWinners() {
		return winners;
	}

	public void setWinners(final Team winners) {
		this.winners = winners;
	}
	// }}
	
	// {{ losers (property)
	private Team losers;

	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	@Persistent(mappedBy = "lose")
	public Team getLosers() {
		return losers;
	}

	public void setLosers(final Team losers) {
		this.losers = losers;
	}
	// }}
	
	// {{ WinnersScore (property)
	private Integer winnersScore;

	@MemberOrder(sequence = "4")
	@Column(allowsNull = "false")
	public Integer getWinnersScore() {
		return winnersScore;
	}

	public void setWinnersScore(final Integer winnersScore) {
		this.winnersScore = winnersScore;
	}
	// }}

	// {{ LosersScore (property)
	private Integer losersScore;

	@MemberOrder(sequence = "5")
	@Column(allowsNull = "false")
	public Integer getLosersScore() {
		return losersScore;
	}

	public void setLosersScore(final Integer losersScore) {
		this.losersScore = losersScore;
	}
	// }}
	
	// {{ UnderTable (property)
	private Boolean underTable;

	@MemberOrder(sequence = "6")
	@Column(allowsNull = "false")
	public Boolean getUnderTable() {
		return underTable;
	}

	public void setUnderTable(final Boolean underTable) {
		this.underTable = underTable;
	}
	// }}

	// {{ WithAlex (property)
	private Boolean withAlex;

	@MemberOrder(sequence = "7")
	@Column(allowsNull = "false")
	public Boolean getWithAlex() {
		return withAlex;
	}

	public void setWithAlex(final Boolean withAlex) {
		this.withAlex = withAlex;
	}
	// }}
	
	// {{ delete (action)
	public static class DeleteDomainEvent extends ActionDomainEvent<Team> {}

    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT
    )	
	@MemberOrder(sequence = "5")
	public List<Match> delete(@Parameter(optionality = Optionality.OPTIONAL)
    						 @ParameterLayout(named="Are you sure?")
    						 final Boolean areYouSure) {
    	container.removeIfNotAlready(this);
        container.flush();
        
        return matchesRepository.listAll();
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

    public String validate() {
    	if (! getWinners().isMatchable(getLosers())) return "Impossible match, check the players";
    	if (getWinnersScore() < 6) return "Invalid Winners score (>=6)";
    	if (getWinnersScore() < getLosersScore()) return "Invalid scores. (Winners score > Losers Score)";
    	if (getWinnersScore() == 6 && getWinnersScore() - getLosersScore() < 2) return "Invalid scores";
    	if (getWinnersScore() > 6 && getWinnersScore() - getLosersScore() > 2)
    		return "Invalid scores";
    	
    	if (getLosersScore() == 0) {
        	setUnderTable(true);
        }
    	return null;
	}
    
    public void updating() {
        if (getLosersScore() == 0) {
        	setUnderTable(true);
        }
    }
    
	/**
     * version (derived property)
     */
	@Property(hidden=Where.EVERYWHERE)
    public java.sql.Timestamp getVersionSequence() {
        return (java.sql.Timestamp) JDOHelper.getVersion(this);
    }

    @Override
    public int compareTo(final Match other) {
        return ObjectContracts.compare(this, other, "winners", "losers", "when");
    }
	
    // {{ SectionName
    @javax.inject.Inject
    DomainObjectContainer container;

    @javax.inject.Inject
    Matches matchesRepository;
	// }}
}
