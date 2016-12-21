/**
 * Function:ActivityController
 * Date:2016.12.11
 * Author:LiuXin
 */
package controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;

import interceptor.LoginInterceptor;
import service.ActivityBiz;
import validate.ActivityInfoValidator;

@Before(LoginInterceptor.class)
public class ActivityController extends Controller {
	ActivityBiz activityService = this.enhance(ActivityBiz.class);

	@Before(ActivityInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pName = this.getPara("ACTIVITY_NAME");
		String pContent = this.getPara("ACTIVITY_CONTENT");
		String pOrganizer = this.getPara("ACTIVITY_ORGANIZER");
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pTime = df.format(day);
		String pSite = this.getPara("ACTIVITY_SITE");
		String pImg = this.getPara("ACTIVITY_IMG");
   
		String result = "���Ϣ����Ϊ��";

	    boolean res = activityService.save(pName, pContent, pOrganizer, pTime, pSite,pImg);
		if (res) {
				result = "��ӳɹ�����������";
		} else {
				result = "���ʧ�ܣ����������";
		}

		this.setAttr("result", result);
		this.render("/addActivity.jsp");
	}

	public void deleteByID() {
		String id = this.getPara(0);
		boolean res = activityService.deleteByID(id);
		String result;
		if (res) {
			result = "ɾ���ɹ�";
		} else {
			result = "ɾ��ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	public void list() {
		List<Record> proactivity = activityService.findAll();
		this.setSessionAttr("proactivity", proactivity);
		this.render("/ActivityList.jsp");
	}

	@Before(ActivityInfoValidator.class)
	public void update() {

		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String pId = this.getPara("ACTIVITY_ID");
		String pName = this.getPara("ACTIVITY_NAME");
		String pContent = this.getPara("ACTIVITY_CONTENT");
		String pOrganizer = this.getPara("ACTIVITY_ORGANIZER");
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pTime = df.format(day);
		String pSite = this.getPara("ACTIVITY_SITE");
		String pImg = this.getPara("ACTIVITY_IMG");

		String result = "������Ϣ����Ϊ��";
		int res = activityService.update(pId,pName, pContent, pOrganizer, pTime, pSite,pImg);
		if (res > 0) {
			result = "���³ɹ�";
		} else {
			result = "����ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("Activity_Id");
		Record rec = activityService.findByID(id);
		this.setAttr("activity", rec);
		this.render("/findActivity.jsp");
	}
}
