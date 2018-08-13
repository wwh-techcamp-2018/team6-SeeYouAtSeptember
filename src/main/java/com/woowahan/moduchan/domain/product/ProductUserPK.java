package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.user.NormalUser;

import java.io.Serializable;

public class ProductUserPK implements Serializable {
    private Product product;
    private NormalUser normalUser;
}

