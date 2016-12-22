/**
 * Function:ProductBiz
 * Date:2016.12.11
 * Author:LiuXin
 */
package service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ProductBiz {
	/**
	 * ������Ʒ
	 */
	public boolean save(String pName, float pPrice, String pDescribe, String pImage, String pTime,
			String pSite, String pView, String pPositive, String pStatus, String pMessage) {
		Record pro = new Record().set("Product_Name", pName).set("Product_Price", pPrice)
				.set("Product_Describe", pDescribe).set("Product_Image", pImage).set("Product_Time", pTime)
				.set("Product_Site", pSite).set("Product_View", pView).set("Product_Positive", pPositive)
				.set("Product_Status", pStatus).set("Product_Message", pMessage);
		boolean res = Db.save("islandtrading_product", pro);
		return res;
	}

	/**
	 * ɾ����Ʒ
	 */
	public boolean deleteByID(String pid) {
		boolean res = Db.deleteById("islandtrading_product", "Product_Id", pid);
		return res;
	}

	/**
	 * �޸���Ʒ
	 */
	public int update(String pId, String pName, String pPrice, String pDescribe, String pImage, String pTime,
			String pSite, String pView, String pPositive, String pStatus, String pMessage) {
		String sql = "UPDATE islandtrading_product SET" + " Product_Id='" + pId + "'," + "Product_Name='" + pName + "',"
				+ "Product_Price='" + pPrice + "'," + "Product_Describe='" + pDescribe + "'," + "Product_Image='"
				+ pImage + "'," + "Product_Time='" + pTime + "'," + "Product_Site='" + pSite + "'," + "Product_View='"
				+ pView + "'," + "Product_Positive='" + pPositive + "'," + "Product_Status='" + pStatus + "',"
				+ "Product_Message='" + pMessage + "'" + " WHERE Product_Id='" + pId + "'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll() {
		List<Record> pros = Db.find("select * from islandtrading_product");
		return pros;
	}

	/**
	 * ������Ʒ
	 */
	public Record findByID(String pid) {
		Record rec = Db.findById("islandtrading_product", "Product_Id", pid);
		return rec;
	}
}
