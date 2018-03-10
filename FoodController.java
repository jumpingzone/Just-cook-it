package com.haha.test.controller;

import java.io.File;
import java.io.IOException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import com.google.gson.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.haha.test.dao.FoodDAO;
import com.haha.test.dao.MenuDAO;
import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.FoodCooking;
import com.haha.test.entity.FoodStock;
import com.haha.test.entity.Menu;
import com.haha.test.entity.Food;

@Controller
public class FoodController {

	ApplicationContext context = new ClassPathXmlApplicationContext("spring-module.xml");

	// show all by food list for customer
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ModelAndView getFoodList() {

		ModelAndView model = new ModelAndView("products");
		// logger.info("controller");
		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		
		List<Food> foodList = new ArrayList<Food>();
		foodList = foodDAO.getFoodList();
		
		List<Menu> menuRecommendList = new ArrayList<Menu>();
		menuRecommendList = menuDAO.getMenuListForRecommend();
		
		model.addObject(foodList);
		model.addObject("menuRecommendList", menuRecommendList);

		return model;
	}
	
	// show details by food_id through foodShowList for customer
	@RequestMapping(value = "/food_content", method = RequestMethod.GET)
	public ModelAndView getMenuId(@ModelAttribute("food_id") String food_id) {

		ModelAndView model = new ModelAndView("food_content");

		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");
		Food food = foodDAO.getFoodId(food_id);

		foodDAO.updateFoodClick(food);
		
		List<Food> foodRecommendList = new ArrayList<Food>();
		foodRecommendList = foodDAO.getFourFoodListForRecommend(Integer.toString(food.getFood_category_sub_id()));
		
		List<FoodCooking> foodCookingList = new ArrayList<FoodCooking>();
		foodCookingList = foodDAO.getFoodCooking(food_id);

		List<FoodStock> foodStockList = new ArrayList<FoodStock>();
		foodStockList = foodDAO.getFoodStockList(food_id);
		
		model.addObject("food", food);
		model.addObject("foodRecommendList", foodRecommendList);
		model.addObject("foodCookingList", foodCookingList);
		model.addObject("foodStockList", foodStockList);
		
		
		return model;
	}

	// show all by food list for background
	@RequestMapping(value = "/background_food", method = RequestMethod.GET)
	public ModelAndView getFoodListForBackground() {

		ModelAndView model = new ModelAndView("background_food");
		// logger.info("controller");
		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");

		List<Food> foodList = new ArrayList<Food>();
		foodList = foodDAO.getFoodForBackgroundList();
		
		model.addObject(foodList);

		return model;
	}
	
	// show all by food list for background
		@RequestMapping(value = "/chef_food", method = RequestMethod.GET)
		public ModelAndView getFoodListForCook() {

			ModelAndView model = new ModelAndView("chef_food");
			// logger.info("controller");
			FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");

			List<Food> foodList = new ArrayList<Food>();
			foodList = foodDAO.getFoodForBackgroundList();
			
			model.addObject(foodList);

			return model;
		}
	

	// Update Food
	// get food detail at first
	@RequestMapping(value = "/food_edit", method = RequestMethod.GET)
	public ModelAndView updateFoodPage(@ModelAttribute("food_id") String food_id) {

		ModelAndView model = new ModelAndView("food_edit");

		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");
		Food food = foodDAO.getFoodId(food_id);

		List<FoodCooking> foodCookingList = new ArrayList<FoodCooking>();
		foodCookingList = foodDAO.getFoodCooking(food_id);

		List<FoodStock> foodStockList = new ArrayList<FoodStock>();
		foodStockList = foodDAO.getFoodStockList(food_id);
		
		model.addObject("food", food);
		model.addObject("foodCookingList", foodCookingList);		
		model.addObject("foodStockList", foodStockList);
		
		
		return model;

	}

	// Update Food
	// Add data
	@RequestMapping(value = "/food_edit", method = RequestMethod.POST)
	// ModelAttribute
	public ModelAndView updateFood(@ModelAttribute Food food, @RequestParam("pic") MultipartFile food_picture,
			HttpServletRequest request) throws IOException {

		ModelAndView model = new ModelAndView("redirect:/background_food");
		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");

		if (food.getFood_cooking_id_array() != null) {
			// 依照逗號分割menu food字串
			String[] foodCookingIDArray = food.getFood_cooking_id_array().split(",");
			String[] foodCookingPriceArray = food.getFood_cooking_price_array().split(",");

			// 每一次新增之前都New一個MenuFood確保資料乾淨
			FoodCooking foodcooking = new FoodCooking();
			// 給一個List來按順序存MenuFood資料
			List<FoodCooking> aFoodCooking = new ArrayList<FoodCooking>();

			for (int i = 0; i < foodCookingIDArray.length; i++) {

				foodcooking.setFood_id(food.getFood_id());
				foodcooking.setFood_cooking_id(Integer.parseInt(foodCookingIDArray[i]));
				foodcooking.setFood_cooking_price(Integer.parseInt(foodCookingPriceArray[i]));

				aFoodCooking.add(foodcooking);

				foodDAO.foodCookingUpdate(foodcooking);
			}
		}

		//存貨的處理
		if(food.getFood_stock_id_array() != null) {
			String[] foodStockIdArray = food.getFood_stock_id_array().split(",");
			String[] foodStockAmountArray = food.getFood_stock_amount_array().split(",");
			
			FoodStock foodstock = new FoodStock();
			List<FoodStock> aFoodStock = new ArrayList<FoodStock>();
			
			for(int i = 0 ; i <foodStockIdArray.length ; i++) {
				foodstock.setFood_id(food.getFood_id());
				foodstock.setFood_stock_id(Integer.parseInt(foodStockIdArray[i]));
				foodstock.setFood_stock_amount(Integer.parseInt(foodStockAmountArray[i]));
				
				aFoodStock.add(foodstock);
				
				foodDAO.foodStockUpdate(foodstock);
			}
			
		}
		
		
		// 保存資料庫的路徑
		String sqlPath = null;
		// 定義文件保存的本地路徑
		String localPath = "/Users/jumpingzone/Documents/workspace/畢業專題/new/haha/src/main/webapp/resources/food_img/";
		// 定義文件名稱
		String filename = null;
		if (!food_picture.isEmpty()) {
			// 生成uuid作為文件名稱
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 獲得文件類型：如果不是图片，禁止上傳
			String contentType = food_picture.getContentType();
			// 獲得文件後缀名
			String suffixName = contentType.substring(contentType.indexOf("/") + 1);
			// 得到 文件名稱
			filename = uuid + "." + suffixName;
			System.out.println(filename);
			// 文件保存路徑
			food_picture.transferTo(new File(localPath + filename));
			//把图片的相对路径保存至数据库
	          sqlPath = "resources/food_img/"+filename;
				
	          foodDAO.foodUpdatePic(food, sqlPath);
	      }
		else
			foodDAO.foodUpdate(food);
		
		return model;

	}
	
	
	// Show Food for chef
	// get food detail at first
	@RequestMapping(value = "/chef_food_show", method = RequestMethod.GET)
	public ModelAndView showFoodPage(@ModelAttribute("food_id") String food_id) {

		ModelAndView model = new ModelAndView("chef_food_show");

		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");
		Food food = foodDAO.getFoodId(food_id);

		List<FoodCooking> foodCookingList = new ArrayList<FoodCooking>();
		foodCookingList = foodDAO.getFoodCooking(food_id);

		List<FoodStock> foodStockList = new ArrayList<FoodStock>();
		foodStockList = foodDAO.getFoodStockList(food_id);

		model.addObject("food", food);
		model.addObject("foodCookingList", foodCookingList);
		model.addObject("foodStockList", foodStockList);

		return model;

	}
		

	// Insert Food
	// get food category list at first
	@RequestMapping(value = "/food_new", method = RequestMethod.GET)
	public ModelAndView insertFoodPage() {

		ModelAndView model = new ModelAndView("food_new");

		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");

		List<FoodCategory> foodCategoryList = new ArrayList<FoodCategory>();
		foodCategoryList = foodDAO.getFoodCategoryList();

		List<FoodCategorySub> foodCategorySubList = new ArrayList<FoodCategorySub>();
		foodCategorySubList = foodDAO.getFoodCategorySubList();

		Gson gson = new Gson();
		String jFoodCategoryList = gson.toJson(foodCategoryList);
		String jFoodCategorySubList = gson.toJson(foodCategorySubList);

		Menu menu = new Menu();
		menu.setjFoodCategoryList(jFoodCategoryList);
		menu.setjFoodCategorySubList(jFoodCategorySubList);

		model.addObject("jFoodCategoryList", jFoodCategoryList);
		model.addObject("jFoodCategorySubList", jFoodCategorySubList);

		return model;

	}

	// Insert Food
	// Add data
	@RequestMapping(value = "/food_new", method = RequestMethod.POST)
	// ModelAttribute
	public ModelAndView insertFood(@ModelAttribute Food food, @RequestParam("pic") MultipartFile food_picture,
			HttpServletRequest request) throws IOException  {

		ModelAndView model = new ModelAndView("redirect:/background_food");
		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");


		// 保存資料庫的路徑
		String sqlPath = null;
		// 定義文件保存的本地路徑
		String localPath = "/Users/jumpingzone/Documents/workspace/畢業專題/new/haha/src/main/webapp/resources/food_img/";
		// 定義文件名稱
		String filename = null;
		if (!food_picture.isEmpty()) {
			// 生成uuid作為文件名稱
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 獲得文件類型：如果不是图片，禁止上傳
			String contentType = food_picture.getContentType();
			// 獲得文件後缀名
			String suffixName = contentType.substring(contentType.indexOf("/") + 1);
			// 得到 文件名稱
			filename = uuid + "." + suffixName;
			System.out.println(filename);
			// 文件保存路徑
			food_picture.transferTo(new File(localPath + filename));
		}
		// 把圖片的相對路徑保存至資料庫
		sqlPath = "resources/food_img/" + filename;

		// 先新增food取出food_id
		foodDAO.foodInsert(food , sqlPath);

		// Food Cooking
		// 依照逗號分割food cooking字串
		String[] foodCookingNameArray = food.getFood_cooking_name_array().split(",");
		String[] foodCookingPriceArray = food.getFood_cooking_price_array().split(",");

		// 每一次新增之前都New一個MenuFood確保資料乾淨
		FoodCooking foodcooking = new FoodCooking();
		// 給一個List來按順序存MenuFood資料
		List<FoodCooking> aFoodCooking = new ArrayList<FoodCooking>();

		for (int i = 0; i < foodCookingNameArray.length; i++) {

			foodcooking.setFood_id(food.getFood_id());
			foodcooking.setFood_cooking_name(foodCookingNameArray[i]);
			foodcooking.setFood_cooking_price(Integer.parseInt(foodCookingPriceArray[i]));

			aFoodCooking.add(foodcooking);

			foodDAO.foodCookingInsert(foodcooking);
		}
		
		return model;

	}

	
}
