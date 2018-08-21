package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductUserPK.class)
public class ProductUserMap extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn
    private NormalUser normalUser;

    private Long quantity;
}