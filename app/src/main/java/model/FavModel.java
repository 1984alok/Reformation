package model;

/**
 * Created by Alok on 14-05-2017.
 */
public class FavModel {
    private String name;

    public FavModel() {

    }

    public FavModel(String name, String name_de, String start, String date, String id, String addrss, String addrss_de, String catg, boolean isFav) {
        this.name = name;
        this.name_de = name_de;
        this.start = start;
        this.date = date;
        this.id = id;
        this.addrss = addrss;
        this.addrss_de = addrss_de;
        this.catg = catg;
        this.isFav = isFav;
    }

    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    private String name_de;
    private String start;
    private String date;



    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    private boolean isFav;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddrss() {
        return addrss;
    }

    public void setAddrss(String addrss) {
        this.addrss = addrss;
    }

    public String getCatg() {
        return catg;
    }

    public void setCatg(String catg) {
        this.catg = catg;
    }

    private String addrss;

    public String getAddrss_de() {
        return addrss_de;
    }

    public void setAddrss_de(String addrss_de) {
        this.addrss_de = addrss_de;
    }

    private String addrss_de;
    private String catg;
}
