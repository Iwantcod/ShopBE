package com.example.shopPJT;

import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
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


        // 기존에 존재하는 Spec 테이블들의 모든 행 제거: modelName이 UK이기 때문에, 충돌 방지를 위함.
        cpuSpecRepository.deleteAll();
        graphicSpecRepository.deleteAll();
        caseSpecRepository.deleteAll();
        memorySpecRepository.deleteAll();
        powerSpecRepository.deleteAll();
        coolerSpecRepository.deleteAll();
        mainBoardSpecRepository.deleteAll();
        storageSpecRepository.deleteAll();
        // 각 Spec 테이블마다 테스트를 위한 튜플 10개씩 삽입하는 스크립트
        for (int i = 1; i <= 10; i++) {
            CpuSpec cpu = new CpuSpec();
            cpu.setModelName("cpuModel-" + i);
            cpu.setCoreNum(i + 4);
            cpu.setThreadNum(i + 8);
            cpu.setL3Cache(i + 2);
            cpu.setBoostClock(i * 100);
            cpu.setBoostClock(i * 200);
            cpu.setManufacturer("cpuMfg" + i);
            cpuSpecRepository.save(cpu);

            GraphicSpec gpu = new GraphicSpec();
            gpu.setModelName("gpuModel-" + i);
            gpu.setChipSetType("chipType" + i);
            gpu.setChipSetManufacturer("chipMfg" + i);
            gpu.setSeries("series" + i);
            gpu.setRecommendPower(i * 50);
            gpu.setCoreClock(i + 2);
            gpu.setBoostClock(i + 4);
            gpu.setVram(i + 6);
            gpu.setManufacturer("gpuMfg" + i);
            graphicSpecRepository.save(gpu);

            CaseSpec caseSpec = new CaseSpec();
            caseSpec.setModelName("caseModel-" + i);
            caseSpec.setSize(i + 10);
            caseSpec.setManufacturer("caseMfg" + i);
            caseSpec.setInnerSpace(100);
            caseSpecRepository.save(caseSpec);

            MemorySpec memory = new MemorySpec();
            memory.setModelName("memModel-" + i);
            memory.setGroups("group" + i);
            memory.setCl(i + 8);
            memory.setVolume(i * 512);
            memory.setManufacturer("memMfg" + i);
            memorySpecRepository.save(memory);

            PowerSpec power = new PowerSpec();
            power.setModelName("powerModel-" + i);
            power.setRatedOutputPower(i * 50);
            power.setGroups("group" + i);
            power.setManufacturer("powerMfg" + i);
            powerSpecRepository.save(power);

            CoolerSpec cooler = new CoolerSpec();
            cooler.setModelName("coolerModel-" + i);
            cooler.setSize(i + 30);
            cooler.setFanSpeed(i * 3);
            cooler.setNoise(i * 2);
            cooler.setGroups("group" + i);
            cooler.setManufacturer("coolerMfg" + i);
            coolerSpecRepository.save(cooler);

            MainBoardSpec mb = new MainBoardSpec();
            mb.setModelName("mbModel-" + i);
            mb.setChipSetType("chipset" + i);
            mb.setCpuSocket("socket" + i);
            mb.setMosFet(i);
            mb.setManufacturer("mbMfg" + i);
            mb.setGroups("group" + i);
            mainBoardSpecRepository.save(mb);

            StorageSpec ssd = new StorageSpec();
            ssd.setModelName("StorageModel-" + i);
            ssd.setFormFactorType("form" + i);
            ssd.setVolume(i * 1000);
            ssd.setFanSpeed(i * 3);
            ssd.setGroups("group" + i);
            ssd.setManufacturer("ssdMfg" + i);
            storageSpecRepository.save(ssd);
        }
    }
}
