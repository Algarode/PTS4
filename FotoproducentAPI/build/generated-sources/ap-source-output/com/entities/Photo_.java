package com.entities;

import com.entities.Collection;
import com.entities.OrderLine;
import com.entities.PhotoAlbum;
import com.entities.ProductPhoto;
import com.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(Photo.class)
public class Photo_ { 

    public static volatile CollectionAttribute<Photo, OrderLine> orderLineCollection;
    public static volatile SingularAttribute<Photo, User> photographerId;
    public static volatile SingularAttribute<Photo, String> name;
    public static volatile CollectionAttribute<Photo, ProductPhoto> productPhotoCollection;
    public static volatile SingularAttribute<Photo, String> location;
    public static volatile SingularAttribute<Photo, String> id;
    public static volatile CollectionAttribute<Photo, PhotoAlbum> photoAlbumCollection;
    public static volatile CollectionAttribute<Photo, Collection> collectionCollection;

}