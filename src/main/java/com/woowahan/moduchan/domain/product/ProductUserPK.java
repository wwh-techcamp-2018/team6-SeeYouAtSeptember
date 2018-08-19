package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.user.NormalUser;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class ProductUserPK implements Serializable {
    private Long product;
    private Long normalUser;

    public ProductUserPK(Long product, Long normalUser) {
        this.product = product;
        this.normalUser = normalUser;
    }
}