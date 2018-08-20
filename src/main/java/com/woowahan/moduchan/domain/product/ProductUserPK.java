package com.woowahan.moduchan.domain.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class ProductUserPK implements Serializable {
    private Long product;
    private Long normalUser;
}