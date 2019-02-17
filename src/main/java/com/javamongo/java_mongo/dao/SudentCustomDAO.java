package com.javamongo.java_mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.javamongo.java_mongo.model.Student;
import com.mongodb.client.result.UpdateResult;

@Repository
public class SudentCustomDAO implements IStudentCustomDAO{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public int update(Student s) {
		Query query = new Query(Criteria.where("_id").is(s.getId()));
		Update update  = new Update();
		update.set("name", s.getName());
		update.set("password", s.getPassword());
		
		UpdateResult result = mongoTemplate.updateFirst(query, update, Student.class);
		
		if(result!=null)
            return 1;
        else
            return 0;
	}

	@Override
	public List<Student> getByEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		List<Student> student = mongoTemplate.find(query, Student.class);
		return student;
	}

}
