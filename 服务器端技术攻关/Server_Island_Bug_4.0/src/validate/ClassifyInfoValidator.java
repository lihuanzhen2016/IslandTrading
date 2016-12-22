/**
 * Function:ClassifyInfoValidator
 * Date:2016.12.11
 * Author:LiuXin
 */
package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ClassifyInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("CLASSIFY_NAME", "nameMsg", "�������������");
		validateRequiredString("CLASSIFY_IMAGE", "imageMsg", "���������ͼƬ");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("CLASSIFY_NAME");
		controller.keepPara("CLASSIFY_IMAGE");
		String actionKey = getActionKey();
		if (actionKey.equals("/classify/save"))
			controller.render("/addClassify.jsp");
		else if (actionKey.equals("/classify/update"))
			controller.render("/editClassify.jsp");
		else if (actionKey.equals("/classify/find"))
			controller.render("/findClassify.jsp");
	}

}
