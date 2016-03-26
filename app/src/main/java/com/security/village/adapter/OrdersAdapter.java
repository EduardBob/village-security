package com.security.village.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.security.village.R;
import com.security.village.settingsholder.Keys;
import com.security.village.webservice.retrofit.ApiNew;
import com.security.village.webservice.retrofit.response.Orders;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fruitware on 12/24/15.
 */
public class OrdersAdapter extends ArrayAdapter {

    public static final int ALL = 0;
    public static final int CUT = 1;

    private Context context;
    private Class clasS;
    private List<Orders.Data> mOrdersList;
    private OnOrderClickListener mOnOrderClickListener;
    private boolean allOrders = false;
    private int style = ALL;

    public OrdersAdapter(Context context, int resource, boolean allOrders, int style) {
        super(context, R.layout.order);
        this.style = style;
        this.context = context;
        this.allOrders = allOrders;
    }

    public void setListOfData(List<Orders.Data> list) {
        mOrdersList = list;
    }

    public void setActivityClass(Class clasS) {
        this.clasS = clasS;
    }

    public void setListener(OnOrderClickListener listener) {
        mOnOrderClickListener = listener;
    }

    @Override
    public int getCount() {
        if (mOrdersList == null)
            return 0;
        return mOrdersList.size();
    }

    @Override
    public Orders.Data getItem(int position) {
        return mOrdersList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        ViewHolder holder = null;
        final Orders.Data order = getItem(pos);

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.order, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText("#" + order.getId());
        holder.whoComing.setEllipsize(TextUtils.TruncateAt.END);
        holder.whoBooked.setEllipsize(TextUtils.TruncateAt.END);
        holder.whoComing.setText(order.getComment());

        if (order.getAdded_from() == null || order.getAdded_from().equalsIgnoreCase("")) {
            holder.whoBooked.setText(order.getUser().getData().getFirst_name().trim() + " " + order.getUser().getData().getLast_name().trim());
        } else {
            holder.whoBooked.setText(order.getAdded_from());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnOrderClickListener.onOrderClickListener(pos, clasS);
            }
        });

        if (order.getService().getData().getImage() != null && order.getService().getData().getImage().getFormats().getSmallThumb() != null) {
            Picasso.with(context).load(ApiNew.SERVER_URL + order.getService().getData().getImage().getFormats().getSmallThumb()).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }


        if (allOrders) {
            if (order.getPayment_status().equalsIgnoreCase(Keys.PAID)) {
                holder.payment.setChecked(true);
            } else {
                holder.payment.setChecked(false);
            }
            if (order.getPayment_type().equalsIgnoreCase("card")) {
                if (order.getPayment_status().equalsIgnoreCase(Keys.PAID)) {
                    holder.payment.setText("Оплата on-line");
                    holder.payment.setChecked(true);
                } else {
                    holder.payment.setText("Оплатить на месте");
                }
            } else {
                holder.payment.setText("Заказ оплачен");
            }

            if (Float.parseFloat(order.getPrice()) == 0) {
                holder.price.setText("Бесплатно");
                holder.payment.setVisibility(View.GONE);
            } else {
                holder.price.setText(order.getPrice().substring(0, order.getPrice().indexOf(".")) + " рублей");
                holder.payment.setVisibility(View.VISIBLE);
            }

            if (order.getStatus().equalsIgnoreCase(Keys.DONE)) {
                holder.arrived.setChecked(true);
            } else {
                holder.arrived.setChecked(false);
            }
        } else {
            holder.checkBoxContainer.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
        }

        switch (style) {
            case ALL:
                holder.checkBoxContainer.setVisibility(View.VISIBLE);
                holder.top.setVisibility(View.VISIBLE);
                //nothing
                break;
            case CUT:
                holder.checkBoxContainer.setVisibility(View.GONE);
                holder.top.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    public interface OnOrderClickListener {
        void onOrderClickListener(int id, Class clasS);
    }

    public static class ViewHolder {
        public LinearLayout view;
        public TextView whoBooked;
        public TextView whoComing;
        public TextView price;
        public TextView id;
        public CheckBox payment;
        public CheckBox arrived;
        public LinearLayout checkBoxContainer;
        public RelativeLayout top;
        public ImageView image;

        public ViewHolder(View view) {
            this.view = (LinearLayout) view.findViewById(R.id.order);
            whoBooked = (TextView) view.findViewById(R.id.name_who_booked);
            whoComing = (TextView) view.findViewById(R.id.name_who_coming);
            price = (TextView) view.findViewById(R.id.price);
            id = (TextView) view.findViewById(R.id.id);
            payment = (CheckBox) view.findViewById(R.id.payment);
            arrived = (CheckBox) view.findViewById(R.id.arrived);
            checkBoxContainer = (LinearLayout) view.findViewById(R.id.check_box_container);
            top = (RelativeLayout) view.findViewById(R.id.top);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
