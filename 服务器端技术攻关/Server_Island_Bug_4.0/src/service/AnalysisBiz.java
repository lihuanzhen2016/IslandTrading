package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class AnalysisBiz {
	/**
	 * �����ύ
	 */
	public boolean palceoreder() {
		// List<Record> orderlist = analysisService.findAll();
		// this.setSessionAttr("orderlist", orderlist);
		// this.render("/orderList.jsp");

		// �������� ������(oid)����¼����(total)����Ʒ������(pId)����Ʒ����(pNum)�������ܼ�(oSum)

		// ��������ʱ�䣬���뵽�����������š�ʱ�䡢�ܼۣ�

		// ��ѯ��Ʒ���õ���Ʒ������Ʒ����

		// ���� ��Ʒ�������Ʒ������Ʒ���ۡ���Ʒ�����������ţ�
		return true;
	}

	/**  
	 * 
	 * ��ѯ�۸�
	 * �޸����ڣ�2016-12-8
	 * ���ߣ�����
	 * �޸��������int�޸�Ϊlong
	 */
	public Record lookupprice(long pid){		
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","Product_Id", pid);
		return Record;		
	}
	

	/*
	 * ʵ�֣�ɾ��ָ����Ʒ ����ֵ��ɾ�����
	 */
	public boolean deleteMyGood(String pid) {
		boolean res = Db.deleteById("islandtrading_product", "Product_Id", pid);
		return res;
	}

	/*
	 *  ʵ�֣��༭ָ����Ʒ ����ֵ���༭���
	 */
	public int editMyGood(String pID, String pName, float pPrice) {

		System.out.println("update(" + pName + "," + pID + "," + ") success!");
		String sql = "UPDATE t_product SET name='" + pName + "',price=" + pPrice + " WHERE pid='" + pID + "'";
		int res = Db.update(sql);
		return res;
	}
	
	/*
	 * ʵ�֣�ͨ��pid��ѯpType
	 * ���ߣ�����
	 * ���ڣ�2016-12-8
	 * �漰��product_classify��islandtrading_product
	 * */
	public String getClassify(int Product_Id){
		String sql = "select Classify_Id from re_product_classify where Product_Id=" + Product_Id;
		int Classify_Id;
		String Classify_Name = null;
		Classify_Id = Db.queryInt(sql);
		String sql1 = "select Classify_Name from islandtrading_classify where Classify_Id=" + Classify_Id;
		Classify_Name = Db.queryStr(sql1);
		
		return Classify_Name;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-1-27
	 * ʵ�֣�ͨ��pName��ѯ��Ʒ
	 * ����ֵ��������Ʒ��Ϣ
	 * */
	public Record lookup_pName(String pName){
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","Product_Name", pName); 
		return Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��type��ѯ��Ʒ���ﵽ����Ч��
	 * ����ֵ��������Ʒ��Ϣ
	 * 
	 * �޸����ڣ�2016-12-8
	 * 
	 * */
	public List<Record> lookup_type(String pType){
		//���ݲ�ѯ
		System.out.println("----pType����:" + pType);
		List<Record> list_Record = new ArrayList<>();
		String sql = "select * from islandtrading_product where Product_Id in (" +
				"select Product_Id from re_product_classify where Classify_Id=(" +
				"select Classify_Id from islandtrading_classify where Classify_Name=" + "'" +
				pType + "'" + ")" + ")";
		list_Record = Db.find(sql);
		return list_Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��act��ѯ���л��ϸ��Ϣ
	 * ����������Ҫ����
	 * ����ֵ���������л����
	 * */
	public List<Record> lookup_act(){
		List<Record> list_Record = new ArrayList<>();
		list_Record = Db.find("select * from islandtrading_activity");
		return list_Record;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��goods��ѯ�����ղ���ϸ��Ϣ
	 * ������user goods
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * 
	 * 2016-12-8�޸�
	 * ����ֵ�޸�ΪRecord
	 * */
	public Record lookup_col(String user, String goods){	//�Ȳ�goods��û����user�ղ���
		System.out.println("Я����goods����! user:"+user+"  goods:"+goods);
		String sql = "select * from re_collect_product_user where User_Id='" + 
						user + "'";
		List<Record> list = new ArrayList<>();
		list = Db.find(sql);	//�õ�user�ղ�������Ʒ
		System.out.println("-----listΪ��" + list.toString());
		int goods_id = Integer.valueOf(goods);
		int Product_Id;
		for (Record mRecord : list){
			Product_Id = mRecord.getInt("Product_Id");
			if(goods_id == Product_Id){
				System.out.println("-----lookup_col()�в�ѯ��ĳuser��ĳgoods��ִ��if");
				Record mRecord_res = Db.findById("islandtrading_product", "Product_Id", goods_id);
				return mRecord_res;
			}
		}
		
//		Record mRecord = Db.findFirst(sql);
		return null;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��ѯ�����ղ���Ʒ��ϸ��Ϣ
	 * ������user
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * */
	public List<Record> lookup_col(String user_id){
		System.out.println("û��goods!");
		List<Record> list_Record = new ArrayList<>();
		String sql = "select * from islandtrading_product where Product_Id in (" + 
				"select Product_Id from re_collect_product_user where User_Id=" + "'" +
				user_id + "'" + ")";
		list_Record = Db.find(sql);
		return list_Record;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ʵ�֣������֤��¼
	 * ������user,ȡ����¼���ɣ���AnalysisController�������֤;
	 * ����ֵ�����ؾ����¼ �� null
	 * */
	public Record lookup_user(String user){
		Record mRecord = Db.findById("islandtrading_user", "User_Username",user);
		return mRecord;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * ʵ�֣���֤ User_TakingId �Ƿ���ڣ�������֤ͨ������½�ɹ�
	 * */
	public Record login_TK(String User_TakingId){
		Record mRecord = Db.findById("islandtrading_user", "User_TakingId", User_TakingId);
		
		return mRecord;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * ʵ�֣�ע�� User_TakingId
	 * */
	public boolean register_TK(Record mRecord){
		String User_TakingId = mRecord.getStr("User_TakingId");	//����tkid
		String sql = "select * from islandtrading_user where User_TakingId='" + 
				User_TakingId + "'";
		List<Record> list = new ArrayList<>();
		list = Db.find(sql);
		for(Record mRec : list){
			String tkid = mRec.getStr("User_TakingId");
			if(tkid.equals(User_TakingId)){	//tkid�Ѵ��ڣ�ע��ʧ�ܣ�
				System.out.println("----register_TK����User_TakingId" + User_TakingId + "�Ѵ��ڣ�");
				return false;
			}
		}
		boolean res = Db.save("islandtrading_user", mRecord);
		return res;
	}
	
	/* ���ߣ�������
	 * ʵ�֣��༭ָ����Ʒ
	 * ����ֵ���༭���
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/editGoods?goodsCode={Product_Id:2,PRODUCT_NAME:����,PRODUCT_PRICE:50,PRODUCT_DESCRIBE:����,PRODUCT_SITE:�����ص�,PRODUCT_STATUS:false}
	 * */
	public int editMyGood(String Product_Id, String PRODUCT_NAME,
						String PRODUCT_PRICE,
						String PRODUCT_DESCRIBE,
						String PRODUCT_SITE, 
						String PRODUCT_STATUS){
		float price = Float.valueOf(PRODUCT_PRICE).floatValue();
		boolean b_status = Boolean.valueOf(PRODUCT_STATUS).booleanValue();
		
//		System.out.println("update("+pName+","+pID+","+ ") success!");
		String sql = "UPDATE islandtrading_product SET PRODUCT_NAME='"+PRODUCT_NAME+"',PRODUCT_PRICE="+price+ 
					", PRODUCT_DESCRIBE='" + PRODUCT_DESCRIBE  + "' ,PRODUCT_SITE='" +
					PRODUCT_SITE + "', PRODUCT_STATUS=" + b_status + " WHERE Product_Id='"+Product_Id+"'";
		int res = Db.update(sql);
		return res;
	}
	
	
	/*
	 * ʵ�֣������Ʒ,ֻ�����Ʒ����
	 * ���ߣ�������
	 * ���ڣ�2016-12-13
	 * �漰��islandtrading_product
	 * */
	public boolean add_Goods(Record mRecord){
		
		boolean res = Db.save("islandtrading_product", mRecord);	
		return res;
	}
	
	/*
	 * �����Ʒ������
	 * ���ߣ�������
	 * ���ڣ�2016-12-13
	 * �漰��product_user
	 * ���ҳ�pid�����ҳ�userid
	 * */
	public boolean add_Goods_User(String pName, String userName){
		Record mRecord = Db.findById("islandtrading_product", "Product_Name", pName);
		int Product_Id = mRecord.getInt("Product_Id");	//�ҵ� Product_Id
		Record mRecord_user = Db.findById("islandtrading_user", "User_Username", userName);
		if(mRecord_user == null){
			System.out.println("-----��islandtrading_user�����ڴ�user��" + userName);
			return false;
		}
		int USER_ID = mRecord_user.getInt("User_Id"); 
		Record myRecord = new Record().set("User_Id", USER_ID)
									.set("Product_Id", Product_Id);
		boolean res = Db.save("re_product_user", myRecord);
		return res;
	}
	
	/*
	 * ���ͼƬ���ص�ַ
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * ���������뱾�ط��������ļ�����
	 * */
	public boolean add_img_url(int Product_Id, String img_Name){
		String sql = "update islandtrading_product set Product_Image='" + 
				img_Name  + "' where Product_Id=" + Product_Id;
		Record mRecord = new Record()
				.set("Product_Id", Product_Id)
				.set("Product_Image_Url", "http://192.168.194.2:8080/IslandTrading/analysis/downloadImg?Product_Id=" + Product_Id);
		Db.update("islandtrading_product", "Product_Id",mRecord);
		
		int i = Db.update(sql);
		if(i == 1){
			return true;
		}
		return false;
	}
	
	/*
	 * ͨ��Product_Id�õ���ƷͼƬ����
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * */
	public String getImg(int pid){
		Record mRecord = Db.findById("islandtrading_product", "Product_Id", pid);
		String Product_Image = mRecord.getStr("Product_Image");
		System.out.println("-----getImg()�ҵ���ͼƬ����" + Product_Image);
		
		return Product_Image;
	}
	
	
	
	
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��act�༭�����
	 * ���������ĸ��ֲ���
	 * ����ֵ��int
	 * */
	public int edit_act(String Activity_Id,
			String ACTIVITY_CONTENT,
			String ACTIVITY_ORGANIZER,
			String ACTIVITY_TIME,
			String ACTIVITY_SITE,
			String ACTIVITY_NAME){
		int res = 0;		
		System.out.println("-----����++"+ACTIVITY_CONTENT+ACTIVITY_TIME);
//		int act_id = Integer.valueOf(Activity_Id);	//���ţ�����Ҳ����Ҫ 
//			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ACTIVITY_TIME);	//�����ݿ���д����Ҫ���˷��˺ö�ʱ��...
		String sql = "update islandtrading_activity set ACTIVITY_CONTENT = '" + ACTIVITY_CONTENT + 
				"', ACTIVITY_ORGANIZER='" + ACTIVITY_ORGANIZER + 
				"', ACTIVITY_TIME='" + ACTIVITY_TIME + 
				"', ACTIVITY_SITE='" + ACTIVITY_SITE +
				"', ACTIVITY_NAME='" + ACTIVITY_NAME +
				"' where Activity_Id=" + Activity_Id;
		res = Db.update(sql);
		return res;
	}
	

	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��Activity_Id�ҵ����¼USER_ID,��USER_IDƥ��APP��USER_ID
	 * ������Activity_Id
	 * */
	public String fetch_User_By_Act(String Activity_Id){
//		String sql = "select USER_ID from activity_user where Activity_Id='" + Activity_Id + "'";
		int activity_id = Integer.valueOf(Activity_Id).intValue();
		Record mRecord = Db.findById("re_activity_user", "Activity_Id", activity_id);
		System.out.println("----fetch_User_By_Act()�õ��� USER_ID��" + mRecord.getInt("User_Id"));
		return mRecord.getInt("User_Id") + "";
	}
	

	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣��û�ͨ���ֻ�ɾ�������Ļ
	 * ������Activity_Id
	 * */
	public boolean del_act(String USER_ID, String Activity_Id){
		boolean res = Db.deleteById("islandtrading_activity", "Activity_Id", Activity_Id);
		return res;
	}
	

	/*
	 * �����û��ķ�����Ϣ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������
	 * ����ֵ��boolean �����ύ���
	 * ����ע�ͣ���ȡ����ע��ת�룡����
	 * 
	 * 2016-12-8�޸�
	 * �޸��˲���
	 * */
	public boolean subfb( String content, 
			String contact, String time){
//		int i_id = Integer.parseInt(id);
//		boolean b_status = Boolean.valueOf(status).booleanValue();
		java.sql.Date time_sql = null;
		Date time_date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			time_date = sdf.parse(time);	//�����ݿ��в������ָ�ʽ��ȷ
			time_sql = new java.sql.Date(time_date.getTime());	//�����ݿ��в������ָ�ʽ����ȷ
		} catch (ParseException | java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().
				set("Fb_Content", content).
				set("Fb_Contact", contact).
				set("Fb_Time", time_date).
				set("Fb_Status", false);
		System.out.println("��ɵ�order------"+order.toString());
		boolean res = false;
		try {
			res = Db.save("islandtrading_feedback", order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("----OrderBiz.java�����������쳣������");
		}
		return res;
	}
	
	/*
	 * ע���û���Ϣ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������user��pwd
	 * ����ֵ��Boolean ע����
	 * App��ע�ͣ�powerĬ��Ϊ0�����Ȩ�ޣ�
	 * 			������Ҫ�϶࣬�������֤�˻��������ţ���ַ��߼Ӹ� ���� nick�ͺ���
	 * ����ע�ͣ�ע������ת��
	 * 
	 * 2016-12-8�޸�
	 * �û������ݶ� ����ҪЯ��������Ĭ��Ϊ��ͼ���0������Աͨ����̨�޸�
	 * ������ʽ��
	 * 
	 * 
	 * */
	public boolean reg_user(String User_Nickname, String User_Username, String User_Password,
							String User_TakingId, String User_Tel, String Hx_Username,String Hx_Password){
//		System.out.println("reg_user()�еĲ��� user:" + user + " pwd:" + pwd + " nick:" + nick);
		
		//ע���ʱ���û����ظ���ʱ����ʾ�Ѵ���
		Record mRecord_user = Db.findById("islandtrading_user", "User_Username", User_Username);
		if(mRecord_user != null){
			System.out.println("----ע��ʧ�ܣ��û����Ѵ��ڣ�");
			return false;
		}
		
		Record mRecord = new Record().set("User_Nickname", User_Nickname)
				.set("User_Username", User_Username)
				.set("User_Password", User_Password)
				.set("User_TakingId", User_TakingId)
				.set("User_Power", 0)
				.set("User_Tel", User_Tel)
				.set("Hx_Username", Hx_Username)
				.set("Hx_Password", Hx_Password);
		boolean res = false;
		try {
			res = Db.save("islandtrading_user", mRecord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ע��ʧ��ඣ�");
			e.printStackTrace();
		}
		return res;
	}
	
	/*
	 * ���涩����Ϣ
	 * ���ߣ�������	ԭsave�����Ѹ���
	 * ���ڣ�2016-11-29
	 * ������һ��
	 * */
	public boolean save(String oid, String address,
					String time, String status){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date time_date = null;
		boolean b_status = Boolean.valueOf(status).booleanValue();
		try {
			time_date = sdf.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().set("Order_Id", oid)
				.set("Order_Site", address)
				.set("Order_Time", time_date)
				.set("Order_Status", b_status);
		boolean res = false;
		try {
			res = Db.save("islandtrading_order", order);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("��������ʧ�ܣ�����");
			e.printStackTrace();
		}
		return res;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-17
	 * ʵ�֣�ͨ��User_Id�õ��䷢������Ʒ �����ظ�User_Id���з�������Ʒ
	 * */
	public List<Record> myRel(String User_Id){
		String sql = "select * from re_product_user where User_Id=" + 
				User_Id + "";
		List<Record> list_temp = new ArrayList<>();
		List<Record> list = new ArrayList<>();
		list_temp = Db.find(sql);
		for(Record mRecord : list_temp){
			int Product_Id = mRecord.getInt("Product_Id");
			Record myRecord = Db.findById("islandtrading_product", "Product_Id", Product_Id);
			list.add(myRecord);
		}
		System.out.println("-----�ҵķ���list:" + list.toString());
		return list;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-17
	 * ʵ�֣�ͨ��User_Id�õ���������Ʒ��ͨ��Product_Status�õ���������Ʒ
	 * */
	public List<Record> mySell(int User_Id){
		
		String sql = "select * from islandtrading_product where Product_Id in(" + 
					"select Product_Id from re_product_user where User_Id =" + 
					User_Id + ")";
		List<Record> list = new ArrayList<>();
		List<Record> list_sell = new ArrayList<>();
		list = Db.find(sql);
		for(Record mRecord : list){
			if(mRecord.getBoolean("Product_Status")){
				System.out.println("----������mRecord��" + mRecord.toString());
				list_sell.add(mRecord);
			}
		}
		System.out.println("----������list_sell��" + list_sell.toString());
		return list_sell;
	}
	

	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-17
	 * ʵ�֣�ͨ��User_Id�õ�������Ϣ���Ӷ����л�ȡ�򵽵���Ʒ
	 * */
	public List<Record> myBuy(int User_Id){
		String sql = "select * from islandtrading_product where Product_Id in(" + 
					"select Product_Id from re_user_order_product where User_Id=" + 
					User_Id + ")";
		List<Record> list = new ArrayList<>();
		list = Db.find(sql);
		return list;
		
		
	}


	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-20
	 * ��ȡ�Ƽ���Ʒ
	 * */
	public List<Record> getTop(){
		List<Record> list = new ArrayList<>();
		String sql = "select * from islandtrading_product where Product_Top=" + 1;
		list = Db.find(sql);
		System.out.println("----�Ƽ���Ʒ��" + list.size() +"  " + list.toString());
		return list;
	}
	

	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-20
	 * ��Ʒ����ʱ������ re_product_classify ��ϵ
	 * */
	public boolean setType(String pType, String Product_Name){
		String sql = "select * from islandtrading_product where Product_Name='" + 
				Product_Name + "'";
		List<Record> list = new ArrayList<>();
		list = Db.find(sql);
		for(Record mRecord : list){
			if(mRecord.get("Product_Type") == null){
//				int Classify_Id = Db.find("select Classify_Id from islandtrading_classify where Classify_Name='" + 
//						pType + "'");
				Record record = Db.findById("islandtrading_classify", "Classify_Name", pType);
				int Classify_Id = record.getInt("Classify_Id");		//�õ���Ʒ����id
				int pid = mRecord.getInt("Product_Id");		//�õ���Ʒid
				System.out.println("-----û��type������pid��" + pid);
				Db.update("update islandtrading_product set Product_Type='" + pType + "' where Product_Id=" + pid);
				Record type_record = new Record().set("Product_Id", pid)
									.set("Classify_Id", Classify_Id);
				Db.save("re_product_classify", type_record);
			}
		}
		
		return false;
	}
	
}
