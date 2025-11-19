package com.ssp.DTO;

import com.ssp.Entity.UserAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponse {

	private String message;
    private String token;
    
    private UserAccount user;
    }
