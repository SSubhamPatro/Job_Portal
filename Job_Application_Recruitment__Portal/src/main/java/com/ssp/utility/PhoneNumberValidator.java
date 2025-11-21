package com.ssp.utility;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator  implements ConstraintValidator<ValidPhoneNumber, String>{

	@Override
	public boolean isValid(String phone, ConstraintValidatorContext context) {

		if(phone==null||phone.isBlank()) {
			return false;
		}
		
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		
		try {
			// Region "ZZ" means "unknown region" → best for international parsing
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, "ZZ");
           
            return phoneUtil.isValidNumber(number);
			
		}catch (Exception e) {
            return false;
		 }
		
		
	}
	
	
   
}
