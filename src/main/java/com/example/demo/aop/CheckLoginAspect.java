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
		HttpSession session = request.getSession(false);

		// セッションからログイン情報取得（例: "account" という名前でログインユーザーを保存している場合）
		MyAccount loginUser = (session != null) ? (MyAccount) session.getAttribute("account") : null;

		if (loginUser == null) {
			// ログインしていない → リダイレクト用の文字列を返す
			return "redirect:/login";
		}

		// ログイン済み → 本来の処理を実行
		return joinPoint.proceed();
	}

}
