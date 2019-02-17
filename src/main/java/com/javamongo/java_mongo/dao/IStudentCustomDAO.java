package com.javamongo.java_mongo.dao;

import java.util.List;

import com.javamongo.java_mongo.model.Student;

public interface IStudentCustomDAO {
	public int update(Student s);
	public List<Student> getByEmail(String email);
}
