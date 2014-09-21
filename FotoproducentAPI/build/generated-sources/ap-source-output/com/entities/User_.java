package com.entities;

import com.entities.Account;
import com.entities.Photo;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-16T19:13:56")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> country;
    public static volatile SingularAttribute<User, Boolean> isPhotographer;
    public static volatile SingularAttribute<User, String> gender;
    public static volatile SingularAttribute<User, String> city;
    public static volatile SingularAttribute<User, String> postalCode;
    public static volatile SingularAttribute<User, String> houseNumber;
    public static volatile CollectionAttribute<User, Photo> photoCollection;
    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, Account> accountId;
    public static volatile SingularAttribute<User, String> street;
    public static volatile SingularAttribute<User, String> middleName;
    public static volatile SingularAttribute<User, Integer> id;

}