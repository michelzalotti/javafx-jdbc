package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.util.InstantiateEntity;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn = null;
    private PreparedStatement pStat = null;
    private ResultSet rSet = null;
    private String sql = null;
    private Integer affectedRows = 0;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        sql = "INSERT INTO seller (name, email, birthdate, basesalary, departmentid) VALUES (?, ?, ?, ?, ?)";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, obj.getName());
            pStat.setString(2, obj.getEmail());
            pStat.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pStat.setDouble(4, obj.getBaseSalary());
            pStat.setInt(5, obj.getDepartment().getId());

            affectedRows = pStat.executeUpdate();

            if (affectedRows == 0)
                throw new DbException("Error! " + affectedRows + " affected rows.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public void update(Seller obj) {
        sql = "UPDATE seller SET name = ?, email = ?, birthdate = ?, basesalary = ?, departmentid = ? WHERE id = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setString(1, obj.getName());
            pStat.setString(2, obj.getEmail());
            pStat.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pStat.setDouble(4, obj.getBaseSalary());
            pStat.setInt(5, obj.getDepartment().getId());
            pStat.setInt(6, obj.getId());

            affectedRows = pStat.executeUpdate();

            if (affectedRows == 0)
                throw new DbException("Error! " + affectedRows + " affected rows.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        sql = "DELETE FROM seller WHERE id = ?";

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
    public Seller findById(Integer id) {
        Seller seller = new Seller();
        sql = "SELECT s.*, d.name AS dep_name FROM seller s INNER JOIN department d ON s.departmentid = d.id  WHERE id = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setInt(1, id);

            rSet = pStat.executeQuery();

            if (rSet.next()) {
                Department department = InstantiateEntity.createDepartment(rSet);
                seller = InstantiateEntity.createSeller(rSet, department);
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return seller;
    }

    @Override
    public List<Seller> findByDepartment(Department obj) {
        List<Seller> sellers = new ArrayList<>();
        sql = "SELECT s.*, d.name AS dep_name FROM seller s INNER JOIN department d ON s.departmentid = d.id WHERE s.departmentid = ?";

        try {
            pStat = conn.prepareStatement(sql);
            pStat.setInt(1, obj.getId());

            rSet = pStat.executeQuery();

            Map<Integer, Department> depMap = new HashMap<Integer, Department>();

            while (rSet.next()) {
                Department department = depMap.get(rSet.getInt("departmentid"));

                if (department == null) {
                    department = InstantiateEntity.createDepartment(rSet);
                    depMap.put(department.getId(), department);
                }

                sellers.add(InstantiateEntity.createSeller(rSet, department));
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return sellers;
    }

    @Override
    public List<Seller> findAll() {
        List<Seller> sellers = new ArrayList<>();
        sql = "SELECT s.*, d.name as dep_name FROM seller s INNER JOIN department d ON s.departmentid = d.id";

        try {
            pStat = conn.prepareStatement(sql);

            rSet = pStat.executeQuery();

            Map<Integer, Department> depMap = new HashMap<Integer, Department>();

            while (rSet.next()) {
                Department department = depMap.get(rSet.getInt("departmentid"));

                if (department == null) {
                    department = InstantiateEntity.createDepartment(rSet);
                    depMap.put(department.getId(), department);
                }

                sellers.add(InstantiateEntity.createSeller(rSet, department));
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return sellers;
    }

}
