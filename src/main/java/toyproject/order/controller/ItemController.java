package toyproject.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.order.domain.Item;
import toyproject.order.service.ItemService;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public String createItem(String name, int price, int stockQuantity) {
        Item item = new Item(name, price, stockQuantity);
        itemService.saveItem(item);
        return "redirct:/";
    }

    @GetMapping("/items")
    public String itemList(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "/items/itemList";
    }
}
