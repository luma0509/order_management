package toyproject.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.order.domain.Item;
import toyproject.order.domain.Member;
import toyproject.order.domain.Order;
import toyproject.order.domain.OrderItem;
import toyproject.order.repository.ItemRepository;
import toyproject.order.repository.MemberRepository;
import toyproject.order.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * 주문 생성
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 1. 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOneWithLock(itemId); // LOCK -> stockQuantity 조회

        // 재고 차감 (재고 검증)
        item.removeStock(count);

        // 2. 주문 상품 생성
        OrderItem orderItem = new OrderItem(item, item.getPrice(), count);

        // 3. 주문 생성 (연결 + 규칙은 Order 담당)
        Order order = Order.createOrder(member, orderItem);

        // 4. 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 + 회원 + 주문상품 + 상품을 fetch join으로 조회
     * - LAZY 로딩으로 인한 N+1 문제 해결
     * - 컬렉션 fetch join으로 중복 제거를 위해 distinct 사용
     */
    public List<Order> findOrdersWithItems() {
        return orderRepository.findAllWithItems();
    }

    // LAZY 로딩으로 인한 N+1
    public List<Order> findOrdersLazy() {
        return orderRepository.findAll();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
}
