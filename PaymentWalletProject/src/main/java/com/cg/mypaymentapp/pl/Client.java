package com.cg.mypaymentapp.pl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client {
	   public static void main( String[] args ){
	    	Map<String,Customer> data= new HashMap<String, Customer>();
	        WalletRepo repo = new WalletRepoImpl(data);
	        
	        WalletService service = new WalletServiceImpl(repo);
	        
	        Customer c = service.createAccount("Yogini", "9922950519", new BigDecimal(2000));
	        
	        
	        System.out.println("==============================================");
	        System.out.println("Create Account returned :: ");
	        System.out.println(c);
	        System.out.println();
	        System.out.println("==============================================");
	        
	        System.out.println("Show balance returned :: ");
	        System.out.println(service.showBalance("9922950519"));
	        System.out.println();
	        System.out.println("==============================================");
	        
	        System.out.println("Deposit returned :: ");
	        System.out.println(service.depositAmount("9922950519", new BigDecimal(1500)));
	        System.out.println();
	        System.out.println("==============================================");
	        
	        System.out.println("Fund Transfer returned :: ");
	        service.createAccount("Renu", "9604744104",new BigDecimal(5000));
	       System.out.println(service.showBalance("9604744104"));
	        System.out.println(service.fundTransfer("9922950519", "9604744104", new BigDecimal(1500)));
	        System.out.println(service.showBalance("9604744104"));
	        System.out.println("==============================================");
	        
	    }
}
