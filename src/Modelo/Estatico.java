/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;

/**
 *
 * @author andre
 */
public abstract class Estatico extends Personagem implements Serializable{

    public Estatico(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    
    
}