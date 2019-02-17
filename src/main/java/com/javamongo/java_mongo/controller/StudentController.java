package com.javamongo.java_mongo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.java_mongo.bo.StudentBo;
import com.javamongo.java_mongo.configuration.LoginRequired;
import com.javamongo.java_mongo.model.Student;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentBo studentBo;
	
	@LoginRequired
	@RequestMapping(value="/all", method=RequestMethod.GET)
	public ResponseEntity<List<Student>> findAll() {
		List<Student> students = new ArrayList<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		try {
			students = studentBo.findAll();
			status = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<Student>>(students, status);
	}
	
	@LoginRequired
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	public ResponseEntity<Student> findById(@PathVariable("id") String id) {
		Student s = new Student();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		try {
			s = studentBo.findById(id);
			if(s != null) {				
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.NOT_FOUND;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Student>(s, status);
	}
	
	@LoginRequired
	@RequestMapping(value="/remove/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String, String>> remove(@PathVariable("id") String id) {
		Map<String, String> rsMap = new HashMap<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		try {
			rsMap = studentBo.remove(id);
			if(rsMap.get("message").equals(StudentBo.DELETE)) {
				status = HttpStatus.OK;
			} else if(rsMap.get("message").equals(StudentBo.NOT_FOUND)) {
				status = HttpStatus.NOT_FOUND;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, String>>(rsMap, status);
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> create(@RequestBody Student student) {
		Map<String, Object> rsMap = new HashMap<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		rsMap.put("message", StudentBo.FAILED);
		try {
			if(student.getEmail() == null || student.getEmail().length() == 0) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				rsMap = studentBo.create(student);
				if(rsMap.get("message").equals(StudentBo.SUCCESS)) {
					status = HttpStatus.OK;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String, Object>>(rsMap, status);
	}
	
	@LoginRequired
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> update(@RequestBody Student student) {
		Map<String, Object> rsMap = new HashMap<>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		rsMap.put("message", StudentBo.FAILED);
		try {
			if(student.getEmail() == null || student.getEmail().length() == 0 || student.getId() == null || student.getId().length()==0) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				rsMap = studentBo.update(student);
				if(rsMap.get("message").equals(StudentBo.SUCCESS)) {
					status = HttpStatus.OK;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String, Object>>(rsMap, status);
	}
}
