package toyproject.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // 재고 차감
    public void removeStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }

    // 재고 복구
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
