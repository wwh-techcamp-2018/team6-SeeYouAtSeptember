package com.woowahan.moduchan.dto.order;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderHistoryDTOTest {

    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void orderHistoryValid_성공() {
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(null, null, null, null, 1L, null, null);
        Set<ConstraintViolation<OrderHistoryDTO>> constraintViolcations = validator.validate(orderHistoryDTO);
        assertThat(constraintViolcations.size()).isEqualTo(0);
    }

    @Test
    public void orderHistoryValid_실패_주문수량_미달() {
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(null, null, null, null, 0L, null, null);
        Set<ConstraintViolation<OrderHistoryDTO>> constraintViolcations = validator.validate(orderHistoryDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }
}
