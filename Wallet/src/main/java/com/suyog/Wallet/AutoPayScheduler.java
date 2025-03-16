package com.suyog.Wallet;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class AutoPayScheduler {
	
    private SessionFactory sessionFactory;

    public AutoPayScheduler(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory; // Ensure sessionFactory is set
        scheduleAutoPayments();
    }

    public void scheduleAutoPayments() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                processAutoPayments();
            }
        }, 0, 24 * 60 * 60 * 1000); // Runs every 24 hours
    }

    private void processAutoPayments() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            List<Utility> utilities = session.createQuery(
                "FROM Utility WHERE autoPayEnabled = true AND dueDate = :day", Utility.class)
                .setParameter("day", dayOfMonth)
                .getResultList();

            for (Utility utility : utilities) {
                Wallet wallet = utility.getPaymentWallet();
                if (wallet.getBalance() >= utility.getAutoPayAmount()) {
                    wallet.setBalance(wallet.getBalance() - utility.getAutoPayAmount());
                    session.update(wallet);
                    UtilityPayment payment = new UtilityPayment(wallet, utility, utility.getAutoPayAmount(), new Date());
                    session.save(payment);
                    System.out.println("Auto-payment successful for " + utility.getUtilityType());
                } else {
                    System.out.println("Auto-payment failed due to insufficient balance for " + utility.getUtilityType());
                }
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void scheduleDueDateNotifications() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendDueDateNotifications();
            }
        }, 0, 24 * 60 * 60 * 1000); // Runs every 24 hours
    }

    private void sendDueDateNotifications() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        Session session = sessionFactory.openSession();
        List<Utility> upcomingUtilities = session.createQuery(
            "FROM Utility WHERE dueDate = :day", Utility.class)
            .setParameter("day", dayOfMonth + 3) // Notify 3 days before
            .getResultList();

        for (Utility utility : upcomingUtilities) {
            System.out.println("Reminder: Your " + utility.getUtilityType() + " bill is due in 3 days.");
        }
        session.close();
    }
}
