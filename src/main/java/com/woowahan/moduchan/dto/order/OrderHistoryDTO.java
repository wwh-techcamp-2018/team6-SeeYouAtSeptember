package com.woowahan.moduchan.dto.order;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderHistoryDTO {
    private Long id;
    private String merchantUid;
    private ProductDTO productDTO;
    private UserDTO userDTO;
    @NotNull
    @Min(value = 1, message = "주문량은 1개 이상 입력해주세요.")
    private Long quantity;
    private String name;
    private Long totalPurchasePrice;

}
