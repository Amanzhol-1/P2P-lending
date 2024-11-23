package spring.p2plending.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.p2plending.service.PaymentService;

@Component
public class PaymentScheduler {

    private final PaymentService paymentService;

    @Autowired
    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //@Scheduled(cron = "0 0 9 * * *") // Каждый день в 9 утра
    @Scheduled(cron = "*/10 * * * * *") // Каждые 10 секунд TEST!!!
    public void scheduleUpcomingPaymentsCheck() {
        paymentService.checkUpcomingPayments();
    }
}
