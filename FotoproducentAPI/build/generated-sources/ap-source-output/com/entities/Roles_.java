package com.entities;

import com.entities.Account;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(Roles.class)
public class Roles_ { 

    public static volatile CollectionAttribute<Roles, Account> accountCollection;
    public static volatile SingularAttribute<Roles, String> role;
    public static volatile SingularAttribute<Roles, Integer> id;

}