package keithcod.es.mineget.wrappers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import keithcod.es.mineget.MineGet;

import java.io.IOException;
import java.net.MalformedURLException;

public class Spiget {

    public class ListItem {
        public int id;
        public String name;
        public String tag;
        public String contributors;
        public String description;
        public int likes;
        public class File {
            public String type;
            public Float size;
            public String sizeUnit;
            public String url;
        }
        public File file;
        public String[] testedVersions;

        //public Object[] links; //!!

        public class Rating {
            public int count;
            public Float average;
        }
        public Rating rating;

        public int releaseDate;
        public int updateDate;
        public int downloads;
        public boolean external;

        public class Icon {
            public String url;
            public String data;
        }

        public Icon icon;

        public class Review {
            public class Author {
                public int id;
                public String name;
                public Icon icon;
            }
            public Author author;
            public Rating rating;
            public String message;
            public String responceMessage;
            public String version;
            public int date;
        }
        public Review[] reviews;
    }

    public static final String spigetURL = "https://api.spiget.org/v2/";

    public static ListItem[] List() {
    return List(20, 0);
    }

    public static ListItem[] List(int size, int page){


        try {
            String json = MineGet.GetJSON(spigetURL + "resources?page=" + page + "&size=" + size);
            if (json != null){
                Gson gson = new Gson();
                ListItem[] items = gson.fromJson(json, ListItem[].class);
                return items;
            }

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }

        return null;
    }
    public static ListItem[] Search(String query) {
        return Search(query, 10, 0);
    }

    public static ListItem[] Search(String query, int size, int page){

        try {
            String json = MineGet.GetJSON(spigetURL + "search/resources/"+query+"?page=" + page + "&size=" + size);
            if(json != null) {
                Gson gson = new Gson();
                ListItem[] items = gson.fromJson(json, ListItem[].class);
                return items;
            }

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }

        return null;
    }

    public static ListItem Get(int id){
        try {
            String json = MineGet.GetJSON(spigetURL + "resources/"+id);
            if(json != null) {
                Gson gson = new Gson();
                ListItem items = gson.fromJson(json, ListItem.class);
                return items;
            }else{
                if(MineGet.code == 500){
                    JsonElement jelement = new JsonParser().parse(MineGet.json);
                    JsonObject jobject = jelement.getAsJsonObject();
                    jobject = jobject.getAsJsonObject("error");
                    if(jobject != null){
                        System.out.println("Error: " + jobject.getAsString());
                    }
                }
            }

        }catch(MalformedURLException ex){

        }catch(IOException ex) {

        }

        return null;
    }

}
