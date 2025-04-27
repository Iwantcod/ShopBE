package com.example.shopPJT.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class ReqUpdateProductInfoDto {
    private String name;
    private int price;
    private MultipartFile productImage; // 실제 이미지 파일(바이트코드)을 받는다.
    private MultipartFile descriptionImage; // 실제 이미지 파일(바이트코드)을 받는다.
}
