package com.woowahan.moduchan.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectGatherDTO {
    private Long pid;
    private String title;
    private String owner;
    private String thumbnailUrl;
    private int period;
    private Long fundraisingAmount;

    public ProjectGatherDTO(Long pid, String title, String owner, String thumbnailUrl, int period, Long fundraisingAmount) {
        this.pid = pid;
        this.title = title;
        this.owner = owner;
        this.thumbnailUrl = thumbnailUrl;
        this.period = period;
        this.fundraisingAmount = fundraisingAmount;
    }
}
