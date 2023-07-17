package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Order;
import finalproject.group1.BE.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query("  select o " +
            " from Order o , OrderDetail od " +
            " where o.id = od.order.id and (od.product.name like %:productName% or :productName is null) " +
            "       and (od.product.sku like %:sku% or :sku is null) " +
            "       and (o.displayId = :orderId or :orderId is null) " +
            "       and (o.orderDate = :orderDate or :orderDate is null) " +
            "       and (o.status = :status or :status is null) " +
            "       and (o.owner.userName = :userName or :userName is null) " +
            "       and (o.owner.id = :userId or :userId is null ) ")
    List<Order> findOrderBySearchConditions(@Param("productName") String productName
            , @Param("sku") String sku , @Param("orderId") String orderId
            , @Param("orderDate") LocalDate orderDate , @Param("status") OrderStatus status
            , @Param("userName") String userName,@Param("userId") Integer userId);
}
