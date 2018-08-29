package com.woowahan.moduchan.web;

import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.support.ApiAcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiOrderControllerTest extends ApiAcceptanceTest {

    private static final String API_URL = "/api/orders";
    private OrderHistoryDTO orderHistoryDTO;

    @Before
    public void setUp() throws Exception {
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
    }

    @Test
    public void 메뉴생성_성공() {
        ResponseEntity<String> response = templateWithNormalUser()
                .postForEntity(API_URL, orderHistoryDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = template().exchange("/api/categories", HttpMethod.GET, createHttpEntity(), String.class);
        assertThat(response.getBody().contains("생성메뉴")).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
