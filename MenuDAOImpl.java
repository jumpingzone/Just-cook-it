package com.haha.text.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.haha.test.entity.Food;
import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.FoodStock;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;


//import com.mysql.jdbc.Statement;
import com.haha.test.entity.Menu;
import com.haha.test.entity.MenuFood;
import com.haha.test.entity.MenuStock;
import com.haha.test.dao.MenuDAO;


public class MenuDAOImpl implements MenuDAO{
	
	
	private DataSource dataSource;
	private Connection conn = null ;
	private ResultSet rs = null ;
	private PreparedStatement smt = null ;
	private PreparedStatement smt2 = null;

	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
	
	//finish
	public void menuInsert(Menu aMenu, String menu_picture) {
		

		
		String sql = "INSERT INTO menu (menu_name, menu_process, menu_price, menu_picture, menu_memo) VALUES(?, ?, ?, ?, ?);";
		
		
		try {
			
			conn = dataSource.getConnection();
			//transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);

			smt.setString(1, aMenu.getMenu_name());
			smt.setString(2, aMenu.getMenu_process());
			smt.setInt(3, aMenu.getMenu_price());
			smt.setString(4, menu_picture);
			smt.setString(5, aMenu.getMenu_memo());

			smt.executeUpdate();			
			
			//在連線關閉前取得最新生成的id
			String sql2 = "SELECT last_insert_id() as menu_id;";
			
			smt2 = conn.prepareStatement(sql2);
			rs = smt2.executeQuery();

			if (rs.next()) {
				//取出food_id
				aMenu.setMenu_id(rs.getInt("menu_id"));
				
			}
			
			//先關閉
			rs.close();
			smt2.close();

			
			smt.close();
			
			conn.commit();
			conn.close();
			
			
				
 
		} 
		
		catch (SQLException e) {
			throw new RuntimeException(e);
 
		} 
		
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
		
	}
	
	// finish
	// insert menu food
	public void menuFoodInsert(MenuFood aMenuFood) {

		String sql2 = "INSERT INTO menufood (menu_id, food_id, menu_food_amount) VALUES(?, ?, ?)";

		
		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql2);
			
			//之後看看menu_food_name是否要新增
			smt.setInt(1, aMenuFood.getMenu_id());
			smt.setInt(2, aMenuFood.getFood_id());
			smt.setString(3, aMenuFood.getMenu_food_amount());
			
			smt.executeUpdate();

			smt.close();

			conn.commit();
			conn.close();

		}

		catch (SQLException e) {
			throw new RuntimeException(e);

		}

		finally {
			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

		}

	}
		
	//update main menu finish with picture
	public void menuUpdatePic(Menu aMenu, String menu_picture) {
		//Menu aMenu=new Menu();
		
		String sql1 = "UPDATE menu SET menu_process=?, menu_price=?, menu_memo=?, menu_picture=? WHERE menu_id = ?";
		
		try {
			
			
			conn = dataSource.getConnection();
			//transaction begin
			conn.setAutoCommit(false);

			
			smt = conn.prepareStatement(sql1);
			
			smt.setString(1, aMenu.getMenu_process());
			smt.setInt(2, (aMenu.getMenu_price()));
			smt.setString(3, aMenu.getMenu_memo());
			smt.setString(4, menu_picture);
			smt.setInt(5, aMenu.getMenu_id());
			
			smt.executeUpdate();			
			smt.close();
			
			conn.commit();
			conn.close();
			
 
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
			
		}
		
	}
	
	//update main menu finish
		public void menuUpdate(Menu aMenu) {
			//Menu aMenu=new Menu();
			
			String sql1 = "UPDATE menu SET menu_process=?, menu_price=?, menu_memo=? WHERE menu_id = ?";
			
			try {
				
				
				conn = dataSource.getConnection();
				//transaction begin
				conn.setAutoCommit(false);

				
				smt = conn.prepareStatement(sql1);
				
				smt.setString(1, aMenu.getMenu_process());
				smt.setInt(2, (aMenu.getMenu_price()));
				smt.setString(3, aMenu.getMenu_memo());
				smt.setInt(4, aMenu.getMenu_id());
				
				smt.executeUpdate();			
				smt.close();
				
				conn.commit();
				conn.close();
				
	 
			} catch (SQLException e) {
				throw new RuntimeException(e);
	 
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {}
				}
				
			}
			
		}
	
	//finish
	//update menu food
	public void menuFoodUpdate(MenuFood aMenuFood) {
		
		
		String sql2 = "UPDATE menufood SET menu_id=?, menu_food_amount=?" + "WHERE menu_food_id = ?";
		
		try {
			
			conn = dataSource.getConnection();
			//transaction begin
			conn.setAutoCommit(false);

			
			smt=conn.prepareStatement(sql2);
			//smt.setString(1, aMenuFood.getMenu_food_name());
			smt.setInt(1, aMenuFood.getMenu_id());
			smt.setString(2, aMenuFood.getMenu_food_amount());
			
			
			smt.setInt(3, aMenuFood.getMenu_food_id());
				
				
			smt.executeUpdate();			
				
			
			
			smt.close();
			
			
			conn.commit();
			conn.close();
			
 
		} 
		
		catch (SQLException e) {
			throw new RuntimeException(e);
 
		}
		
		finally {
			if (conn != null) {
				
				try {
					conn.close();
				} catch (SQLException e) {}
			}
			
		}
		
	}
	
	//update menu stock
	public void menuStockUpdate(MenuStock aMenuStock) {

		String sql = "UPDATE menustock SET menu_id=?, menu_stock_amount=? WHERE menu_stock_id = ?";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);

			smt.setInt(1, aMenuStock.getMenu_id());
			smt.setInt(2, aMenuStock.getMenu_stock_amount());
			smt.setInt(3, aMenuStock.getMenu_stock_id());

			smt.executeUpdate();

			smt.close();

			conn.commit();
			conn.close();

		}

		catch (SQLException e) {
			throw new RuntimeException(e);

		}

		finally {
			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

		}

	}

	
	// update main menu finish
	public void menuUpdateForChef(Menu aMenu) {
		// Menu aMenu=new Menu();

		String sql1 = "UPDATE menu SET menu_recommend=? WHERE menu_id = ?";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql1);

			smt.setInt(1, aMenu.getMenu_recommend());
			smt.setInt(2, aMenu.getMenu_id());

			smt.executeUpdate();
			smt.close();

			conn.commit();
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

		}

	}
	
	
	//just show menu_name and menu_price
	public List<Menu> getList() {
		String sql = "SELECT * FROM menu";
		return getList(sql);
	}
	
	//show menu where has stock for user
	public List<Menu> getMenuList() {
		String sql = "SELECT A.* FROM topic.menu AS A left join menustock AS B on A.menu_id = B.menu_id WHERE B.menu_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or B.menu_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d') group by A.menu_id HAVING sum(B.menu_stock_amount) > '0';";
		return getList(sql);
	}
	
	// get menu for recommend on products: 12/06
	public List<Menu> getMenuListForRecommend() {
		String sql = "SELECT main.* FROM topic.menu AS main LEFT JOIN menustock AS sub on main.menu_id = sub.menu_id WHERE sub.menu_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.menu_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d') group by main.menu_id HAVING sum(sub.menu_stock_amount) > '0' ORDER BY RAND() LIMIT 1;";
		return getList(sql);
	}
	
	// get menu for recommend on cusine: 12/07
	public List<Menu> getFourMenuListForRecommend() {
		String sql = "SELECT main.* FROM topic.menu AS main LEFT JOIN menustock AS sub on main.menu_id = sub.menu_id WHERE sub.menu_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.menu_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d') group by main.menu_id HAVING sum(sub.menu_stock_amount) > '0' ORDER BY RAND() LIMIT 4;";
		return getList(sql);
	}
	
	//get Menu data
	public List<Menu> getList(String sql) {
		
		List<Menu> menuList = new ArrayList<Menu>();
		
		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while(rs.next()){
				Menu aMenu = new Menu();
				
				aMenu.setMenu_id(rs.getInt("menu_id"));			
				aMenu.setMenu_name(rs.getString("menu_name"));
				aMenu.setMenu_process(rs.getString("menu_process"));
				aMenu.setMenu_price(rs.getInt("menu_price"));
				aMenu.setMenu_picture(rs.getString("menu_picture"));
				aMenu.setMenu_memo(rs.getString("menu_memo"));
				
				
				menuList.add(aMenu);
			}
			rs.close();
			smt.close();
 
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return menuList;
	}
	
	//get menu_id from to "menuFoodShow" method
	public List<MenuFood> getMenuFood(String menu_id){
		//String sql = "SELECT * FROM topic.menufood where menufood.menu_id="+menu_id;
		
		String sql = "SELECT menu_food_id, menu_id, menu_food_amount, A.food_id, food_name FROM topic.menufood AS A LEFT JOIN food AS B on A.food_id = B.food_id where A.menu_id="+menu_id;
		
		
		return menuFoodShow(sql);
	}
	//finish
	//get MenuFood data
	public List<MenuFood> menuFoodShow(String sql) {
			
		List<MenuFood> menuFoodShowList = new ArrayList<MenuFood>();
		
		
		
		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			
			rs = smt.executeQuery();
			while(rs.next()){
				MenuFood aMenuFood = new MenuFood();
						
				aMenuFood.setMenu_food_id(rs.getInt("menu_food_id"));
				aMenuFood.setMenu_food_amount(rs.getString("menu_food_amount"));
				aMenuFood.setFood_id(rs.getInt("food_id"));
				aMenuFood.setFood_name(rs.getString("food_name"));
				
				
					
				menuFoodShowList.add(aMenuFood);
			}
			rs.close();
			smt.close();
	 
		} catch (SQLException e) {
			throw new RuntimeException(e);
	 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return menuFoodShowList;
	}
	
	//finish
	//show menu detail by menu_id
	public Menu getMenuId(String menu_id) {
		Menu aMenu=new Menu();
		
		String sql = "SELECT * FROM topic.menu WHERE menu.menu_id = ?;";
		
		try {
		
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			Integer.parseInt(menu_id);
			
			
			smt.setInt(1, Integer.parseInt(menu_id));
			
			rs = smt.executeQuery();
			
			if(rs.next()){
				aMenu.setMenu_id(rs.getInt("menu_id"));			
				aMenu.setMenu_name(rs.getString("menu_name"));
				aMenu.setMenu_process(rs.getString("menu_process"));
				aMenu.setMenu_price(rs.getInt("menu_price"));
				aMenu.setMenu_picture(rs.getString("menu_picture"));
				aMenu.setMenu_memo(rs.getString("menu_memo"));
				aMenu.setMenu_recommend(rs.getInt("menu_recommend"));
				aMenu.setMenu_count_rate(rs.getInt("menu_count_rate"));
				
				
				
			}
			rs.close();
			smt.close();
 
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return aMenu;
	}
	
	public void updateMenuClick(Menu aMenu) {

		//String sql = "SELECT * FROM food WHERE food.food_id = ?;";
		String sql = "UPDATE menu SET menu_count_rate=? WHERE menu_id = ?";

		try {

			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			
			smt.setInt(1, aMenu.getMenu_count_rate() + 1);
			smt.setInt(2, aMenu.getMenu_id());

			smt.executeUpdate();

			smt.close();

			//conn.commit();
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	// get foodCategory
	public List<FoodCategory> getFoodCategoryList() {
		String sql = "SELECT * FROM topic.foodcategory ";
		return getFoodCategoryList(sql);
	}

	public List<FoodCategory> getFoodCategoryList(String sql) {

		List<FoodCategory> foodCategoryList = new ArrayList<FoodCategory>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				FoodCategory aFoodCategory = new FoodCategory();

				aFoodCategory.setFood_category_id(rs.getInt("food_category_id"));
				aFoodCategory.setFood_category_name(rs.getString("food_category_name"));

				foodCategoryList.add(aFoodCategory);

			}
			rs.close();
			smt.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return foodCategoryList;
	}	
	
	//get foodCategorySub
	public List<FoodCategorySub> getFoodCategorySubList() {
		String sql = "SELECT * FROM topic.foodcategorysub ";
		return getFoodCategorySubList(sql);
	}

	public List<FoodCategorySub> getFoodCategorySubList(String sql) {
		
		List<FoodCategorySub> foodCategorySubList = new ArrayList<FoodCategorySub>();
		
		try { 
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while(rs.next()){
				FoodCategorySub aFoodCategorySub = new FoodCategorySub();
				
				aFoodCategorySub.setFood_category_sub_id(rs.getInt("food_category_sub_id"));
				aFoodCategorySub.setFood_category_sub_name(rs.getString("food_category_sub_name"));
				aFoodCategorySub.setFood_category_id(rs.getInt("food_category_id"));
				
				
				foodCategorySubList.add(aFoodCategorySub);
				
			}
			rs.close();
			smt.close();
 
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return foodCategorySubList;
	}

	// show menu stock by menu_id
	public List<MenuStock> getMenuStockList(String menu_id) {
		String sql = "SELECT * FROM menustock WHERE (menu_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or menu_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) AND menu_id ="+ menu_id;
		return showMenuStockList(sql);
	}

	public List<MenuStock> showMenuStockList(String sql) {

		List<MenuStock> menuStockList = new ArrayList<MenuStock>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				MenuStock aMenuStock = new MenuStock();

				aMenuStock.setMenu_stock_id(rs.getInt("menu_stock_id"));
				aMenuStock.setMenu_id(rs.getInt("menu_id"));
				aMenuStock.setMenu_stock_amount(rs.getInt("menu_stock_amount"));
				aMenuStock.setMenu_stock_date(rs.getString("menu_stock_date"));

				menuStockList.add(aMenuStock);

			}
			rs.close();
			smt.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return menuStockList;
	}

	// get menu with two days stock for background
	public List<Menu> getMenuForBackgroundList() {
		String sql = "SELECT M.*, (SELECT menu_stock_amount FROM menustock where menu_id = M.menu_id and menu_stock_date=DATE_FORMAT(NOW(),'%Y-%m-%d')) as menu_stock_date, (SELECT menu_stock_amount FROM menustock where menu_id = M.menu_id and menu_stock_date=DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) as menu_stock_date_tomorrow FROM menu as M;";
		return getMenuBackgroundList(sql);
	}

	// FOR SURE TO HAVE MENU STOCK OR IT WILL BOOOOOOOOOM
	// (CRAZY LAGHFING
	public List<Menu> getMenuBackgroundList(String sql) {

		List<Menu> menuList = new ArrayList<Menu>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				Menu aMenu = new Menu();

				aMenu.setMenu_id(rs.getInt("menu_id"));			
				aMenu.setMenu_name(rs.getString("menu_name"));
				aMenu.setMenu_process(rs.getString("menu_process"));
				aMenu.setMenu_price(rs.getInt("menu_price"));
				aMenu.setMenu_picture(rs.getString("menu_picture"));
				aMenu.setMenu_memo(rs.getString("menu_memo"));
				
				// not in food table
				aMenu.setMenu_stock_date(rs.getString("menu_stock_date"));
				aMenu.setMenu_stock_date_tomorrow(rs.getString("menu_stock_date_tomorrow"));

				menuList.add(aMenu);
			}
			rs.close();
			smt.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return menuList;
	}
	
	
}
