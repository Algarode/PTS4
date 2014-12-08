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
 * @author rob
 */
@Entity
@Table(name = "album")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a"),
    @NamedQuery(name = "Album.findById", query = "SELECT a FROM Album a WHERE a.id = :id"),
    @NamedQuery(name = "Album.findByNaam", query = "SELECT a FROM Album a WHERE a.naam = :naam")})
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naam")
    private String naam;
    @JoinColumn(name = "photographerID", referencedColumnName = "id")
    @ManyToOne
    private User photographerID;
    @OneToMany(mappedBy = "albumID")
    private Collection<com.entities.Collection> collectionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "albumId")
    private Collection<PhotoAlbum> photoAlbumCollection;

    public Album() {
    }

    public Album(String id) {
        this.id = id;
    }

    public Album(String id, String naam) {
        this.id = id;
        this.naam = naam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public User getPhotographerID() {
        return photographerID;
    }

    public void setPhotographerID(User photographerID) {
        this.photographerID = photographerID;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<com.entities.Collection> getCollectionCollection() {
        return collectionCollection;
    }

    public void setCollectionCollection(Collection<com.entities.Collection> collectionCollection) {
        this.collectionCollection = collectionCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<PhotoAlbum> getPhotoAlbumCollection() {
        return photoAlbumCollection;
    }

    public void setPhotoAlbumCollection(Collection<PhotoAlbum> photoAlbumCollection) {
        this.photoAlbumCollection = photoAlbumCollection;
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
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Album[ id=" + id + " ]";
    }
    
}
