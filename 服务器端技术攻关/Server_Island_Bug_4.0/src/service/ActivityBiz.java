/**
 * Function:ActivityBiz
 * Date:2016.12.11
 * Author:LiuXin
 */
package service;

import java.util.List;

/** 
 * �����ҵ����
 */

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ActivityBiz {
	/**
	 * ���ӻ
	 */
	public boolean save(String pName, String pContent, String pOrganizer, String pTime, String pSite, String pImg) {
		Record pro = new Record().set("Activity_Name", pName).set("Activity_Content", pContent)
				.set("Activity_Organizer", pOrganizer).set("Activity_Time", pTime).set("ACTIVITY_SITE", pSite)
				.set("Activity_Img", pImg);
		boolean res = Db.save("islandtrading_activity", pro);
		return res;
	}

	/**
	 * ɾ���
	 */
	public boolean deleteByID(String pid) {
		boolean res = Db.deleteById("islandtrading_activity", "Activity_Id", pid);
		return res;
	}

	/**
	 * �޸Ļ
	 */
	public int update(String pID, String pName, String pContent, String pOrganizer, String pTime, String pSite
			,String pImg) {
		String sql = "UPDATE islandtrading_activity SET" + " Activity_Id='" + pID + "'," + "Activity_Name='" + pName
				+ "'," + "Activity_Content='" + pContent + "'," + "Activity_Organizer='" + pOrganizer + "',"
				+ "Activity_Time='" + pTime + "'," + "ACTIVITY_SITE='" + pSite +
				"'," + "ACTIVITY_IMG='" + pImg + "'" +" WHERE Activity_Id='" + pID + "'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll() {
		List<Record> pros = Db.find("select * from islandtrading_activity");
		return pros;
	}

	/**
	 * ɾ���
	 */
	public Record findByID(String id) {
		Record rec = Db.findById("islandtrading_activity", "Activity_Id", id);
		return rec;
	}

}
