package keithcod.es.mineget;


import com.google.gson.Gson;
import com.sun.deploy.util.StringUtils;
import keithcod.es.mineget.wrappers.Bukget;
import keithcod.es.mineget.wrappers.Spiget;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.Base64;
import java.util.HashMap;

public class MineGet {

    public static void main(String[] args) {


        if (args.length > 0 && args[0].equals("list")){
            int size = args.length > 1 ? Integer.parseInt(args[1], 10) : 10;
            int page = args.length > 2 ? Integer.parseInt(args[2], 0) : 0;
            System.out.println("Page size: " + size + "   Page: " + page);
            System.out.println(" === Bukkit ===");

            Bukget.ListItem[] bukgetSearch = Bukget.List(size, page);

            if(bukgetSearch != null) {
                for (Bukget.ListItem si : bukgetSearch) {
                    System.out.println(si.slug + " - " + si.description);
                }
            }else{
                System.out.println("BukGet appears to be down!");
            }

            System.out.println(" === Spigot ===");
            Spiget.ListItem[] spigetSearch = Spiget.List(size, page);
            if(spigetSearch != null){
                for(Spiget.ListItem si : spigetSearch){
                    System.out.println(si.id + " - " + si.name);
                }
            }else{
                System.out.println("SpiGet appears to be down!");
            }
        }

        if (args.length > 0 && args[0].equals("search")){
            String query = args.length > 1 ? args[1] : "";
            int size = args.length > 2 ? Integer.parseInt(args[2], 10) : 10;
            int page = args.length > 3 ? Integer.parseInt(args[3], 0) : 0;
            System.out.println("Searching for: " + query + "   Page size: " + size + "   Page: " + page);
            System.out.println(" === Bukkit ===");
            Bukget.ListItem[] bukgetSearch = Bukget.Search(query, size, page);
            if(bukgetSearch != null) {
                for(Bukget.ListItem si : bukgetSearch){
                    System.out.println(si.slug + " - " + si.description);
                }
            }else if (code == 404){
                System.out.println("Cannot find any plugins matching the search");
            }{
                System.out.println("BukGet appears to be down!");
            }

            System.out.println(" === Spigot ===");
            Spiget.ListItem[] spigetSearch = Spiget.Search(query, size, page);
            if(spigetSearch != null){
                for(Spiget.ListItem si : spigetSearch){
                    System.out.println(si.id + " - " + si.tag);
                }
            }else if (code == 404){
                System.out.println("Cannot find any plugins matching the search");
            } else {
                System.out.println("SpiGet appears to be down!");
            }
        }

        if (args.length > 0 && args[0].equals("info")){
            String query = args.length > 1 ? args[1] : "";

            System.out.println("Getting info for: " + query);

            if(!isPrimitive(query)) { // Bukget uses slugs
                System.out.println(" === Bukkit ===");
                Bukget.PluginInfo bukgetSearch = Bukget.Get(query);
                if (bukgetSearch != null) {
                    System.out.println(bukgetSearch.plugin_name);
                    System.out.println(String.join("\n", bukgetSearch.authors));
                    System.out.println(String.join(", ", bukgetSearch.categories));
                    System.out.println(" === Description === ");
                    System.out.println(bukgetSearch.description);
                    System.out.println(bukgetSearch.plugin_name);
                    System.out.println(bukgetSearch.website);
                } else {
                    System.out.println("BukGet appears to be down!");
                }
            }else { // SpiGet uses integer IDs
                System.out.println(" === Spigot ===");
                Spiget.ListItem spigotSearch = Spiget.Get(Integer.parseInt(query));
                if (spigotSearch != null) {
                    System.out.println(spigotSearch.name);
                    System.out.println(spigotSearch.contributors);
                    System.out.println(spigotSearch.tag);
                    System.out.println(" === Description === ");
                    String desc = new String(Base64.getDecoder().decode(spigotSearch.description));
                    desc = desc.substring(0, ordinalIndexOf(desc, "\n", 8)) + "\n (.....)";
                    System.out.println(desc);
                   // System.out.println(String.join("\n", spigotSearch.links));
                } else {
                    System.out.println("SpiGet appears to be down!");
                }

            }

        }

        if (args.length > 0 && args[0].equals("install")){
            String query = args.length > 1 ? args[1] : "";

            System.out.println("Installing " + query + "...");

            String cwd = Paths.get(".").toAbsolutePath().normalize().toString();

            File f = new File(cwd + "/plugins");
            if(f.exists() && f.isDirectory()) {
                cwd += "/plugins";
            }

            System.out.println(cwd);


            if(!isPrimitive(query)) { // Bukget uses slugs
                System.out.println("Using Bukkit");
                Bukget.PluginInfo bukgetSearch = Bukget.Get(query);
                if (bukgetSearch != null) {
                    String url = Bukget.bukgetURL + "plugins/bukkit/" + query + "/latest/download";

                    System.out.println(url);

                    Download(url, cwd + "/" + bukgetSearch.plugin_name + ".jar");
                    if(code == 404){
                        System.out.println("Cannot find plugin file to download!");
                    }else{
                        System.out.println("Successfully downloaded " + bukgetSearch.plugin_name);
                    }



                } else {
                    System.out.println("BukGet appears to be down!");
                }
            }else { // SpiGet uses integer IDs
                System.out.println("Using Spigot");
                Spiget.ListItem spigotSearch = Spiget.Get(Integer.parseInt(query));
                if (spigotSearch != null) {
                    //String url = Spiget.spigetURL + "resources/"+query+"/versions/latest/download";
                    String url = Spiget.spigetURL + "resources/"+query+"/download";

                    Download(url, cwd + "/" + spigotSearch.name + ".jar");
                    if(code == 404){
                        System.out.println("Unfortunately because of the way SpiGet works,");
                        System.out.println("sometimes automated downloads are not possible ");
                        System.out.println("on spigot.org because it's linked to externally.");
                        System.out.println("Please manually download the latest version: ");
                        System.out.println(Spiget.spigetURL + "resources/"+query+"/versions/latest/download");
                    }else{
                        System.out.println("Successfully downloaded " + spigotSearch.name);
                    }
                } else if (code == 404) {
                    System.out.println("Resource not found!");
                } else {
                    System.out.println("SpiGet appears to be down!");
                }
            }
        }
    }

    public static void Download(String url, String dest){
        try {
                        /* Follow the redirection tree */
            int responseCode = 300;//httpConn.getResponseCode();
            while ((responseCode / 100) == 3) { /* codes 3XX are redirections */
                URL website = new URL(url);

                HttpURLConnection httpConn = (HttpURLConnection) website.openConnection();
                httpConn.setInstanceFollowRedirects(false);
                httpConn.connect();

                responseCode = httpConn.getResponseCode();

                if ((responseCode / 100) == 3) {
                    String newLocationHeader = httpConn.getHeaderField("Location");
                    url = newLocationHeader;
                }
                           /* open a new connection and get the content for the URL newLocationHeader */
                               /* ... */
                            /* do it until you get some code that is not a redirection */
            }
            if(responseCode == 404){
                code = 404;
                return;
            }

            System.out.println(url);


            URL download = new URL(url);
            Path path = FileSystems.getDefault().getPath(dest);
            try (InputStream in = download.openStream()) {
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(MalformedURLException ex){
            System.out.println("Error creating save path! Please report.");
        }
        catch(IOException ex_){
            System.out.println("Error writing file, do you have permission?");
        }
    }


    public static int code;
    public static String json;
    public static String GetJSON(String u) throws MalformedURLException, IOException{
        System.out.println("Resolving " + u + "...");
        URL url = new URL(u);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        int status = request.getResponseCode();
        System.out.println(status);
        code = status;

        switch (status) {
            case 200:
            case 201: {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                return sb.toString();
            }
            default: {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                json = sb.toString();
            }
        }
        return null;
    }

    private static boolean isPrimitive(String value){
        boolean status=true;
        if(value.length()<1)
            return false;
        for(int i = 0;i<value.length();i++){
            char c=value.charAt(i);
            if(Character.isDigit(c) || c=='.'){

            }else{
                status=false;
                break;
            }
        }
        return status;
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
}
