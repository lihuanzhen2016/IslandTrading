/**
 * Function:FeedbackInfoValidator
 * Date:2016.12.11
 * Author:LiuXin
 */
package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class FeedbackInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("FEEDBACK_CONTENT", "contentMsg", "�����뷴��");
		validateRequiredString("FEEDBACK_CONTACT", "contactMsg", "�����뷴����ϵ��ʽ");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("FEEDBACK_CONTENT");
		controller.keepPara("FEEDBACK_CONTACT");

		String actionKey = getActionKey();
		if (actionKey.equals("/feedback/find"))
			controller.render("/addFeedback.jsp");
	}

}
