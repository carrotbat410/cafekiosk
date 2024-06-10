package sample.cafekiosk.spring.domain.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findBySellingTypeIn() {
        // given
        Product product1 = new Product("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", BOTTLE, HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", BAKERY, STOP_SELLING, "크로와상", 3500);

//        productRepository.saveAll(List.of(product1, product2, product3));
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        // when
        List<Product> products = productRepository.findAllBySellingTypeIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingType")//필요한것만 뽑기
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );

    }

    @Test
    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product product1 = new Product("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", BOTTLE, HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", BAKERY, STOP_SELLING, "크로와상", 3500);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingType")//필요한것만 뽑기
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );

    }
}