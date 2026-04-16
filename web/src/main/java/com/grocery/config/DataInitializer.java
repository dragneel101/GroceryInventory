package com.grocery.config;

import com.grocery.model.AppUser;
import com.grocery.model.Item;
import com.grocery.repository.AppUserRepository;
import com.grocery.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${seed.admin.username}")
    private String adminUsername;

    @Value("${seed.admin.password}")
    private String adminPassword;

    @Value("${seed.customer.username}")
    private String customerUsername;

    @Value("${seed.customer.password}")
    private String customerPassword;

    @Override
    public void run(String... args) {
        seedUsers();
        seedItems();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        AppUser employee = new AppUser();
        employee.setUsername(adminUsername);
        employee.setPassword(passwordEncoder.encode(adminPassword));
        employee.setRole("ROLE_EMPLOYEE");
        userRepository.save(employee);

        AppUser customer = new AppUser();
        customer.setUsername(customerUsername);
        customer.setPassword(passwordEncoder.encode(customerPassword));
        customer.setRole("ROLE_CUSTOMER");
        userRepository.save(customer);
    }

    private void seedItems() {
        if (itemRepository.count() > 0) return;

        String[][] data = {
            {"Whole Milk 1L",         "ITM001", "Dairy",     "4",  "Aisle 1", "50", "2.99"},
            {"Cheddar Cheese 500g",   "ITM002", "Dairy",     "4",  "Aisle 1", "30", "5.49"},
            {"White Bread",           "ITM003", "Bakery",    "20", "Aisle 2", "40", "3.29"},
            {"Chicken Breast 1kg",    "ITM004", "Meat",      "2",  "Aisle 3", "25", "8.99"},
            {"Bananas (bunch)",       "ITM005", "Produce",   "18", "Aisle 4", "60", "1.99"},
            {"Apples Fuji 1kg",       "ITM006", "Produce",   "10", "Aisle 4", "45", "3.49"},
            {"Orange Juice 2L",       "ITM007", "Beverages", "4",  "Aisle 5", "35", "4.29"},
            {"Pasta 500g",            "ITM008", "Dry Goods", "20", "Aisle 6", "80", "1.79"},
            {"Tomato Sauce 400g",     "ITM009", "Canned",    "20", "Aisle 6", "3",  "2.49"},
            {"Greek Yogurt 500g",     "ITM010", "Dairy",     "4",  "Aisle 1", "2",  "3.99"},
            {"Sourdough Loaf",        "ITM011", "Bakery",    "20", "Aisle 2", "15", "4.99"},
            {"Salmon Fillet 500g",    "ITM012", "Meat",      "2",  "Aisle 3", "20", "12.99"},
            {"Broccoli",              "ITM013", "Produce",   "5",  "Aisle 4", "40", "2.29"},
            {"Sparkling Water 6pk",   "ITM014", "Beverages", "20", "Aisle 5", "55", "3.99"},
            {"Brown Rice 1kg",        "ITM015", "Dry Goods", "20", "Aisle 6", "70", "2.49"},
        };

        for (String[] row : data) {
            Item item = new Item();
            item.setDescription(row[0]);
            item.setItemNumber(row[1]);
            item.setCategory(row[2]);
            item.setTemperature(Double.parseDouble(row[3]));
            item.setLocation(row[4]);
            item.setQuantity(Integer.parseInt(row[5]));
            item.setPrice(Double.parseDouble(row[6]));
            itemRepository.save(item);
        }
    }
}
