package objectDB;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadym Volokhov
 */
@Entity
@Table(name = "tienda")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false, name = "direccion")
    private String direction;

    @Column(nullable = false, name = "ventas")
    private int sales;

    @ManyToMany
    private List<Employee> employees = new ArrayList<>();

    public Store() {
    }

    public Store(String direction, int sales) {
        this.direction = direction;
        this.sales = sales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
