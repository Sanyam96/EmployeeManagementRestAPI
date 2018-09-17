package com.nagarro.repository;

import com.nagarro.models.Employee;
import com.nagarro.utils.SessionUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Layer to do all db transaction
 * @author Sanyam Goel created on 16/9/18
 */
@Repository
public class EmployeeRepository {

    /**
     * Function to get all data from Employee table
     * @return list of employees
     */
    @SuppressWarnings("unchecked")
    public List<Employee> getEmployees() {
        List<Employee> employees = null;

        Session session = SessionUtil.getSession();
//        Query query = session.createQuery("from Employee");
//        employees = query.list();
        try {
            employees = session.createCriteria(Employee.class).list();
        } catch (HibernateException he) {
            System.out.println("Hibernate Exception Found" + he);
        }
        session.close();
        return employees;
    }

    /**
     * Function to retrieve single employee data from db
     * @param empCode
     * @return single employee data
     */
    public Employee getEmployee(long empCode) {
        Session session = SessionUtil.getSession();
        Employee employee1 = null;
        try {
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.eq("employeeCode", empCode));
            employee1 = (Employee) criteria.uniqueResult();
        } catch (HibernateException he) {
            System.out.println("Hibernate Exception Found" + he);
        }
        session.close();
        return employee1;
    }

    /**
     * function to insert data into db
     * @param employee
     */
    public void addEmployee(Employee employee) {
        Session session = SessionUtil.getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.save(employee);
            tx.commit();
        } catch (HibernateException he) {
            System.out.println("Hibernate Exception Found" + he);
        }
        session.close();
    }

    /**
     * function to update data in employee table
     * @param employee
     */
    public void updateEmployee(Employee employee) {
        Session session = SessionUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.eq("employeeCode", employee.getEmployeeCode()));
            Employee employee1 = (Employee) criteria.uniqueResult();
            tx.commit();
            employee.setId(new Long(employee1.getId()));
            session.clear();
            session.beginTransaction();
            session.update(employee);
            tx.commit();
        } catch (HibernateException he) {
            System.err.println("Hibernate Exception FOUND!! ->  " + he);
        }

        session.close();
    }
}
