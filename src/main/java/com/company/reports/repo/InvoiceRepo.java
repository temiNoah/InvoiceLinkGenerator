package com.company.reports.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.company.reports.model.Invoice;

public interface InvoiceRepo extends CrudRepository<Invoice,String>  {
	Optional<Invoice> findByDocUuid(String docUuid);
}
