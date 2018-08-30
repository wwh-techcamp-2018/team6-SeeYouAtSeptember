package com.woowahan.moduchan.support;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {
    @Autowired
    private TestRestTemplate template;

    public TestRestTemplate template() {
        return template;
    }

    public TestRestTemplate templateWithNormalUser() {
        return template().withBasicAuth("a", "a");
    }

    protected UserDTO getDefaultUserDTO() {
        return new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
    }

    protected ProductDTO getDefaultProductDTO() {
        return new ProductDTO(
                null,
                "product title",
                10000L,
                1000L,
                "product description",
                new HashSet<NormalUser>(),
                null);
    }

    protected ProjectDTO getDefaultProjectDTO() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(getDefaultProductDTO());
        return new ProjectDTO(
                2L,
                null,
                "project title",
                "project description",
                "http://moduchan.xyz/modu.jpg",
                null,
                1542153600000L,
                null,
                null,
                productDTOS,
                10000000L,
                0L);
    }

    protected OrderHistoryDTO getDefaultOrderHistoryDTO() {
        ProductDTO productDTO = getDefaultProductDTO();
        productDTO.setPid(1L);
        return new OrderHistoryDTO(
                null,
                "uuid",
                productDTO,
                getDefaultUserDTO(),
                1L,
                null,
                0L
        );
    }
}
