package org.anhtran.mymail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.anhtran.mymail.R;
import org.anhtran.mymail.mail.MailItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by anhtran on 3/24/17.
 */

public class MailAdapter extends ArrayAdapter {
    private Map<Integer,Integer> messageNumbers = new HashMap<>();

    public MailAdapter(Context context, ArrayList<MailItem> mailItems) {
        super(context, 0, mailItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mailItemView = convertView;
        if (mailItemView == null) {
            mailItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.email_list_item, parent, false);
        }

        MailItem currentMailItem = (MailItem) getItem(position);

        TextView listItemSubject =
                (TextView) mailItemView.findViewById(R.id.list_item_subject);
        listItemSubject.setText(currentMailItem.getSubject());

        TextView listItemSender =
                (TextView) mailItemView.findViewById(R.id.list_item_sender_name);
        listItemSender.setText(currentMailItem.getFrom());

        TextView listItemContent =
                (TextView) mailItemView.findViewById(R.id.list_item_time);
        listItemContent.setText(currentMailItem.getReceivedDate());

        TextView listItemFirstLetter =
                (TextView) mailItemView.findViewById(R.id.list_item_first_letter);
        listItemFirstLetter.setText(currentMailItem.getFrom().substring(0,1).toUpperCase());

        messageNumbers.put(position, currentMailItem.getMessageNumber());
        return mailItemView;
    }

    public Map<Integer, Integer> getMsgNumbers () {
        return messageNumbers;
    }
}
