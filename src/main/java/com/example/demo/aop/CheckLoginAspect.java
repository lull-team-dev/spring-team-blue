package com.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.model.MyAccount;

@Aspect
@Component
public class CheckLoginAspect {

	@Around("execution(* com.example.demo.controller.MypageController.*(..)) || " +
			"execution(* com.example.demo.controller.OrderController.*(..)) || " +
			"(execution(* com.example.demo.controller.ChatController.*(..)) && " +
			"!execution(* com.example.demo.controller.ChatController.chat(..)))")
	public Object loginChecked(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession(true);
		String path = request.getRequestURI();
		String query = request.getQueryString();
		String fullPath = query != null ? path + "?" + query : path;

		System.out.println("アクセスパス: " + path);

		if (path.startsWith("/account/") || path.startsWith("/chat/")) {
			return joinPoint.proceed();
		}

		MyAccount loginUser = (MyAccount) session.getAttribute("account");

		if (loginUser == null) {
			System.out.println("ゲスト");
			session.setAttribute("redirectAfterLogin", fullPath);
			return "redirect:/account/login";
		} else {
			System.out.println(loginUser.getName() + "さんがログイン");
		}

		System.out.println(joinPoint.getSignature());

		return joinPoint.proceed();
	}
}
