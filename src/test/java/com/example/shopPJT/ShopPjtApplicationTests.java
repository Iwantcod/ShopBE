package com.example.shopPJT;

import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
@Rollback(false)
class ShopPjtApplicationTests {
	private final CategoryRepository categoryRepository;
    private final CpuSpecRepository cpuSpecRepository;
    private final GraphicSpecRepository graphicSpecRepository;
    private final CaseSpecRepository caseSpecRepository;
    private final MemorySpecRepository memorySpecRepository;
    private final PowerSpecRepository powerSpecRepository;
    private final CoolerSpecRepository coolerSpecRepository;
    private final MainBoardSpecRepository mainBoardSpecRepository;
    private final StorageSpecRepository storageSpecRepository;
	private final UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

	@Autowired
	ShopPjtApplicationTests(CategoryRepository categoryRepository, CpuSpecRepository cpuSpecRepository, GraphicSpecRepository graphicSpecRepository, CaseSpecRepository caseSpecRepository,
							MemorySpecRepository memorySpecRepository, PowerSpecRepository powerSpecRepository, CoolerSpecRepository coolerSpecRepository,
							MainBoardSpecRepository mainBoardSpecRepository, StorageSpecRepository storageSpecRepository, UserRepository userRepository) {
		this.categoryRepository = categoryRepository;
		this.cpuSpecRepository = cpuSpecRepository;
		this.graphicSpecRepository = graphicSpecRepository;
		this.caseSpecRepository = caseSpecRepository;
		this.memorySpecRepository = memorySpecRepository;
		this.powerSpecRepository = powerSpecRepository;
		this.coolerSpecRepository = coolerSpecRepository;
		this.mainBoardSpecRepository = mainBoardSpecRepository;
		this.storageSpecRepository = storageSpecRepository;
		this.userRepository = userRepository;
	}

	@Test
	void contextLoads() {
		Category category = categoryRepository.findByName(CategoryName.CPU.toString()).get();
		User user = userRepository.findById(1L).get();
		for(int i = 0; i < 100; i++) {
			Product product = new Product();
			product.setCategory(category);
			product.setName("Product " + i);
			product.setUser(user);
			product.setInventory(100);
			product.setLogicalFK(1L);
			product.setPrice(10000);
			productRepository.save(product);
		}

	}

}
