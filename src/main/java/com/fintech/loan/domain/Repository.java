package com.fintech.loan.domain;

import java.util.List;

public interface Repository<T, ID> {
void save (T entity);
T findybyID (ID id);
List<T> findall();

    Loan findById(Long id);

    List<Loan> findAll();

    int count ();
}