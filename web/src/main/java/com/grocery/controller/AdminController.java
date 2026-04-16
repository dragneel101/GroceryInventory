package com.grocery.controller;

import com.grocery.model.Item;
import com.grocery.repository.TransactionRepository;
import com.grocery.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping({"/", "/inventory"})
    public String inventory(Model model) {
        model.addAttribute("items", itemService.findAll());
        model.addAttribute("totalItems", itemService.countAll());
        model.addAttribute("totalValue", String.format("%.2f", itemService.totalInventoryValue()));
        model.addAttribute("lowStockItems", itemService.getLowStockItems(5));
        model.addAttribute("lowStockCount", itemService.getLowStockItems(5).size());
        model.addAttribute("transactionCount", transactionRepository.count());
        return "admin/inventory";
    }

    @GetMapping("/item/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("isNew", true);
        return "admin/item-form";
    }

    @PostMapping("/item/save")
    public String saveItem(@ModelAttribute Item item, RedirectAttributes ra) {
        itemService.save(item);
        ra.addFlashAttribute("successMsg", "\"" + item.getDescription() + "\" added successfully.");
        return "redirect:/admin/inventory";
    }

    @GetMapping("/item/{id}/edit")
    public String editItemForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.findById(id));
        model.addAttribute("isNew", false);
        return "admin/item-form";
    }

    @PostMapping("/item/{id}/update")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes ra) {
        item.setId(id);
        itemService.save(item);
        ra.addFlashAttribute("successMsg", "\"" + item.getDescription() + "\" updated successfully.");
        return "redirect:/admin/inventory";
    }

    @PostMapping("/item/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes ra) {
        Item item = itemService.findById(id);
        String name = item.getDescription();
        itemService.delete(id);
        ra.addFlashAttribute("successMsg", "\"" + name + "\" deleted.");
        return "redirect:/admin/inventory";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String type,
                         @RequestParam(required = false) String query,
                         Model model) {
        if (type != null && query != null && !query.isBlank()) {
            model.addAttribute("results", itemService.search(type, query));
            model.addAttribute("searchType", type);
            model.addAttribute("searchQuery", query);
        }
        return "admin/search";
    }
}
