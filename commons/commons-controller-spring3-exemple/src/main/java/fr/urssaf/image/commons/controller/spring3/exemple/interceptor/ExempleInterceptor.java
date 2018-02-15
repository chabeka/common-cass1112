package fr.urssaf.image.commons.controller.spring3.exemple.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ExempleInterceptor implements HandlerInterceptor {

	@Override
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
		//rien à initialiser
	}

	@Override
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//rien à initialiser
	}

	@Override
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		return true;
	}

}
