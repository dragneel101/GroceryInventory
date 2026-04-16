package com.grocery.controller;

import com.grocery.model.*;
import com.grocery.repository.TransactionRepository;
import com.grocery.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private TransactionRepository transactionRepository;

    @ModelAttribute("cart")
    public List<CartItem> createCart() {
        return new ArrayList<>();
    }

    // ── Browse ──────────────────────────────────────────────────────────────

    @GetMapping({"/", "/browse"})
    public String browse(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        model.addAttribute("items", itemService.findAll());
        model.addAttribute("cartCount", cart.size());
        return "cart/browse";
    }

    // ── Add / Remove ────────────────────────────────────────────────────────

    @PostMapping("/add/{itemId}")
    public String addToCart(@PathVariable Long itemId,
                            @ModelAttribute("cart") List<CartItem> cart,
                            @RequestParam(defaultValue = "browse") String from,
                            RedirectAttributes ra) {
        Item item = itemService.findById(itemId);
        boolean found = false;
        for (CartItem ci : cart) {
            if (ci.getItemId().equals(itemId)) {
                ci.setQuantity(ci.getQuantity() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(item.getId(), item.getDescription(), item.getPrice(), 1));
        }
        ra.addFlashAttribute("successMsg", "\"" + item.getDescription() + "\" added to cart.");
        return "redirect:/cart/" + from;
    }

    @PostMapping("/remove/{index}")
    public String removeFromCart(@PathVariable int index,
                                  @ModelAttribute("cart") List<CartItem> cart) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        return "redirect:/cart/view";
    }

    @PostMapping("/clear")
    public String clearCart(@ModelAttribute("cart") List<CartItem> cart) {
        cart.clear();
        return "redirect:/cart/browse";
    }

    // ── View Cart ───────────────────────────────────────────────────────────

    @GetMapping("/view")
    public String viewCart(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        double total = cart.stream().mapToDouble(CartItem::getSubtotal).sum();
        model.addAttribute("total", String.format("%.2f", total));
        return "cart/cart";
    }

    // ── Checkout ────────────────────────────────────────────────────────────

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("cart") List<CartItem> cart,
                           Authentication authentication,
                           SessionStatus sessionStatus,
                           Model model) {
        if (cart.isEmpty()) {
            return "redirect:/cart/view";
        }

        Transaction transaction = new Transaction();
        transaction.setCustomerUsername(authentication.getName());
        transaction.setTransactionDate(LocalDateTime.now());

        double total = 0;
        for (CartItem ci : cart) {
            TransactionItem ti = new TransactionItem();
            ti.setTransaction(transaction);
            ti.setDescription(ci.getDescription());
            ti.setPrice(ci.getPrice());
            ti.setQuantity(ci.getQuantity());
            transaction.getItems().add(ti);
            total += ci.getSubtotal();

            // Reduce stock
            Item item = itemService.findById(ci.getItemId());
            item.setQuantity(Math.max(0, item.getQuantity() - ci.getQuantity()));
            itemService.save(item);
        }
        transaction.setTotal(total);
        Transaction saved = transactionRepository.save(transaction);

        sessionStatus.setComplete();

        model.addAttribute("transaction", saved);
        model.addAttribute("total", String.format("%.2f", total));
        return "cart/bill";
    }

    // ── Search ──────────────────────────────────────────────────────────────

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String type,
                         @RequestParam(required = false) String query,
                         @ModelAttribute("cart") List<CartItem> cart,
                         Model model) {
        if (type != null && query != null && !query.isBlank()) {
            model.addAttribute("results", itemService.search(type, query));
            model.addAttribute("searchType", type);
            model.addAttribute("searchQuery", query);
        }
        model.addAttribute("cartCount", cart.size());
        return "cart/search";
    }
}
