package com.example;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URLConnection;
import java.sql.Array;
import java.util.*;
import java.io.IOException;
import java.net.URL;
// Import de FileUtils pour la manipulation de fichiers

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class Main {


    public static List<String> users() {
        // Entrer ses données utilisateurs de Github
        java.net.URL url = null;
        String username = "*******************";
        String password = "***********";
        String file = "";
        try {
            // Se connecter au Github
            url = new java.net.URL("https://raw.githubusercontent.com/SIDNEYAUDIBERT/MSPR1/main/staff.txt");
            java.net.URLConnection uc;
            uc = url.openConnection();
            uc.setRequestProperty("X-Requested-With", "Curl");
            java.util.ArrayList<String> list = new java.util.ArrayList<String>();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            // Créer une variable file qui contient toutes les lignes du staff.txt
            String line = null;
            while ((line = reader.readLine()) != null)
                //System.out.println(line);
                file = file + line + "\n";
        } catch (IOException e) {
            System.out.println("Wrong username and password");
        }


        // Créer un array contenant toutes les lignes de la variable file
        String[] words = file.split("\\s+");
        List<String> wordsList = Arrays.asList(words);


        for (String word: wordsList){
            word = word.replaceAll("[^\\w]", "");
        }
        //Renvoyer l'array contenant la liste des Prénoms
        return wordsList;
    }


    public static void generate (String...args) throws Exception {

        // Créer un array names qui contient le renvoi de la fonction users, donc les prénoms du staff.txt
        String[] names = users().toArray(new String[0]);

        //Création d'un stringBuilder contenant l'array names => stringbuiler est une liste d'objets sans les virgules et crochets de l'array
        StringBuilder stringNoms = getStringBuilder(names);

        for (String prenom : names) {

            System.out.println("Création du fichier pour " + prenom);

            // Entrer ses données utilisateurs de Github
            URL url = null;
            String username = "sidney.audibert@gmail.com";
            String password = "Babounez01!";
            String file = "";
            try {
                // Se connecter au Github
                url = new URL("https://raw.githubusercontent.com/SIDNEYAUDIBERT/MSPR1/main/" + prenom + "/" + prenom + ".txt");
                URLConnection uc;
                uc = url.openConnection();
                uc.setRequestProperty("X-Requested-With", "Curl");
                ArrayList<String> list = new ArrayList<String>();
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                uc.setRequestProperty("Authorization", basicAuth);
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                // Créer une variable file qui contient toutes les lignes du .txt
                String line = null;
                while ((line = reader.readLine()) != null)
                    //System.out.println(line);
                    file = file + line + "\n";
            } catch (IOException e) {
                System.out.println("Wrong username and password");
            }


            // Créer un array contenant toutes les lignes de la variable file
            String[] words = file.split("\\s+");
            // Créer une List contenant les mots définis précédemment
            List<String> wordsList = Arrays.asList(words);


            for (String word : wordsList) {
                word = word.replaceAll("[^\\w]", "");
            }


            // Déterminer le fichier template pour user
            File htmlTemplateFile = new File("src/webpages/template_user.html");
            // Variable contenant toutes les lignes du template pour user
            String htmlString = FileUtils.readFileToString(htmlTemplateFile);
            // Création d'un nouveau fichier au nom du prenom du user
            File newHtmlFile = new File("src/webpages/"+prenom+".html");
            // Ecriture du contenu du template dans le nouveau fichier créé
            writeStringToFile(newHtmlFile, htmlString);


            // Même procédure avec le template index
            File htmlTemplateIndexFile = new File("src/webpages/template_index.html");
            File newIndexFile = new File("src/webpages/index.html");
            String htmlStringTemplateIndexFile = FileUtils.readFileToString(htmlTemplateIndexFile);
            writeStringToFile(newIndexFile, htmlStringTemplateIndexFile);


            // Création d'un array user
            List<String> user = new ArrayList<>();

            // Création d'un array equipments contenant toutes les infos à du deuxième indice de wordsLists (qui contient les lignes du .txt)
            List<String> equipments = new ArrayList(wordsList.subList(2, wordsList.size() - 1));

            // Push des infos nom et prenom dans l'array user
            user.add(wordsList.get(0));
            user.add(wordsList.get(1));

            // Lire les données du fichiers html
            String data = FileUtils.readFileToString(newHtmlFile);
            String dataIndex = FileUtils.readFileToString(newIndexFile);

            // Remplacer les balises par les données : $links par les liens et $people par des cards
            dataIndex = dataIndex.replace("$links", stringNoms.toString());
            dataIndex = dataIndex.replace("$people", Arrays.toString(names));
            // Ecriture
            writeStringToFile(newIndexFile, dataIndex);


            // Remplacer les infos du fichiers html par les nouvelles de l'utilisateur
            data = data.replace("$lastname", user.get(0));
            data = data.replace("$name", user.get(1));
            data = data.replace("$tools", equipments.toString().substring(1, equipments.toString().length() - 1));
            data = data.replace("$imgsrc", "https://github.com/SIDNEYAUDIBERT/MSPR1/blob/main/"+prenom+"/"+prenom+".jpg?raw=true");
            // Ecriture
            writeStringToFile(newHtmlFile, data);

        }
    }

    private static StringBuilder getStringBuilder(String[] names) {
        // Création d'un arraylist
        List<String> names3 = new ArrayList<>();
        // Pour chaque nom, on créer une card en html
        for (String test : names) {
            test = "<div class=\"card\">\n" +
                    "\n" +
                    "                <h2 class=\"card__title\"><a href=\""+test+".html\"><i class=\"fas fa-user-alt\"></i>"+test+"</a></h2>\n" +
                    "\n" +
                    "                <div class=\"card__container\">\n" +
                    "                    <span class=\"card__container-light\"></span>\n" +
                    "                    <span><a href=\""+test+".html\" class=\"card__container-link\"> voir le Profil <i class=\"fas fa-chevron-right\"></i></a></span>\n" +
                    "                </div>\n" +
                    "\n" +
                    "            </div>";
            names3.add(test);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: names3){
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder;
    }


    public static void main(String... args) throws Exception {

        generate();

    }
}

