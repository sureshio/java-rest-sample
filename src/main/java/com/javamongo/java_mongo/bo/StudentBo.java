package com.javamongo.java_mongo.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javamongo.java_mongo.dao.IStudentCustomDAO;
import com.javamongo.java_mongo.dao.IStudentDAO;
import com.javamongo.java_mongo.model.Student;
import com.javamongo.java_mongo.utils.Password;
import com.javamongo.java_mongo.utils.SendEmail;

@Service
public class StudentBo {
	
	public static final String NOT_FOUND = "Not Found";
	public static final String DELETE = "Data Deleted";
	public static final String FAILED = "Failed";
	public static final String SUCCESS = "Successfully created";
	public static final String INVALID_EMAIL = "Invalid Email";

	@Autowired
	private IStudentDAO studentDAO;
	
	@Autowired
	private IStudentCustomDAO studentCustomDAO;
	
	public List<Student> findAll() throws Exception {
		List<Student> students = new ArrayList<>();
		Iterable<Student> i = studentDAO.findAll();
		
		for (Student student : i) {
			students.add(student);
		}
		
		return students;
	}
	
	public Student findById(String id) throws Exception {
		Student s = new Student();
		Optional<Student> val = studentDAO.findById(id);
		
		if(val.isPresent()) {			
			s = val.get();
			if(s.getPassword() != null || !s.getPassword().isEmpty()) {
				Password pwd = new Password();
				s.setPassword(pwd.decrypt(s.getPassword()));
			}
		} else {
			s = null;
		}
		return s;
	}
	
	public Map<String, String> remove(String id) throws Exception {
		Map<String, String> rsMap = new HashMap<>();
		rsMap.put("message", FAILED);
		try {
			Student s = findById(id);
			if(s != null) {
				studentDAO.deleteById(id);
				rsMap.put("message", DELETE);
			} else {
				rsMap.put("message", NOT_FOUND);
			}
			
		} catch (Exception e) {
			rsMap.put("message", FAILED);
			e.printStackTrace();
		}
		
		return rsMap;
	}
	
	public Map<String, Object> create(Student student) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		rsMap.put("message", FAILED);
		try {
			Password pwd = new Password();
			String password = pwd.encrypt(student.getPassword());
			student.setPassword(password);
			
			Student save = studentDAO.save(student);
			if(save != null) {
				rsMap.put("message", SUCCESS);
				Student s = new Student();
				s.setEmail(save.getEmail());
				s.setId(save.getId());
				s.setName(save.getName());
				rsMap.put("student", s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rsMap;
	}
	
	public Map<String, Object> update(Student student) throws Exception {
		Map<String, Object> rsMap = new HashMap<>();
		rsMap.put("message", FAILED);
		try {
			
			Student s = findById(student.getId());
			if(s != null) {
				Password pwd = new Password();
				String password = pwd.encrypt(student.getPassword());
				s.setPassword(password);
				s.setName(student.getName());
				int i = studentCustomDAO.update(s);		
				if(i != 0) {
					rsMap.put("message", SUCCESS);
				}
			} else {
				rsMap.put("message", NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rsMap;
	}
	
	public Student login(String email, String password) throws Exception {
		Student s1 = new Student();
		List<Student> stud = studentCustomDAO.getByEmail(email);
		if(stud.size() > 0) {
			Student s = stud.get(0);
			Password pwd = new Password();
			String pass = pwd.decrypt(s.getPassword());
			
			if(pass.equals(password)) {
				s1.setEmail(s.getEmail());
				s1.setId(s.getId());
				s1.setName(s.getName());
			}
		}
	
		return s1;
	}
	
	public Map<String, String> forgotPassword(String email) throws Exception{
		Map<String, String> rsMap = new HashMap<>();
		rsMap.put("message", FAILED);
		List<Student> stud = studentCustomDAO.getByEmail(email);
		if(stud.size() > 0) {
			Student s = stud.get(0);
			Password pwd = new Password();
			String pass = pwd.decrypt(s.getPassword());
			
			SendEmail send = new SendEmail();
			String message = send.email(email, pass);
			rsMap.put("message", message);
			
		} else {
			rsMap.put("message", INVALID_EMAIL);
		}
	
		return rsMap;
	}
	
}
