package com.example.shopPJT.benchmark.repository;

import com.example.shopPJT.benchmark.entity.BenchMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BenchMarkRepository extends JpaRepository<BenchMark, Long> {

    @Query("SELECT b FROM BenchMark b JOIN FETCH b.cpuSpec JOIN FETCH b.graphicSpec WHERE b.cpuSpec.id = :cpuSpecId AND b.graphicSpec.id = :graphicSpecId")
    Optional<BenchMark> findByCpuSpecIdAndGraphicSpecId(@Param("cpuSpecId") Long cpuSpecId, @Param("graphicSpecId") Long graphicSpecId);
}
