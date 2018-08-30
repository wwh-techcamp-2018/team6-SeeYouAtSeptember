package com.woowahan.moduchan.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.moduchan.domain.user.NormalUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Comparable<ProductDTO> {
    private Long pid;

    @NotBlank(message = "상품 제목을 입력해주세요.")
    @Size(max = 30, message = "상품 제목을 최대 30자로 입력해주세요.")
    private String title;

    @NotNull
    @Min(value = 1000, message = "상품 가격은 1000원 이상 입력해주세요.")
    private Long price;

    @NotNull
    @Min(value = 1, message = "상품 공급량은 1개 이상 입력해주세요.")
    private Long quantitySupplied;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    @Size(max = 100, message = "상품 설명을 최대 100자로 입력해주세요.")
    private String description;

    private Set<NormalUser> supporters;
    private Long quantityRemained;

    @Override
    public int compareTo(ProductDTO productDTO) {
        if (this.price < productDTO.getPrice()) {
            return -1;
        } else if (this.price == productDTO.getPrice()) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
