package me.kjy.restapitest.events;

/*
ctrl + shift +  a ==> optimize imports => 필요없는 import 제거!
 */

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    public void bulder() {
        Event event=Event.builder()
                .name("inflearn Spring REST API")
                .description("Rest API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void test() {
        /*
        코드 리팩토링
        ALT + Ctrl +  V
         */
        // Given
        String name="Event";
        String description = "Spring";

        // When
        Event event=new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}