package com.mks.sageit.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mks.sageit.model.Item;
import com.mks.sageit.model.Order;
import com.mks.sageit.service.ItemService;
import com.mks.sageit.service.OrderService;

@Controller
public class StockController {
	@Autowired
	private ItemService service;

	@Autowired
	private OrderService orderService;

	// Show Heading as Nagigation bar
	@GetMapping("/navbar")
	public String getNavbar() {
		return "navbar";
	}

	// get all the items
	@GetMapping({ "/allitems", "/" })
	public String getAllitems(Model model) {
		model.addAttribute("items", service.findAll());
		return "allitems";
	}

	// get all the orders
	@GetMapping("/allorders")
	public String getAllorders(Model model) {
		model.addAttribute("orders", orderService.findAll());
		return "allorders";
	}

	// get new item
	@GetMapping("/newitem")
	public String getNewitem(Model model) {
		model.addAttribute("item", new Item());
		return "newitem";
	}

	// get new order
	@GetMapping("/neworder")
	public String newOrder(Model model) {
		model.addAttribute("order", new Order());
		model.addAttribute("items", service.findAll());
		return "order";
	}

	// Save order
	@GetMapping("/saveitem")
	public String saveItem(@ModelAttribute("item") Item item, Model model) {
		System.out.println("Save Item Starts");
		service.saveItem(item);
		model.addAttribute("items", service.findAll());
		System.out.println("Save Item Ends");
		return "allitems";

	}
	
	@GetMapping("/edititem")
	public String editItem(@RequestParam("id") int item_id, Model model) {
		System.out.println("Edit Item Starts"+ item_id);
		model.addAttribute("item", service.findOne(item_id));
		System.out.println("Edit Item Ends");
		return "edititem";

	}
	
	@GetMapping("/updateitem")
	public String updateItem(@ModelAttribute("item") Item item, Model model) {
		System.out.println("update Item Starts");
		service.saveItem(item);
		model.addAttribute("items", service.findAll());
		System.out.println("update Item Ends");
		return "allitems";

	}
	
	@GetMapping("/deleteitem")
	public String deleteItem(@RequestParam("id") int item_id, Model model) {
		System.out.println("Delete Item Starts"+ item_id);
		service.deleteById(item_id);
		model.addAttribute("items", service.findAll());
		System.out.println("Delete Item Ends");
		return "allitems";

	}

	// save the orders
	@GetMapping("/saveorder")
	public String saveOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttribute, Model model) {
		order.setOrderCreatedOn(new Date());
		Item item = service.findOne(order.getItem_id());
		System.out.println(item);

		int current_stock = item.getCurrent_stock();
		System.out.println(current_stock);

		int quantity = order.getQuantity();
		System.out.println(quantity);

		if (quantity <= current_stock) {
			current_stock -= quantity;
			System.out.println("after reducing: " + current_stock);
			item.setCurrent_stock(current_stock);

			service.saveItem(item);
			orderService.saveOrder(order);
			model.addAttribute("order", orderService.findAll());
			model.addAttribute("success", "Order created successfully!");
			model.addAttribute("alertClass", "alert-sucess");
			return "allorders";
		}

		else {
			model.addAttribute("error", "Error Quantity can not be greater than current stock! ");
			model.addAttribute("alertClass", "alert-danger");
			model.addAttribute("order", orderService.getByID(order.getOrderID()));
			return "order";

		}

	}
}
