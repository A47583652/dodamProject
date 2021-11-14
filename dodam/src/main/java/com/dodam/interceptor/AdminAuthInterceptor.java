package com.dodam.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dodam.domain.members.MemberVo;

public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession ses = request.getSession();
		MemberVo loginmem = (MemberVo)ses.getAttribute("loginSession");
		String uri = request.getRequestURI();
		String path = uri.substring(0, uri.lastIndexOf("/"));
		
		String returnPath = "";
		// 로그인 하지 않았으면
		if (loginmem == null) {
			returnPath = "/member/login";
			response.sendRedirect(returnPath);
			
		// 로그인한 아이디가 관리자 계정이 아니면
		} else if (!(loginmem.getIsadmin().equals("Y"))) {
			// 반려동물 전시회에서 온 요청이라면(전시회 글쓰기 권한 접근)
			if (path.equals("/board/event")) {
				returnPath = "/board/event/listPage";
			// 공지사항에서 온 요청이라면(공지사항 글쓰기 권한 접근)
			} else if (path.equals("/board/notice")) {
				returnPath = "/board/notice/listAll";
			// 그 외 관리자 계정 권한 접근
			} else {
				returnPath = "/";
			}
			
			response.sendRedirect(returnPath);
		}
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

}
