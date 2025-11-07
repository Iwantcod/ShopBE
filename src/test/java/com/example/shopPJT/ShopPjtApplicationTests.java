package com.example.shopPJT;

import com.example.shopPJT.benchmark.repository.BenchMarkRepository;
import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto;
import com.example.shopPJT.recommendedOriginal.repository.RecommendedOriginalRepository;
import com.example.shopPJT.recommendedProduct.dto.ResRecommended;
import com.example.shopPJT.recommendedProduct.entity.RecommendedProduct;
import com.example.shopPJT.recommendedProduct.service.RecommendedProductService;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Rollback(false)
class ShopPjtApplicationTests {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CpuSpecRepository cpuSpecRepository;
	@Autowired
    private GraphicSpecRepository graphicSpecRepository;
	@Autowired
    private CaseSpecRepository caseSpecRepository;
	@Autowired
    private MemorySpecRepository memorySpecRepository;
	@Autowired
    private PowerSpecRepository powerSpecRepository;
	@Autowired
    private CoolerSpecRepository coolerSpecRepository;
	@Autowired
    private MainBoardSpecRepository mainBoardSpecRepository;
	@Autowired
    private StorageSpecRepository storageSpecRepository;
	@Autowired
	private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BenchMarkRepository benchMarkRepository;
    @Autowired
    private RecommendedOriginalRepository recommendedOriginalRepository;
    @Autowired
    private RecommendedProductService recommendedProductService;


	@Test
	@Disabled
	void contextLoads() {
		ResRecommendedOriginalDto result = recommendedOriginalRepository.findByUsageIdAndPrice(1, 790000);
		if(result == null) {
			System.out.println("견적 원본 조회 결과가 존재하지 않습니다.");
		} else {
			System.out.println("견적원본 식별자: "+result.getRecommendedOriginalId()+", 견적원본 예상가격: "+result.getEstimatePrice());
		}
	}

	@Test
	@DisplayName("견적 상품 insert")
	void insertRP() {
		recommendedProductService.updateRecommendedProduct();
	}

	@Test
	@Disabled
	@DisplayName("추천 견적 조회")
	void getRecommendedProduct() {
		ResRecommended resRecommended = recommendedProductService.getRecommendedProduct(2, 50000000);
		System.out.println(resRecommended);
	}

}
