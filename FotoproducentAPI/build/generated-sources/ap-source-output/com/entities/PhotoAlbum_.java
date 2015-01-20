package com.entities;

import com.entities.Album;
import com.entities.Photo;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-10T14:59:30")
@StaticMetamodel(PhotoAlbum.class)
public class PhotoAlbum_ { 

    public static volatile SingularAttribute<PhotoAlbum, Album> albumId;
    public static volatile SingularAttribute<PhotoAlbum, Photo> photoId;
    public static volatile SingularAttribute<PhotoAlbum, Integer> id;

}