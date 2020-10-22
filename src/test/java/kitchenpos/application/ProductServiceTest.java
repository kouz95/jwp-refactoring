package kitchenpos.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.factory.ProductFactory;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private ProductFactory productFactory;

    private Product product;

    @BeforeEach
    void setUp() {
        productFactory = new ProductFactory();
        product = productFactory.create(1L, "강정치킨", BigDecimal.valueOf(17_000L));
    }

    @DisplayName("상품 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("상품을 생성한다.", this::createSuccess),
                dynamicTest("상품을 생성 요청의 가격이 존재하지 않을때, IllegalArgumentException 발생.", this::noPrice),
                dynamicTest("상품을 생성 요청의 가격이 음수일 때, IllegalArgumentException 발생.",
                        this::invalidPrice)
        );
    }

    @DisplayName("상품 전체 조회")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(singletonList(product));

        List<Product> list = productService.list();
        assertAll(
                () -> assertThat(list.get(0).getId()).isEqualTo(product.getId()),
                () -> assertThat(list.get(0).getName()).isEqualTo(product.getName()),
                () -> assertThat(list.get(0).getPrice()).isEqualTo(product.getPrice())
        );
    }

    private void createSuccess() {
        Product request = productFactory.create("강정치킨", BigDecimal.valueOf(17_000L));

        given(productDao.save(request)).willReturn(product);

        assertThat(productService.create(request).getId()).isEqualTo(product.getId());
    }

    private void noPrice() {
        Product request = productFactory.create("강정치킨");

        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }

    private void invalidPrice() {
        Product request = productFactory.create("강정치킨", BigDecimal.valueOf(-1L));

        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }
}