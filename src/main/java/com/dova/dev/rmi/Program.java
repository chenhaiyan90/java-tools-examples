package com.dova.dev.rmi;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Program{

public static void main(String[] args) {
	try {
			PersonServiceImpl personService = new PersonServiceImpl();
			//注册通讯端口
			Registry registry = LocateRegistry.createRegistry(6600);
			//注册通讯路径
			Naming.rebind("rmi://localhost:6600/PersonService", personService);
			System.out.println("Service Start!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}