package com.example.john.myapplication;

import android.content.Context;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant la gestion d'un fichier de favoris en mémoire interne
 */

public class FileManager {

    /**
     * Permet de savoir si le fichier a déjà été crée
     * @param fileName: Nom du Fichier
     * @param ctx : Context de l'application
     */
    public boolean exists(final String fileName, Context ctx) throws NullPointerException, SecurityException {
        final File file = new File(ctx.getFilesDir() + "/" + fileName);
        boolean exists = file.exists();
        return exists;
    }


    /**
     * Création du fichier
     * @param fileName
     * @param ctx
     */
    public boolean createFile(final String fileName, Context ctx) throws NullPointerException, IOException, SecurityException {
        /*final File file = new File(fileName);
        return file.createNewFile();*/
        File file=null;
        Boolean success=false;
        file=new File(ctx.getFilesDir(), fileName);

        if(file != null) {
            success=true;
        }
        return success;
    }

    /**
     * Ecriture dans le fichier écrasant son contenu actuel
     * @param fileName
     * @param text    : String à écrire dans le fichier
     * @param ctx
     */
    public void writeFile(final String fileName, final String text,Context ctx)
            throws NullPointerException, IOException, SecurityException {
        File file = new File(ctx.getFilesDir(),fileName);
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();
        //readFile(fileName, ctx);

    }

    /**
     * Lecture du fichier entier, chaque ligne séparée par \n
     * @param fileName
     * @param ctx
     */
    public String readFile(final String fileName, Context ctx) throws IOException, SecurityException, FileNotFoundException {
        String filePath = ctx.getFilesDir() + "/" + fileName;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String text = "";
        String currentLine = "";
        while ((currentLine = bufferedReader.readLine()) != null) {
            text += currentLine + "\n";
        }
        bufferedReader.close();
        return text;
    }


    /**
     * Ajout d'une ligne au fichier
     * @param fileName
     * @param content
     */
    public void appendToFile(final String fileName, final String content, Context ctx) throws IOException {
        FileOutputStream out =ctx.openFileOutput(fileName, Context.MODE_APPEND);
        out.write(content.getBytes());
        out.close();
    }

    /**
     * Renvoie true si "content" est dans le fichier
     * @param fileName
     * @param content
     */
    public boolean findInFile(final String fileName, final String content, Context ctx) throws IOException {
        String filePath = ctx.getFilesDir() + "/" + fileName;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String currentLine = "";
        while ((currentLine = bufferedReader.readLine()) != null) {
            if(currentLine.split(" ")[0].equals(content)) {
                return true;
            }
        }
        bufferedReader.close();
        return false;
    }

    /**
     * Suppression d'une ligne dans le fichier
     * @param fileName
     * @param lineToRemove : String correspondant au contenu de la ligne à supprimer
     * @param ctx
     */
    public void deleteLinefromFile(final String fileName, final String lineToRemove, Context ctx)
            throws IOException, SecurityException, FileNotFoundException {
        String filePath = ctx.getFilesDir() + "/" + fileName;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String content="";
        String currentLine;

        while ((currentLine = bufferedReader.readLine()) != null) {
            if(!currentLine.equals(lineToRemove) ) {
                content=content+currentLine+"\n";
            }
        }
        bufferedReader.close();
        this.writeFile(fileName,content,ctx);
        System.out.println(content);
    }


    /**
     * Renvoie true si le fichier est vide ou inexistant
     * @param fileName
     * @param ctx
     * @return
     */
    public boolean isEmpty(final String fileName,Context ctx){
        if(!exists(fileName,ctx)){
            return true;
        }
        else try {
            if(readFile(fileName,ctx).equals("")){
                return true;
            }
            else{return false;}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }



}




