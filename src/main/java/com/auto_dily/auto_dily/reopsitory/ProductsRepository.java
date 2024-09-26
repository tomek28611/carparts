package com.auto_dily.auto_dily.reopsitory;

import com.auto_dily.auto_dily.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends MongoRepository<Product, String> {

}
