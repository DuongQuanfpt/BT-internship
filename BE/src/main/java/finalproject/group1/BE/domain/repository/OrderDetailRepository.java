package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderId(int id);

    long countByProductId(int id);

    long countByProductIdAndOrderOrderDateAfter(int id , LocalDate orderDate);

    @Query("Select COALESCE(SUM(od.quantity),0) " +
            "from OrderDetail od where od.product.id = :productid")
    int sumQuantityByProductId(int productid);

    @Query("Select COALESCE(SUM(od.quantity),0) " +
            "from OrderDetail od where " +
            "od.product.id = :productid and " +
            "od.order.orderDate >= :date")
    int sumQuantityByProductIdAndAfterDate(int productid,LocalDate date);
}
