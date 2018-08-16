package com.woowahan.moduchan.dto.project;

import com.woowahan.moduchan.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long pid;
    private Long cid;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long goalFundRaising;
    private Date endAt;
    private List<Product> productList;
}
