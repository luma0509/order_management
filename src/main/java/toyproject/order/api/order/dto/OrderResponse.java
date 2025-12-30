package toyproject.order.api.order.dto;

import lombok.Getter;
import toyproject.order.domain.Order;
import toyproject.order.domain.OrderItem;
import toyproject.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemResponse> items;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.memberName = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .toList();
    }

    @Getter
    public static class OrderItemResponse {
        private String itemName;
        private int price;
        private int count;

        public OrderItemResponse(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.price = orderItem.getOrderPrice(); // 너 README는 price로 내려가니까 매핑
            this.count = orderItem.getCount();
        }
    }
}