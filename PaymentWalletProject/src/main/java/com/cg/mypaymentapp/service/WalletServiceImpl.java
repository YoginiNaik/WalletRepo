
package com.cg.mypaymentapp.service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;


public class WalletServiceImpl implements WalletService{

private WalletRepo repo;
	
	public WalletServiceImpl(Map<String, Customer> data){
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}

	public WalletServiceImpl() {
		Map<String, Customer> data= new HashMap<String, Customer>();
		repo= new WalletRepoImpl(data);
}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		if(amount.intValue()>0 && name!=null ){
		Customer customer = new Customer(name,mobileNo, new Wallet(amount));
		if(repo.save(customer)){
			return customer;
		}
		else
			throw new InvalidInputException("invalid details");
		}
		else
			throw new InvalidInputException("Negative Amount entered...");
				
		}

	public Customer showBalance(String mobileNo) {
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		Customer giver = repo.findOne(sourceMobileNo);
		Customer taker = repo.findOne(targetMobileNo);
		if(giver!=null && taker!=null){
		Wallet sourceWallet = giver.getWallet();
		Wallet targetWallet = taker.getWallet();
		
		if(sourceWallet != null && targetWallet != null) {
			if (sourceWallet.getBalance().compareTo(amount)>=0){
				
			sourceWallet.setBalance(sourceWallet.getBalance().subtract(amount));
			targetWallet.setBalance(targetWallet.getBalance().add(amount));
			
			
			giver.setWallet(sourceWallet);
			taker.setWallet(targetWallet);
			
			repo.save(giver);
			repo.save(taker);
			return giver;
			}
			else
				throw new InsufficientBalanceException("Balance is not sufficient for transfer");
			}
		else
			throw new InvalidInputException("Invalid amount in account");
		}
		else
			throw new InvalidInputException("Invalid input ");
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		if(amount.intValue()>0){
		Customer customer = repo.findOne(mobileNo);
		if(customer!=null && customer.getWallet()!=null){
			Wallet wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().add(amount));
			customer.setWallet(wallet);
			repo.save(customer);
			return customer;
		}
		else
			throw new InvalidInputException("Invalid details ");
		}
		else
			throw new InvalidInputException("Negative amount for deposit");
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		// TODO Auto-generated method stub
		Customer customer = repo.findOne(mobileNo);
		if(customer!=null && customer.getWallet()!=null){
			if(amount.intValue()<= customer.getWallet().getBalance().intValue()){
			Wallet wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().subtract(amount));
			customer.setWallet(wallet);
			repo.save(customer);
			return customer;
		}
			else
				throw new InsufficientBalanceException("Low balance for withdraw");
		}
		else
			throw new InvalidInputException("Invalid input");
	}

}
