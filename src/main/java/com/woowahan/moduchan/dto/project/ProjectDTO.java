package com.woowahan.moduchan.dto.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
    private Long cid;
    private Long pid;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Date createdAt;
    private Date endAt;
    private Project.STATUS status;
    private String owner;
    private List<ProductDTO> products;
    private Long goalFundRaising;
    private Long fundraisingAmount;
}
