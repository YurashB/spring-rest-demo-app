package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Passport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Transactional
    @Modifying
    @Query("update Employee e set e.passport = ?2 where e.id = ?1")
    void addPassportToEmployee(Integer employeeId, Passport passport);

    Employee findByName(String name);

    @NotNull
    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findByCountryContaining(String country, Pageable pageable);


    @Query(value = "select * from users join addresses on users.id = addresses.employee_id " +
            "where users.gender = :gender and addresses.country = :country", nativeQuery = true)
    List<Employee> findByGender(String gender, String country);

    /*
        Using SQL
        @Query(value = "select * from users u where u.country in ?1", nativeQuery = true)
        List<Employee> findByCountryList(Collection<String> countries);

        @Query(value = "select * from users u join addresses a on u.id = a.employee_id " +
                "where u.name = ?2 and a.city in ?1", nativeQuery = true)
        List<Employee> findByCityListAndName(Collection<String> cities, String name);

     */
    @Query(value = "select e from Employee e where e.country in :countries order by e.name asc ")
    List<Employee> findByCountryList(@Param("countries")Collection<String> countries);

    @Query(value = "select e from Employee e join e.addresses a " +
            "where e.name = :name and a.city in :cities " +
            "order by e.name asc")
    List<Employee> findByCityListAndName(@Param("cities")Collection<String> cities, @Param("name")String name);


}
