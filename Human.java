public class Human {
    private final String FirstName;
    private final String LastName;
    private  String dateOfBirth;
    private  int id;
    private String City;
    private String Gender;
    private String Photo;
    private boolean isBanned = false;

    public Human(String name){
        String[] a = name.split(" ");
        this.FirstName=a[0];
        this.LastName=name.replaceAll(a[0]+" ","");
    }

    public void setGender1(String value){
        this.Gender=value;
    }

    public void setGenderCsv(int value){
        if(value == 1)
            this.Gender ="Female";
        else if (value == 2)
            this.Gender ="Male";
        else this.Gender ="Null";
    }

    public String getGender(){
        return Gender;
    }

    public void setPhoto(String Photo){
        this.Photo =Photo;
    }

    public String getPhoto(){
        return Photo;
    }

    public void setIsBanned(boolean isBanned){
        this.isBanned =isBanned;
    }

    public boolean getisBanned(){
        return isBanned;
    }

    public void setdateOfBirth(String dateOfBirth){
        this.dateOfBirth =dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setCity(String City){
        this.City =City;
    }

    public String getCity(){
        return City;
    }

    public void setId(int id){
        this.id =id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return FirstName+" "+LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void showInf()
    {
        System.out.println("Name :" + FirstName+" "+LastName+" dateOfBirth :" + dateOfBirth);
    }
}
