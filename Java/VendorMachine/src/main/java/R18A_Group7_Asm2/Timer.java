package R18A_Group7_Asm2;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {

    private static Timer timer;

    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private String time;

    private String currentFrame;

    private OwnerSummary ownerSummary;




    private Timer() {
        ownerSummary = OwnerSummary.getInstance();

        date = new Date();
        simpleDateFormat = new SimpleDateFormat("mm:ss");


        Thread thread = new Thread(this::run);
        thread.start();
    }

    public static Timer getInstance() {
        if(timer == null) {
            timer = new Timer();
        }

        return timer;
    }


    public void run() {
        javax.swing.Timer realTimer = new javax.swing.Timer(1000, this::timerListener);
        realTimer.start();
    }


    private void timerListener(ActionEvent e) {
        Date actualTime = new Date();
        time = simpleDateFormat.format(new Date(actualTime.getTime() - date.getTime()));
        System.out.println(time);

        if(time.equals("02:00")) {
            removeFrame(currentFrame);

            System.out.println("time over");
        }
    }

    public void reset() {
        date = new Date();
    }

    public void removeFrame(String name) {
        switch (name) {
            case "vend" :
                VendingMachine.reset();
                break;
            case "lvend" :
                UserLoggedVendingMachine.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "sign" :
                SignUp.frame.dispose();
                break;

            case "oa" :
                OwnerAdmin.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "os" :
                OwnerToSeller.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "oc" :
                OwnerToCashier.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "od" :
                OrderCompleted.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "log" :
                LogIn.frame.dispose();
                break;
            case "lc" :
                LoggedCheckOut.frame.dispose();
                LoggedCheckOut.addCancelledTransaction();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "ch" :
                CheckOut.frame.dispose();
                CheckOut.addCancelledTransaction();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "ca" :
                CashierAdmin.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
            case "se" :
                SellerAdmin.frame.dispose();
                VendingMachine.main(new String[]{});
                this.currentFrame = "vend";
                break;
        }

        reset();
    }

    public void setCurrentFrame(String currentFrame) {
        date = new Date();
        this.currentFrame = currentFrame;
    }


}
