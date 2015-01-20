package com.entities;

import com.entities.Photo;
import com.entities.Product;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(ProductPhoto.class)
public class ProductPhoto_ { 

    public static volatile SingularAttribute<ProductPhoto, Product> productID;
    public static volatile SingularAttribute<ProductPhoto, Photo> photoID;
    public static volatile SingularAttribute<ProductPhoto, Integer> id;

}