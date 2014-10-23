/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rob
 */
@Entity
@Table(name = "photo_share")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhotoShare.findAll", query = "SELECT p FROM PhotoShare p"),
    @NamedQuery(name = "PhotoShare.findById", query = "SELECT p FROM PhotoShare p WHERE p.id = :id")})
public class PhotoShare implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "share_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Share shareId;
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Photo photoId;

    public PhotoShare() {
    }

    public PhotoShare(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Share getShareId() {
        return shareId;
    }

    public void setShareId(Share shareId) {
        this.shareId = shareId;
    }

    public Photo getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Photo photoId) {
        this.photoId = photoId;
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
        if (!(object instanceof PhotoShare)) {
            return false;
        }
        PhotoShare other = (PhotoShare) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.PhotoShare[ id=" + id + " ]";
    }
    
}
