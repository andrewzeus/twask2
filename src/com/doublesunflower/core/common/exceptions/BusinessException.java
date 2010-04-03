/*
 * BusinessException.java
 *
 * Created on August 4, 2007, 12:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.doublesunflower.core.common.exceptions;

/**
 *
 * @author Matias
 */
public class BusinessException extends Exception{
    
    /** Creates a new instance of BusinessException */
    public BusinessException() {
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause)  {
        super(message, cause);
    }
    
    public BusinessException(Throwable cause)  {
        super(cause);
    }
    
}
