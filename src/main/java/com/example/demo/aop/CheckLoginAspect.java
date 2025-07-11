package com.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.model.MyAccount;

@Aspect
@Component
public class CheckLoginAspect {


	@Autowired
	MyAccount myAccount;

	@Before("execution(* com.example.demo.controller.MypageController.*(..)) || " +
			"execution(* com.example.demo.controller.OrderController.*(..)) ")
	public void loginCheck(JoinPoint joinPoint) {
		// ログインしてなければリダイレクトや例外など
		if (myAccount == null || myAccount.getName() == null || myAccount.getName().length() == 0) {
			System.out.println("ゲスト");
		} else {
			System.out.println(myAccount.getName() + "さんがログイン");
		}
		System.out.println(joinPoint.getSignature());
	}


	@Around("execution(* com.example.demo.controller.MypageController.*(..)) || " +
			"execution(* com.example.demo.controller.OrderController.*(..))")
	public Object loginChecked(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession(true);
		String path = request.getRequestURI();

		// ログインページや登録ページは対象外
		if (path.contains("/account/login") || path.contains("/account/register")) {
			return joinPoint.proceed();
		}

		MyAccount loginUser = (MyAccount) session.getAttribute("account");

		if (loginUser == null) {
			// 元のリクエストURIを記録
			session.setAttribute("redirectAfterLogin", path);
			return "redirect:/account/login";
		}

		return joinPoint.proceed();
	}
}
