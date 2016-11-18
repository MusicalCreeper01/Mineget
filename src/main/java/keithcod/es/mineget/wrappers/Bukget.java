package keithcod.es.mineget.wrappers;

import com.google.gson.*;

import com.google.gson.annotations.SerializedName;
import keithcod.es.mineget.MineGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public class Bukget {

    public class ListItem {
        public String description = "";
        public String plugin_name = "";
        public String slug = "";
    }

    public static final String bukgetURL = "http://api.bukget.org/3/";

    public static ListItem[] List(){
        return List(10, 0);
    }

    public static ListItem[] List(int size){
        return List(size, 0);
    }

    public static ListItem[] List(int size, int start){
        try {
            String json = MineGet.GetJSON(bukgetURL + "plugins?start=" + start + "&size=" + size);
            if (json != null) {
                Gson gson = new Gson();
                ListItem[] items = gson.fromJson(json, ListItem[].class);
                return items;
            }


            /*JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            items = rootobj.getAsJsonArray().*/

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }


        return null;
    }

    public static ListItem[] Search(String query, int size, int start){

        try {
            String json = MineGet.GetJSON(bukgetURL + "search/plugin_name/like/"+query+"?start=" + start + "&size=" + size);
            if (json != null) {
                Gson gson = new Gson();
                ListItem[] items = gson.fromJson(json, ListItem[].class);
                return items;
            }



            /*JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            items = rootobj.getAsJsonArray().*/

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }

        return null;
    }

    public class PluginInfo {
        public String website;
        public String dbo_page;
        public String description;
        public String logo_full;

        public class Version {
            public String status;
            public class Command {
                public String usage;
                public String[] aliases;
                public String command;
                public String permission_message;
                public String permission;
            }
            public Command[] commands;
            public String changelog;
            public String[] game_versions;
            public String filename;
            public String[] hard_dependencies;
            public int date;
            public String version;
            public String link;
            public String download;
            public String md5;
            public String type;
            public String slug;
            public String[] soft_dependencies;
            public class Permission {
                @SerializedName("default")
                public String default_;
                public String role;
            }
            public Permission[] permissions;
        }
        public Version[] versions;
        public String plugin_name;

        public class Popularity {
            public int monthly;
            public int daily;
            public int weekly;
            public int total;
        }
        public Popularity popularity;
        public String server;
        public String main;
        public String[] authors;
        public String logo;
        public String slug;
        public String[] categories;
        public String stage;
    }

    public static PluginInfo Get(String slug){

        try {
            String json = MineGet.GetJSON(bukgetURL + "plugins/bukkit/"+slug+"?size=20");
            if (json != null) {
                Gson gson = new Gson();
                PluginInfo item = gson.fromJson(json, PluginInfo.class);
                return item;
            }



            /*JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

            items = rootobj.getAsJsonArray().*/

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }

        return null;
    }

    /*{
    "website": "http://dev.bukkit.org/server-mods/abacus/",
    "dbo_page": "http://dev.bukkit.org/server-mods/abacus",
    "description": "Offers ability to perform calculations while in Minecraft",
    "logo_full": "",
    "versions": [
        {
            "status": "Semi-normal",
            "commands": [
                {
                    "usage": "",
                    "aliases": [],
                    "command": "abacus",
                    "permission-message": "§cYou do not have access to that command.",
                    "permission": "abacus.abacus"
                }
            ],
            "changelog": "LONG STRING OF STUFF!!!!!",
            "game_versions": [
                "CB 1.4.5-R0.2"
            ],
            "filename": "Abacus.jar",
            "hard_dependencies": [],
            "date": 1353964798,
            "version": "0.9.3",
            "link": "http://dev.bukkit.org/server-mods/abacus/files/4-abacus-v0-9-2-cb-1-4-5-r0-2-compatible-w-1-3-2/",
            "download": "http://dev.bukkit.org/media/files/650/288/Abacus.jar",
            "md5": "1e1b6b6e131c617248f98c53bf650874",
            "type": "Beta",
            "slug": "4-abacus-v0-9-2-cb-1-4-5-r0-2-compatible-w-1-3-2",
            "soft_dependencies": [],
            "permissions": [
                {
                    "default": "op",
                    "role": "abacus.*"
                },
                {
                    "default": true,
                    "role": "abacus.abacus"
                }
            ]
        }
    ],
    "plugin_name": "Abacus",
    "popularity": {
        "monthly": 133,
        "daily": 994,
        "weekly": 238,
        "total": 1500
    },
    "server": "bukkit",
    "main": "org.ruhlendavis.mc.abacus.Abacus",
    "authors": [
        "Feaelin {iain@ruhlendavis.org}"
    ],
    "logo": "",
    "slug": "abacus",
    "categories": [
        "Informational"
    ],
    "stage": "Beta"
}*/


}
