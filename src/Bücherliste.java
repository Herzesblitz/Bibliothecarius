import java.util.ArrayList;
import java.util.List;

public class Bücherliste {
//Liste von Book aehnliche Buecher
//kann auch nur ein einfaches Linkobjekt sein, welches alle aehnlichen buecher enthaelt muss eine Baumstufe hoeher platziert werden

String thema;
List<String> title;
List<String> autor;
List<Buch> Liste_von_Informationen_der_Buecher; //TODO:muss noch erstellt werden
    public Bücherliste() {
        Liste_von_Informationen_der_Buecher=new ArrayList<Buch>();
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

<<<<<<< HEAD:src/Bücherliste.java

    public void addBuch(Buch buchmeta) {
=======
    public void addBuch(Book buchmeta) {
>>>>>>> cc5f96127ed96a06d1a558317e2bef6072198a8d:src/BookList.java
        if (!Liste_von_Informationen_der_Buecher.contains(buchmeta)) Liste_von_Informationen_der_Buecher.add(buchmeta);
    }
    
    public void removeBuchtitel(Buch titel){
        if(Liste_von_Informationen_der_Buecher.contains(titel)) Liste_von_Informationen_der_Buecher.remove(titel);
    }
}


