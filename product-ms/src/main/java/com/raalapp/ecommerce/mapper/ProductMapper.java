package com.raalapp.ecommerce.mapper;

import com.raalapp.ecommerce.dtos.ProductRequest;
import com.raalapp.ecommerce.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromProductRequestToProduct(ProductRequest userRequest);
}
