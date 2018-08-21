package com.woowahan.moduchan.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ProductUserMapDTO {
    @ApiModelProperty(example = "1", position = 1)
    private Long pid;
    @ApiModelProperty(hidden = true)
    private Long uid;
    @ApiModelProperty(example = "1", position = 2)
    private Long quantity;
}
