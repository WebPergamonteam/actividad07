/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package actividad07.libreria;


import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


/**
 * @author Vadym Volokhov
 */
public class LibreriaMain {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        Connection connection = MySQLConnection.getConnection();
        while (!exit) {
            System.out.println("Que queres hacer?");
            System.out.println("1 - restableser la base de datos");
            System.out.println("2 - mostrar autores");
            System.out.println("3 - mostrar libros");
            System.out.println("4 - modificar un autor");
            System.out.println("5 - eliminar un libro");
            System.out.println("0 - salir");
            int option = scanner.nextInt();
            scanner.nextLine(); // borrar búfer


            switch (option) {
                case 1:
                    resetDatabase(connection);
                    break;
                case 2:
                    showAuthors(connection);
                    break;
                case 3:
                    showBooks(connection);
                    break;
                case 4:
                    modifyAuthor(connection, scanner);
                    break;
                case 5:
                    deleteBook(connection, scanner);
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
        connection.close();
    }

    private static void resetDatabase(Connection connection) throws SQLException {
        // Eliminar base de datos si ya existe
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS actividad07");
        }

        // Crear base de datos
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE actividad07");
        }

        // Usar base de datos
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("USE actividad07");
        }

        // crear una tabla para almacenar información sobre los autores
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE autor ("
                            + "id INT NOT NULL AUTO_INCREMENT,"
                            + "nombre VARCHAR(255) NOT NULL,"
                            + "apellidos VARCHAR(255) NOT NULL,"
                            + "PRIMARY KEY (id)"
                            + ")"
            );
        }

        // Crear una tabla para almacenar información sobre libros.
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE libro ("
                            + "id INT NOT NULL AUTO_INCREMENT,"
                            + "titulo VARCHAR(255) NOT NULL,"
                            + "autor_id INT NOT NULL,"
                            + "pais VARCHAR(255) NOT NULL,"
                            + "paginas INT NOT NULL,"
                            + "genero VARCHAR(255) NOT NULL,"
                            + "PRIMARY KEY (id),"
                            + "FOREIGN KEY (autor_id) REFERENCES autor(id)"
                            + ")"
            );
        }

        // Agregar publicaciones de apertura para autores
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO autor (nombre, apellidos) VALUES ('Manuel', 'Escribano')");
            statement.executeUpdate("INSERT INTO autor (nombre, apellidos) VALUES ('Juana', 'Escritor')");
        }

        // Agregar entradas de apertura para libros
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "INSERT INTO libro (titulo, autor_id, pais, paginas, genero) "
                            + "VALUES ('La casa de papel', 1, 'España', 300, 'Thriller')"
            );
            statement.executeUpdate(
                    "INSERT INTO libro (titulo, autor_id, pais, paginas, genero) "
                            + "VALUES ('El tiempo entre costuras', 2, 'España', 400, 'Histórico')"
            );
        }

        System.out.println("Base de datos creada y reiniciada correctamente");
    }

    private static void showAuthors(Connection connection) {
        try {
            // Crear una consulta para obtener todos los autores
            String query = "SELECT * FROM autor";

            // Crear declaración y ejecutar consulta
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Mostrar una lista de autores
            System.out.println("---- Listado de autores ----");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("nombre");
                String lastName = resultSet.getString("apellidos");
                System.out.printf("%d - %s %s\n", id, firstName, lastName);
            }

            // Cerrar declaración y conjunto de resultados
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de autores: " + e.getMessage());
        }
    }


    private static void showBooks(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM libro");
            System.out.println("---- Listado de libros ----");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("titulo");
                int authorId = resultSet.getInt("autor_id");
                String country = resultSet.getString("pais");
                int pages = resultSet.getInt("paginas");
                String genre = resultSet.getString("genero");
                System.out.printf("%d - %s (Autor: %s, País: %s, Páginas: %d, Género: %s)%n", id, title, authorId, country, pages, genre);
            }
            System.out.println();

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener el listado de libros: " + e.getMessage());
        }
    }


    private static void modifyAuthor(Connection connection, Scanner scanner) throws SQLException {
        showAuthors(connection);

        System.out.print("\nIndica el id del autor a modificar: ");
        int authorId = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        Author author = getAuthor(connection, authorId);
        if (author == null) {
            System.out.println("No existe un autor con id " + authorId);
            return;
        }

        System.out.println("\nIndica que modificar:");
        System.out.println("1 - nombre");
        System.out.println("2 - apellidos");
        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        String newValue = null;
        switch (option) {
            case 1:
                System.out.print("\nIndica el nuevo valor: ");
                newValue = scanner.nextLine();
                author.setFirstName(newValue);
                break;
            case 2:
                System.out.print("\nIndica el nuevo valor: ");
                newValue = scanner.nextLine();
                author.setLastName(newValue);
                break;
            default:
                System.out.println("\nOpción inválida");
                return;
        }

        updateAuthor(connection, author);
    }

    private static void deleteBook(Connection connection, Scanner scanner) {
        try {
            // Obtener una lista de libros de una base de datos y mostrarla
            showBooks(connection);

            // Solicitar al usuario el ID del libro que se va a eliminar
            System.out.print("\nIndica el id del libro a eliminar: ");
            int bookId = scanner.nextInt();

            // Eliminar libro de la base de datos
            String deleteQuery = "DELETE FROM libro WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nLibro eliminado correctamente.");
            } else {
                System.out.println("\nNo se pudo eliminar el libro. Comprueba el ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el libro: " + e.getMessage());
        }
    }

    private static Author getAuthor(Connection connection, int authorId)  {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM autor where id = ?");
            statement.setInt(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("nombre");
                String lastName = resultSet.getString("apellidos");
                return new Author(id, firstName, lastName);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el autor: " + e.getMessage());
        }
        return null;
    }

    static void updateAuthor(Connection connection, Author author) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE autor set nombre = ?, apellidos = ? where id = ?");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setInt(3, author.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("\nAutor modificado correctamente.");
            } else {
                System.out.println("\nNo se pudo modificar el autor.");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar el autor: " + e.getMessage());
        }
    }
}
