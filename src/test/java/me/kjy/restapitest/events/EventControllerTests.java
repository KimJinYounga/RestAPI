package me.kjy.restapitest.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {
    // Test 단축키 : Ctrl + Shift + F10
    /*
    *** MockMvc
    * mocking되어있는 dispatcher servlet을 상대로 요청을 만들어서
      그 dispatcher servlet에 보내고 그 응답을 확인할 수 있는 Test를 만들 수 있음
    * web과 관련된 bean들만 등록해서 만듬 <== slicing test
    * web server를 띄우지 않기 때문에 좀 더 빠름
    * dispatcher servlet을 띄워야 하기에 단위테스트 보다는 조금 느림
    */
    @Autowired
    MockMvc mockMvc;

    // 자동으로 objectMapper가 bean으로 등록되어 있음.
    @Autowired
    ObjectMapper objectMapper;

    // Mock 객체이므로 save를 하더라도 리턴되는 값이 모두 null임
    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event=Event.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        // Mock 객체이므로 save를 하더라도 리턴되는 값이 모두 null이므로 Mockito.when을 통해
        // 이벤트 실행시 리턴값 정의를 해줘야함
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))) // json으로 바꾼 후 본문에 넣어줌
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));
    }



}
