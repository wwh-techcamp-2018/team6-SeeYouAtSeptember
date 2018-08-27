package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderHistory,String> {
}
