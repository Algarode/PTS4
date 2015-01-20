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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rob
 */
@Entity
@Table(name = "product_photo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductPhoto.findAll", query = "SELECT p FROM ProductPhoto p"),
    @NamedQuery(name = "ProductPhoto.findById", query = "SELECT p FROM ProductPhoto p WHERE p.id = :id")})
public class ProductPhoto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "productID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productID;
    @JoinColumn(name = "photoID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Photo photoID;

    public ProductPhoto() {
    }

    public ProductPhoto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public Photo getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Photo photoID) {
        this.photoID = photoID;
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
        if (!(object instanceof ProductPhoto)) {
            return false;
        }
        ProductPhoto other = (ProductPhoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.ProductPhoto[ id=" + id + " ]";
    }
    
}
