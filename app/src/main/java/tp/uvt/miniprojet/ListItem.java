package tp.uvt.miniprojet;

/**
 * Created by Dell on 27/10/2017.
 */

public class ListItem {
    public  int Id;
    public  String Name;
    public  String Description;
    public  String Date_operation;
    ListItem(int Id, String Name, String Description, String Date_operation)
    {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Date_operation = Date_operation;
    }
}