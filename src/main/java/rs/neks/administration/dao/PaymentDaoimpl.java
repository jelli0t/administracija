package rs.neks.administration.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import rs.neks.administration.model.Payment;

@Repository(value = "paymentDao")
public class PaymentDaoimpl extends CommonRepositoryImp<Payment>
	implements PaymentDao {

	
}