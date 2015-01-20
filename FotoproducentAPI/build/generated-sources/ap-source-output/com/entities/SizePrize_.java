package com.entities;

import com.entities.OrderLine;
import com.entities.Size1;
import com.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(SizePrize.class)
public class SizePrize_ { 

    public static volatile CollectionAttribute<SizePrize, OrderLine> orderLineCollection;
    public static volatile SingularAttribute<SizePrize, User> photographerID;
    public static volatile SingularAttribute<SizePrize, Size1> sizeID;
    public static volatile SingularAttribute<SizePrize, Double> price;
    public static volatile SingularAttribute<SizePrize, Integer> id;

}