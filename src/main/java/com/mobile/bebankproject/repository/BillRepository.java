package com.mobile.bebankproject.repository;

import com.mobile.bebankproject.model.Bill;
import com.mobile.bebankproject.model.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    List<Bill> findByBillStatusAndUserIdOrderByCreatedDateDesc(BillStatus billStatus, int userId);

    Bill findByBillCode(String billCode);

    List<Bill> findAllByOrderByCreatedDateDesc();

    List<Bill> findByUserIdOrderByCreatedDateDesc(Integer userId);


}