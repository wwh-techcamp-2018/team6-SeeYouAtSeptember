package com.woowahan.moduchan.domain;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import org.aspectj.weaver.ast.Or;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderHistoryTest {

    private OrderHistory orderHistory;

    private UserDTO normalUserDTO;

    private ProductDTO productDTO;

    private OrderHistoryDTO orderHistoryDTO;

    private ProjectDTO projectDTO;

    private static Long todayTime;

//    @Before
//    public void setUp() throws Exception {
//        todayTime =
//        orderHistoryDTO = new OrderHistoryDTO(null,null,null,null,1L,null,null);
//        normalUserDTO = new UserDTO(1L, "test@naver.com", "qwe123!!", "테스트", "010-1234-1234", "올림픽로 295");
//        productDTO = new ProductDTO(null, "테스트상품", 1000L, 10L, "테스트입니다", null, null);
//        projectDTO =new ProjectDTO(2L, null, "프로젝트", "프로젝트설명", "https://moduchan-img.s3.ap-northeast-2.amazonaws.com/media/53e15324-3732-444c-8528-468d41cf160a.png", null
//                ,todayTime + ONE_DAY_DIFF * 31, null, null, productDTOs, 1000000L, 1000000L);
//    }
//
//    @Test
//    public void orderHistory_상태_변화_성공() {
//        OrderHistory orderHistory = OrderHistory.from(orderHistoryDTO,normalUser,product,UUID.randomUUID().toString());
//        assertThat(orderHistory.changeOrderStatusSuccess().getStatus()).isEqualTo(OrderHistory.STATUS.SUCCESS);
//    }
//
//    @Test
//    public void orderHistory_상태_변화_실패() {
//        OrderHistory orderHistory = OrderHistory.from(orderHistoryDTO,normalUser,product,UUID.randomUUID().toString());
//        orderHistory.updatePendingOrderIntoFail();
//        assertThat(orderHistory.getStatus()).isEqualTo(OrderHistory.STATUS.FAIL);
//    }
//
//    @Test
//    public void orderHistory_후원한_유저_상태별로_가져오기() {
//        OrderHistory orderHistory = OrderHistory.from(orderHistoryDTO,normalUser,product,UUID.randomUUID().toString());
//        assertThat(orderHistory.getNormalUser(OrderHistory.STATUS.PENDING)).isEqualTo(normalUser);
//
//        assertThat(orderHistory.getNormalUser(OrderHistory.STATUS.SUCCESS)).isEqualTo(null);
//
//        assertThat(orderHistory.getNormalUser(OrderHistory.STATUS.FAIL)).isEqualTo(null);
//
//        orderHistory.changeOrderStatusSuccess();
//        assertThat(orderHistory.getNormalUser(OrderHistory.STATUS.SUCCESS)).isEqualTo(normalUser);
//
//        orderHistory.updatePendingOrderIntoFail();
//        assertThat(orderHistory.getNormalUser(OrderHistory.STATUS.FAIL)).isEqualTo(normalUser);
//    }
}
