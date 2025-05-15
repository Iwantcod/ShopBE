package com.example.shopPJT.productSpec.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // 인터페이스를 구현한 Service Bean 클래스들을 Map으로 합치고, 제공하는 Factory 클래스
public class ProductSpecServiceFactory {
    private final Map<String, ProductSpecServiceStrategy> strategies;

    // Spring DI 컨테이너가 ProductSpecServiceStrategy 인터페이스의 구현체를 전부 모아 리스트에 add 한다.
    public ProductSpecServiceFactory(List<ProductSpecServiceStrategy> strategieList) {
        // 모든 ProductSpecServiceStrategy 구현체를 '불변 Map'으로 변환하여 저장.
        this.strategies = Collections.unmodifiableMap(  // Collections.unmodifiableMap(): 인자로 넘겨진 Map을 불변화(확실한 Thread-Safe을 위함)
                strategieList.stream()
                    .collect(Collectors.toMap(
                        ProductSpecServiceStrategy::getSpecType, // 스펙 타입 이름(문자열): key
                        strategy -> strategy // 구현체 클래스(Override한 메소드만 호출가능): value
                        )
                    )
                );
    }

    public ProductSpecServiceStrategy getStrategy(String specType) {
        // HashMap은 원소의 개수에 상관없이 조회에 걸리는 시간이 O(1)이다. -> 빠름
        ProductSpecServiceStrategy strategy = strategies.get(specType);
        if (strategy == null) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + specType);
        }
        return strategy;
    }
}
