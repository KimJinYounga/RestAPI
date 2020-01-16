package me.kjy.restapitest.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value="/api/events", produces= MediaTypes.HAL_JSON_VALUE)
public class EventController {

//    @Autowired
//    EventRepository eventRepository;

    // 생성자로 주입을 받을 수 있음
    private final EventRepository eventRepository;
    //@Autowired
    public EventController(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        // created()로 보낼 땐 항상 uri 가 있어야함
        // ==> HATEOS가 제공하는 linkTo(), methodOn() 사용
       Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
