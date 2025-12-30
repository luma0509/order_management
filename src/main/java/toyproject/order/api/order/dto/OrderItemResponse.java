package toyproject.order.api.order.dto;

public record OrderItemResponse(
      String itemName,
      int orderPrice,
      int count
) {
}