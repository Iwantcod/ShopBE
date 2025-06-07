package com.example.shopPJT.businessInfo.repository;

import com.example.shopPJT.businessInfo.entity.BusinessInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, Integer> {
    @Query("SELECT b FROM BusinessInfo b WHERE b.user.id = :userId")
    Optional<BusinessInfo> findByUserId(@Param("userId") Long userId);


    @Query("SELECT b FROM BusinessInfo b WHERE b.isApproval = false")
    Slice<BusinessInfo> findByIsApprovalFalsePaging(Pageable pageable);

}
