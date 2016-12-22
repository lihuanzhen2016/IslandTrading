/**
 * Function:ProductInfoValidator
 * Date:2016.12.11
 * Author:LiuXin
 */
package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ProductInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("PRODUCT_NAME", "nameMsg", "��������Ʒ����!");
		validateRequiredString("PRODUCT_PRICE", "priceMsg", "��������Ʒ�۸�!");
		validateRequiredString("PRODUCT_DESCRIBE", "describeMsg", "��������Ʒ����!");
		validateRequiredString("PRODUCT_IMAGE", "imageMsg", "���ϴ���ƷͼƬ!");
		validateRequiredString("PRODUCT_SITE", "siteMsg", "��������Ʒ����λ��!");
		validateRequiredString("PRODUCT_VIEW", "viewMsg", "��������Ʒ�����!");
		validateRequiredString("PRODUCT_POSITIVE", "positiveMsg", "��������Ʒ������!");
		validateRequiredString("PRODUCT_STATUS", "statusMsg", "��������Ʒ״̬!");
		validateRequiredString("PRODUCT_MESSAGE", "messageMsg", "��������Ʒ����!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepPara("PRODUCT_NAME");
		controller.keepPara("PRODUCT_PRICE");
		controller.keepPara("PRODUCT_DESCRIBE");
		controller.keepPara("PRODUCT_IMAGE");
		controller.keepPara("PRODUCT_SITE");
		controller.keepPara("PRODUCT_VIEW");
		controller.keepPara("PRODUCT_POSITIVE");
		controller.keepPara("PRODUCT_STATUS");
		controller.keepPara("PRODUCT_MESSAGE");
		
		String actionKey = getActionKey();
		if (actionKey.equals("/product/save"))
				controller.render("/addProduct.jsp");
		else if (actionKey.equals("/product/update"))
			controller.render("/editProduct.jsp");
		else if (actionKey.equals("/product/find"))
		controller.render("/findProduct.jsp");
	}

}
