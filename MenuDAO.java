package com.haha.test.dao;

import java.util.List;

import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.Menu;
import com.haha.test.entity.MenuFood;
import com.haha.test.entity.MenuStock;



public interface MenuDAO {	
	
	public void menuInsert(Menu aMenu, String menu_picture); //insert menu
	public void menuFoodInsert(MenuFood aMenuFood); //insert menu food
	
	public void menuUpdate(Menu aMenu); //update menu 
	public void menuUpdatePic(Menu aMenu, String menu_picture); //update menu with picture
	public void menuFoodUpdate(MenuFood aMenuFood); //update menu food
	public void menuStockUpdate(MenuStock aMenuStock); //update menu stock
	public void menuUpdateForChef(Menu aMenu); //update menu
	public void updateMenuClick(Menu aMenu);//show food click rate with menu_id	
	
	
	public List<Menu> getList();//show menu all 
	public List<MenuFood> getMenuFood(String menu_id);//search menu food detail by menu_id
	public List<MenuStock> getMenuStockList(String menu_id); //show menu stock by menu_id
	
	public List<FoodCategory> getFoodCategoryList();//food category
	public List<FoodCategorySub> getFoodCategorySubList();//food sub category
	
	public Menu getMenuId(String menu_id); //show menu detail by menu_id
	
	public List<Menu> getMenuForBackgroundList();// get menu with two days stock for background
	public List<Menu> getMenuList(); //show menu where has stock for user
	public List<Menu> getMenuListForRecommend(); // get menu for recommend on products
	public List<Menu> getFourMenuListForRecommend(); // get menu for recommend on cusine: 12/07
	
	
	
	}
