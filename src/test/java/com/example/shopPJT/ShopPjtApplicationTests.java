package com.example.shopPJT;

import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
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

	}

}
