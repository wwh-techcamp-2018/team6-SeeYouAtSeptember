package com.woowahan.moduchan.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long pid;
    private String title;
    private Long price;
    private Long quantitySupplied;
    private String description;
    private int supporterCount;
    private Long quantityConsumed;

    public Long getRemainQuantity(){
        return quantitySupplied - quantityConsumed;
    }

    public boolean isIsRemainQuantity(){
        return quantitySupplied > quantityConsumed;
    }
}
