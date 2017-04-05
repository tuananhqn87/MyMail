package org.anhtran.mymail.utils;

import java.util.Comparator;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Created by anhtran on 3/25/17.
 */

public class DateCompare {

    private DateCompare() {
    }

    public static class DescendingOrderCompare implements Comparator<Message> {
        @Override
        public int compare(Message o1, Message o2) {
            try {
                //
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public static class AscendingOrderCompare implements Comparator<Message> {
        @Override
        public int compare(Message o1, Message o2) {
            try {
                //
                return o1.getReceivedDate().compareTo(o2.getReceivedDate());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

}
