package toyproject.order.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.order.domain.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        em.persist(item);
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public Item findOneWithLock(Long id) {
        return em.createQuery(
                        "select i from Item i where i.id = :id", Item.class)
                .setParameter("id", id)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .setHint("jakarta.persistence.lock.timeout", 3000)
                .getSingleResult();
    }

    public Item findOneWithOptimisticLock(Long id) {
        return em.find(Item.class, id, LockModeType.OPTIMISTIC);
    }
}