package com.entities;

import com.entities.Album;
import com.entities.Photo;
import com.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(Collection.class)
public class Collection_ { 

    public static volatile SingularAttribute<Collection, Album> albumID;
    public static volatile SingularAttribute<Collection, Photo> photoID;
    public static volatile SingularAttribute<Collection, Integer> id;
    public static volatile SingularAttribute<Collection, User> userID;

}