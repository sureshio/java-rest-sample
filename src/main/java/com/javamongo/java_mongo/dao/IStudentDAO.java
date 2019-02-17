package com.javamongo.java_mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javamongo.java_mongo.model.Student;

public interface IStudentDAO extends MongoRepository<Student, String>{

}
