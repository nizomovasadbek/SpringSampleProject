package net.jetsh.dao;

import net.jetsh.models.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private List<Person> people = new ArrayList<>();

    @Value("jdbc:sqlite:")
    private String url;

    @Value("D:/fetch.db")
    private String path;

    @Value("org.sqlite.JDBC")
    private String className;

    private Connection connection;
    private ResultSet resultSet;

    @PreDestroy
    public void finalization(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertQuery(Person person){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(String.format("INSERT INTO person VALUES(%d, '%s', %d, '%s');", person.getId(),
                    person.getName(), person.getAge(), person.getEmail()));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuery(int id, Person updatedPerson){
        try {
            Statement statement = connection.createStatement();
            statement.execute(String.format("UPDATE person SET name='%s', age=%d, email='%s' WHERE id=%d",
                    updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id));
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteQuery(int id){
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(String.format("DELETE FROM person WHERE id=%d", id));
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void connect(){
        try{
            Class.forName(className);
            connection = DriverManager.getConnection(url+path);
            getDatas();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDatas(){
        try{
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM person");
            while(resultSet.next()){
                int myId = resultSet.getInt("id");
                PEOPLE_COUNT = myId;
                people.add(new Person(myId, resultSet.getString("name")
                , resultSet.getInt("age"), resultSet.getString("email")));
            }
            statement.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateList(){
        people.clear();
        getDatas();
    }

    public List<Person> index(){
        return people;
    }

    public Person show(int id){
        return people.stream().filter(person->person.getId()==id).findAny().orElse(null);
    }

    public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
        insertQuery(person);
    }

    public void update(int id, Person updatedPerson){
        Person personToBeUpdated = show(id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
        updateQuery(id, updatedPerson);
    }

    public void delete(int id){
        people.removeIf(p->p.getId()==id);
        deleteQuery(id);
    }
}
