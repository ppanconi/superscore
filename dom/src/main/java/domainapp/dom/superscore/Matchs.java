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
public class Matchs {
	
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
    public static class CreateDomainEvent extends ActionDomainEvent<Match> {
        public CreateDomainEvent(final Match source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public Match create(
            final @ParameterLayout(named="Winners") Team winners,
            final @ParameterLayout(named="Losers") Team losers,
            final @ParameterLayout(named="Winners Score") Integer winnersScore,
            final @ParameterLayout(named="Losers Score") Integer losersScore
            ) {
        final Match obj = container.newTransientInstance(Match.class);
        obj.setWinners(winners);
        obj.setLosers(losers);
        obj.setWinnersScore(winnersScore);
        obj.setLosersScore(losersScore);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion

}
