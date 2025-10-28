package com.example.shopPJT.recommendedProduct.entity;

import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.recommendedOriginal.entity.RecommendedOriginal;
import com.example.shopPJT.recommendedUsage.entity.RecommendedUsage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "RECOMMENDED_PRODUCT_ID")
    private Long recommendedProductId;
    @Column(nullable = false)
    private Integer totalPrice;
    @CreationTimestamp
    @Column(insertable = false, updatable = false,
            columnDefinition = "DATE DEFAULT (CURRENT_DATE)")
    private LocalDate createdAt;

    @JoinColumn(name = "CPU_PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product cpuProduct;

    @JoinColumn(name = "GRAPHIC_PRODUCT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product graphicProduct;

    @JoinColumn(name = "CASE_PRODUCT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product caseProduct;

    @JoinColumn(name = "MAINBOARD_PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product mainboardProduct;

    @JoinColumn(name = "POWER_PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product powerProduct;

    @JoinColumn(name = "STORAGE_PRODUCT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product storageProduct;

    @JoinColumn(name = "COOLER_PRODUCT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product coolerProduct;

    @JoinColumn(name = "MEMORY_PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product memoryProduct;

    @JoinColumn(name = "RECOMMENDED_USAGE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RecommendedUsage recommendedUsage;

    @JoinColumn(name = "RECOMMENDED_ORIGINAL_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RecommendedOriginal recommendedOriginal;

    @Builder
    public RecommendedProduct(Product cpuProduct, Product graphicProduct, Product caseProduct, Product mainboardProduct, Product powerProduct, Product storageProduct, Product coolerProduct, Product memoryProduct, RecommendedUsage recommendedUsage, RecommendedOriginal recommendedOriginal) {
        this.cpuProduct = cpuProduct;
        this.graphicProduct = graphicProduct;
        this.caseProduct = caseProduct;
        this.mainboardProduct = mainboardProduct;
        this.powerProduct = powerProduct;
        this.storageProduct = storageProduct;
        this.coolerProduct = coolerProduct;
        this.memoryProduct = memoryProduct;
        this.recommendedUsage = recommendedUsage;
        this.recommendedOriginal = recommendedOriginal;
        // 견적 총 가격: 견적 요소의 가격 총합
        this.totalPrice = cpuProduct.getPrice() + graphicProduct.getPrice() + caseProduct.getPrice() + mainboardProduct.getPrice()
                + powerProduct.getPrice() + storageProduct.getPrice() + coolerProduct.getPrice() + memoryProduct.getPrice();
    }
}
