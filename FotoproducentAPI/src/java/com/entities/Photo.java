/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Hafid
 */
@Entity
@Table(name = "photo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p"),
    @NamedQuery(name = "Photo.findById", query = "SELECT p FROM Photo p WHERE p.id = :id"),
    @NamedQuery(name = "Photo.findByName", query = "SELECT p FROM Photo p WHERE p.name = :name"),
    @NamedQuery(name = "Photo.findByLocation", query = "SELECT p FROM Photo p WHERE p.location = :location"),
    @NamedQuery(name = "Photo.findBySize", query = "SELECT p FROM Photo p WHERE p.size = :size")})
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Column(name = "size")
    private int size;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photoId")
    private Collection<PhotoAlbum> photoAlbumCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photoID")
    private Collection<OrderLine> orderLineCollection;
    @JoinColumn(name = "photographer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User photographerId;
    @OneToMany(mappedBy = "photoID")
    private Collection<com.entities.Collection> collectionCollection;

    public Photo() {
    }

    public Photo(String id) {
        this.id = id;
    }

    public Photo(String id, String name, String location, int size) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<PhotoAlbum> getPhotoAlbumCollection() {
        return photoAlbumCollection;
    }

    public void setPhotoAlbumCollection(Collection<PhotoAlbum> photoAlbumCollection) {
        this.photoAlbumCollection = photoAlbumCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<OrderLine> getOrderLineCollection() {
        return orderLineCollection;
    }

    public void setOrderLineCollection(Collection<OrderLine> orderLineCollection) {
        this.orderLineCollection = orderLineCollection;
    }

    public User getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(User photographerId) {
        this.photographerId = photographerId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<com.entities.Collection> getCollectionCollection() {
        return collectionCollection;
    }

    public void setCollectionCollection(Collection<com.entities.Collection> collectionCollection) {
        this.collectionCollection = collectionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Photo[ id=" + id + " ]";
    }
    
}
