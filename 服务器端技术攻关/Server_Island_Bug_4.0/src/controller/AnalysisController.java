/**
 * Function:AnalysisController
 * Date:2016.12.11
 * Author:SunCheng
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.AnalysisBiz;
import service.OrderBiz;
import service.ProductBiz;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class AnalysisController extends Controller {
	AnalysisBiz analysisService = this.enhance(AnalysisBiz.class);
	OrderBiz orderService = this.enhance(OrderBiz.class);
	ProductBiz productService = this.enhance(ProductBiz.class);

	/**
	 * 
	 * 2016-11-27 ��Ʒ��ѯ ����pId��pName�������Ʒ����ϸ��Ϣ
	 * ������ʽ��http://localhost:8080/supermarket/analysis/lookupprice?pName={pName:
	 * ��Ϊ}
	 * 
	 * App��ע�ͣ� App����Ҫ����json���󡢽��� App����������Я��json��������keyΪ "pId"����"pName"��
	 * App��pId��pNameΪkeyֵ�������� pIdĬ��Ϊ123456����ʱ��ΪAppû��Я��pId���������⣬��̨���Ҫ�ܿ���pId��
	 * pNameĬ��Ϊ��Default��,��ʱ��ΪAppû��Я��pName������
	 * 
	 * ����ע�ͣ� App����������Я��json��������keyΪ "pId"����"pName"��
	 * pIdĬ��Ϊ123456����ʱ��ΪAppû��Я��pId���������⣬��̨���Ҫ�ܿ���pId��
	 * pNameĬ��Ϊ��Default��,��ʱ��ΪAppû��Я��pName������
	 * jsonContent_pId��jsonContent_pName��ͨ�������õ�����Ʒ��ϸ��Ϣ��(����ֻȡ��һ)
	 * ͨ��jsonContent_pId �� jsonContent_pName�½�JSONObject��������һ�������ݿ����ж�Ӧ���ɲ�ѯ�������
	 * ͨ�� res �ؼ��ּ�����ѯ�����"ok"Ϊ��ѯ����Ʒ��"no"Ϊû�в�ѯ��ָ����Ʒ һ��ע�� ���� ���⣡����ֻ���漰����ַ�е�����!!!
	 * 
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/lookupprice?pId={
	 * Product_Id:3}
	 * http://localhost:8080/IslandTrading/analysis/lookupprice?pName={
	 * Product_Name:Apple}
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void lookupprice() throws UnsupportedEncodingException {

		String jsonContent_pId = this.getPara("pId"); // �õ�����
		String jsonContent_pName = this.getPara("pName"); // �õ�����
		String jsonContent = jsonContent_pId == null ? jsonContent_pName : jsonContent_pId; // ���Ȳ�ѯpid
		System.out.println("-----00�õ��Ĳ�����" + jsonContent_pId + "  " + jsonContent_pName);
		int pid = 0; // Ĭ��pid��Ӧ����Ϊ�˷�ֹ��ȡ����pId���õ�
		String pName = "Default"; // Ĭ��pName��Ϊ�˷�ֹ��ȡ����pName���õ�
		String res = "ok"; // ��ǣ���ѯ�ɹ�������ʱ�����Դ�keyΪ��ѯ�����okΪ������Ӧ��Ʒ��noΪ������
		float price;
		String str_des;
		String str_type; // ��Ҫ�м���ѯ

		// ��������
		String str_site;
		int hit = 0;
		int favour = 0;
		boolean status = false;
		boolean top = false;
		double Product_Lagitude;
		double Product_Longgitude;
		String Product_Image_Url;

		Date date_time;
		String str_time;

		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();

		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		if (jsonContent_pId != null) {
			System.out.println("Я����pId��" + jsonContent_pId + "  pName��" + jsonContent_pName);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				pid = jsonObject.getInt("Product_Id"); // �õ���Ʒpid
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("lookup price Product_Id:" + pid + "  lookup pname Product_Name:" + pName);
			Record rec_pId = analysisService.lookupprice(pid);

			// ���û��ָ����Ʒ��ִֹͣ��
			if (rec_pId == null) {
				res = "no";
				System.out.println("-----û��ָ����Ʒ lookupprice���� Product_Id:" + pid);
				try {
					content.put("res", res);
					content.put("tip", "û��ָ����Ʒ   Product_Id:" + pid);
					good.put("PRODUCT", content);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// writer.write("û��ָ����Ʒ Product_Id:" + pid);
				writer.write(good.toString());
				this.renderNull();
				return;
			}

			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pId.getFloat("Product_Price");
			// price=Float.parseFloat(new
			// java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
			pName = rec_pId.getStr("Product_Name");
			str_des = rec_pId.getStr("Product_Describe");
			str_type = analysisService.getClassify(pid);
			str_site = rec_pId.getStr("Product_Site");
			date_time = rec_pId.getDate("Product_Time");
			str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);
			hit = rec_pId.getInt("Product_View");
			favour = rec_pId.getInt("Product_Positive");
			status = rec_pId.getBoolean("Product_Status");
			top = rec_pId.getBoolean("Product_Top");
			Product_Lagitude = rec_pId.getDouble("Product_Lagitude");
			Product_Longgitude = rec_pId.getDouble("Product_Longgitude");
			Product_Image_Url = rec_pId.getStr("Product_Image_Url");
		} else { // ֻЯ����pName
			System.out.println("Я����pId��" + jsonContent_pId + "  pName��" + jsonContent_pName);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String pName_temp = jsonObject.getString("Product_Name"); // �õ���ƷpName
				pName = new String(pName_temp.getBytes("iso-8859-1"), "UTF-8");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("lookup pId:" + pid + "  lookup pName:" + pName);
			Record rec_pName = analysisService.lookup_pName(pName);

			// ���û��ָ����Ʒ��ִֹͣ��
			if (rec_pName == null) {
				res = "no";
				System.out.println("-----û��ָ����Ʒ lookupprice���� pName:" + pName);
				writer.write("û��ָ����Ʒ PRODUCT_NEME:" + pName);
				this.renderNull();
				return;
			}
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pName.getFloat("Product_Price");
			pid = rec_pName.getInt("Product_Id");
			str_des = rec_pName.getStr("Product_Describe");
			str_type = analysisService.getClassify(pid);
			str_site = rec_pName.getStr("Product_Site");
			date_time = rec_pName.getDate("Product_Time");
			str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);

			str_site = rec_pName.getStr("Product_Site");
			hit = rec_pName.getInt("Product_View");
			favour = rec_pName.getInt("Product_Positive");
			status = rec_pName.getBoolean("Product_Status");
			top = rec_pName.getBoolean("Product_Top");
			Product_Lagitude = rec_pName.getDouble("Product_Lagitude");
			Product_Longgitude = rec_pName.getDouble("Product_Longgitude");
			Product_Image_Url = rec_pName.getStr("Product_Image_Url");
		}
		// ��װJson��
		try {

			// {"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}
			System.out.println("name" + pName);

			content.put("Product_Id", pid);
			content.put("Product_Name", pName);
			content.put("Product_Price", price);
			content.put("Product_Describe", str_des);
			content.put("Classify_Name", str_type);
			content.put("Product_Site", str_site);
			content.put("Product_Time", str_time);
			content.put("Product_Lagitude", Product_Lagitude);
			content.put("Product_Longgitude", Product_Longgitude);

			// ����������
			content.put("Product_Positive", favour);
			content.put("Product_View", hit);
			content.put("Product_Status", status);
			content.put("Product_Top", top);
			content.put("PRODUCT_TYPE", str_type);
			content.put("Product_Image_Url", Product_Image_Url);

			// {"res":"1","content":{"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}}
			json.put("res", res);
			json.put("content", content);
			good.put("PRODUCT", json);

			System.out.println("----ƴ�ӵ�PRODUCT:" + good.toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		writer.write(good.toString());
		writer.flush();
		writer.close();
		// this.renderJson(jsonText);
		this.renderNull();
	}

	/*
	 * ����ɸѡ ���ߣ������� ���ڣ�2016-11-28
	 * ������ʽ��http://localhost:8080/supermarket/analysis/type_collection?pType={
	 * pType:����} App��ע�ͣ��������ͼƬ����ȡ����ؼ��֣���ѯ����ͬ���͵�������Ʒ��
	 * ����ע�ͣ�ע�⣬��ַ�漰�����ģ�����ֻ������ַ���ļ��ɣ� ע���޸�AnalysisBiz����õķ�����
	 * Ŀǰֻ�ܵõ�һ����Ʒ��Ϣ��ͨ��ѭ����ﵽ��������ܣ�
	 * 
	 * 2016-12-8�޸�, bug���֣�����ɸѡ����������json����Ҫ��json���������� []
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/type_collection?pType={
	 * pType:����} ����ֵ��JSONArray
	 */
	public void type_collection() {
		String jsonContent = this.getPara("pType"); // �õ�Я���Ĳ���pType
		String str_type = "default"; // ����Ĭ������
		String res = "ok"; // Ĭ�ϲ�ѯ���

		// ���ڴ洢���ĳ��Ʒ����ϸ��Ϣ
		long pid = 0;
		String pName = null;
		float price = 0;
		String str_des = null;
		String str_site = null;
		Date date_time;
		String str_time = null;

		// ��������
		int hit = 0;
		int favour = 0;
		boolean status = false;
		boolean top = false;
		double Product_Lagitude;
		double Product_Longgitude;
		String Product_Image_Url;

		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		JSONArray goods = new JSONArray();
		System.out.println("----�õ���jsonContent:" + jsonContent.toString());
		if (jsonContent != null) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String str_temp = jsonObject.getString("pType");
				str_type = new String(str_temp.getBytes("iso-8859-1"), "UTF-8");
				System.out.println("----���ķ� type:" + str_type); // �ɹ��õ�����
				List<Record> list_Record_type = new ArrayList<>();
				list_Record_type = analysisService.lookup_type(str_type);

				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();

				if (list_Record_type.size() == 0) {
					res = "no";
					System.out.println("type_collection()------���������ƷΪ�գ�" + str_type);
					writer.write(str_type + "�����û����Ʒ��");
					this.renderNull();
				}
				// int num = 0;
				// ѭ�����ұ�
				for (Record rec_pType : list_Record_type) {
					content = null;
					json = null;
					good = null;
					content = new JSONObject();
					json = new JSONObject();
					good = new JSONObject();
					// ��ȡ��Ʒ�ֶ���Ϣ
					pid = rec_pType.getInt("Product_Id");
					pName = rec_pType.getStr("Product_Name");
					price = rec_pType.getFloat("Product_Price");
					str_des = rec_pType.getStr("Product_Describe");
					str_site = rec_pType.getStr("Product_Site");
					date_time = rec_pType.getDate("Product_Time");
					str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);

					hit = rec_pType.getInt("Product_View");
					favour = rec_pType.getInt("Product_Positive");
					status = rec_pType.getBoolean("Product_Status");
					top = rec_pType.getBoolean("Product_Top");
					Product_Lagitude = rec_pType.getDouble("Product_Lagitude");
					Product_Longgitude = rec_pType.getDouble("Product_Longgitude");
					Product_Image_Url = rec_pType.getStr("Product_Image_Url");

					// ��װjson��
					try {
						System.out.println("name" + pName);
						content.put("Product_Id", pid);
						content.put("Product_Name", pName);
						content.put("Product_Price", price);
						content.put("Product_Describe", str_des);
						content.put("Classify_Name", str_type);
						content.put("Product_Site", str_site);
						content.put("Product_Time", str_time);

						content.put("Product_View", hit);
						content.put("Product_Positive", favour);
						content.put("Product_Status", status);
						content.put("Product_Top", top);
						content.put("Product_Lagitude", Product_Lagitude);
						content.put("Product_Longgitude", Product_Longgitude);
						content.put("Product_Image_Url", Product_Image_Url);

						json.put("res", res);
						json.put("content", content);
						good.put("good", json);
						goods.put(good);

						System.out.println("----good��:" + good.toString());

						// writer.append(good.toString());

						// this.renderJson(jsonText);
						this.renderNull();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} // for
				writer.write(goods.toString());
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} // if(jsonContent != null)
	}

	/*
	 * �����б� ���ߣ������� ���ڣ�2016-11-28
	 * ������ʽ��localhost:8080/supermarket/analysis/request_acts ����������Ҫ����
	 * ʵ�֣�App�����ȡ���л
	 * 
	 * 2016-12-8�޸� ������ʽ��localhost:8080/IslandTrading/analysis/request_acts
	 * 
	 */
	public void request_acts() {
		String res = "ok"; // Ĭ�ϲ�ѯ���

		// ���ڴ洢��õĻ��ϸ��Ϣ
		long id;
		String str_name;
		String str_content;
		String str_organizer;
		Date date_temp;
		String str_time;
		String str_site;
		String Activity_Img;

		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		JSONArray goods = new JSONArray();
		try {
			List<Record> list_Record_act = new ArrayList<>();
			list_Record_act = analysisService.lookup_act();

			PrintWriter writer;
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			writer = response.getWriter();
			if (list_Record_act.size() == 0) {
				res = "no";
				System.out.println("request_acts()------��ʱû�л��");
				writer.write("��ʱû�л��");
				this.renderNull();
			}
			int num = 0;
			// ѭ�����ұ�
			for (Record rec_pType : list_Record_act) {
				content = null;
				json = null;
				good = null;
				content = new JSONObject();
				json = new JSONObject();
				good = new JSONObject();
				System.out.println("-----��Χfor��" + rec_pType.toString());
				// ��ȡ��Ʒ�ֶ���Ϣ
				id = rec_pType.getInt("Activity_Id");
				str_name = rec_pType.getStr("Activity_Name");
				str_content = rec_pType.getStr("Activity_Content");
				str_organizer = rec_pType.getStr("Activity_Organizer");
				date_temp = rec_pType.getDate("Activity_Time");
				str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_temp);
				str_site = rec_pType.getStr("Activity_Site");
				Activity_Img = rec_pType.getStr("Activity_Img");

				// ��װjson��
				try {
					System.out.println("name" + str_name);
					content.put("Activity_Id", id);
					content.put("Activity_Name", str_name);
					content.put("Activity_Content", str_content);
					content.put("Activity_Organizer", str_organizer);
					content.put("Activity_Site", str_site);
					content.put("Activity_Time", str_time);
					content.put("Activity_Img", Activity_Img);

					json.put("res", res);
					json.put("content", content);
					good.put("good", json);
					goods.put(num++, good);
					System.out.println("----good��:" + good.toString());

					// writer.append(good.toString());

					// this.renderJson(jsonText);
					this.renderNull();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // for
			writer.write(goods.toString());
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/*
	 * �༭� ���ߣ������� ���ڣ�2016-12-11 ������act
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/editActs?act={
	 * Activity_Id:1,Activity_Content:������޸�,Activity_Organizer:���֯�޸�,
	 * Activity_Time:'2016-12-12
	 * 12:12:12',Activity_Site:��ص��޸�,Activity_Name:����޸�} ������
	 */
	public void editActs() {
		String jsonContent = this.getPara("act");
		System.out.println("----act���:" + jsonContent);

		// //�洢Ҫ���ĵ�����
		String Activity_Id;
		String Activity_Content;
		String Activity_Organizer;
		String Activity_Time;
		String Activity_Site;
		String Activity_Name;

		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			Activity_Id = jsonObject.getString("Activity_Id");
			Activity_Content = new String((jsonObject.getString("Activity_Content")).getBytes("iso-8859-1"), "UTF-8");
			Activity_Organizer = new String((jsonObject.getString("Activity_Organizer")).getBytes("iso-8859-1"),
					"UTF-8");
			Activity_Time = jsonObject.getString("Activity_Time");
			Activity_Site = new String((jsonObject.getString("Activity_Site")).getBytes("iso-8859-1"), "UTF-8");
			Activity_Name = new String((jsonObject.getString("Activity_Name")).getBytes("iso-8859-1"), "UTF-8");

			// System.out.println("��õĲ�������ô��-----"+Activity_Content + " " +
			// Activity_Organizer + " " + Activity_Time + " " +
			// Activity_Site);

			int res = analysisService.edit_act(Activity_Id, Activity_Content, Activity_Organizer, Activity_Time,
					Activity_Site, Activity_Name);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if (res != 0) { // �༭��ɹ�
				writer.write("��༭�ɹ���Activity_Id��" + Activity_Id);
				writer.flush();
				writer.close();
				this.renderNull();

			} else {
				writer.write("��༭ʧ�ܣ�Activity_Id��" + Activity_Id);
				writer.flush();
				writer.close();
				this.renderNull();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * ɾ������û������� ���ߣ������� ���ڣ�2016-12-11 ע�ͣ�APP��ƴ������ӿڣ�APP�˻�ȡ�id���϶����ԣ�
	 * �жϻ�ǲ��Ǵ��û������� ������
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/deleteAct?Activity_Id=1
	 * &User_Id=1
	 */
	public void deleteAct() {
		String Activity_Id = this.getPara("Activity_Id");
		String User_Id = this.getPara("User_Id");
		System.out.println("-----deleteAct������Activity_Id:" + Activity_Id + "  User_Id:" + User_Id);
		String User_Id_fetch = analysisService.fetch_User_By_Act(Activity_Id); // �ҵ���Activity_Id�ķ�����
		System.out.println("-----User_Id_fetch:" + User_Id_fetch);

		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (User_Id.equals(User_Id_fetch)) { // ��activity�����ߺ�APP��½��userid��ͬ
			boolean res = analysisService.del_act(User_Id, Activity_Id);
			if (res == true) {
				writer.write("ɾ���ɹ�! Activity_Id��" + Activity_Id);
				writer.flush();
				writer.close();
				this.renderNull();
			} else {
				writer.write("ɾ��ʧ��! Activity_Id��" + Activity_Id);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		} // if(User_Id == User_Id)
		else { // APP��½�߲���ɾ�����˷����Ļ������Ҳ�߲��������֧����
			writer.write("����ɾ�����˵Ļ! Activity_Id��" + Activity_Id);
			writer.flush();
			writer.close();
			this.renderNull();
		}
	}

	/*
	 * �����ҵ��ղ� ���ߣ������� ���ڣ�2016-11-28
	 * ������ʽ��http://localhost:8080/supermarket/analysis/request_col?user={user:
	 * 20161130} App��ע�ͣ�Я��id_user��id_goods����id�����ܻ�ȡ׼ȷ��Ʒ ����ֻЯ��id_user���ﵽ��ȡĳ���ղص�Ч��
	 * ����ע�ͣ���������ȷ��׼ȷ��Ʒ��user����������Ʒ�� ��Ҫ����Я���Ĳ�����������α�ʾ������user�ش������ԣ����ж�goods_id����
	 * ����ҳ����ʾ��ʾ��Ϣ��ͨ�� writer.write("  ");
	 * this.renderNull();֪ͨjfinal��ת���ĸ�ҳ�棬��仰Ӧ���ǲ���ת��
	 * Sql��ͨ��bit�洢java�е�booleanֵ����ֱ�� getboolean(coloum)
	 * 
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/request_col?User_Id={
	 * User_Id:20161130}
	 * http://localhost:8080/IslandTrading/analysis/request_col?User_Id={User_Id
	 * :20161130}&goods_id={goods_id:2}
	 * 
	 */
	public void request_col() {
		String jsonContent_goods = this.getPara("goods_id");
		String jsonContent_user = this.getPara("User_Id");
		String goods_id = "default"; // �õ�goods_id
		String User_Id = null;

		// �洢��Ʒ��Ϣ��
		long Product_Id = 0;
		String Product_Name = null;
		float Product_Price = 0;
		String Product_Describe = null;
		Date time_temp;
		String Product_Time = null;
		String Product_Site = null;
		int Product_View = 0;
		int Product_Positive = 0;
		boolean Product_Status = false;
		boolean Product_Top = false;
		double Product_Lagitude = 0;
		double Product_Longgitude = 0;
		String Product_Image_Url;

		JSONObject json = null;
		JSONObject content = null;
		JSONObject good = null;
		JSONArray goods = new JSONArray();
		if (jsonContent_goods != null) {
			try {
				JSONObject jsonObject_temp = new JSONObject(jsonContent_goods);
				goods_id = jsonObject_temp.getString("goods_id");
			} catch (JSONException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		}
		// ���û��Я��goods_id
		if (goods_id.equals("default")) { // û��Я��goods_id��ֻ����user����
			// res = "ok";
			System.out.println("----ִ��if��");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent_user);
				User_Id = jsonObject.getString("User_Id");
				List<Record> list_Record_Col = new ArrayList<>();
				list_Record_Col = analysisService.lookup_col(User_Id);

				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();

				// ����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if (list_Record_Col.size() == 0) {
					writer.write("�û���" + User_Id + " û���ղ��κ���Ʒ!");
					this.renderNull();
					return;
				}
				// ѭ�����ұ�
				for (Record rec_pType : list_Record_Col) {
					json = new JSONObject();
					content = new JSONObject();
					good = new JSONObject();
					// ��ȡ��Ʒ�ֶ���Ϣ
					Product_Id = rec_pType.getInt("Product_Id");
					Product_Name = rec_pType.getStr("Product_Name");
					Product_Price = rec_pType.getFloat("Product_Price");
					Product_Describe = rec_pType.getStr("Product_Describe");
					Product_Time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
							.format(rec_pType.getDate("Product_Time"));
					Product_Site = rec_pType.getStr("Product_Site");
					Product_View = rec_pType.getInt("Product_View");
					Product_Positive = rec_pType.getInt("Product_Positive");
					Product_Status = rec_pType.getBoolean("Product_Status");
					Product_Top = rec_pType.getBoolean("Product_Top");
					Product_Lagitude = rec_pType.getDouble("Product_Lagitude");
					Product_Longgitude = rec_pType.getDouble("Product_Longgitude");
					Product_Image_Url = rec_pType.getStr("Product_Image_Url");

					// ��װjson��
					try {
						content.put("Product_Id", Product_Id);
						content.put("Product_Name", Product_Name);
						content.put("Product_Price", Product_Price);
						content.put("Product_Describe", Product_Describe);
						content.put("Product_Time", Product_Time);
						content.put("Product_Site", Product_Site);
						content.put("Product_View", Product_View);
						content.put("Product_Positive", Product_Positive);
						content.put("Product_Status", Product_Status);
						content.put("Product_Top", Product_Top);
						content.put("Product_Lagitude", Product_Lagitude);
						content.put("Product_Longgitude", Product_Longgitude);
						content.put("Product_Image_Url", Product_Image_Url);

						json.put("content", content);
						good.put("good", json);
						goods.put(good);

						System.out.println("----good��:" + good.toString());

						// writer.append(good.toString());

						// this.renderJson(jsonText);
						this.renderNull();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("-----�����쳣   e1 !!!");
						e.printStackTrace();
					}
				} // for
				writer.write(goods.toString());
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e2 !!!");
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e3 !!!");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e4 !!!");
				e1.printStackTrace();
			}
		} // ����if ֻЯ��һ��user����
		else { // Я����goods_id������Ʒid��������������
			content = new JSONObject();
			json = new JSONObject();
			good = new JSONObject();

			System.out.println("----ִ��else��");
			JSONObject jsonObject_goods;
			JSONObject jsonObject_user;
			try {
				System.out.println("----else�еõ��Ĳ�����jsonContent_user" + jsonContent_user.toString()
						+ "  jsonObject_goods:" + jsonContent_goods.toString());
				jsonObject_user = new JSONObject(jsonContent_user);
				jsonObject_goods = new JSONObject(jsonContent_goods);
				User_Id = jsonObject_user.getString("User_Id");
				goods_id = jsonObject_goods.getString("goods_id");
				System.out.println("-----user:" + User_Id + "  goods:" + goods_id);
				Record rec_pType = analysisService.lookup_col(User_Id, goods_id);
				// System.out.println(rec_pType.toString()+"-----"); //list��û����
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();

				// ����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if (rec_pType == null) {
					System.out.println("-----û��ƥ������ request_col if��֧.");
					writer.write("��Ʒ�Ѳ����ڻ���������Ʒid�Ƿ���");
					this.renderNull();
					return;
				} else {
					// ѭ�����ұ�
					// for(Record rec_pType: list_Record_type){
					// ��ȡ��Ʒ�ֶ���Ϣ
					Product_Id = rec_pType.getInt("Product_Id");
					Product_Name = rec_pType.getStr("Product_Name");
					Product_Price = rec_pType.getFloat("Product_Price");
					Product_Describe = rec_pType.getStr("Product_Describe");
					Product_Time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
							.format(rec_pType.getDate("Product_Time"));
					Product_Site = rec_pType.getStr("Product_Site");
					Product_View = rec_pType.getInt("Product_View");
					Product_Positive = rec_pType.getInt("Product_Positive");
					Product_Status = rec_pType.getBoolean("Product_Status");
					Product_Top = rec_pType.getBoolean("Product_Top");
					Product_Lagitude = rec_pType.getDouble("Product_Lagitude");
					Product_Longgitude = rec_pType.getDouble("Product_Longgitude");
					Product_Image_Url = rec_pType.getStr("Product_Image_Url");
				}

				// ��װjson��
				try {
					content.put("Product_Id", Product_Id);
					content.put("Product_Name", Product_Name);
					content.put("Product_Price", Product_Price);
					content.put("Product_Describe", Product_Describe);
					content.put("Product_Time", Product_Time);
					content.put("Product_Site", Product_Site);
					content.put("Product_View", Product_View);
					content.put("Product_Positive", Product_Positive);
					content.put("Product_Status", Product_Status);
					content.put("Product_Top", Product_Top);
					content.put("Product_Lagitude", Product_Lagitude);
					content.put("Product_Longgitude", Product_Longgitude);
					content.put("Product_Image_Url", Product_Image_Url);

					json.put("content", content);
					good.put("good", json);
					goods.put(good);
					System.out.println("----good��:" + good.toString());

					writer.write(good.toString());

					// this.renderJson(jsonText);
					this.renderNull();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("-----�����쳣   e1 !!!");
					e.printStackTrace();
				}
				// }//for
				// writer.write(goods.toString());
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e2 !!!");
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e3 !!!");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e4 !!!");
				e1.printStackTrace();
			}
		} // else

	}

	/*
	 * �ύ������ ���ߣ������� ���ڣ�2016-11-29 App��ע�ͣ���ȡ�û���д����Ϣ�� ��Ҫ�淶�ϴ������ڸ�ʽ��
	 * ������ʽ��http://localhost:8080/supermarket/analysis/submit_fb?checkout={"id":
	 * "1","User_Id":"2","content":"���","time":"2016-11-25 11:13:01"} ����ע�ͣ�Я������
	 * checkout
	 * 
	 * 2016-12-8�޸� �޸ģ��ؼ��ָ�Ϊ fb;statusΪfalse��true�����ݿ��Ӧ�洢Ϊ0��1 ע�⣺�ύ���� ʱ�� ������ ����
	 * ˫���� ������ʽ��http://localhost:8080/IslandTrading/analysis/submit_fb?fb={
	 * "FB_CONTENT":"���Ŀ���","FB_CONTACT":"1523015666","FB_TIME":
	 * "2016-12-9 20:20:39"}
	 * 
	 */
	public void submit_fb() {
		String jsonContent = this.getPara("fb");

		String FB_ID;
		String FB_CONTENT;
		String FB_CONTACT;
		String FB_TIME;
		// String FB_STATUS; //�ύ�ķ����϶�����δ���״̬�����Բ�Я��״̬��

		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			// FB_ID = jsonObject.getString("FB_ID");
			FB_CONTENT = new String((jsonObject.getString("FB_CONTENT")).getBytes("iso-8859-1"), "UTF-8");
			FB_CONTACT = jsonObject.getString("FB_CONTACT");
			FB_TIME = jsonObject.getString("FB_TIME");
			// FB_STATUS = jsonObject.getString("FB_STATUS");

			boolean b = analysisService.subfb(FB_CONTENT, FB_CONTACT, FB_TIME);
			System.out.println("-----submit_fb()��õĲ���jsonContent:" + jsonContent + "\nת�����Ч�� content:" + jsonContent);

			if (b == true) {
				// System.out.println("�����ύ�ɹ���id" + FB_ID);
				this.renderText("�����ύ�ɹ���");
				return;
			} else {
				// System.out.println("�����ύʧ�ܣ�id" + FB_ID);
				// this.renderHtml("�����ύʧ�ܣ�id��" + FB_ID);
				return;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("-----submit_fb() �����쳣e1");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * ����û�ע�ᡢ��½������ ���ߣ������� ���ڣ�2016-11-29
	 * ������ʽ��http://localhost:8080/supermarket/analysis/reg_log_user?mode=check&
	 * User_Username=������&User_Password=123abc App��ע�ͣ� App��Я��mode��������֤��½check ����
	 * ע��register ע��ʱ��App���ܻ�ȡ��Ҫע���˺š����벢�ύ
	 * ��¼ʱ��App���ύ�û�������û����������ύ�������ݿ�������֤����App�˷��� ����ע�ͣ�
	 * 
	 * 2016-12-8�޸� ע��ֻ�������ݿ�д����½�����ݿ��������֤�Ƿ���ȷ
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/reg_log_user?mode=check
	 * &User_Username=������&User_Password=123abc
	 * http://localhost:8080/IslandTrading/analysis/reg_log_user?mode=register&
	 * User_Nickname=����&User_Username=�������û���&User_Password=1234&User_TakingId=
	 * 15686565&User_Tel=15230153136&Hx_Username=a12345&Hx_Password=12345
	 */
	public void reg_log_user() {
		String str_mode = this.getPara("mode"); // ������֤��ʽ����½����ע��

		// 1.��̨��֤
		if (str_mode.equals("check")) {
			String user_temp = this.getPara("User_Username"); // ֱ�ӵõ����������ˣ�����json��

			String pwd = this.getPara("User_Password");
			String user = null;
			try {
				user = new String(user_temp.getBytes("iso-8859-1"), "UTF-8");
				System.out.println("----reg_log_user()�õ��Ĳ���--user:" + user);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String str_nickname;

			// JSONObject����������װ�õ��Ĳ��������״̬��ǵ���Ϣ��
			JSONObject json = new JSONObject();
			JSONObject content = new JSONObject();
			JSONObject good = new JSONObject();

			// ����ҳ����Ⱦ��Ϣ
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			Record mRecord = analysisService.lookup_user(user);
			if (mRecord == null) { // �û�������
				System.out.println("-----reg_log_user()--����ֻ��˵��������û�������" + user);
				writer.write("�û��������ڣ�");
				writer.flush();
				writer.close();
				this.renderNull();
			} else { // �û�����ȷ
				System.out.println("-----reg_log_user()�õ��ļ�¼:" + mRecord.toString());

				// �û�����֤ͨ�������������ݿ��е���Ϣ��������֤�����Ƿ���ȷ
				String str_pwd = mRecord.getStr("User_Password");
				if (str_pwd.equals(pwd)) { // �û�����������ȷ
					System.out.println("�û�����������ȷ������");
					str_nickname = mRecord.getStr("User_Nickname");
					writer.write("�û���¼�ɹ���");
					writer.flush();
					writer.close();
					this.renderNull();
				} else { // �û������������
					writer.write("�������");
					this.renderNull();
				}
			}

		} else if (str_mode.equals("register")) {
			try {
				String User_Nickname = new String((this.getPara("User_Nickname")).getBytes("iso-8859-1"), "UTF-8");
				String User_Username = new String((this.getPara("User_Username")).getBytes("iso-8859-1"), "UTF-8");
				String User_Password = this.getPara("User_Password");
				String User_TakingId = this.getPara("User_TakingId");
				String User_Tel = this.getPara("User_Tel");
				String Hx_Username = this.getPara("Hx_Username");
				String Hx_Password = this.getPara("Hx_Password");
				boolean res = analysisService.reg_user(User_Nickname, User_Username, User_Password, User_TakingId,
						User_Tel, Hx_Username, Hx_Password);

				// ��������ҳ����Ⱦ��Ϣ
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();

				if (res == true) {
					writer.write("ע��ɹ��� User_Username:" + User_Username);
					writer.flush();
					writer.close();
					this.renderNull();
				} else {
					System.out.println("str_user::::" + User_Username);
					writer.write("ע��ʧ�ܣ� User_Username:" + User_Username + "�Ѵ��ڣ�");
					writer.flush();
					writer.close();
					this.renderNull();
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} // else if
	}

	/**
	 * �û������ύ ���ߣ������� ���ڣ�2016-11-29
	 * ������total��Ʒ������record��Ʒ��Ϣ��oSum�����ܼۣ�address���׵�ַ��telphone��ҵ绰
	 * ����������Ʒid����һһ�����˺ö��أ�������������ƾͲ��ܶ��򣡣���
	 * 
	 * �������磺/order={osm:30.0,address:"���ѧԺ",telphone:1234566666,User_Id:700,pid:
	 * 7878788}
	 * localhost:8080/supermarket/analysis/oreder?order={osm:30.0,address:"���ѧԺ"
	 * ,telphone:1234566666,User_Id:700,pid:7878788}
	 * 
	 * 
	 * 2016-12-8�޸�
	 * localhost:8080/IslandTrading/analysis/oreder?order={ORDER_ID:1001,
	 * ORDER_SITE:�ӱ�ʦ��,ORDER_TIME:'2016-12-8 20:46:55',OEDER_STATUS:true}
	 * 
	 */
	public void oreder() {

		String jsonContent = this.getPara("order"); // �õ�һ�Ѳ���
		// String jsonContent = "{ oId:A20160816001, total:3,record:["
		// + "{pId:123462,pNum:1},"
		// + "{pId:123460,pNum:2},"
		// + "{pId:123461,pNum:1}]," + "oSum:30.5}";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonContent);
			// �ύ���ݣ�������ΪString
			String ORDER_ID = jsonObject.getString("ORDER_ID");
			String ORDER_SITE = new String((jsonObject.getString("ORDER_SITE")).getBytes("iso-8859-1"), "UTF-8");
			String ORDER_TIME = jsonObject.getString("ORDER_TIME");
			String OEDER_STATUS = jsonObject.getString("OEDER_STATUS");
			boolean res = analysisService.save(ORDER_ID, ORDER_SITE, ORDER_TIME, OEDER_STATUS);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();

			if (res == true) {
				writer.write("�����ύ�ɹ���" + ORDER_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			} else {
				writer.write("�����ύʧ�ܣ�����" + ORDER_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.renderNull();
	}

	/**
	 * �����ύ
	 * 
	 */
	public void palceoreder() {
		// �������� ������(oid)����¼����(total)����Ʒ������(pId)����Ʒ����(pNum)�������ܼ�(oSum)
		// ��������ʱ�䣬���뵽�����������š�ʱ�䡢�ܼۣ�
		// ��ѯ��Ʒ���õ���Ʒ������Ʒ����
		// ���� ��Ʒ�������Ʒ������Ʒ���ۡ���Ʒ�����������ţ�
		/*
		 * { ��oId��:��00001��, ��total��:3,��record��:[ {��pId��:��050043��,�� pNum��:��1�� },
		 * {��pId��:��050044��,�� pNum��:��2�� }, {��pId��:��050045��,�� pNum��:��1��
		 * }],��oSum��:��30.5��}
		 */
		String jsonContent = this.getPara("checkout");
		// String jsonContent = "{ oId:A20160816001, total:3,record:["
		// + "{pId:123462,pNum:1},"
		// + "{pId:123460,pNum:2},"
		// + "{pId:123461,pNum:1}]," + "oSum:30.5}";
		String oid = "A20160816001";
		float osum = 0;
		int total = 0;
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat oidformat = new SimpleDateFormat("yyMMddHHmmss");
		String time = format.format(date);
		oid = oidformat.format(date);
		System.out.println("p2:" + time);
		System.out.println("oid:" + oid);
		try {
			// ����json����,��ȡ�����ţ������м�¼���������ܼ�
			JSONObject jsonObject = new JSONObject(jsonContent);

			// oid = jsonObject.getString("oId");
			total = Integer.parseInt(jsonObject.getString("total"));
			osum = Float.parseFloat(jsonObject.getString("oSum"));

			System.out.println("oid:" + oid + "total:" + total + "osum:" + osum);

			// ���涩����¼
			// orderService.save(oid, time, osum);

			JSONArray jsonrecos = jsonObject.getJSONArray("record");
			JSONObject jsonreco = null;
			List<Record> recordList = new ArrayList<Record>();

			// ������� ��Ʒ������Ʒ�۸���������������
			String pID;
			String pName;
			float pPrice;
			String pNum;

			// ���������м�¼
			for (int i = 0; i < total; i++) {

				jsonreco = jsonrecos.getJSONObject(i);

				// ������Ʒ�ţ������Ʒ���Ƽ�����
				pID = jsonreco.getString("pId");
				System.out.println("pID:" + pID);

				Record product = productService.findByID(pID);

				// ��ȡ��Ʒ��Ϣ
				pName = product.getStr("name");
				pPrice = product.getFloat("price");
				pNum = jsonreco.getString("pNum");

				System.out.println("name:" + pName + "price:" + pPrice + "pNum:" + pNum + "oid:" + oid);

				// ����Ʒ��Ϣ��װ�ɼ�¼
				Record record = new Record().set("name", pName).set("price", pPrice).set("number", pNum).set("oid",
						oid);
				System.out.println("name:" + i + ":" + record.getStr("name"));
				recordList.add(i, record);
			}

			for (Record record2 : recordList) {
				System.out.println("name:" + record2.getStr("name"));
			}

			// �����洢������ϸ��Ϣ��¼
			int[] res = orderService.batchsave(recordList, total);
			JSONObject resjson = new JSONObject();
			JSONObject order = new JSONObject();
			if (res.length > 0) {
				resjson.put("res", "ok");
			} else {
				resjson.put("res", "no");
			}
			order.put("order", resjson);
			// ��Ӧ
			try {
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.write(order.toString());
				writer.flush();
				writer.close();
				// this.renderJson(jsonText);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.renderNull();
	}

	/*
	 * ���ߣ����� ɾ����Ʒ
	 * 
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/deleteById?goodsCode={
	 * Product_Id:123456}
	 */
	public void deleteById() {
		String jsonContent = this.getPara("goodsCode");// ����"{pid:123458}"
		String pId = "0"; // Ĭ��pId
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonContent);
			pId = jsonObject.getString("Product_Id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean delete_result = analysisService.deleteMyGood(pId);
		if (delete_result == true) {
			System.out.println("deleteById" + "ɾ���ɹ���");
			this.renderHtml("ɾ���ɹ���" + pId);
		} else {
			System.out.println("deleteById������" + "ɾ��ʧ�ܣ�");
			this.renderHtml("ɾ��ʧ�ܣ�" + pId);
		}
	}

	/*
	 * ���ߣ����� �༭��Ʒ
	 * 
	 * 2016-12-8�޸� ע�ͣ� ����ʱ��ʼ��Ϊ�༭ʱ�䣬�������ã�ϵ�Զ����ã� ��γ�Ȳ������޸ģ� �����Ա༭����������������ö���
	 * booleanֱ����url�л�ȡ getBoolean ������ Я�����ٲ�������Ӧ������Ĭ�ϲ���
	 * ͨ��APP�˻�ȡProduct_Id�޸���Ʒ��Ϣ��Product_Name��ȻҲ��Ϊ���������������ظ������Բ���������ȷ����Ʒ
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/editGoods?goodsCode={
	 * Product_Id:4,Product_Name:�ֻ�,Product_Price:50,Product_Describe:�ݻ��ֻ�,
	 * Product_Site:�����ص�,Product_Status:true}
	 */
	public void editGoods() {

		String jsonContent = this.getPara("goodsCode");

		String Product_Id;
		String Product_Name;
		String Product_Price;
		String Product_Describe;
		String Product_Site;
		String Product_Status;
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			Product_Id = jsonObject.getString("Product_Id");
			Product_Name = new String((jsonObject.getString("Product_Name")).getBytes("iso-8859-1"), "UTF-8");
			Product_Price = jsonObject.getString("Product_Price");
			Product_Describe = new String((jsonObject.getString("Product_Describe")).getBytes("iso-8859-1"), "UTF-8");
			Product_Site = new String((jsonObject.getString("Product_Site")).getBytes("iso-8859-1"), "UTF-8");
			Product_Status = jsonObject.getString("Product_Status");

			int i = analysisService.editMyGood(Product_Id, Product_Name, Product_Price, Product_Describe, Product_Site,
					Product_Status);

			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if (i == 1) {
				writer.write("��Ʒ�༭�ɹ���Product_Id��" + Product_Id);
				writer.flush();
				writer.close();
				this.renderNull();
			} else {
				writer.write("��Ʒ�༭ʧ�ܣ�Product_Id��" + Product_Id);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ���ߣ������� ���ڣ�2016-12-13 ʵ�֣�������Ʒ �����Ʒ ������һ��
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/addGoods?goods={
	 * User_Username:������,Product_Name:����S5
	 * G9008V,Product_Price:2000,Product_Describe:������Ʒ����,Product_Time:
	 * "2016-12-15 10:46:10"
	 * ,Product_Site:���ѧԺ,Product_View:50,Product_Positive:40,Product_Status:
	 * TRUE,Product_Top:false,Product_Longgitude:340.0,Product_Lagitude:350.0,
	 * Product_Type=�ֻ�Phone}
	 */
	public void addGoods() {
		String jsonContent = this.getPara("goods");

		// ��ȡ��Ʒ����
		// String Product_Id;
		String Product_Name;
		String str_Product_Price;
		float Product_Price;
		String Product_Describe;
		// String PRODUCT_IMAGE
		String Product_Time;
		String Product_Site;
		String Product_View;
		String Product_Positive;
		boolean Product_Status;
		boolean Product_Top;
		double Product_Longgitude; // ����
		double Product_Lagitude; // γ��
		String Product_Type;

		// ��ȡ�û���
		String User_Username;

		try {
			JSONObject jsonObject = new JSONObject(jsonContent);

			Product_Name = new String((jsonObject.getString("Product_Name")).getBytes("iso-8859-1"),"UTF-8");
			str_Product_Price = jsonObject.getString("Product_Price");
			Product_Price = Float.valueOf(str_Product_Price).floatValue();
			Product_Describe = new String((jsonObject.getString("Product_Describe")).getBytes("iso-8859-1"),"UTF-8");
			Product_Time = jsonObject.getString("Product_Time");
			Product_Site = new String((jsonObject.getString("Product_Site")).getBytes("iso-8859-1"),"UTF-8");
			Product_View = jsonObject.getString("Product_View");
			Product_Positive = jsonObject.getString("Product_Positive");
			Product_Status = jsonObject.getBoolean("Product_Status");
			Product_Top = jsonObject.getBoolean("Product_Top");
			Product_Longgitude = jsonObject.getDouble("Product_Longgitude");
			Product_Lagitude = jsonObject.getDouble("Product_Lagitude");
			Product_Type = new String((jsonObject.getString("Product_Type")).getBytes("iso-8859-1"),"UTF-8");
			// System.out.println("----�ӿ���Product_Status�� " + Product_Status+"
			// Product_Top:" + Product_Top);
//			Product_Name = jsonObject.getString("Product_Name");
//			Product_Describe = jsonObject.getString("Product_Describe");
//			Product_Site = jsonObject.getString("Product_Site");
//			Product_Type = jsonObject.getString("Product_Type");

			// ��ȡUSER_NAME
			User_Username = new String((jsonObject.getString("User_Username")).getBytes("iso-8859-1"),"UTF-8");
//			User_Username = jsonObject.getString("User_Username");
			System.out.println("------User_Username:" + jsonObject.getString("User_Username"));
			Record mRecord = new Record().set("Product_Name", Product_Name).set("Product_Price", Product_Price)
					.set("Product_Describe", Product_Describe).set("Product_Time", Product_Time)
					.set("Product_Site", Product_Site).set("Product_View", Product_View)
					.set("Product_Positive", Product_Positive).set("Product_Status", Product_Status)
					.set("Product_Top", Product_Top).set("Product_Longgitude", Product_Longgitude)
					.set("Product_Lagitude", Product_Lagitude).set("Product_Type", Product_Type);
			// boolean res_goods = analysisService.add_Goods(mRecord);
			// boolean res_goods_user =
			// analysisService.add_Goods_User(Product_Name,User_Username);
			// System.out.println("----����״̬���ӣ�res_goods:" + res_goods + "
			// res_goods_user:" + res_goods_user);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();

			if (analysisService.add_Goods_User(Product_Name, User_Username) && analysisService.add_Goods(mRecord)) {
				
				//��Ʒ�����ɹ������ù�ϵ�� re_product_classify
				analysisService.setType(Product_Type,Product_Name);
				
				writer.write("��Ʒ�����ɹ���");
				writer.flush();
				writer.close();
				renderText("��Ʒ�����ɹ���");
				this.renderNull();
			} else { // �ǵô����쳣
				writer.write("��Ʒ����ʧ�ܣ�");
				writer.flush();
				writer.close();
				renderText("��Ʒ����ʧ�ܣ�");
				this.renderNull();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ʵ�֣��ϴ�ͼƬ ���ߣ������� ���ڣ�2016-12-13 ע�ͣ��������Product_Id�����ִ�ͼƬ�Ǹ��ĸ���Ʒ�ģ�
	 * ������ʽ��http://192.168.194.2:8080/IslandTrading/analysis/uploadImg?
	 * Product_Id=1
	 */
	public void uploadImg() {
		UploadFile uploadFile = getFile("profile_picture");

		int Product_Id = this.getParaToInt("Product_Id");
		String img_Name = uploadFile.getFileName();
		analysisService.add_img_url(Product_Id, img_Name); // ��Ȼ�������淶�ˣ������ܱ�֤����
		System.out.println("------" + img_Name); // �õ����Ǵ�������������֣���Ҳ�Ƿ��ص�����
		renderText("ok");
	}

	/*
	 * ʵ�֣�����ͼƬ ���ߣ������� ���ڣ�2016-12-13
	 * ������ʽ��http://192.168.194.2:8080/IslandTrading/analysis/downloadImg?
	 * Product_Id=1
	 */
	public void downloadImg() {
		int pid = this.getParaToInt("Product_Id"); // Ҳ������ȷ�õ�����
		String str_img_name = analysisService.getImg(pid);

		renderFile(str_img_name);
		// renderText("download_ok");
		System.out.println("-----����downloadImg()--str_img_name:" + str_img_name);
	}

	/*
	 * ʵ�֣�QQע�ᡢ��½ ���ߣ������� ���ڣ�2016-12-15 ע�ͣ�ֻ��Ҫ User_TakingId ������mode ��֤��½����ע��
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/registQQ?mode=regisger&
	 * User_TakingId=12345&Hx_Username=12345&Hx_Password=123abc
	 * http://localhost:8080/IslandTrading/analysis/registQQ?mode=check&
	 * User_TakingId=12345
	 */
	public void registQQ() {
		String User_TakingId = this.getPara("User_TakingId");
		String mode = this.getPara("mode");
		String Hx_Username_c = this.getPara("Hx_Username");
		String Hx_Password_c = this.getPara("Hx_Password");
		System.out.println("-----��õ�User_TakingId:" + User_TakingId);

		Record mRecord = null;
		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mode.equals("check")) { // ��½
			mRecord = analysisService.login_TK(User_TakingId);
			if (mRecord != null) {
				String Hx_Username = mRecord.getStr("Hx_Username");
				String Hx_Password = mRecord.getStr("Hx_Password");
				System.out.println("----���ؿͻ��˵Ļ��ţ�Hx_Username:" + Hx_Username + "  Hx_Password:" + Hx_Password);
				writer.write("��½�ɹ���Hx_Username:" + Hx_Username + "  Hx_Password:" + Hx_Password);
				writer.flush();
				writer.close();
				renderText("��½�ɹ���");
			} // ��½�ɹ�
			else {
				writer.write("��½ʧ�ܣ�");
				writer.flush();
				writer.close();
				renderText("��½ʧ�ܣ�text");
			}
		} // if��½
		else if (mode.equals("regisger")) { // ע��
			mRecord = new Record().set("User_Username", User_TakingId).set("User_TakingId", User_TakingId)
					.set("User_Power", 0).set("Hx_Username", Hx_Username_c).set("Hx_Password", Hx_Password_c);
			boolean res = analysisService.register_TK(mRecord);
			if (res == true) {
				writer.write("-----ע��tkid�ɹ��� User_TakingId:" + User_TakingId);
				writer.flush();
				writer.close();
				renderText("ע��tkid�ɹ���");
			} else {
				writer.write("-----ע��tkidʧ�ܣ� User_TakingId:" + User_TakingId);
				writer.flush();
				writer.close();
				renderText("ע��tkidʧ�ܣ�");
			}
		}

	}

	/*
	 * ���ߣ������� ���ڣ�2016-12-17 ʵ�֣����ҷ����ġ�������Ʒ
	 * �漰��re_product_user��islandtrading_product
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/myRelease?User_Id="700"
	 */
	public void myRelease() {
		String User_Id = this.getPara("User_Id");
		List<Record> list = new ArrayList<>();
		list = analysisService.myRel(User_Id);

		String Product_Name = null;
		float Product_Price;
		String Product_Image_Url = null;
		int Product_Id;

		JSONObject content = null;
		JSONArray goods = new JSONArray();

		for (Record mRecord : list) {
			content = new JSONObject();
			Product_Id = mRecord.getInt("Product_Id");
			Product_Name = mRecord.getStr("Product_Name");
			Product_Price = mRecord.getFloat("Product_Price");
			Product_Image_Url = mRecord.getStr("Product_Image_Url");
			try {
				content.put("Product_Id", Product_Id);
				content.put("Product_Name", Product_Name);
				content.put("Product_Price", Product_Price);
				content.put("Product_Image_Url", Product_Image_Url);
				goods.put(content);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // for
		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		if (goods.length() == 0) {
			writer.write("�û�" + User_Id + "û�з�������Ʒ!");
			writer.flush();
			writer.close();
			this.renderText("no");
			this.renderNull();
		}

		writer.write(goods.toString());
		writer.flush();
		writer.close();
		this.renderText("ok");
		this.renderNull();
	}

	/*
	 * ���ߣ������� ���ڣ�2016-12-17 ʵ�֣����������ġ�������Ʒ
	 * �漰��re_product_user��islandtrading_product ����ע�ͣ�ͨ����Ʒ״̬�ж���Ʒ�Ƿ�����
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/mySell?User_Id=700
	 * ע�⣬����û����
	 */
	public void mySell() {
		int User_Id = this.getParaToInt("User_Id");

		List<Record> list = new ArrayList<>();
		list = analysisService.mySell(User_Id);

		String Product_Name = null;
		float Product_Price;
		String Product_Image_Url = null;

		JSONObject content = new JSONObject();
		JSONArray goods = new JSONArray();

		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		int i = 0;
		if (list.size() != 0) {
			for (Record mRecord : list) {
				content = new JSONObject();
				System.out.println("-----mRecord��ɶ��" + mRecord.toString());
				Product_Name = mRecord.getStr("Product_Name");
				Product_Price = mRecord.getFloat("Product_Price");
				Product_Image_Url = mRecord.getStr("Product_Image_Url");

				try {
					content.put("Product_Name", Product_Name);
					content.put("Product_Price", Product_Price);
					content.put("Product_Image_Url", Product_Image_Url);
					goods.put(content);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // for
			System.out.println("-----goods��ɶ��" + goods.toString());
			writer.write(goods.toString());
			writer.flush();
			writer.close();
			this.renderText("ok");
			this.renderNull();

		} // if
		else {
			writer.write("���û�User_Id:" + User_Id + "û����������Ʒ");
			writer.flush();
			writer.close();
			this.renderText("no");
			this.renderNull();
		} // else û����������Ʒ
	}

	/*
	 * ���ߣ������� ���ڣ�2016-12-17 ʵ�֣������򵽵ġ�
	 * �漰��islandtrading_product��islandtrading_order��re_user_order_product
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/myBuy?User_Id=700
	 */
	public void myBuy() {
		int User_Id = this.getParaToInt("User_Id");
		List<Record> list = new ArrayList<>();
		list = analysisService.myBuy(User_Id);

		String Product_Name = null;
		float Product_Price;
		String Product_Image_Url = null;

		JSONObject content = null;
		JSONArray goods = new JSONArray();

		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		if (list.size() != 0) {
			for (Record mRecord : list) {
				content = new JSONObject();
				System.out.println("-----mRecord��ɶ��" + mRecord.toString());
				Product_Name = mRecord.getStr("Product_Name");
				Product_Price = mRecord.getFloat("Product_Price");
				Product_Image_Url = mRecord.getStr("Product_Image_Url");
				try {
					content.put("Product_Name", Product_Name);
					content.put("Product_Price", Product_Price);
					content.put("Product_Image_Url", Product_Image_Url);
					goods.put(content);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // for
			writer.write(goods.toString());
			writer.flush();
			writer.close();
			this.renderText("ok");
		} // if
		else { // ���û�û�й������Ʒ
			writer.write("���û���" + User_Id + "û�й������Ʒ");
			writer.flush();
			writer.close();
			this.renderText("no");
		}
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-20
	 * ʵ�֣���ȡ�Ƽ���Ʒ
	 * ��Ʊ�islandtrading_product
	 * ������ʽ��http://localhost:8080/IslandTrading/analysis/getTop
	 * */
	public void getTop(){
		List<Record> list_top = new ArrayList<>();
		list_top = analysisService.getTop();
		
		HttpServletResponse response = this.getResponse();	
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		//���ơ��������۸�ͼƬ
		int Product_Id;
		String Product_Describe;
		Float Product_Price;
		String Product_Image_Url;
		String Product_Name = null;
		
		JSONObject content = null;
		JSONArray jArray = new JSONArray();
		
		for(Record mRecord : list_top){
			content = new JSONObject();
			Product_Id = mRecord.getInt("Product_Id");
			Product_Describe = mRecord.getStr("Product_Describe");
			Product_Price = mRecord.getFloat("Product_Price");
			Product_Image_Url = mRecord.getStr("Product_Image_Url");
			Product_Name = mRecord.getStr("Product_Name");
			try {
				content.put("Product_Id", Product_Id);
				content.put("Product_Name", Product_Name);
				content.put("Product_Describe", Product_Describe);
				content.put("Product_Price", Product_Price);
				content.put("Product_Image_Url", Product_Image_Url);
				jArray.put(content);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//for
		writer.write(jArray.toString());
		writer.flush();
		writer.close();
		renderText("��ȡ�Ƽ���Ʒ�ɹ���");
	}
	
	
}
