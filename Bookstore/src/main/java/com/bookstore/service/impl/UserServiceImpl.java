package com.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;

@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserPaymentRepository userPaymentRepository;
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user,final String token) {
		
		final PasswordResetToken myToken = new PasswordResetToken(token,user);
		passwordResetTokenRepository.save(myToken);
		
	}

	@Override
	public User findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception{
		
		User localUser = userRepository.findByUsername(user.getUsername());
		if(localUser!=null){
			System.out.println("user already exits, nothing will be done.");
			//throw new Exception ("user already exits, nothing will be done.");
			
		}else{
			for(UserRole ur: userRoles){
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());
			
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}
	
	//added by me -- modifying login part
	/*public boolean validUserLogin(String username,String password){
		
		User user = userRepository.findByUsername(username);
		
		//String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		//user.setPassword(encryptedPassword);
		if(user == null){
			return false;
		}else if(user.getPassword().equalsIgnoreCase(encryptedPassword)){
			return true;
		}else{
			return false;
		}
		
	}*/
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		
		user.getUserPaymentList().add(userPayment);
		save(user);
		
	}

	@Override
	public void setUserDefaultPayment(Long defaultPaymentId, User user) {
		
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
		
		for(UserPayment userPayment: userPaymentList){
			if(userPayment.getId() == defaultPaymentId){
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			}else{
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
				
			}
		}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
		
	}

	@Override
	public void setUserDefaultShipping(Long defaultShippingId, User user) {
		
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
		for(UserShipping userShipping: userShippingList){
			if(userShipping.getId() == defaultShippingId){
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);
			}else{
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);	
			}
		}
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}
}
