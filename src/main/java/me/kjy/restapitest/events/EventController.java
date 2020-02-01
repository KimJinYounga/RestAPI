package me.kjy.restapitest.events;

import me.kjy.restapitest.common.ErrorResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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

    private final EventValidator eventValidator;
    //@Autowired
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator){
        this.eventRepository=eventRepository;
        this.modelMapper=modelMapper;
        this.eventValidator = eventValidator;
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
            return badRequest(errors);
        }
        /**
         * errors라는 객체를 json으로 변환을 바로 할수가 없기 때문에 body(errors)는 에러남.
         * 그렇다면 Event는 json으로 변환 가능하고, Errors는 왜 변환할 수 없는가?
         * ==> 이유 : java bean 스펙을 따르기 때문에 event 도메인은 beanSerializer를 사용해서
         * Serialization(객체 -> json)할 수 있음
         * Errors는 java bean스펙을 준수하고 있는 객체가 아니기 때문에 변환 할 수 없음
          */

        // eventDto 검증 실행
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/html5/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
//        return ResponseEntity.ok(assembler.toModel(eventRepository.findAll(pageable)));
        Page<Event> page = this.eventRepository.findAll(pageable);

        PagedModel<EventResource> pagedResources = assembler.toModel(page, e -> new EventResource(e));
        pagedResources.add(new Link("/html5/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if (!optionalEvent.isPresent()) {
            return notFoundResponse();
        }

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/html5/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok().body(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, 
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        // existingEvent.setName(eventDto.getName())등등 할 필요 없이, modeMapper가 한번에 해줌
        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent= this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/html5/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);


    }

    private ResponseEntity<Object> notFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }




}
