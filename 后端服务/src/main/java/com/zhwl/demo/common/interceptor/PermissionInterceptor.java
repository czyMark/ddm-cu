package com.zhwl.demo.common.interceptor;

import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.zhwl.demo.exception.NoLoginException;
import com.zhwl.demo.exception.TokenNullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxueli 2015-12-12 18:09:04
 */
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		String sessionId = request.getHeader("sessionId");

		if(sessionId == null || "".equals(sessionId)){
			throw new TokenNullException("sessionId不能为空");
		}
		XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
		if (xxlUser == null) {
			throw new NoLoginException("用户未登录或已在异地登录");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
