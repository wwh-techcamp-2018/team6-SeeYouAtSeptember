package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private boolean deleted = false;

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductUserMap updateQuantity(Long quantity) {
        this.quantity = quantity;
        deleted = false;
        return this;
    }

    public ProductUserMap appendQuantity(Long quantity) {
        this.quantity += quantity;
        return this;
    }
}