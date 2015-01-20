package com.entities;

import com.entities.Collection;
import com.entities.PhotoAlbum;
import com.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(Album.class)
public class Album_ { 

    public static volatile SingularAttribute<Album, User> photographerID;
    public static volatile SingularAttribute<Album, String> id;
    public static volatile SingularAttribute<Album, String> naam;
    public static volatile CollectionAttribute<Album, PhotoAlbum> photoAlbumCollection;
    public static volatile CollectionAttribute<Album, Collection> collectionCollection;

}