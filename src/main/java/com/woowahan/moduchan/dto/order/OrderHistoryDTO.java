package com.woowahan.moduchan.dto.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDTO {
    private Long id;
    private String merchantUid;
    private Long pid;
    private Long uid;
    private String name;
    private Long purchasePrice;
    private Long quantity;


}
