package com.woowahan.moduchan.domain.order;

import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

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
    private String name;
    private Long pid;
    private Long uid;
    private Long purchasePrice;
    private Long quantity;
    private STATUS status;

    public static OrderHistory from(OrderHistoryDTO orderHistoryDTO, Long uid) {
        return new OrderHistoryBuilder().merchantUid(UUID.randomUUID().toString())
                .pid(orderHistoryDTO.getPid())
                .uid(uid)
                .purchasePrice(orderHistoryDTO.getPurchasePrice())
                .quantity(orderHistoryDTO.getQuantity())
                .status(STATUS.FAIL)
                .name(orderHistoryDTO.getName())
                .build();
    }

    public OrderHistoryDTO toDTO() {
        return new OrderHistoryDTO(id, merchantUid,pid, uid, name, purchasePrice, quantity);
    }

    public OrderHistory changeOrderStatusSuccess() {
        this.status = STATUS.SUCCESS;
        return this;
    }

    public enum STATUS {
        SUCCESS,
        FAIL,
        CANCEL
    }
}
