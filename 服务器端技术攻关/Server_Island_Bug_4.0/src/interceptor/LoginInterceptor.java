/**
 * Function:LoginInterceptor
 * Date:2016.12.11
 * Author:LiuXin
 */
package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		Controller con = inv.getController();
		// String akey = inv.getActionKey();
		// String method = inv.getMethodName();

		String userName = con.getSessionAttr("userName");

		if (userName != null && !userName.isEmpty()) {
			inv.invoke();
		} else {
			con.setSessionAttr("mess", "���ȵ�¼");
			con.render("/login.jsp");
		}
	}
}
