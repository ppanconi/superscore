package domainapp.dom.superscore;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.isisaddons.module.security.dom.user.ApplicationUser;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Team"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@DomainObject
public class Team {

	// {{ player1 (property)
	private ApplicationUser player1;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public ApplicationUser getplayer1() {
		return player1;
	}

	public void setplayer1(final ApplicationUser player1) {
		this.player1 = player1;
	}
	// }}
	
	// {{ player2 (property)
	private ApplicationUser player2;

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	public ApplicationUser getplayer2() {
		return player2;
	}

	public void setplayer2(final ApplicationUser player2) {
		this.player2 = player2;
	}
	// }}


}
