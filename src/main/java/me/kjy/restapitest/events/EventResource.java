package me.kjy.restapitest.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event> {

    /**
     * EntityModel<> 사용시 => jsonUnwrapped애노테이션을 사용하지 않아도됌(내장되어있음)
     */
    public EventResource(Event event, Link... links) {
        super(event, links);
        /**
         * 아래방법은 typesafe하지 않음
         * add(new Link("http://localhost:8080/api/events/"+event.getId()));
         */

        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

/**
 *  RepresentationModel 사용시
    ==>
     /*
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event=event;
    }
    public Event getEvent() {
        return event;
    }
     */
}
