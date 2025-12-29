package toyproject.order.stocktest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.order.domain.Item;
import toyproject.order.domain.Member;
import toyproject.order.repository.ItemRepository;
import toyproject.order.repository.MemberRepository;
import toyproject.order.service.OrderService;
import toyproject.order.service.TestDataService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static toyproject.order.service.TestDataService.*;

@SpringBootTest
//@Transactional
public class StockConcurrencyTest {

    @Autowired
    OrderService orderService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private TestDataService testDataService;

    Long memberId;
    Long itemId;

    @BeforeEach
    void setUp() {
        TestIds ids = testDataService.init();
        memberId = ids.memberId();
        itemId = ids.itemId();
    }

    @Test
    public void 동시_주문_재고_음수_안됨() throws Exception{
        //given
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.order(memberId, itemId, 1);
                } catch (Exception e) {
                    // 실패시 정상 (재고 부족 or 락 타임아웃)
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Item findItem = itemRepository.findOne(itemId);
        Assertions.assertThat(findItem.getStockQuantity()).isEqualTo(0);
    }
}
