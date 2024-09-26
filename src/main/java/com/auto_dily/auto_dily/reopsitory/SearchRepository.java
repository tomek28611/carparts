package com.auto_dily.auto_dily.reopsitory;

import com.auto_dily.auto_dily.model.Product;

import java.util.List;

public interface SearchRepository {

    List<Product> findByText(String text);

}