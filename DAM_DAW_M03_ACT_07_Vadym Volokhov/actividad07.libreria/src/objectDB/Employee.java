package objectDB;

import javax.persistence.*;
import java.util.List;

/**
 * @author Vadym Volokhov
 */
@Entity
@Table(name = "empleado")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false, name = "nombre")
    private String firstName;

    @Column(nullable = false, name = "apellido")
    private String lastName;

    @ManyToMany
    private List<Store> stores;

    public Employee() {
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        return String.format("%d - %s %s", id, firstName, lastName);
    }
}
