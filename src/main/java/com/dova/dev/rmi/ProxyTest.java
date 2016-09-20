package com.dova.dev.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
	
	InvocationHandler handler = new InvocationHandler() {
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// TODO Auto-generated method stub
			System.out.println("handler method:" + method.getName());
			return null;
		}
	};
	
	public void print(String... a){
		System.out.println(a.getClass());
	}
	public ProxyTest(){
		A a = (A)Proxy.newProxyInstance(A.class.getClassLoader(), new Class[]{A.class}, handler);
		a.a();
		System.out.println(a instanceof A);
		System.out.println(a.getClass());
	}
	
	public static void main(String[] args) {
		ProxyTest proxyTest = new ProxyTest();
		proxyTest.print("aa","bb","cc");
	}
	
}


interface A{
	
	public void a();
}