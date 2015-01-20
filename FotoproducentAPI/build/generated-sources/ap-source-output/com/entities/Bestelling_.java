package com.entities;

import com.entities.OrderLine;
import com.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(Bestelling.class)
public class Bestelling_ { 

    public static volatile SingularAttribute<Bestelling, Date> date;
    public static volatile CollectionAttribute<Bestelling, OrderLine> orderLineCollection;
    public static volatile SingularAttribute<Bestelling, Integer> id;
    public static volatile SingularAttribute<Bestelling, User> userID;

}