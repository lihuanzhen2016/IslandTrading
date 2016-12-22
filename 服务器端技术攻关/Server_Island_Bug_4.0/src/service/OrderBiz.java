/**
 * Function:OrderBiz
 * Date:2016.12.11
 * Author:LiuXin
 */
package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class OrderBiz {
	/**
	 * ���Ҷ���
	 */
	public List<Record> findAll() {
		List<Record> orders = Db.find("select * from islandtrading_order order by Order_Id desc");
		return orders;
	}

	/**
	 * ���Ҷ���
	 */
	public Record findByID(String oid) {
		Record rec = Db.findById("islandtrading_order", "Order_Id", oid);
		return rec;
	}

	/**
	 * ���Ҷ�������
	 */
	public List<Record> findDetailByID(String oid) {
		String sql = "select * from islandtrading_order where Order_Id='" + oid + "'";
		List<Record> orders = Db.find(sql);
		return orders;
	}

	// ���涩��
	public boolean save(String oID, Date time, String status, String site) {
		Record order = new Record().set("Order_Id", oID).set("Order_Time", time).set("Order_Status", status)
				.set("Order_Site", site);
		boolean res = Db.save("islandtrading_order", order);
		return res;
	}

	// �������涩�������¼
	public int[] batchsave(List<Record> recordList, int batchSize) {
		int[] res = Db.batchSave("islandtrading_order", recordList, batchSize);
		return res;
	}
	
	
	
	
	
	
	
}
