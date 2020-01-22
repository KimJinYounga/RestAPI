package me.kjy.restapitest.events;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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

    private final ModelMapper modelMapper;
    //@Autowired
    public EventController(EventRepository eventRepository, ModelMapper modelMapper){
        this.eventRepository=eventRepository;
        this.modelMapper=modelMapper;
    }

    // @Valid ==> Dto의 각 필드 조건을 검사한 후, @Valid바로 오른쪽에 있는 Error객체에 에러를 담아줌
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
//        Event event = Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                .build();

        // 위의 과정과 같은 역할 ==> ModelMaper
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
