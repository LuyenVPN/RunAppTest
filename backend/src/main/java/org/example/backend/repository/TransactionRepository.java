
package org.example.backend.repository;

import org.example.backend.model.entity.Transaction;
import org.example.backend.model.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @EntityGraph(attributePaths = {"user", "category"})
    Optional<Transaction> findByIdAndFamilyId(Long id, Long familyId);

    @Override
    @EntityGraph(attributePaths = {"user", "category"})
    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    /**
     * Kiểm tra gia đình có giao dịch trong khoảng thời gian hay không.
     * Dùng để phân biệt không có dữ liệu với tổng giao dịch bằng 0.
     */
    boolean existsByFamilyIdAndTransactionDateBetween(
            Long familyId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("select coalesce(sum(t.amount), 0) from Transaction t "
            + "where t.family.id = :familyId and t.category.type = :type "
            + "and t.transactionDate between :startDate and :endDate")
    BigDecimal sumByType(
            @Param("familyId") Long familyId,
            @Param("type") CategoryType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @EntityGraph(attributePaths = {"category", "user"})
    @Query("select t from Transaction t "
            + "where t.family.id = :familyId "
            + "and t.transactionDate between :startDate and :endDate")
    List<Transaction> findAllByFamilyIdAndDateBetween(
            @Param("familyId") Long familyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @EntityGraph(attributePaths = "category")
    @Query("select t from Transaction t "
            + "where t.family.id = :familyId and t.category.type = :type "
            + "and t.transactionDate between :startDate and :endDate "
            + "order by t.amount desc, t.transactionDate desc, t.id desc")
    List<Transaction> findTopExpensesByFamilyAndDateBetween(
            @Param("familyId") Long familyId,
            @Param("type") CategoryType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"user", "category"})
    List<Transaction> findTop10ByFamilyIdOrderByTransactionDateDescIdDesc(Long familyId);

    @Transactional
    @Modifying
    @Query("update Transaction t set t.receiptImage = :url "
            + "where t.id = :id and t.family.id = :familyId")
    int updateReceiptImage(
            @Param("id") Long id,
            @Param("familyId") Long familyId,
            @Param("url") String url
    );

    @Modifying(flushAutomatically = true)
    @Query("delete from Transaction t where t.family.id = :familyId")
    int deleteAllByFamilyId(@Param("familyId") Long familyId);
}
