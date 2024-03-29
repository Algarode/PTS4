/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Hafid
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.accountmanagement.ChangeAccount.class);
        resources.add(com.generic.Auth.class);
        resources.add(com.generic.CodeClaim.class);
        resources.add(com.generic.OrderInfo.class);
        resources.add(com.generic.PhotoPrice.class);
        resources.add(com.generic.Photographer.class);
        resources.add(com.generic.Products.class);
        resources.add(com.generic.Register.class);
        resources.add(upload.UploadResource.class);
    }
    
}
