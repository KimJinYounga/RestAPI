package me.kjy.restapitest.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

// compoment 로 빈 등록.
@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() >0) {
            // ctrl + p ==> 어떤 파라미터가 있는지 보여줌
            errors.rejectValue("basePrice", "wrongValue", "Base price is wrong.");
            errors.rejectValue("maxPrice", "wrongValue", "Max price is wrong.");
        }
         LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if ( endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong.");
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime

    }
}
