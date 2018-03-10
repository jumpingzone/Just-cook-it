package com.haha.text.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.haha.test.entity.CustomerFood;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;

//import com.mysql.jdbc.Statement;
import com.haha.test.entity.Food;
import com.haha.test.entity.FoodCategory;
import com.haha.test.entity.FoodCategorySub;
import com.haha.test.entity.FoodCooking;
import com.haha.test.entity.FoodStock;
import com.haha.test.entity.Menu;
import com.haha.test.entity.MenuFood;
import com.haha.test.dao.FoodDAO;

public class FoodDAOImpl implements FoodDAO {

	private DataSource dataSource;
	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement smt = null;
	private PreparedStatement smt2 = null;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// finish
	// insert main food finish
	public Food foodInsert(Food aFood , String food_picture) {

		String sql = "INSERT INTO food (food_name, food_gram, food_price, food_picture, food_category_id, food_category_sub_id) VALUES(?, ?, ?, ?, ?, ?);";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);

			smt.setString(1, aFood.getFood_name());
			smt.setString(2, aFood.getFood_gram());
			smt.setInt(3, (aFood.getFood_price()));

			smt.setString(4, food_picture);
			smt.setInt(5, (aFood.getFood_category_id()));
			smt.setInt(6, (aFood.getFood_category_sub_id()));

			smt.executeUpdate();

			// �b�s�u�����e���o�̷s�ͦ���id
			String sql2 = "SELECT last_insert_id() as food_id;";

			smt2 = conn.prepareStatement(sql2);
			rs = smt2.executeQuery();

			if (rs.next()) {
				// ���Xfood_id
				aFood.setFood_id(rs.getInt("food_id"));

			}

			rs.close();
			smt2.close();

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
		return aFood;

	}

	// finish
	// insert food cooking
	public void foodCookingInsert(FoodCooking aFoodCooking) {

		String sql = "INSERT INTO foodcooking (food_cooking_name, food_id, food_cooking_price) VALUES(?, ?, ?);";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);
			smt.setString(1, aFoodCooking.getFood_cooking_name());
			smt.setInt(2, aFoodCooking.getFood_id());
			smt.setInt(3, aFoodCooking.getFood_cooking_price());

			// smt.setInt(4, aFoodCooking.getFood_cooking_id());

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

	// get food for recommend on food_content: 12/06
	public List<Food> getFourFoodListForRecommend(String food_category_sub_id) {
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodcategorysub AS sub ON main.food_category_sub_id = sub.food_category_sub_id LEFT JOIN foodstock AS sub2 on main.food_id = sub2.food_id WHERE main.food_category_sub_id =" + food_category_sub_id +" AND (sub2.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub2.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) group by main.food_id HAVING sum(sub2.food_stock_amount) > '0' ORDER BY RAND() LIMIT 4;";
		return getList(sql);
	}
	
	// get food for recommend on products2: 12/07
	public List<Food> getFoodListForRecommend() {
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d') group by main.food_id HAVING sum(sub.food_stock_amount) > '0' ORDER BY RAND() LIMIT 1;";
		return getList(sql);
	}
	
	// finish
	// just show food_name and food_price
	public List<Food> getList() {
		String sql = "SELECT * From food;";
		return getList(sql);
	}

	// get Food data
	public List<Food> getList(String sql) {

		List<Food> foodList = new ArrayList<Food>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				Food aFood = new Food();

				aFood.setFood_id(rs.getInt("food_id"));
				aFood.setFood_category_id(rs.getInt("food_category_id"));
				aFood.setFood_category_sub_id(rs.getInt("food_category_sub_id"));
				aFood.setFood_name(rs.getString("food_name"));
				aFood.setFood_gram(rs.getString("food_gram"));
				aFood.setFood_price(rs.getInt("food_price"));
				aFood.setFood_picture(rs.getString("food_picture"));
				aFood.setFood_sale(rs.getBoolean("food_sale"));
				aFood.setFood_recommend(rs.getBoolean("food_recommend"));
				aFood.setFood_count_rate(rs.getInt("food_count_rate"));
				
				foodList.add(aFood);
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
		return foodList;
	}

	// update main food finish with picture
		public void foodUpdatePic(Food aFood, String food_picture) {
			

			String sql1 = "UPDATE food SET food_price=?, food_gram=?, food_picture=?  WHERE food_id = ?";

			try {

				conn = dataSource.getConnection();
				// transaction begin
				conn.setAutoCommit(false);

				smt = conn.prepareStatement(sql1);

				smt.setInt(1, (aFood.getFood_price()));
				smt.setString(2, aFood.getFood_gram());
				smt.setString(3, food_picture);
				smt.setInt(4, aFood.getFood_id());

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

		// update main food finish
			public void foodUpdate(Food aFood) {
				

				String sql1 = "UPDATE food SET food_price=?, food_gram=? WHERE food_id = ?";

				try {

					conn = dataSource.getConnection();
					// transaction begin
					conn.setAutoCommit(false);

					smt = conn.prepareStatement(sql1);

					smt.setInt(1, (aFood.getFood_price()));
					smt.setString(2, aFood.getFood_gram());
					smt.setInt(3, aFood.getFood_id());

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

	// update food cooking
	public void foodCookingUpdate(FoodCooking aFoodCooking) {

		String sql = "UPDATE foodcooking SET food_id=?, food_cooking_price=? WHERE food_cooking_id = ?";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);

			smt.setInt(1, aFoodCooking.getFood_id());
			smt.setInt(2, aFoodCooking.getFood_cooking_price());

			smt.setInt(3, aFoodCooking.getFood_cooking_id());

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

	public void foodStockUpdate(FoodStock aFoodStock) {

		String sql = "UPDATE foodstock SET food_id=?, food_stock_amount=? WHERE food_stock_id = ?";

		try {

			conn = dataSource.getConnection();
			// transaction begin
			conn.setAutoCommit(false);

			smt = conn.prepareStatement(sql);

			smt.setInt(1, aFoodStock.getFood_id());
			smt.setInt(2, aFoodStock.getFood_stock_amount());

			smt.setInt(3, aFoodStock.getFood_stock_id());

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

	// get food_id from to "FoodCookingShow" method
	public List<FoodCooking> getFoodCooking(String food_id) {
		String sql = "SELECT * FROM topic.foodcooking where foodcooking.food_id=" + food_id;
		return foodCookingShow(sql);
	}

	// finish
	// get FoodCooking data
	public List<FoodCooking> foodCookingShow(String sql) {

		List<FoodCooking> foodCookingShowList = new ArrayList<FoodCooking>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			// smt.setInt(1, Integer.parseInt(menu_id));

			rs = smt.executeQuery();
			while (rs.next()) {
				FoodCooking aFoodCooking = new FoodCooking();

				aFoodCooking.setFood_cooking_id(rs.getInt("food_cooking_id"));
				aFoodCooking.setFood_cooking_name(rs.getString("food_cooking_name"));
				aFoodCooking.setFood_cooking_price(rs.getInt("food_cooking_price"));
				aFoodCooking.setFood_id(rs.getInt("food_id"));

				foodCookingShowList.add(aFoodCooking);
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
		return foodCookingShowList;
	}
	
	public void updateFoodClick(Food aFood) {

		String sql = "UPDATE food SET food_count_rate=? WHERE food_id = ?";

		try {

			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);

			smt.setInt(1, aFood.getFood_count_rate() + 1);
			smt.setInt(2, aFood.getFood_id());

			smt.executeUpdate();

			smt.close();

			// conn.commit();
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

	// finish
	// show Food detail by food_id
	public Food getFoodId(String food_id) {
		Food aFood = new Food();

		// String sql = "SELECT * FROM food WHERE food.food_id = ?;";
		String sql = "SELECT A.food_category_id, food_category_name, A.food_category_sub_id, food_category_sub_name, food_gram, food_id, food_name, food_picture, food_price, food_recommend, food_count_rate, food_sale FROM topic.food AS A LEFT JOIN foodcategory AS B on A.food_category_id = B.food_category_id LEFT JOIN foodcategorysub AS C on A.food_category_sub_id = C.food_category_sub_id WHERE A.food_id = ?;";

		try {

			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			Integer.parseInt(food_id);

			smt.setInt(1, Integer.parseInt(food_id));

			rs = smt.executeQuery();

			if (rs.next()) {
				aFood.setFood_id(rs.getInt("food_id"));
				aFood.setFood_category_name(rs.getString("food_category_name"));
				aFood.setFood_category_id(rs.getInt("food_category_id"));
				aFood.setFood_category_sub_name(rs.getString("food_category_sub_name"));
				aFood.setFood_category_sub_id(rs.getInt("food_category_sub_id"));

				aFood.setFood_name(rs.getString("food_name"));
				aFood.setFood_gram(rs.getString("food_gram"));
				aFood.setFood_price(rs.getInt("food_price"));
				aFood.setFood_picture(rs.getString("food_picture"));
				aFood.setFood_sale(rs.getBoolean("food_sale"));
				aFood.setFood_recommend(rs.getBoolean("food_recommend"));
				aFood.setFood_count_rate(rs.getInt("food_count_rate"));
				
				
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
		return aFood;
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

	// get foodCategorySub
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
			while (rs.next()) {
				FoodCategorySub aFoodCategorySub = new FoodCategorySub();

				aFoodCategorySub.setFood_category_id(rs.getInt("food_category_id"));
				aFoodCategorySub.setFood_category_sub_id(rs.getInt("food_category_sub_id"));
				aFoodCategorySub.setFood_category_sub_name(rs.getString("food_category_sub_name"));

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
				} catch (SQLException e) {
				}
			}
		}
		return foodCategorySubList;
	}

	// �z�L�ϥΪ��I���v������for index 4 items
	@Override
	public List<Food> getListbycountrate() {
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE food_recommend=1 AND (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) group by main.food_id HAVING sum(sub.food_stock_amount) > '0' ORDER BY food_count_rate DESC LIMIT 4;";
		return getList(sql);
	}

	// �z�L�O�_���D�p�]�w��ĳ�M����for index 4 items
	@Override
	public List<Food> getListbyrecommand() {
		// TODO Auto-generated method stub
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE food_recommend=1 AND (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) group by main.food_id HAVING sum(sub.food_stock_amount) > '0' LIMIT 4;";
		return getList(sql);
	}

	// �z�L�ϥΪ��I���v������for index 12 items
	@Override
	public List<Food> getListbycountratemore() {
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE food_recommend=1 AND (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) group by main.food_id HAVING sum(sub.food_stock_amount) > '0' ORDER BY food_count_rate DESC LIMIT 12;";
		return getList(sql);
	}

	// �z�L�O�_���D�p�]�w��ĳ�M����for index 12 items
	@Override
	public List<Food> getListbyrecommandmore() {
		String sql = "SELECT main.* FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE food_recommend=1 AND (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) group by main.food_id HAVING sum(sub.food_stock_amount) > '0' LIMIT 12;";
		return getList(sql);
	}

	// �Ҧ��Ȥ�q�椤�I���v�̰���(������ʶR�L)
	public List<Food> getlistfromcustomerorderbycountrate() {
		String sql = "SELECT main.* FROM customerfood LEFT JOIN food AS main ON customerfood.customer_food_id=main.food_id LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE food_recommend=1 AND (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) GROUP BY main.food_name ORDER BY main.food_count_rate DESC LIMIT 12";
		return getList(sql);
	}
	
	//finish
	//show food where has stock for user
	public List<Food> getFoodList() {
		String sql = "SELECT A.* FROM topic.food AS A left join foodstock AS B on A.food_id = B.food_id WHERE B.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or B.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d') group by A.food_id HAVING sum(B.food_stock_amount) > '0';";
		return getList(sql);
	}
	
	//show food stock by food_id
	public List<FoodStock> getFoodStockList(String food_id){
		String sql = "SELECT * FROM foodstock WHERE (food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) AND food_id =" + food_id;
		return showFoodStockList(sql);
	}
	
	public List<FoodStock> showFoodStockList(String sql) {

		List<FoodStock> foodStockList = new ArrayList<FoodStock>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				FoodStock aFoodStock = new FoodStock();

				aFoodStock.setFood_stock_id(rs.getInt("food_stock_id"));
				aFoodStock.setFood_id(rs.getInt("food_id"));
				aFoodStock.setFood_stock_amount(rs.getInt("food_stock_amount"));
				aFoodStock.setFood_stock_date(rs.getString("food_stock_date"));

				foodStockList.add(aFoodStock);

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
		return foodStockList;
	}

	//get food with two days stock for background
	public List<Food> getFoodForBackgroundList() {
		String sql = "SELECT M.*, food_category_name, (SELECT food_stock_amount FROM foodstock where food_id = M.food_id and food_stock_date=DATE_FORMAT(NOW(),'%Y-%m-%d')) as food_stock_date, (SELECT food_stock_amount FROM foodstock where food_id = M.food_id and food_stock_date=DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) as food_stock_date_tomorrow FROM food as M LEFT JOIN foodcategory AS B on M.food_category_id = B.food_category_id Order By M.food_id;";
		return getFoodBackgroundList(sql);
	}
	// FOR SURE TO HAVE FOOD STOCK AND FOOD CATEGORY NAME OR IT WILL BOOOOOOOOOM (CRAZY LAGHFING
	public List<Food> getFoodBackgroundList(String sql) {

		List<Food> foodList = new ArrayList<Food>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				Food aFood = new Food();

				aFood.setFood_id(rs.getInt("food_id"));
				aFood.setFood_category_id(rs.getInt("food_category_id"));
				aFood.setFood_category_sub_id(rs.getInt("food_category_sub_id"));
				aFood.setFood_name(rs.getString("food_name"));
				aFood.setFood_gram(rs.getString("food_gram"));
				aFood.setFood_price(rs.getInt("food_price"));
				aFood.setFood_picture(rs.getString("food_picture"));
				aFood.setFood_sale(rs.getBoolean("food_sale"));
				aFood.setFood_recommend(rs.getBoolean("food_recommend"));

				// not in food table
				aFood.setFood_category_name(rs.getString("food_category_name"));
				aFood.setFood_stock_date(rs.getString("food_stock_date"));
				aFood.setFood_stock_date_tomorrow(rs.getString("food_stock_date_tomorrow"));
				
				foodList.add(aFood);
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
		return foodList;
	}
	
	//get food sale top three list for index 
	public List<Food> getFoodSaleTopThreeForIndex() {
		String sql = "SELECT DISTINCT main.food_id, main.food_name, main.food_price, main.food_picture, (SELECT COUNT(*) FROM customerfood WHERE customer_food_id = main.food_id) as customer_food_amount FROM food AS main LEFT JOIN foodstock AS sub on main.food_id = sub.food_id WHERE (sub.food_stock_date = DATE_FORMAT(NOW(),'%Y-%m-%d') or sub.food_stock_date = DATE_FORMAT(date_add(now(), interval 1 day),'%Y-%m-%d')) ORDER by customer_food_amount DESC LIMIT 3;;";
		return getFoodSaleTopThreeListForIndex(sql);
	}
	
	public List<Food> getFoodSaleTopThreeListForIndex(String sql) {

		List<Food> foodList = new ArrayList<Food>();

		try {
			conn = dataSource.getConnection();
			smt = conn.prepareStatement(sql);
			rs = smt.executeQuery();
			while (rs.next()) {
				Food aFood = new Food();

				aFood.setFood_id(rs.getInt("food_id"));
				aFood.setFood_name(rs.getString("food_name"));
				aFood.setFood_price(rs.getInt("food_price"));
				aFood.setFood_picture(rs.getString("food_picture"));
				
				
				foodList.add(aFood);
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
		return foodList;
	}
	
}
