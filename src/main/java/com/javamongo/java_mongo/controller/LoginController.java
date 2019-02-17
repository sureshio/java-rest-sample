package com.javamongo.java_mongo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.java_mongo.bo.StudentBo;
import com.javamongo.java_mongo.model.Login;
import com.javamongo.java_mongo.model.Student;
import com.javamongo.java_mongo.utils.JWTUtil;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	private StudentBo studentBo;

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody Login login) {
		Map<String, Object> rsMap = new HashMap<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();
		
		try {
			Student s = studentBo.login(login.getEmail(), login.getPassword());
			if(s.getId() != null) {
				JWTUtil util = new JWTUtil();
				String token = util.createToken(login.getEmail());
				headers.add("x-token", token);
				status = HttpStatus.OK;
				rsMap.put("message", "Login Success");
				rsMap.put("info", s);
			} else {
				status = HttpStatus.UNAUTHORIZED;
				rsMap.put("message", "Login Failed");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String,Object>>(rsMap, headers, status);
	}
	
	@RequestMapping(value="/forgot-password/{email}", method=RequestMethod.POST)
	public ResponseEntity<Map<String, String>> forgotPassword(@PathVariable String email) {
		Map<String, String> rsMap = new HashMap<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();
		
		try {
			rsMap = studentBo.forgotPassword(email);
			if(rsMap.get("message").equals(StudentBo.INVALID_EMAIL) || rsMap.get("message").equals(StudentBo.FAILED)) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.OK;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String,String>>(rsMap, headers, status);
	}
}
