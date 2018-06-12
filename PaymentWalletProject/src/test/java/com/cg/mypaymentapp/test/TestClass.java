package com.cg.mypaymentapp.test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;


public class TestClass {

	
	WalletService service;
	
	@Before
	public void initData(){
		 Map<String,Customer> data= new HashMap<String, Customer>();
		 Customer cust1=new Customer("Amit", "9900112212",new Wallet(new BigDecimal(9000)));
		 Customer cust2=new Customer("Ajay", "9963242422",new Wallet(new BigDecimal(6000)));
		 Customer cust3=new Customer("Yogini", "9922950519",new Wallet(new BigDecimal(7000)));
				
		 data.put("9900112212", cust1);
		 data.put("9963242422", cust2);	
		 data.put("9922950519", cust3);	
			service= new WalletServiceImpl(data);
			
	}
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccountForNullName(){
		BigDecimal bg= new BigDecimal(34099);
		Customer customer =service.createAccount(null, "9824383129",bg);
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccountForNegativeAmount(){
		BigDecimal bg= new BigDecimal(-34099);
		Customer customer =service.createAccount("Yogini", "9823831294",bg);
	}

	@Test
	public void testCreateAccountForCorrectData(){
		BigDecimal bg= new BigDecimal(34099);
		Customer customer =service.createAccount("Yogini", "9823823129",bg);
		Assert.assertEquals("Yogini", customer.getName());
		Assert.assertEquals("9823823129", customer.getMobileNo());
		Assert.assertEquals(bg, customer.getWallet().getBalance());
		
	}
	
	@Test(expected=InvalidInputException.class)
	public void testShowBalanceForInvalidMobileNo(){
			Customer customer =service.showBalance("9354332123");
	}
	
	@Test
	public void testShowBalanceForValidMobileNo(){
		BigDecimal bg= new BigDecimal(1200);
		Customer c1= service.createAccount("Ankita","9020301234",bg);
		
		Customer customer =service.showBalance("9020301234");
		Assert.assertEquals("9020301234", customer.getMobileNo());
		Assert.assertEquals(bg, customer.getWallet().getBalance());
		
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransferForNullData(){
			
		 Customer cust3=service.fundTransfer("9900112212", "9963242451", new BigDecimal(2000));
		 
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransferForNullSourceMobileNo(){
		BigDecimal bg= new BigDecimal(34620);
			Customer customer=service.fundTransfer(null, "9829329112", bg);
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransferForInvalidMobileNoData(){
		BigDecimal bg= new BigDecimal(34620);
		Customer customer=service.fundTransfer("901230", "921993211", bg);
		
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransferForInvalidAmount(){
		BigDecimal bg= new BigDecimal(-34620.99);
		Customer customer=service.fundTransfer("9030201234", "9002012345", bg);

	}
	//given mobile number does not exist in DB.
	@Test(expected=InvalidInputException.class)
	public void testDepositAmountForInvalidMobileNumber(){
		BigDecimal bg= new BigDecimal(9000);
		Customer customer=service.depositAmount("9020320132", bg);
	}
	@Test(expected=InvalidInputException.class)
	public void testDepositAmountForNegativeAmount(){
		BigDecimal bg= new BigDecimal(-9000);
		Customer customer=service.depositAmount("9020320132", bg);
	}
	@Test(expected=InvalidInputException.class)
	public void testDepositAmountForZero(){
		BigDecimal bg= new BigDecimal(0);
		Customer customer =service.depositAmount("9012013953", bg);
	}

	@Test 
	public void testDepositAmountForValidAmount(){
		Customer cust= service.showBalance("9922950519");
		int oldBal=cust.getWallet().getBalance().intValue();
		BigDecimal bg= new BigDecimal(4500);
		BigDecimal oldBalance= new BigDecimal(oldBal);
		Customer customer =service.depositAmount("9922950519", bg);
		Assert.assertEquals(customer.getWallet().getBalance(),oldBalance.add(bg));
	}
	
	/// for Withdraw method
	//given mobile number does not exist in DB.
		@Test(expected=InvalidInputException.class)
		public void testWithdrawAmountForInvalidMobileNumber(){
			BigDecimal bg= new BigDecimal(9000);
			Customer customer=service.withdrawAmount("9020320132", bg);
		}
		@Test(expected=InvalidInputException.class)
		public void testWithdrawAmountForNegativeAmount(){
			BigDecimal bg= new BigDecimal(-9000);
			Customer customer=service.withdrawAmount("9020320132", bg);
		}
		@Test(expected=InvalidInputException.class)
		public void testWithdrawAmountForZero(){
			BigDecimal bg= new BigDecimal(0);
			Customer customer =service.withdrawAmount("9012013953", bg);
		}

		@Test 
		public void testWithdrawAmountForValidAmount(){
			Customer cust= service.showBalance("9922950519");
			int oldBal=cust.getWallet().getBalance().intValue();
			BigDecimal bg= new BigDecimal(1000);
			BigDecimal oldBalance= new BigDecimal(oldBal);
			Customer customer =service.withdrawAmount("9922950519", bg);
			Assert.assertEquals(customer.getWallet().getBalance(),oldBalance.subtract(bg));
		}

		@Test(expected=InsufficientBalanceException.class)
		public void testWithdrawAmountForLowBalance(){
			Customer cust= service.showBalance("9922950519");
			BigDecimal bg= new BigDecimal(20000);
			Customer customer =service.withdrawAmount("9922950519", bg);
		}

}
