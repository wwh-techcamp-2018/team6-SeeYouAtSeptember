package com.woowahan.moduchan.dto.product;

import com.woowahan.moduchan.domain.user.NormalUser;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductDTOTest {
    private static Validator validator;
    private static ProductDTO productDTO;
    private static Set<NormalUser> normalUsers;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void productValid_성공() {
        productDTO = new ProductDTO(null, "테스트상품", 1000L, 10L, "테스트입니다", null, null);
        Set<ConstraintViolation<ProductDTO>> constraintViolcations = validator.validate(productDTO);
        assertThat(constraintViolcations.size()).isEqualTo(0);
    }

    @Test
    public void productValid_실패_공급량_미달() {
        productDTO = new ProductDTO(null, "테스트상품", 1000L, 0L, "테스트입니다", null, null);
        Set<ConstraintViolation<ProductDTO>> constraintViolcations = validator.validate(productDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }


    @Test
    public void productValid_실패_상품가격_미달() {
        productDTO = new ProductDTO(null, "테스트상품", 999L, 10L, "테스트입니다", null, null);
        Set<ConstraintViolation<ProductDTO>> constraintViolcations = validator.validate(productDTO);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }
}
