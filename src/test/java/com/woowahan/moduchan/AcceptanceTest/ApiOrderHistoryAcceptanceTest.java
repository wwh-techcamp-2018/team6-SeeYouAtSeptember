package com.woowahan.moduchan.AcceptanceTest;

import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import util.ApiAcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiOrderHistoryAcceptanceTest extends ApiAcceptanceTest {
    private List<OrderHistoryDTO> orderHistoryDTOS;

    @Before
    public void setUp() throws Exception {
        orderHistoryDTOS = new ArrayList<>();
        orderHistoryDTOS.add(getDefaultOrderHistoryDTO());
    }

    @Test
    public void createOrderTest_성공() {

        ResponseEntity<OrderHistoryDTO> response = templateWithNormalUser()
                .postForEntity("/api/orders", orderHistoryDTOS, OrderHistoryDTO.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("짭조름한 일본");
    }

    @Test
    public void createOrderTest_실패_로그인_하지않은_사용자() {
        ResponseEntity<String> response = template()
                .postForEntity("/api/orders", orderHistoryDTOS, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
