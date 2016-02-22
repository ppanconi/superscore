package domainapp.dom.superscore;

import java.util.Date;
import java.util.List;

import org.apache.isis.applib.ApplicationException;
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
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.TitleBuffer;

import domainapp.dom.simple.SimpleObject;
import domainapp.dom.simple.SimpleObjects;
import domainapp.dom.simple.SimpleObjects.CreateDomainEvent;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Match.class
)
@DomainServiceLayout(
        menuOrder = "9"
)
public class Matches {
	
	//region > title
    public TranslatableString title() {
        return TranslatableString.tr("Matchs");
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
    public List<Match> listAll() {
        return container.allInstances(Match.class);
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<Matches> {
        public CreateDomainEvent(final Matches source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @ActionLayout(
    		named="Create Match"
    		)
    @MemberOrder(sequence = "3")
    public Match create(
            final @ParameterLayout(named="Winners") Team winners,
            final @ParameterLayout(named="Losers") Team losers,
            final @ParameterLayout(named="Winners Score") Integer winnersScore,
            final @ParameterLayout(named="Losers Score") Integer losersScore,
            final @ParameterLayout(named="Under Table") Boolean underTable,
            final @ParameterLayout(named="With Alex") Boolean withAlex,
            final @ParameterLayout(named="When") Date when
            ) {
        final Match obj = container.newTransientInstance(Match.class);
        obj.setWinners(winners);
        obj.setLosers(losers);
        obj.setWinnersScore(winnersScore);
        obj.setLosersScore(losersScore);
        obj.setUnderTable(underTable);
        obj.setWithAlex(withAlex);
        obj.setWhen(when);
        
        if (losersScore == 0) {
        	obj.setUnderTable(true);
        }
        
        String cause = obj.validate();
        if (cause != null)
        	throw new ApplicationException(cause);

        container.persistIfNotAlready(obj);	
        return obj;
    }
    public Integer default2Create() {
		return 6;
	}
    
    public Integer default3Create() {
		return 0;
	}
    public Boolean default4Create() {
		return Boolean.FALSE;
	}
    
    public Boolean default5Create() {
		return Boolean.FALSE;
	}
    public Date default6Create() {
		return new Date();
	}
    
    public String validateCreate(
    		final @ParameterLayout(named="Winners") Team winners,
            final @ParameterLayout(named="Losers") Team losers,
            final @ParameterLayout(named="Winners Score") Integer winnersScore,
            final @ParameterLayout(named="Losers Score") Integer losersScore,
            final @ParameterLayout(named="Under Table") Boolean underTable,
            final @ParameterLayout(named="With Alex") Boolean withAlex,
            final @ParameterLayout(named="When") Date when
            ) {
    	final Match obj = container.newTransientInstance(Match.class);
        obj.setWinners(winners);
        obj.setLosers(losers);
        obj.setWinnersScore(winnersScore);
        obj.setLosersScore(losersScore);
        obj.setUnderTable(underTable);
        obj.setWithAlex(withAlex);
        obj.setWhen(when);
        
        return obj.validate();
    }
    //endregion

    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion

}
