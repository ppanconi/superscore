package domainapp.dom.superscore;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;

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
public class Match {
	
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

	@MemberOrder(sequence = "1")
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

	@MemberOrder(sequence = "2")
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

	@MemberOrder(sequence = "1")
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

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Integer getLosersScore() {
		return losersScore;
	}

	public void setLosersScore(final Integer losersScore) {
		this.losersScore = losersScore;
	}
	// }}
	
}
