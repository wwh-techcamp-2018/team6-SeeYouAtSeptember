package com.woowahan.moduchan.dto.project;

import java.util.List;

import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.util.Date;

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
