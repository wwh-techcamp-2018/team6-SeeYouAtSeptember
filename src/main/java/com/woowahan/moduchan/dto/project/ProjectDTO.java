package com.woowahan.moduchan.dto.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
    private final static String URL_REGEX = "(http(s?):\\/)(\\/[^/]+)+\\.(?:jpg|gif|png)";
    private final static Long TODAY_LONG = new Date().getTime();
    private final static Long ONE_DAY_DIFF = 86400000L;

    @NotNull(message = "프로젝트 카테고리를 입력해주세요.")
    @Min(value = 2, message = "프로젝트 카테고리는 2부터 존재합니다.")
    @Max(value = 8, message = "프로젝트 카테고리는 8까지 존재합니다.")
    private Long cid;
    private Long pid;

    @NotBlank(message = "프로젝트 제목을 입력해주세요.")
    @Size(max = 50, message = "프로젝트 이름은 50자 이하 입력해주세요.")
    private String title;

    @NotBlank(message = "프로젝트 설명을 입력해주세요.")
    private String description;

    @Pattern(regexp = URL_REGEX, message = "프로젝트 이미지 URL 형식이 올바르지 않습니다.")
    @NotBlank(message = "프로젝트 이미지 URL을 입력해주세요.")
    private String thumbnailUrl;

    private Long createdAt;

    @NotNull(message = "프로젝트 목표 날짜를 입력해주세요.")
    private Long endAt;

    private Project.STATUS status;
    private String owner;

    @Valid
    @NotNull(message = "프로젝트 상품을 입력해주세요.")
    @Size(min = 1, max = 4, message = "프로젝트 상품은 최소 1개, 최대 4개 입력해주세요.")
    private List<ProductDTO> products;

    @NotNull(message = "프로젝트 목표 금액을 입력해주세요.")
    @Min(value = 1000000, message = "프로젝트 목표 금액은 1000000원 이상 입력해주세요.")
    @Max(value = 1000000000, message = "프로젝트 목표 금액은 1000000000원 이하 입력해주세요.")
    private Long goalFundRaising;
    private Long currentFundRaising;

    @AssertTrue(message = "프로젝트 상품들의 합이 목표 금액보다 적습니다.")
    public boolean isMoreThanMinProductsPrice() {
        return goalFundRaising <= products.stream()
                .map(productDTO -> productDTO.getPrice() * productDTO.getQuantitySupplied())
                .reduce(0L, (x, y) -> x + y);
    }

    @AssertTrue(message = "프로젝트 목표 날짜는 현재 날짜의 30일 이후로 입력해주세요.")
    public boolean isMoreThanMinEndAt() {
        return endAt >= TODAY_LONG + ONE_DAY_DIFF * 30;
    }

    @AssertTrue(message = "프로젝트 목표 날짜는 현재 날짜의 180일 이전으로 입력해주세요.")
    public boolean isLessThanMaxEndAt() {
        return endAt <= TODAY_LONG + ONE_DAY_DIFF * 180;
    }

    public Long getSupporterCount() {
        return products.stream().map(productDTO -> productDTO.getSupporters())
                .reduce(new HashSet<>(), (x, y) -> {
                    x.addAll(y);
                    return x;
                })
                .stream().filter(normalUser -> normalUser != null).count();
    }

    public Long getDayRemainingUntilDeadline() {
        return (endAt - TODAY_LONG) / 1000 / 60 / 60 / 24;
    }

    public int getProgress() {
        return (int) ((float) currentFundRaising / goalFundRaising * 100);
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
