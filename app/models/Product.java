package models;

import javax.persistence.*;

import com.avaje.ebean.*;


@Entity
public class Product extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Integer id;

    public String name;

    public String description;
    
    public Product() {}

    public Product(String name) {
        this.name = name;
    }
}
