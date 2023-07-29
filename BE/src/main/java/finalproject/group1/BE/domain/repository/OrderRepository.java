package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Order;
import finalproject.group1.BE.domain.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query("  select o " +
            " from Order o join OrderDetail od on o.id = od.order.id" +
            " where (:productName is null or od.product.name like %:productName% ) " +
            "       and (:sku is null or od.product.sku like %:sku% ) " +
            "       and (:orderId is null or o.displayId = :orderId ) " +
            "       and (:orderDate is null or o.orderDate = :orderDate ) " +
            "       and (:status is null or o.status = :status ) " +
            "       and (:userName is null or o.owner.userName like %:userName% ) " +
            "       and (:userId is null or o.owner.id = :userId  )" +
            " group by o.id ")
    List<Order> findOrderBySearchConditions(@Param("productName") String productName
            , @Param("sku") String sku , @Param("orderId") String orderId
            , @Param("orderDate") LocalDate orderDate , @Param("status") OrderStatus status
            , @Param("userName") String userName, @Param("userId") Integer userId, Pageable pageable);

    Optional<Order> findByIdAndDisplayId(int id, String displayId);

    Optional<Order> findByOwnerIdAndDisplayId(int ownerId, String displayId);
}
