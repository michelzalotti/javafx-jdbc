package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.util.InstantiateEntity;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn = null;
    private PreparedStatement pStat = null;
    private ResultSet rSet = null;
    private String sql = null;
    private Integer affectedRows = 0;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        sql = "INSERT INTO department (name) VALUES (?)";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, obj.getName());

            affectedRows = pStat.executeUpdate();

            if (affectedRows == 0)
                throw new DbException("Error! " + affectedRows + " affected rows.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {
        sql = "UPDATE department SET name = ? WHERE id = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, obj.getName());
            pStat.setInt(2, obj.getId());

            affectedRows = pStat.executeUpdate();

            if (affectedRows == 0)
                throw new DbException("Error! " + affectedRows + " affected rows.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        sql = "DELETE FROM department WHERE id = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setInt(1, id);

            affectedRows = pStat.executeUpdate();

            if (affectedRows == 0)
                throw new DbException("Error! " + affectedRows + " affected rows.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        Department department = null;
        sql = "SELECT id AS departmentid, name As dep_name FROM department WHERE id = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setInt(1, id);

            rSet = pStat.executeQuery();

            if (rSet.next())
                department = InstantiateEntity.createDepartment(rSet);

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return department;
    }

    @Override
    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        sql = "SELECT id AS departmentid, name As dep_name FROM department";

        try {
            pStat = conn.prepareStatement(sql);
            rSet = pStat.executeQuery();

            while (rSet.next())
                departments.add(InstantiateEntity.createDepartment(rSet));

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return departments;
    }
}
