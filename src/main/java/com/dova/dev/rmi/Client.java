package com.dova.dev.rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.util.List;

public class Client {
	public static void main(String[] args){
		try{
			//调用远程对象，注意RMI路径与接口必须与服务器配置一致
			Remote remote = Naming.lookup("rmi://127.0.0.1:6600/PersonService");
			System.out.println(remote.getClass());
			for (Class inter : remote.getClass().getInterfaces()){
				System.out.println(inter);
			}
			PersonService personService=(PersonService)remote;
			
			List<PersonEntity> personList=personService.GetList();
			for(PersonEntity person:personList){
				System.out.println("ID:"+person.getId()+" Age:"+person.getAge()+" Name:"+person.getName());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
