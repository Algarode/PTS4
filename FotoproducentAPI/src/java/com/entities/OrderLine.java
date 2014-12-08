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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rob
 */
@Entity
@Table(name = "order_line")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderLine.findAll", query = "SELECT o FROM OrderLine o"),
    @NamedQuery(name = "OrderLine.findById", query = "SELECT o FROM OrderLine o WHERE o.id = :id"),
    @NamedQuery(name = "OrderLine.findByProductPhotoId", query = "SELECT o FROM OrderLine o WHERE o.productPhotoId = :productPhotoId"),
    @NamedQuery(name = "OrderLine.findByAmount", query = "SELECT o FROM OrderLine o WHERE o.amount = :amount")})
public class OrderLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "product_photo_id")
    private Integer productPhotoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private int amount;
    @JoinColumn(name = "sizeID", referencedColumnName = "id")
    @ManyToOne
    private SizePrize sizeID;
    @JoinColumn(name = "photoID", referencedColumnName = "id")
    @ManyToOne
    private Photo photoID;
    @JoinColumn(name = "orderID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bestelling orderID;

    public OrderLine() {
    }

    public OrderLine(Integer id) {
        this.id = id;
    }

    public OrderLine(Integer id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductPhotoId() {
        return productPhotoId;
    }

    public void setProductPhotoId(Integer productPhotoId) {
        this.productPhotoId = productPhotoId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public SizePrize getSizeID() {
        return sizeID;
    }

    public void setSizeID(SizePrize sizeID) {
        this.sizeID = sizeID;
    }

    public Photo getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Photo photoID) {
        this.photoID = photoID;
    }

    public Bestelling getOrderID() {
        return orderID;
    }

    public void setOrderID(Bestelling orderID) {
        this.orderID = orderID;
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
        if (!(object instanceof OrderLine)) {
            return false;
        }
        OrderLine other = (OrderLine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.OrderLine[ id=" + id + " ]";
    }
    
}
