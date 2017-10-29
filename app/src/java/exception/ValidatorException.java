/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Joel Tay
 */
public class ValidatorException extends Exception{

    /**
     * Creates a new ValidatorException object with an error msg
     * @param s error message specified
     */
    public ValidatorException(String s){
        super(s);
    }
}
