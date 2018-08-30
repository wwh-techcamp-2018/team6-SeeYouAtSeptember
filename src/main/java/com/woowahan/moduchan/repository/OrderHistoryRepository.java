package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.user.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    Optional<List<OrderHistory>> findByMerchantUidAndNormalUser(String s, NormalUser normalUser);

    List<OrderHistory> findAllByNormalUser(NormalUser normalUser);

    List<OrderHistory> findByStatus(OrderHistory.STATUS status);

    List<OrderHistory> findByStatusAndCreatedAtLessThan(OrderHistory.STATUS status, Date limitTime);

}
