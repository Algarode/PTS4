package com.entities;

import com.entities.Bestelling;
import com.entities.Photo;
import com.entities.SizePrize;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(OrderLine.class)
public class OrderLine_ { 

    public static volatile SingularAttribute<OrderLine, SizePrize> sizeID;
    public static volatile SingularAttribute<OrderLine, Integer> amount;
    public static volatile SingularAttribute<OrderLine, Integer> productPhotoId;
    public static volatile SingularAttribute<OrderLine, Bestelling> orderID;
    public static volatile SingularAttribute<OrderLine, Photo> photoID;
    public static volatile SingularAttribute<OrderLine, Integer> id;

}