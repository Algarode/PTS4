package com.entities;

import com.entities.Account;
import com.entities.Album;
import com.entities.Bestelling;
import com.entities.Collection;
import com.entities.Photo;
import com.entities.SizePrize;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> country;
    public static volatile SingularAttribute<User, String> gender;
    public static volatile SingularAttribute<User, String> city;
    public static volatile CollectionAttribute<User, Bestelling> bestellingCollection;
    public static volatile SingularAttribute<User, String> postalCode;
    public static volatile SingularAttribute<User, String> houseNumber;
    public static volatile CollectionAttribute<User, Photo> photoCollection;
    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, Account> accountId;
    public static volatile CollectionAttribute<User, SizePrize> sizePrizeCollection;
    public static volatile SingularAttribute<User, String> street;
    public static volatile CollectionAttribute<User, Album> albumCollection;
    public static volatile SingularAttribute<User, String> middleName;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile CollectionAttribute<User, Collection> collectionCollection;

}