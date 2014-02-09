package org.danielli.xultimate.browser.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danielli.xultimate.web.util.BrowserCacheGenerator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BrowserCacheInterceptor implements HandlerInterceptor {

	private BrowserCacheGenerator browserCacheGenerator;
	
	private Integer cacheSeconds;

	public void setBrowserCacheGenerator(BrowserCacheGenerator browserCacheGenerator) {
		this.browserCacheGenerator = browserCacheGenerator;
	}
	
	public void setCacheSeconds(Integer cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (ex != null) {
			browserCacheGenerator.checkAndPrepare(request, response, cacheSeconds, true);
		}
	}

}
