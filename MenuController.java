package com.haha.test.controller;
import java.io.File;
import java.io.IOException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.haha.test.dao.FoodDAO;
import com.haha.test.dao.MenuDAO;
import com.haha.test.entity.CustomerMenu;
import com.haha.test.entity.Food;
import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.FoodStock;
import com.haha.test.entity.Menu;
import com.haha.test.entity.MenuFood;
import com.haha.test.entity.MenuStock;

@Controller
public class MenuController {
	
	ApplicationContext context =  new ClassPathXmlApplicationContext("spring-module.xml");
	
	//show all by menulist for customer
	@RequestMapping(value = "/products2", method = RequestMethod.GET)
	public ModelAndView getMenuList(){
	
		ModelAndView model = new ModelAndView("products2");
		//logger.info("controller");
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		FoodDAO foodDAO = (FoodDAO) context.getBean("foodDAO");
		
		List<Menu> menuList = new ArrayList<Menu>();
		menuList = menuDAO.getMenuList();
		
		List<Food> foodRecommendList = new ArrayList<Food>();
		foodRecommendList = foodDAO.getFoodListForRecommend();
		
		model.addObject(menuList);
		model.addObject("foodRecommendList", foodRecommendList);
		
		return model;
	}
	
	
	//show details by menu_id through menuShowList for customer
	@RequestMapping(value = "/cusine", method = RequestMethod.GET)
	public ModelAndView getMenuId(@ModelAttribute("menu_id") String menu_id){
		
		ModelAndView model = new ModelAndView("cusine");
		
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		Menu menu = menuDAO.getMenuId(menu_id);
		menuDAO.updateMenuClick(menu);
		
		List<MenuFood> menuFoodList = new ArrayList<MenuFood>();
		menuFoodList = menuDAO.getMenuFood(menu_id);
		
		List<MenuStock> menuStockList = new ArrayList<MenuStock>();
		menuStockList = menuDAO.getMenuStockList(menu_id);
		
		List<Menu> menuRecommendList = new ArrayList<Menu>();
		menuRecommendList = menuDAO.getFourMenuListForRecommend();
		
		model.addObject("menu" , menu);
		model.addObject("menuFoodList" , menuFoodList);
		model.addObject("menuStockList" , menuStockList);
		model.addObject("menuRecommendList", menuRecommendList);
			
		return model;
	}
	
	
	//show all by menulist for background
	@RequestMapping(value = "/background_cusine", method = RequestMethod.GET)
	public ModelAndView getMenuListForBackground(){
		
		ModelAndView model = new ModelAndView("background_cusine");
		//logger.info("controller");
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
			
		List<Menu> menuList = new ArrayList<Menu>();
		menuList = menuDAO.getMenuForBackgroundList();
			
			
		model.addObject(menuList);
			
		return model;
	}
		
	
	// show all by menu list for chef
	@RequestMapping(value = "/chef_cusine", method = RequestMethod.GET)
	public ModelAndView getMenuListForCook() {

		ModelAndView model = new ModelAndView("chef_cusine");
		// logger.info("controller");
		MenuDAO menuDAO = (MenuDAO) context.getBean("menuDAO");

		List<Menu> menuList = new ArrayList<Menu>();
		menuList = menuDAO.getMenuForBackgroundList();

		model.addObject(menuList);

		return model;
	}
	
	//Insert Menu
	@RequestMapping(value = "/cusine_new", method = RequestMethod.GET)
	public ModelAndView insertMenuPage(){
	
		ModelAndView model = new ModelAndView("cusine_new");
		
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		FoodDAO foodDAO = (FoodDAO)context.getBean("foodDAO");
		
		List<FoodCategory> foodCategoryList = new ArrayList<FoodCategory>();
		foodCategoryList = menuDAO.getFoodCategoryList();
		
		List<FoodCategorySub> foodCategorySubList = new ArrayList<FoodCategorySub>();
		foodCategorySubList = menuDAO.getFoodCategorySubList();
		
		List<Food> foodList = new ArrayList<Food>();
		foodList = foodDAO.getList();

		
		Gson gson = new Gson();
		String jFoodCategoryList = gson.toJson(foodCategoryList);
		String jFoodCategorySubList = gson.toJson(foodCategorySubList);
		String jFoodList = gson.toJson(foodList);
		
		Menu menu = new Menu();
		menu.setjFoodCategoryList(jFoodCategoryList);
	    menu.setjFoodCategorySubList(jFoodCategorySubList);
	    menu.setjFoodList(jFoodList);
	    	
	    
		
		//連動待補
		model.addObject("jFoodCategoryList" , jFoodCategoryList);
		model.addObject("jFoodCategorySubList" , jFoodCategorySubList);
		model.addObject("jFoodList" , jFoodList);
		
		return model;
	
	}
	
	// Insert Menu	
	@RequestMapping(value = "/cusine_new", method = RequestMethod.POST)
	public ModelAndView insertMenu(@ModelAttribute Menu menu, @RequestParam("pic") MultipartFile menu_picture,
			HttpServletRequest request) throws IOException{
		
		ModelAndView model = new ModelAndView("redirect:/background_cusine");
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		
		// 保存資料庫的路徑
		String sqlPath = null;
		// 定義文件保存的本地路徑
		String localPath = "/Users/jumpingzone/Documents/workspace/畢業專題/new/haha/src/main/webapp/resources/menu_img/";
		// 定義文件名稱
		String filename = null;
		if (!menu_picture.isEmpty()) {
			// 生成uuid作為文件名稱
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 獲得文件類型：如果不是图片，禁止上傳
			String contentType = menu_picture.getContentType();
			// 獲得文件後缀名
			String suffixName = contentType.substring(contentType.indexOf("/") + 1);
			// 得到 文件名稱
			filename = uuid + "." + suffixName;
			System.out.println(filename);
			// 文件保存路徑
			menu_picture.transferTo(new File(localPath + filename));
		}
		// 把圖片的相對路徑保存至資料庫
		sqlPath = "resources/menu_img/" + filename;

		
		//先新增menu取出menu_id
		menuDAO.menuInsert(menu, sqlPath);
		
		if (menu.getMenu_food_id_array() != null) {
			// 依照逗號分割menu food字串
			// String [] menuFoodNameArray = menu.getMenu_food_name_array().split(",");
			String[] menuFoodAmountArray = menu.getMenu_food_amount_array().split(",");
			String[] foodIdArray = menu.getFood_id_array().split(",");

			// 每一次新增之前都New一個MenuFood確保資料乾淨
			MenuFood menufood = new MenuFood();
			// 給一個List來按順序存MenuFood資料
			List<MenuFood> aMenuFood = new ArrayList<MenuFood>();

			for (int i = 0; i < menuFoodAmountArray.length; i++) {

				// 把資料加進物件
				menufood.setMenu_id(menu.getMenu_id());
				// menufood.setMenu_food_name(menuFoodNameArray[i]);
				menufood.setFood_id(Integer.parseInt(foodIdArray[i]));
				menufood.setMenu_food_amount(menuFoodAmountArray[i]);

				aMenuFood.add(menufood);

				menuDAO.menuFoodInsert(menufood);
			}
		}
		
		return model;
		
	}
	
	
	
	
	
	//Update Menu
	//get menu detail at first
	@RequestMapping(value = "/cusine_edit", method = RequestMethod.GET)
	public ModelAndView updateMenuPage(@ModelAttribute("menu_id") String menu_id){
		//System.out.print("進入UPDATE MENU");
		ModelAndView model = new ModelAndView("cusine_edit");
		
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		Menu menu = menuDAO.getMenuId(menu_id);
		
		List<MenuFood> menuFoodList = new ArrayList<MenuFood>();
		menuFoodList = menuDAO.getMenuFood(menu_id);
		
		List<FoodCategorySub> foodCategorySubList = new ArrayList<FoodCategorySub>();
		foodCategorySubList = menuDAO.getFoodCategorySubList();
		
		List<MenuStock> menuStockList = new ArrayList<MenuStock>();
		menuStockList = menuDAO.getMenuStockList(menu_id);
		
		model.addObject("menu" , menu);
		model.addObject("menuFoodList" , menuFoodList);
		model.addObject("menuStockList" , menuStockList);
		model.addObject("foodCategorySubList" , foodCategorySubList);
		
		
		return model;

		
	}
	
	//Update Menu
	//Add data 
	@RequestMapping(value = "/cusine_edit", method = RequestMethod.POST)
	//ModelAttribute
	public ModelAndView updateMenu(@ModelAttribute Menu menu, @RequestParam("pic") MultipartFile menu_picture,
			HttpServletRequest request) throws IOException{
		
		ModelAndView model = new ModelAndView("redirect:/background_cusine");
		MenuDAO menuDAO = (MenuDAO)context.getBean("menuDAO");
		

		if (menu.getMenu_food_id_array() != null) {
			// 依照逗號分割menu food字串
			String[] menuFoodIDArray = menu.getMenu_food_id_array().split(",");
			String[] menuFoodAmountArray = menu.getMenu_food_amount_array().split(",");

			// 每一次新增之前都New一個MenuFood確保資料乾淨
			MenuFood menufood = new MenuFood();
			// 給一個List來按順序存MenuFood資料
			List<MenuFood> aMenuFood = new ArrayList<MenuFood>();

			for (int i = 0; i < menuFoodIDArray.length; i++) {

				// 把資料加進物件
				menufood.setMenu_id(menu.getMenu_id());
				menufood.setMenu_food_id(Integer.parseInt(menuFoodIDArray[i]));
				// menufood.setMenu_food_name(menuFoodNameArray[i]);
				menufood.setMenu_food_amount(menuFoodAmountArray[i]);

				aMenuFood.add(menufood);

				menuDAO.menuFoodUpdate(menufood);
			}

		}
		
		// 存貨的處理
		if (menu.getMenu_stock_id_array() != null) {
			String[] menuStockIdArray = menu.getMenu_stock_id_array().split(",");
			String[] menuStockAmountArray = menu.getMenu_stock_amount_array().split(",");

			MenuStock menustock = new MenuStock();
			List<MenuStock> aMenuStock = new ArrayList<MenuStock>();

			for (int i = 0; i < menuStockIdArray.length; i++) {
				menustock.setMenu_id(menu.getMenu_id());
				menustock.setMenu_stock_id(Integer.parseInt(menuStockIdArray[i]));
				menustock.setMenu_stock_amount(Integer.parseInt(menuStockAmountArray[i]));

				aMenuStock.add(menustock);

				menuDAO.menuStockUpdate(menustock);
			}

		}
		
		//保存資料庫的路徑
		String sqlPath = null; 
		//定義文件保存的本地路徑
		String localPath="/Users/jumpingzone/Documents/workspace/畢業專題/new/haha/src/main/webapp/resources/menu_img/";
		//定義文件名稱
		String filename=null;  
		if(!menu_picture.isEmpty()){  
			//生成uuid作為文件名稱
			String uuid = UUID.randomUUID().toString().replaceAll("-","");  
			//獲得文件類型：如果不是图片，禁止上傳  
			String contentType=menu_picture.getContentType();  
			//獲得文件後缀名 
			String suffixName=contentType.substring(contentType.indexOf("/")+1);
			//得到 文件名稱
			filename=uuid+"."+suffixName; 
			System.out.println(filename);
			//文件保存路徑
			menu_picture.transferTo(new File(localPath+filename));  
			//把圖片的相對路徑保存至資料庫
			sqlPath = "resources/menu_img/"+filename;

			menuDAO.menuUpdatePic(menu, sqlPath);
			
		}
		
		else
			menuDAO.menuUpdate(menu);
		
		
		
		
		return model;
	
	}
	
	
	// Update Menu
	// get menu detail at first
	@RequestMapping(value = "/chef_cusine_edit", method = RequestMethod.GET)
	public ModelAndView updateMenuPageForChef(@ModelAttribute("menu_id") String menu_id) {
		
		ModelAndView model = new ModelAndView("chef_cusine_edit");

		MenuDAO menuDAO = (MenuDAO) context.getBean("menuDAO");
		Menu menu = menuDAO.getMenuId(menu_id);

		List<MenuFood> menuFoodList = new ArrayList<MenuFood>();
		menuFoodList = menuDAO.getMenuFood(menu_id);

		List<FoodCategorySub> foodCategorySubList = new ArrayList<FoodCategorySub>();
		foodCategorySubList = menuDAO.getFoodCategorySubList();

		List<MenuStock> menuStockList = new ArrayList<MenuStock>();
		menuStockList = menuDAO.getMenuStockList(menu_id);

		model.addObject("menu", menu);
		model.addObject("menuFoodList", menuFoodList);
		model.addObject("menuStockList", menuStockList);
		model.addObject("foodCategorySubList", foodCategorySubList);

		return model;

	}

	// Update Menu
	// Add data
	@RequestMapping(value = "/chef_cusine_edit", method = RequestMethod.POST)
	// ModelAttribute
	public ModelAndView updateMenuforChef(@ModelAttribute Menu menu ) {

		ModelAndView model = new ModelAndView("redirect:/chef_cusine");
		MenuDAO menuDAO = (MenuDAO) context.getBean("menuDAO");

		menuDAO.menuUpdateForChef(menu);

		return model;

	}
	
}
