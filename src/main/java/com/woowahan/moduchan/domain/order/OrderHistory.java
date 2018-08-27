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
    private String id;
    private String name;
    private Long pid;
    private Long uid;
    private Long purchasePrice;
    private Long quantity;
    private STATUS status;

    public static OrderHistory from(OrderHistoryDTO orderHistoryDTO, Long uid){
        return new OrderHistoryBuilder().id(UUID.randomUUID().toString())
                .pid(orderHistoryDTO.getPid())
                .uid(uid)
                .purchasePrice(orderHistoryDTO.getPurchasePrice())
                .quantity(orderHistoryDTO.getQuantity())
                .status(STATUS.FAIL)
                .name(orderHistoryDTO.getName())
                .build();
    }

    public OrderHistoryDTO toDTO() {
        return new OrderHistoryDTO(id,pid,uid,name,purchasePrice,quantity);
    }

    public OrderHistory changeSuccessOrderStatus() {
        this.status = STATUS.SUCCESS;
        return this;
    }

    public enum STATUS {
        SUCCESS,
        FAIL,
        CANCEL
    }
}
