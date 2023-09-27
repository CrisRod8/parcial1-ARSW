/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) throws InterruptedException, IOException {//Este es el método principal main que se ejecutará cuando se inicie el programa.

        //En esta parte, se crea un objeto Scanner para recibir entrada del usuario y se le pide al usuario
        //que ingrese el número de dígitos de Pi que desea calcular y el número de hilos que desea utilizar para el cálculo.
        //Luego se imprime "Procesando..." y se llama a un método para calcular los dígitos de Pi y mostrarlos en formato hexadecimal.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite el numero de digitos a conocer:");
        int dig = scanner.nextInt();
        System.out.println("Digite el numero de hilos a crear:");
        int hil = scanner.nextInt();
        int ini = 0;

        System.out.println("Procesando...");
        System.out.println(bytesToHex(new PiDigits().getDigits(ini, dig, hil)));

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();//Aquí se define una matriz de caracteres que contiene los dígitos hexadecimales.

    public static String bytesToHex(byte[] bytes) {
        //Este método convierte un arreglo de bytes en su representación hexadecimal.
        //Toma cada byte, lo convierte a dos dígitos hexadecimales y los agrega a una cadena de caracteres que luego se devuelve.
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);
        }
        return sb.toString();
    }

}