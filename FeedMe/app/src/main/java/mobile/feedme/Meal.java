package mobile.feedme;

/**
 * Created by Mateo on 16/03/2016.
 */
public class Meal {

    private String PersonneId;
    private String Titre;
    private String Description;
    private Double Latitude;
    private Double Longitude;
    private int     poid;
    private int     prix;

    public String getPersonneId() {
        return PersonneId;
    }

    public void setPersonneId(String personneId) {
        PersonneId = personneId;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public int getPoid() {
        return poid;
    }

    public void setPoid(int poid) {
        this.poid = poid;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
}
