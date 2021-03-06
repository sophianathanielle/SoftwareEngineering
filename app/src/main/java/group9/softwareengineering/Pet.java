package group9.softwareengineering;

public class Pet {

    private String id;
    private String name;
    private int age;
    private String bio;
    private String species;
    private String breed;
    private String photo_reference;
    private String owner_id;
    private boolean isSelected;


    public Pet() {

    }

    public Pet(String name, int age, String bio, String species, String breed, String photo_reference,String owner_id){
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.species = species;
        this.breed = breed;
        this.photo_reference = photo_reference;
        this.owner_id = owner_id;
        this.isSelected = false;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getID() {
        return id;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
