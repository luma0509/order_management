package toyproject.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.order.domain.Item;
import toyproject.order.domain.Member;
import toyproject.order.repository.ItemRepository;
import toyproject.order.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class TestDataService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public TestIds init() {
            Member member = new Member("member");
            memberRepository.save(member);

            Item item = new Item("item1", 10000, 1);
            itemRepository.save(item);
        return new TestIds(member.getId(), item.getId());
        }

    public record TestIds (Long memberId, Long itemId){
    }
}
