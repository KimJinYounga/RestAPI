package me.kjy.restapitest.common;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ErrorResource extends EntityModel<Errors> {

    public ErrorResource(Errors content, Link... links) {
        super(content, links);
//        add(linkTo(IndexController.class).withRel("index"));
    }
}
