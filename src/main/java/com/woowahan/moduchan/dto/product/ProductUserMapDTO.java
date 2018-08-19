package com.woowahan.moduchan.dto.product;

import lombok.Getter;

@Getter
public class ProductUserMapDTO {
    private Long pid;
    private Long uid;
    private Long quantity;

    public void setUid(Long id) {
        this.uid = id;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
