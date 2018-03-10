package com.haha.test.dao;

import java.util.List;

import com.haha.test.entity.Food;
import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.FoodCooking;
import com.haha.test.entity.FoodStock;
import com.haha.test.entity.Menu;
import com.haha.test.entity.MenuFood;

public interface FoodDAO {	
	
	
	
	public Food foodInsert(Food aFood , String food_picture); //insert food
	public void foodCookingInsert(FoodCooking aFoodCooking); //insert food cooking
	
	public void foodUpdatePic(Food aFood, String food_picture); //update food with picture
	public void foodUpdate(Food aFood); //update food with picture
	public void foodCookingUpdate(FoodCooking aFoodCooking); //update food cooking
	public void foodStockUpdate(FoodStock aFoodStock); //update food stock
	public void updateFoodClick(Food aFood);//show food click rate with food_id
	
	public List<FoodCooking> getFoodCooking(String food_id);//search food cooking detail by food_id
	public List<Food> getFoodList();//show all food where has food_stock
	public List<Food> getList();//show all food
	public List<Food> getFourFoodListForRecommend(String food_category_sub_id); //get menu for recommend on food_content: 12/06
	public List<Food> getFoodListForRecommend(); //get menu for recommend on products: 12/07
	public List<Food> getFoodSaleTopThreeForIndex(); //get food sale top three list for index 
	
	public Food getFoodId(String food_id); //show food detail by food_id
	
	public List<FoodCategory> getFoodCategoryList();//food category
	public List<FoodCategorySub> getFoodCategorySubList();//food sub category
	
	public List<FoodStock> getFoodStockList(String food_id); //food stock
	public List<Food> getFoodForBackgroundList();
	
	
	//�z�L�ϥΪ��I���v������for index 4 items
	public List<Food> getListbycountrate();
	//�z�L�O�_���D�p�]�w��ĳ�M����for index 4 items
	public List<Food> getListbyrecommand();
	//�z�L�ϥΪ��I���v������for index 12 items
	public List<Food> getListbycountratemore();
	//�z�L�O�_���D�p�]�w��ĳ�M����for index 12 items
	public List<Food> getListbyrecommandmore();
	//�Ҧ��Ȥ�q�椤�I���v�̰���(������ʶR�L)
	public List<Food> getlistfromcustomerorderbycountrate();
	
	

}
