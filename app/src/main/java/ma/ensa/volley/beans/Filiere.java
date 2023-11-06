package ma.ensa.volley.beans;

public class Filiere {

    private int id;
    private String code;
    private String name;

    public Filiere(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }


    public Filiere(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcode() {
        return code;
    }

    public void setcode(String code) {
        this.code = code;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
