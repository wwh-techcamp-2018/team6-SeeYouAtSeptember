package com.woowahan.moduchan.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ProjectDetailDTO {
    private Long pid;
    private Long cid;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long goalFundRaising;
    private Date endAt;
}
