package me.kjy.restapitest.events;

/*
ctrl + shift +  a ==> optimize imports => 필요없는 import 제거!
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    @Test
//    @Parameters(method = "parametersForTestFree")
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    /**
     * parametersFor메소드이름 <== Convention
     *  ==> test시 @Parameters에 메소드 이름을 붙이지 않아도 가능.
     * @return
     */
    private Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0,0,true},
                new Object[] {100,0,false},
                new Object[] {0,100,false},
                new Object[] {100,200,false}

        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"강남역 네이버 D2 스타텁 팩토리", true},
                new Object[] {null, false}
        };
    }
}