package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.dto.MenuResponse;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        Long menuId = menuService.create(MENU_REQUEST);

        assertThat(menuId).isNotNull();
    }

    @Test
    void list() {
        menuService.create(MENU_REQUEST);
        List<MenuResponse> list = menuService.list();

        assertThat(list.isEmpty()).isFalse();
    }
}