package com.example.shopPJT;

import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // application.yml에 명시된 데이터베이스 설정을 따라감(기본값은 H2기 때문에 이러한 별도의 설정 필요)
@Rollback(false)
public class InitSettingScript {
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
    public InitSettingScript(CategoryRepository categoryRepository, CpuSpecRepository cpuSpecRepository, GraphicSpecRepository graphicSpecRepository, CaseSpecRepository caseSpecRepository, MemorySpecRepository memorySpecRepository, PowerSpecRepository powerSpecRepository, CoolerSpecRepository coolerSpecRepository, MainBoardSpecRepository mainBoardSpecRepository, StorageSpecRepository storageSpecRepository, UserRepository userRepository) {
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
    @Disabled
    void contextLoads() {
//         카테고리 정보 추가하는 스크립트
		CategoryName[] categoryName = {CategoryName.CPU, CategoryName.GRAPHIC, CategoryName.CASE, CategoryName.MEMORY, CategoryName.POWER,
				CategoryName.MAINBOARD, CategoryName.COOLER, CategoryName.STORAGE};
		for(int i = 0; i < categoryName.length; i++) {
			Category category = new Category();
            category.setId(i+1);
			category.setName(categoryName[i].toString());
			categoryRepository.save(category);
		}
    }

    @Test
    @Disabled
    @DisplayName("그래픽 카드 평균정보 갱신")
    void graphicAvgPriceUpdate() {
        graphicSpecRepository.updateAvgPrice();
    }
}
