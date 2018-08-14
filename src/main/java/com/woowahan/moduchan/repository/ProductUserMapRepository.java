package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.product.ProductUserMap;
import com.woowahan.moduchan.domain.product.ProductUserPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserMapRepository extends JpaRepository<ProductUserMap, ProductUserPK> {
}
