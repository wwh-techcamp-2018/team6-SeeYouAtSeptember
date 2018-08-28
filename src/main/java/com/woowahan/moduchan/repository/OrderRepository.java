package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderHistory,String> {
    Optional<OrderHistory> findByIdAndUid(String s,Long uid);
}
