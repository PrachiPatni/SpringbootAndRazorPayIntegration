package com.prachi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prachi.dto.StudentOrder;

public interface StudentOrderRepo extends JpaRepository<StudentOrder, Long> {
public StudentOrder findByRazorPayId(String orderId);
}
