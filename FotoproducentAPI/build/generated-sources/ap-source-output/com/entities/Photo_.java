package com.entities;

import com.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-16T19:13:56")
@StaticMetamodel(Photo.class)
public class Photo_ { 

    public static volatile SingularAttribute<Photo, User> photographerId;
    public static volatile SingularAttribute<Photo, String> extension;
    public static volatile SingularAttribute<Photo, Integer> size;
    public static volatile SingularAttribute<Photo, Double> price;
    public static volatile SingularAttribute<Photo, String> name;
    public static volatile SingularAttribute<Photo, String> location;
    public static volatile SingularAttribute<Photo, Integer> id;
    public static volatile SingularAttribute<Photo, Double> dimensions;

}