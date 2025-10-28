package com.example.shopPJT.recommendedProduct.dto;

import com.example.shopPJT.product.dto.ResProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRecommended {
    private ResProductDto cpuProduct;
    private ResProductDto graphicProduct;
    private ResProductDto caseProduct;
    private ResProductDto memoryProduct;
    private ResProductDto powerProduct;
    private ResProductDto mainboardProduct;
    private ResProductDto coolerProduct;
    private ResProductDto storageProduct;
    private Integer totalPrice;

    @Override
    public String toString() {
        return "ResRecommended{" +
                "\ncpuProduct=" + cpuProduct +
                "\ngraphicProduct=" + graphicProduct +
                "\ncaseProduct=" + caseProduct +
                "\nmemoryProduct=" + memoryProduct +
                "\npowerProduct=" + powerProduct +
                "\nmainboardProduct=" + mainboardProduct +
                "\ncoolerProduct=" + coolerProduct +
                "\nstorageProduct=" + storageProduct +
                "\ntotalPrice=" + totalPrice +
                "\n}";
    }
}
