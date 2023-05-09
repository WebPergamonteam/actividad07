/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objectDB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Vadym Volokhov
 */
public class GestionaTiendas {

    public static void main(String[] args) {
        String dbName = String.format("objectdb:$objectdb/db/points_%d.odb", new Random().nextInt());
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dbName);
        EntityManager entityManager = emf.createEntityManager();

        init(entityManager);

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Introduzca la operacion a realizar del seguente menu de opciones:");
            System.out.println("1 - Muestra los empleados");
            System.out.println("2 - Muestra las tiendas");
            System.out.println("3 - Mostrar tiendas ordenadas por ventas");
            System.out.println("4 - Modificar un empleado");
            System.out.println("5 - Anade una tiendas");
            System.out.println("0 - salir");
            int option = scanner.nextInt();
            scanner.nextLine(); // borrar búfer

            switch (option) {
                case 1:
                    showEmployees(entityManager);
                    break;
                case 2:
                    showStores(entityManager);
                    break;
                case 3:
                    showStoresBySalesOrdering(scanner, entityManager);
                    break;
                case 4:
                    modifyEmployee(scanner, entityManager);
                    break;
                case 5:
                    createStore(scanner, entityManager);
                    break;
                case 0:
                    exit = true;
                    System.out.println("Adios!");
                    break;
                default:
                    System.out.println("Opcion no valida, por favor intenta de nuevo.");
                    break;
            }
        }

        entityManager.close();
        emf.close();
    }

    private static void createStore(Scanner scanner, EntityManager entityManager) {
        System.out.println("---- Anade una tienda ----");
        System.out.println("Direccion de la tienda:");
        String direction = scanner.nextLine();

        System.out.println("Ventas:");
        int sales = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        Store store = new Store();
        store.setDirection(direction);
        store.setSales(sales);

        entityManager.getTransaction().begin();
        entityManager.persist(store);
        entityManager.getTransaction().commit();

        showEmployees(entityManager);

        boolean addEmployeeFlag = true;
        while (addEmployeeFlag) {
            System.out.println("Index del empleado que quieras anadir a la tienda:");
            int employeeId = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            Employee employee = getEmployee(entityManager, employeeId);
            if (employee == null) {
                System.out.println("No existe un empleado con id " + employeeId);
                return;
            }

            if (store.getEmployees() == null) {
                store.setEmployees(Collections.singletonList(employee));
            } else {
                store.getEmployees().add(employee);
            }

            entityManager.getTransaction().begin();
            entityManager.persist(store);
            entityManager.getTransaction().commit();

            System.out.println("Empleado anadido correctamente");

            System.out.println("Queres anadir un nuevo empleado:");
            System.out.println("1 - Si");
            System.out.println("2 - No");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            switch (option) {
                case 1:
                    break;
                case 2:
                    addEmployeeFlag = false;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor intenta de nuevo.");
                    break;
            }
        }
    }

    private static void showStores(EntityManager entityManager) {
        System.out.println("---- Listado de tiendas ----");
        TypedQuery<Store> storeQuery = entityManager.createQuery("SELECT s from Store s", Store.class);
        List<Store> stores = storeQuery.getResultList();
        for (Store store : stores) {
            System.out.printf("%d - direccion: %s, ventas: %s, empleados: %s", store.getId(), store.getDirection(), store.getSales(), store.getEmployees());
            System.out.println();
        }
        System.out.println("\n");
    }

    private static void showStoresBySalesOrdering(Scanner scanner, EntityManager entityManager) {
        System.out.println("Ordenar segun ventas Ascendiente o Descendiente?");
        System.out.println("1 - Ascendiente");
        System.out.println("2 - Descendiente");
        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        TypedQuery<Store> storeQuery;
        switch (option) {
            case 1:
                System.out.println("Mostrar tiendas por ventas ascendiente: ");
                storeQuery = entityManager.createQuery("SELECT s FROM Store s ORDER BY sales", Store.class);
                break;
            case 2:
                System.out.println("Mostrar tiendas por ventas descendiente: ");
                storeQuery = entityManager.createQuery("SELECT s FROM Store s ORDER BY sales DESC", Store.class);
                break;
            default:
                System.out.println("\nOpción inválida");
                return;

        }
        System.out.println();

        List<Store> stores = storeQuery.getResultList();
        for (Store store : stores) {
            System.out.printf("%d - direccion: %s, ventas: %s, empleados: %s", store.getId(), store.getDirection(), store.getSales(), store.getEmployees());
            System.out.println();
        }
        System.out.println();
    }

    private static void showEmployees(EntityManager entityManager) {
        System.out.println("---- Listado de empleados ----");
        TypedQuery<Employee> employeeQuery = entityManager.createQuery("SELECT e from Employee e", Employee.class);
        List<Employee> employees = employeeQuery.getResultList();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
        System.out.println();
    }

    private static void modifyEmployee(Scanner scanner, EntityManager entityManager) {
        showEmployees(entityManager);

        System.out.println("Id del empleado a modificar:");
        int employeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        Employee employee = getEmployee(entityManager, employeeId);
        if (employee == null) {
            System.out.println("No existe un empleado con id " + employeeId);
            return;
        }

        System.out.println("Indica el atributo a modificar:");
        System.out.println("1 - Nombre");
        System.out.println("2 - Apellidos");
        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        String newValue = null;
        switch (option) {
            case 1:
                System.out.print("\nIndica el nuevo valor: ");
                newValue = scanner.nextLine();
                employee.setFirstName(newValue);
                break;
            case 2:
                System.out.print("\nIndica el nuevo valor: ");
                newValue = scanner.nextLine();
                employee.setLastName(newValue);
                break;
            default:
                System.out.println("\nOpción inválida");
                return;
        }

        entityManager.getTransaction().begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
    }

    private static Employee getEmployee(EntityManager entityManager, int employeeId) {
        return entityManager.find(Employee.class, employeeId);
    }

    private static void init(EntityManager entityManager) {
        Employee firstEmployee = new Employee("Black", "Orchid");
        Employee secondEmployee = new Employee("Voldo", "Soul");
        Employee thirdEmployee = new Employee("Blanca", "Street");

        Store firstStore = new Store("Calle Elm 84", 100);
        Store secondStore = new Store("Calle Malasana 32", 90);
        Store thirdStore = new Store("Calle Cloverfield 10", 80);

        entityManager.getTransaction().begin();

        entityManager.persist(firstEmployee);
        entityManager.persist(secondEmployee);
        entityManager.persist(thirdEmployee);

        firstStore.setEmployees(Collections.singletonList(firstEmployee));
        secondStore.setEmployees(Collections.singletonList(secondEmployee));
        thirdStore.setEmployees(Collections.singletonList(thirdEmployee));

        entityManager.persist(firstStore);
        entityManager.persist(secondStore);
        entityManager.persist(thirdStore);

        entityManager.getTransaction().commit();
    }
}
