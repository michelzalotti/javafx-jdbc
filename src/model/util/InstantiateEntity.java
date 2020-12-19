package model.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.entities.Department;
import model.entities.Seller;

public class InstantiateEntity {

    public static Department createDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("departmentid"));
        department.setName(result.getString("dep_name"));

        return department;
    }

    public static Seller createSeller(ResultSet result, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(result.getInt("id"));
        seller.setName(result.getString("name"));
        seller.setEmail(result.getString("email"));
        seller.setBirthDate(result.getDate("birthdate"));
        seller.setBaseSalary(result.getDouble("basesalary"));
        seller.setDepartment(department);

        return seller;
    }

}
