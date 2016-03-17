package spiderbiggen.shoppingcart.dialogcreators;

import android.app.Dialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import io.realm.RealmResults;
import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.datamanagement.Item;
import spiderbiggen.shoppingcart.datamanagement.RealmManager;
import spiderbiggen.shoppingcart.datamanagement.Store;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class ItemDialog {


    private static final String TAG = ItemDialog.class.getSimpleName();
    private static RealmManager realmManager = RealmManager.getInstance();

    public static Dialog openNewItemDialog(final AppCompatActivity activity) {

        final Dialog dialog = initDialog(activity, R.string.item_dialog_title);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        final Spinner spin = (Spinner) activity.findViewById(R.id.spinner);

        spinner.setSelection(spin.getSelectedItemPosition());

        TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final Item item = DialogItem(dialog);
                realmManager.add(item);

                spin.setSelection(spinner.getSelectedItemPosition());

                Snackbar.make(activity.findViewById(R.id.main_content), activity.getResources().getString(R.string.item_added), Snackbar.LENGTH_LONG)
                        .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realmManager.remove(realmManager.getItem(item.getItemId()));
                            }
                        })
                        .setActionTextColor(Color.RED)
                        .show();

                dialog.dismiss();


            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog openChangeItemDialog(final Item item, final AppCompatActivity activity) {

        final Dialog dialog = initDialog(activity, R.string.change_item_dialog);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        final Spinner spin = (Spinner) activity.findViewById(R.id.spinner);
        final EditText editItemName = (EditText) dialog.findViewById(R.id.edit_item_name);
        final EditText editAmount = (EditText) dialog.findViewById(R.id.edit_amount);

//        Store store = realmManager.getStore(item.getStoreId());
//        StoreSpinnerAdapter adapter = (StoreSpinnerAdapter)spinner.getAdapter();
//        spinner.setSelection(adapter.getPosition(store));

        editItemName.setText(item.getItemName());
        editAmount.setText(String.valueOf(item.getAmount()));

        TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final Item oldItem = realmManager.getCopy(item);
                Item.copyFrom(DialogItem(dialog), item);
                spin.setSelection(spinner.getSelectedItemPosition());

                Snackbar.make(activity.findViewById(R.id.main_content), activity.getResources().getString(R.string.item_added), Snackbar.LENGTH_LONG)
                        .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Item.copyFrom(oldItem, item);
                            }
                        })
                        .setActionTextColor(Color.RED)
                        .show();

                dialog.dismiss();
            }
        });


        dialog.show();
        return dialog;
    }

    private static Dialog initDialog(final AppCompatActivity activity, final int title) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_item);
        dialog.setTitle(title);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        StoreSpinnerAdapter adapter = new StoreSpinnerAdapter(activity, (RealmResults<Store>) RealmManager.getInstance().getStoresDialog());
        spinner.setAdapter(adapter);

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Cancel pressed");
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private static Item DialogItem(Dialog dialog) {
        Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        EditText editItemName = (EditText) dialog.findViewById(R.id.edit_item_name);
        EditText editAmount = (EditText) dialog.findViewById(R.id.edit_amount);
        Switch neededButton = (Switch) dialog.findViewById(R.id.needed_button);

        String itemName = editItemName.getText().toString();
        int amount;
        try {
            amount = Integer.parseInt(editAmount.getText().toString());
            Log.d(TAG, "DialogItem: Parsed integer " + amount);
        } catch (NumberFormatException e) {
            Log.e(TAG, "DialogItem: Failed to parse integer", e);
            amount = 1;
        }
        boolean neededNow = neededButton.isChecked();
        int area = 0;
        int store = ((Store) spinner.getSelectedItem()).getStoreId();
        return new Item(itemName, amount, neededNow, area, store);
    }


}
