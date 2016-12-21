/**
 * Function:ProductController
 * Date:2016.12.11
 * Author:LiuXin
 */
package controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import interceptor.LoginInterceptor;
import service.ProductBiz;
import validate.ProductInfoValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;

@Before(LoginInterceptor.class)
public class ProductController extends Controller {
	ProductBiz productService = this.enhance(ProductBiz.class);

	@Before(ProductInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pName = this.getPara("PRODUCT_NAME");
		String price = this.getPara("PRODUCT_PRICE");
		String pDescribe = this.getPara("PRODUCT_DESCRIBE");
		String pImage = this.getPara("PRODUCT_IMAGE");
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pTime = df.format(day);
		String pSite = this.getPara("PRODUCT_SITE");
		String pView = this.getPara("PRODUCT_VIEW");
		String pPositive = this.getPara("PRODUCT_POSITIVE");
		String pStatus = this.getPara("PRODUCT_STATUS");
		String pMessage = this.getPara("PRODUCT_MESSAGE");

		float pPrice = 0;
		String result;
		pPrice = Float.parseFloat(price);
	    boolean res = productService.save(pName, pPrice, pDescribe, pImage, pTime, pSite, pView, pPositive,
						pStatus, pMessage);
		if (res) {
			result = "��Ʒ��ӳɹ�,�������";
		} else {
			result = "��Ʒ���ʧ�ܣ��������";
		}
			this.setAttr("result", result);
			this.render("/addProduct.jsp");	
	}
	public void deleteByID() {
		String id = this.getPara(0);
		boolean res = productService.deleteByID(id);
		String result;
		if (res) {
			result = "��Ʒɾ���ɹ�";
		} else {
			result = "��Ʒɾ��ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	public void list() {
		List<Record> prolist = productService.findAll();
		this.setSessionAttr("prolist", prolist);
		this.render("/productList.jsp");
	}

	@Before(ProductInfoValidator.class)
	public void update() {

		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String pID = this.getPara("PRODUCT_ID");
		String pName = this.getPara("PRODUCT_NAME");
		String pPrice = this.getPara("PRODUCT_PRICE");
		String pDescribe = this.getPara("PRODUCT_DESCRIBE");
		String pImage = this.getPara("PRODUCT_IMAGE");
		String pSite = this.getPara("PRODUCT_SITE");
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pTime = df.format(day);
		String pView = this.getPara("PRODUCT_VIEW");
		String pPositive = this.getPara("PRODUCT_POSITIVE");
		String pStatus = this.getPara("PRODUCT_STATUS");
		String pMessage = this.getPara("PRODUCT_MESSAGE");

		String result;

		int res = productService.update(pID, pName, pPrice, pDescribe, pImage, pTime, pSite, pView, pPositive, pStatus,
				pMessage);
		if (res > 0) {
			result = "��Ʒ���³ɹ�";
		} else {
			result = "��Ʒ����ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("Product_Id");
		Record rec = productService.findByID(id);
		this.setAttr("product", rec);
		this.render("/findProduct.jsp");
	}

}
