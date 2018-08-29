package com.woowahan.moduchan.domain.order;

import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String merchantUid;

    @ManyToOne
    @JoinColumn
    private Product product;

    @ManyToOne
    @JoinColumn
    private NormalUser normalUser;

    private Long quantity;
    private STATUS status;

    public static OrderHistory from(OrderHistoryDTO orderHistoryDTO, NormalUser normalUser, Product product, String merchantUid) {
        return new OrderHistoryBuilder()
                .merchantUid(merchantUid)
                .product(product)
                .normalUser(normalUser)
                .quantity(orderHistoryDTO.getQuantity())
                .status(STATUS.FAIL)
                .build();
    }

    public Project getProject() {
        return this.product.getProject();
    }

    public Project getProject(STATUS status) {
        return this.status == status ? getProject() : null;
    }

    public NormalUser getNormalUser(STATUS status) {
        return this.status == status ? normalUser : null;
    }

    public OrderHistoryDTO toDTO(int size, Long totalPurchasePrice) {
        if (size == 1) {
            return new OrderHistoryDTO(id, merchantUid, product.toDTO(), normalUser.toDTO(), quantity, product.getTitle(), totalPurchasePrice);
        }
        return new OrderHistoryDTO(id, merchantUid, product.toDTO(), normalUser.toDTO(),
                quantity, String.format("%s 등 %d개의 물품", product.getTitle(), size), totalPurchasePrice);
    }

    public OrderHistory changeOrderStatusSuccess() {
        this.status = STATUS.SUCCESS;
        return this;
    }

    public boolean isSuccess() {
        return this.status == STATUS.SUCCESS;
    }

    public Long calculateTotalProductPrice() {
        return this.product.getPrice() * quantity;
    }

    public enum STATUS {
        SUCCESS,
        FAIL,
        CANCEL
    }
}
