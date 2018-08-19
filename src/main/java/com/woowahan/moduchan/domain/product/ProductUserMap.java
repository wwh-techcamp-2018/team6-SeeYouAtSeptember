package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.product.ProductUserMapDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
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

    @Builder
    public ProductUserMap(NormalUser normalUser, Long quantity) {
        this.normalUser = normalUser;
        this.quantity = quantity;
    }

    public static ProductUserMap from(ProductUserMapDTO productUserMapDTO, NormalUser loginUser) {
        return new ProductUserMapBuilder()
                .normalUser(loginUser)
                .quantity(productUserMapDTO.getQuantity())
                .build();
    }

    public Long addQuantityToTotal(Long total) {
        return this.quantity + total;
    }

    public ProductUserMap addProduct(Product product) {
        this.product = product;
        return this;
    }

    public ProductUserMap addNormalUser(NormalUser normalUser) {
        this.normalUser = normalUser;
        return this;
    }
}