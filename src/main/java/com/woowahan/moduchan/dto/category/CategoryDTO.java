package com.woowahan.moduchan.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    @ApiModelProperty(position = 1)
    private Long id;
    @ApiModelProperty(example = "title", position = 2)
    private String title;
    @ApiModelProperty(example = "https://i.ebayimg.com/images/g/LcsAAOSwc6Jaem96/s-l300.jpg", position = 3)
    private String categoryImageUrl;
}
