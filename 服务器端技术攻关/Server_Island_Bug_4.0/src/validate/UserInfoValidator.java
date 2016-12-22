/**
 * Function:UserInfoValidator
 * Date:2016.12.11
 * Author:LiuXin
 */
package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UserInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("USER_NICKNAME", "nicknameMsg", "�������û��ǳ�!");
		validateRequiredString("USER_USERNAME", "usernameMsg", "�������û��˻�!");
		validateRequiredString("USER_PASSWORD", "passwordMsg", "�������û�����!");
		validateRequiredString("USER_IMAGE", "imageMsg", "�������û�ͷ��!");
		validateRequiredString("USER_POWER", "powerMsg", "�������û�Ȩ��!");
		validateRequiredString("USER_TAKINGID", "takingidMsg", "�������û�����ID!");
		validateRequiredString("USER_TEL", "telMsg", "�������û��绰!");
		validateRequiredString("HX_USERNAME", "hx_usernameMsg", "�����뻷���û��˻�!");
		validateRequiredString("HX_PASSWORD", "hx_passwordMsg", "�����뻷���û�����!");

	}

	protected void handleError(Controller controller) {
		controller.keepPara("USER_NICKNAME");
		controller.keepPara("USER_USERNAME");
		controller.keepPara("USER_PASSWORD");
		controller.keepPara("USER_IMAGE");
		controller.keepPara("USER_POWER");
		controller.keepPara("USER_TAKINGID");
		controller.keepPara("USER_TEL");
		controller.keepPara("HX_USERNAME");
		controller.keepPara("HX_PASSWORD");

		String actionKey = getActionKey();
		if (actionKey.equals("/user/save"))
			controller.render("/addUser.jsp");
		else if (actionKey.equals("/user/update"))
			controller.render("/editUser.jsp");
		else if (actionKey.equals("/user/find"))
			controller.render("/findUser.jsp");
	}

}
