/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToDoMain;

import java.io.Serializable;

/**
 *
 * @author katinas
 */
public class PermissionDenied extends Exception implements Serializable {

    PermissionDenied(String error) {
        if(error.equals("user task project")){
            throw new UnsupportedOperationException("User does not have rights to add Task to this Project");
        } else if(error.equals("duplicate project user")){
            throw new UnsupportedOperationException("User is already added to this Project");
        } else if(error.equals("duplicate user")){
            throw new UnsupportedOperationException("Cannot register, login already taken");
        } else if(error.equals("project completed")){
            throw new UnsupportedOperationException("Cannot add task because project is already completed");
        } else if(error.equals("bad input")){
            throw new UnsupportedOperationException("Incorrect input, try again");
        }

    }

}
