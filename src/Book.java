import java.util.ArrayList;
import java.util.List;

public class Book{
        public String title;
        public String author;
        public String publisher;
        public String covertext;
        public String blurb; //Klappentext
        public double rating;
        public List<String> Genre;
        public List<String> similar_Books;
        public List<String> Characters;
        public List<String> Author;

        public Book (String title, String author, String publisher, String blurb, double rating) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.blurb = blurb;
            this.rating = rating;
        }
    public Book () {
        this.title = "";
        this.author = "";
        this.publisher = "";
        this.blurb = "";
        this.rating = 0;
        this.Genre=new ArrayList<String>();
        this.similar_Books=new ArrayList<String>();
        this.Characters=new ArrayList<String>();
        this.Author=new ArrayList<String>();
    }

    public void setAuthor(List<String> autor) {
        Author.addAll(autor) ;
    }

    public void addAuthor(String autor) {
        if (!Author.contains(autor))
        {
            Author.add(autor);
        }
    }
    public void removeAuthor(String autor){
        if(Author.contains(autor))
        {
            Author.remove(autor);
        }

    }




    public void setGenre(List<String> genre) {
        Genre.addAll(genre) ;
    }

    public void addGenre(String genre) {
        if (!Genre.contains(genre))
        {
            Genre.add(genre);
        }
    }
    public void removeGenre(String genre){
        if(Genre.contains(genre))
        {
            Genre.remove(genre);
        }

    }

    public void setSimilar_Books(List<String> similarBuch) {
        similar_Books.addAll(similarBuch) ;
    }

    public void addAehnlichBuch(String bookname) {
        if (!similar_Books.contains(bookname))
        {
            similar_Books.add(bookname);
        }
    }
    public void removeAehnlichBuch(String bookname){
        if(Genre.contains(bookname))
        {
            Genre.remove(bookname);
        }

    }

    public void setCharacters(List<String> Characters) {
        similar_Books.addAll(Characters) ;
    }

    public void addCharacter(String Character) {
        if (!Characters.contains(Character))
        {
            Characters.add(Character);
        }
    }
    public void removeCharacter(String Character){
        if(Genre.contains(Characters))
        {
            Genre.remove(Characters);
        }

    }
}



class BookList {
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
        if (!title.contains(genre))
        {
            title.add(genre);
        }
    }
    public void removeBuchtitel(String titel){
        if(title.contains(titel))
        {
            title.remove(titel);
        }

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
        if (!this.autor.contains(autor))
        {
            this.autor.add(autor);
        }
    }
    public void removeAutor(String autor){
        if(this.autor.contains(autor))
        {
            this.autor.remove(autor);
        }

    }


    public void addBuch(Book buchmeta) {
        if (!Liste_von_Informationen_der_Buecher.contains(buchmeta))
        {
            Liste_von_Informationen_der_Buecher.add(buchmeta);
        }
    }
    public void removeBuchtitel(Book titel){
        if(Liste_von_Informationen_der_Buecher.contains(titel))
        {
            Liste_von_Informationen_der_Buecher.remove(titel);
        }

    }
}


