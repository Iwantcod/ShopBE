# 컴퓨터 부품 거래 플랫폼 BE
컴퓨터 부품 거래 플랫폼 BE
> **개발기간: 2025.04.15. ~ ing**

---
## 📚 STACKS

<div>
  <img src="https://img.shields.io/badge/Java%2021-007396?style=for-the-badge&logo=Java&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot%203.4-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white" alt="Spring Boot 3.4"><br>
  <img src="https://img.shields.io/badge/MariaDB%2015.1-003545?style=for-the-badge&logo=MariaDB&logoColor=white" alt="MariaDB 15.1"><br>
  <img src="https://img.shields.io/badge/Raspberry%20Pi-A22846?style=for-the-badge&logo=RaspberryPi&logoColor=white" alt="Raspberry Pi 4b">
</div>

---
## 📝 API 명세서 및 ERD
> **ERD** : [ERD Cloud](https://www.erdcloud.com/d/obR5XazviS6jRyov8) <br>
> **API** : [Swagger UI](https://iwantcod.github.io/shopAPI/) <br>

---
## 📌 개요
기존에는 소비자가 컴퓨터 부품에 대한 지식이 없다면 견적을 맞출 때 호환성이 맞는지, 성능은 어느정도 되는지에 대해 정확하게 파악하기 어려운 경우가 많았습니다. 이로 인해 구매를 남에게 위임하거나 정해져있는 완성품만 선택하는 등 구매에 제약이 있었습니다.
<br>
<br>
👉 이를 개선하기 위해 사용자의 용도 및 예산에 맞는 컴퓨터 견적을 추천하여 제공하고, 해당 견적의 수치적인 벤치마크 정보를 제공하는 플랫폼을 개발하였습니다.

---
# 🔍 주요 기능
## ⭐️ 성능 정보 제공
- CPU, GPU 조합의 실측 벤치마크 수치 정보를 제공합니다.<br>
- 장바구니, 주문내역, 견적추천 메뉴에서 확인할 수 있습니다.
## ⭐ 맞춤 견적 추천</h3>
- 사용자가 입력한 예산과 용도 정보를 통해 조합할 수 있는 최대 성능의 컴퓨터 견적을 본 플랫폼 내의 상품으로 추천하여 제공합니다.<br>
- 사용자는 추천 견적의 총 가격과 성능 정보를 확인할 수 있으며, 빠르게 구매할 수 있습니다.
## ⭐ 유저 타입 별 CRUD 권한 분리
- 관리자: 모든 CRUD 권한 및 판매자 권한 승인 권한
- 판매자: 자신의 상품 및 리뷰 CRUD 권한
## 상품 업로드
- 상품 업로드 시 '대표 이미지'와 '상세 페이지 이미지'를 업로드해야 합니다. 상세 페이지는 세로로 긴 하나의 이미지로 대체합니다.

---
# 아키텍처
## 디렉토리 구조
```bash
.
├── java
│   └── com
│       └── example
│           └── shopPJT
│               ├── ShopPjtApplication.java
│               ├── admin
│               │   └── controller
│               │       └── AdminController.java # 관리자 API
│               ├── benchmark
│               │   ├── controller
│               │   │   └── BenchMarkController.java # 벤치마크 성능 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   └── BenchMark.java
│               │   ├── repository
│               │   │   └── BenchMarkRepository.java
│               │   └── service
│               │       └── BenchMarkService.java
│               ├── businessInfo
│               │   ├── controller
│               │   │   └── BusinessInfoController.java # 판매자 정보 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   └── BusinessInfo.java
│               │   ├── repository
│               │   │   └── BusinessInfoRepository.java
│               │   └── service
│               │       └── BusinessInfoService.java
│               ├── cart
│               │   ├── controller
│               │   │   └── CartController.java # 장바구니 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   └── Cart.java
│               │   ├── repository
│               │   │   └── CartRepository.java
│               │   └── service
│               │       └── CartService.java
│               ├── config
│               │   ├── SecurityConfig.java # Spring Security 및 Bean 설정
│               │   └── SwaggerConfig.java # Swagger UI 설정
│               ├── filter
│               │   ├── JwtFilter.java # 'OncePerRequestFilter' 클래스 상속
│               │   └── LoginFilter.java # 'UsernamePasswordAuthenticationFilter' 클래스 상속
│               ├── global
│               │   └── exception
│               │       ├── ApplicationError.java # 발생시킬 예외 정의한 Enum(HttpStatus, String)
│               │       ├── ApplicationException.java # RuntimeException 클래스 상속
│               │       └── GlobalExceptionHandler.java # RestControllerAdvice
│               ├── oauth2 # OAuth2 설정 및 성공/실패 핸들러
│               │   ├── ...
│               ├── order
│               │   ├── controller
│               │   │   └── OrderController.java # 주문 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   ├── DeliveryStatus.java # 배송 상태 Enum
│               │   │   └── Order.java
│               │   ├── repository
│               │   │   └── OrderRepository.java
│               │   └── service
│               │       └── OrderService.java
│               ├── orderItems
│               │   ├── controller
│               │   │   └── OrderItemsController.java # 주문 요소 API
│               │   ├── dto
│               │   │   └── ...
│               │   ├── entity
│               │   │   └── OrderItems.java
│               │   ├── repository
│               │   │   └── OrderItemsRepository.java
│               │   └── service
│               │       └── OrderItemsService.java
│               ├── product
│               │   ├── controller
│               │   │   ├── CategoryController.java # 카테고리 API
│               │   │   └── ProductController.java # 상품 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   ├── Category.java
│               │   │   ├── CategoryName.java # 카테고리 이름 Enum
│               │   │   └── Product.java
│               │   ├── repository
│               │   │   ├── CategoryRepository.java
│               │   │   └── ProductRepository.java
│               │   └── service
│               │       ├── CategoryService.java
│               │       └── ProductService.java
│               ├── productSpec # 8개 카테고리의 부품 상세 정보 테이블 및 API
│               │   ├── controller
│               │   │   └── ProductSpecController.java # 부품 상세 정보 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   ├── CaseSpec.java
│               │   │   ├── CoolerSpec.java
│               │   │   ├── CpuSpec.java
│               │   │   ├── GraphicSpec.java
│               │   │   ├── MainBoardSpec.java
│               │   │   ├── MemorySpec.java
│               │   │   ├── PowerSpec.java
│               │   │   ├── Spec.java
│               │   │   └── StorageSpec.java
│               │   ├── repository
│               │   │   ├── ...
│               │   └── service
│               │       ├── ProductSpecServiceFactory.java # 8개 구현체를 Map 자료구조(Read Only)로 모아 필요할 때마다 사용
│               │       ├── ProductSpecServiceStrategy.java # 부품 정보 CRUD 로직 인터페이스(제네릭)
│               │       └── impl # 8개의 구현체
│               │           ├── ...
│               ├── recommendedOriginal # 추천 견적 원본: 8개의 모델 부품 테이블의 식별자로 구성
│               │   ├── controller
│               │   │   └── RecommendedOriginalController.java # 추천 견적 원본 API
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   └── RecommendedOriginal.java
│               │   ├── repository
│               │   │   └── RecommendedOriginalRepository.java
│               │   └── service
│               │       └── RecommendedOriginalService.java
│               ├── recommendedProduct # 추천 견적 실제 상품: 8개 카테고리의 실제 상품 식별자로 구성
│               │   ├── controller
│               │   ├── dto
│               │   ├── entity
│               │   │   └── RecommendedProduct.java
│               │   ├── repository
│               │   │   └── RecommendedProductRepository.java
│               │   └── service
│               │       └── RecommendedProductService.java
│               ├── recommendedUsage # 추천 견적 용도
│               │   ├── controller
│               │   ├── dto
│               │   ├── entity
│               │   │   └── RecommendedUsage.java
│               │   ├── repository
│               │   └── service
│               ├── review
│               │   ├── controller
│               │   │   └── ReviewController.java # 리뷰 API
│               │   ├── dto
│               │   │   ├── ReqReviewDto.java
│               │   │   ├── ReqUpdateReviewDto.java
│               │   │   └── ResReviewDto.java
│               │   ├── entity
│               │   │   └── Review.java
│               │   ├── repository
│               │   │   └── ReviewRepository.java
│               │   └── service
│               │       └── ReviewService.java
│               ├── seller
│               │   └── controller
│               │       └── SellerController.java # 판매자 API
│               ├── user
│               │   ├── controller
│               │   │   ├── AuthController.java # 인증/인가, 회원가입 관련 API
│               │   │   └── UserController.java # 회원 API
│               │   ├── details
│               │   │   └── JwtUserDetails.java
│               │   ├── dto
│               │   │   ├── ...
│               │   ├── entity
│               │   │   ├── RoleType.java
│               │   │   └── User.java
│               │   ├── repository
│               │   │   └── UserRepository.java
│               │   └── service
│               │       ├── JwtUserDetailsService.java
│               │       └── UserService.java
│               └── util
│                   ├── AuthUtil.java # Security Context에 저장된 유저 인증 정보 파싱 Util
│                   └── JwtUtil.java # JWT 생성 및 파싱 Util
└── resources
    ├── application-dev.yml # 테스트 배포 시 프로파일
    ├── application-local.yml # 로컬 구동 시 프로파일:기본값
    ├── application-prod.yml # 운영 프로파일
    ├── application.yml # 루트 프로파일
    ├── static
    └── templates
