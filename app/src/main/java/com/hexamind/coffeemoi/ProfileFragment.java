package com.hexamind.coffeemoi;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProfileFragment extends Fragment {
    private View root;
    private TextView username;
    private RecyclerView recyclerView;
    private DatabaseHelper helper;
    private OrdersAdapter adapter;
    private List<Orders> ordersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        helper = new DatabaseHelper(root.getContext(), "", null, 0);
        username = root.findViewById(R.id.username);
        recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrdersAdapter(ordersList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
        private List<Orders> ordersList;

        public OrdersAdapter(List<Orders> ordersList) {
            this.ordersList = ordersList;
        }

        @NonNull
        @Override
        public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrdersViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_past_orders, null, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
            final Orders order = ordersList.get(position);

            holder.size.setText(order.getSize());
            holder.type.setText(order.getType());
            holder.expressoShot.setText(String.valueOf(order.isExpressoShot()));
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(root.getContext())
                            .setTitle(getString(R.string.cancel_order_title_string))
                            .setMessage(getString(R.string.cancel_order_messsage_string))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    helper.removeCoffee(helper.getId(order));
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersList.size();
        }

        public class OrdersViewHolder extends RecyclerView.ViewHolder {
            public TextView size, type, expressoShot;
            public AppCompatButton remove;

            public OrdersViewHolder(@NonNull View itemView) {
                super(itemView);

                size = itemView.findViewById(R.id.size);
                type = itemView.findViewById(R.id.type);
                expressoShot = itemView.findViewById(R.id.expressoShot);
                remove = itemView.findViewById(R.id.remove);
            }
        }
    }
}
