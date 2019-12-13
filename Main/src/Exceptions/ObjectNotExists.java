/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import java.io.Serializable;

/**
 *
 * @author katinas
 */
public class ObjectNotExists extends Exception implements Serializable {

    public ObjectNotExists(String error) {
        if(error.equals("user")){
            throw new UnsupportedOperationException("User not found");
        } else if(error.equals("project")){
            throw new UnsupportedOperationException("Project not found");
        } else if(error.equals("task")){
            throw new UnsupportedOperationException("Task not found");
        } else if(error.equals("company")){
            throw new UnsupportedOperationException("Company not found");
        } else if(error.equals("file")){
            throw new UnsupportedOperationException("File not found");
        }

    }
    
}
