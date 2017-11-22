import java.util.ArrayList;
import java.util.List;

public class BookList {
//Liste von Book aehnliche Buecher
//kann auch nur ein einfaches Linkobjekt sein, welches alle aehnlichen buecher enthaelt muss eine Baumstufe hoeher platziert werden

String thema;
List<String> title;
List<String> autor;
List<Book> Liste_von_Informationen_der_Buecher; //TODO:muss noch erstellt werden
    public BookList() {
        Liste_von_Informationen_der_Buecher=new ArrayList<Book>();
        title =new ArrayList<String>();
        autor =new ArrayList<String>();
        thema="";
    }

    public void setBuchtitel(List<String> titleliste) {
        title.addAll(titleliste) ;
    }

    public void addBuchtitel(String genre) {
        if (!title.contains(genre))  title.add(genre);
    }
    public void removeBuchtitel(String titel){
        if(title.contains(titel)) title.remove(titel);
    }

    public void angleichenBuchtitle(){
    String titlename="";
        for (int i = 0; i <title.size() ; i++)
        {
            if(!title.get(i).equals(titlename=Liste_von_Informationen_der_Buecher.get(i).title))
            {
                //ersetzen
                title.set(i,titlename);
            }
        }
    }

    public void setAutor(List<String> titleliste) {
        autor.addAll(titleliste) ;
    }

    public void addAutor(String autor) {
        if (!this.autor.contains(autor)) this.autor.add(autor);
    }
    
    public void removeAutor(String autor){
        if(this.autor.contains(autor)) this.autor.remove(autor);
    }


    public void addBuch(Book buchmeta) {
        if (!Liste_von_Informationen_der_Buecher.contains(buchmeta)) Liste_von_Informationen_der_Buecher.add(buchmeta);
    }
    
    public void removeBuchtitel(Book titel){
        if(Liste_von_Informationen_der_Buecher.contains(titel)) Liste_von_Informationen_der_Buecher.remove(titel);
    }
}


