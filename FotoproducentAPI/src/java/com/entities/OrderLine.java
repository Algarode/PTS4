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
 * @author Hafid
 */
@Entity
@Table(name = "order_line")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderLine.findAll", query = "SELECT o FROM OrderLine o"),
    @NamedQuery(name = "OrderLine.findById", query = "SELECT o FROM OrderLine o WHERE o.id = :id"),
    @NamedQuery(name = "OrderLine.findByAmount", query = "SELECT o FROM OrderLine o WHERE o.amount = :amount")})
public class OrderLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private int amount;
    @JoinColumn(name = "orderID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bestelling orderID;
    @JoinColumn(name = "sizeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Size1 sizeID;
    @JoinColumn(name = "photoID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Photo photoID;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Bestelling getOrderID() {
        return orderID;
    }

    public void setOrderID(Bestelling orderID) {
        this.orderID = orderID;
    }

    public Size1 getSizeID() {
        return sizeID;
    }

    public void setSizeID(Size1 sizeID) {
        this.sizeID = sizeID;
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
