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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "size")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Size1.findAll", query = "SELECT s FROM Size1 s"),
    @NamedQuery(name = "Size1.findById", query = "SELECT s FROM Size1 s WHERE s.id = :id"),
    @NamedQuery(name = "Size1.findByHeight", query = "SELECT s FROM Size1 s WHERE s.height = :height"),
    @NamedQuery(name = "Size1.findByWidth", query = "SELECT s FROM Size1 s WHERE s.width = :width"),
    @NamedQuery(name = "Size1.findByPrice", query = "SELECT s FROM Size1 s WHERE s.price = :price")})
public class Size1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "height")
    private int height;
    @Basic(optional = false)
    @NotNull
    @Column(name = "width")
    private int width;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sizeID")
    private Collection<OrderLine> orderLineCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sizeID")
    private Collection<SizePrize> sizePrizeCollection;

    public Size1() {
    }

    public Size1(Integer id) {
        this.id = id;
    }

    public Size1(Integer id, int height, int width, double price) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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

    @XmlTransient
    @JsonIgnore
    public Collection<SizePrize> getSizePrizeCollection() {
        return sizePrizeCollection;
    }

    public void setSizePrizeCollection(Collection<SizePrize> sizePrizeCollection) {
        this.sizePrizeCollection = sizePrizeCollection;
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
        if (!(object instanceof Size1)) {
            return false;
        }
        Size1 other = (Size1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Size1[ id=" + id + " ]";
    }
    
}
