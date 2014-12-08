/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author rob
 */
@Entity
@Table(name = "size_prize")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SizePrize.findAll", query = "SELECT s FROM SizePrize s"),
    @NamedQuery(name = "SizePrize.findById", query = "SELECT s FROM SizePrize s WHERE s.id = :id"),
    @NamedQuery(name = "SizePrize.findByPrice", query = "SELECT s FROM SizePrize s WHERE s.price = :price")})
public class SizePrize implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToMany(mappedBy = "sizeID")
    private Collection<OrderLine> orderLineCollection;
    @JoinColumn(name = "sizeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Size1 sizeID;
    @JoinColumn(name = "photographerID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User photographerID;

    public SizePrize() {
    }

    public SizePrize(Integer id) {
        this.id = id;
    }

    public SizePrize(Integer id, double price) {
        this.id = id;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<OrderLine> getOrderLineCollection() {
        return orderLineCollection;
    }

    public void setOrderLineCollection(Collection<OrderLine> orderLineCollection) {
        this.orderLineCollection = orderLineCollection;
    }

    public Size1 getSizeID() {
        return sizeID;
    }

    public void setSizeID(Size1 sizeID) {
        this.sizeID = sizeID;
    }

    public User getPhotographerID() {
        return photographerID;
    }

    public void setPhotographerID(User photographerID) {
        this.photographerID = photographerID;
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
        if (!(object instanceof SizePrize)) {
            return false;
        }
        SizePrize other = (SizePrize) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.SizePrize[ id=" + id + " ]";
    }
    
}
