package spiderbiggen.shoppingcart;

import android.app.Activity;
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

import io.realm.Realm;
import spiderbiggen.shoppingcart.Adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.Data.Item;
import spiderbiggen.shoppingcart.Data.RealmManager;
import spiderbiggen.shoppingcart.Data.Store;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class OnClickListeners {


    private static final String TAG = OnClickListeners.class.getSimpleName();
    private static RealmManager realmManager = RealmManager.getInstance();

    public static OnClickListener openNewItemDialog(final AppCompatActivity activity) {

        return new OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = initDialog(activity, view, activity.getString(R.string.item_dialog_title));

                final Spinner spin = (Spinner) activity.findViewById(R.id.spinner);
                final TextView neededButton = (TextView) dialog.findViewById(R.id.needed_button);

                neededButton.setText(R.string.button_yes);

                TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
                saveButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Item item = DialogItem(activity, dialog);
                        realmManager.add(item);

                        final Spinner spinner = (Spinner) activity.findViewById(R.id.spinner);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((Store) spinner.getSelectedItem()))
                                .commit();

                        Snackbar.make(activity.findViewById(R.id.main_content), activity.getResources().getString(R.string.item_added), Snackbar.LENGTH_LONG)
                                .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        realmManager.remove(item);
                                        activity.getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((Store) spin.getSelectedItem()))
                                                .commit();
                                    }
                                })
                                .setActionTextColor(Color.RED)
                                .show();

                        dialog.dismiss();


                    }
                });

                dialog.show();
            }
        };
    }

    public static OnClickListener openChangeItemDialog(final Item item, final AppCompatActivity activity) {

        return new OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = initDialog(activity, view, activity.getString(R.string.change_item_dialog));

                final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
                final Spinner spin = (Spinner) activity.findViewById(R.id.spinner);
                final EditText editItemName = (EditText) dialog.findViewById(R.id.edit_item_name);
                final TextView neededButton = (TextView) dialog.findViewById(R.id.needed_button);

                neededButton.setText(item.isNeededNow() ? R.string.button_yes : R.string.button_no);

                if (((Store) spin.getSelectedItem()).getStoreId() == -1) {
                    for (int i = 0; i < spinner.getCount(); i++) {
                        if (item.getStoreId() == ((Store) spinner.getItemAtPosition(i)).getStoreId()) {
                            spinner.setSelection(i);
                            break;
                        }
                    }
                } else {
                    spinner.setSelection(spin.getSelectedItemPosition());
                }

                editItemName.setText(item.getItemName());

                TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
                saveButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Item oldItem = realmManager.getCopy(item);
                        Item.copyFrom(DialogItem(activity, dialog), item);

                        Snackbar.make(activity.findViewById(R.id.main_content), activity.getResources().getString(R.string.item_added), Snackbar.LENGTH_LONG)
                                .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Item.copyFrom(oldItem, item);
                                        activity.getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((Store) spin.getSelectedItem()))
                                                .commit();
                                    }
                                })
                                .setActionTextColor(Color.RED)
                                .show();

                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        };
    }

    private static Dialog initDialog(final AppCompatActivity activity, final View view, final String title) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_item_dialog);
        dialog.setTitle(title);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        StoreSpinnerAdapter adapter = new StoreSpinnerAdapter(dialog.getContext(), RealmManager.getInstance().getStoresDialog());
        spinner.setAdapter(adapter);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((Store) spinner.getSelectedItem()))
                .commit();

        final TextView neededButton = (TextView) dialog.findViewById(R.id.needed_button);

        neededButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Needed pressed " + neededButton.getText());
                neededButton.setText(neededButton.getText().toString().equals(activity.getString(R.string.button_yes)) ? R.string.button_no : R.string.button_yes);
            }
        });

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

    private static Item DialogItem(Activity ac, Dialog dialog) {
        Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
        EditText editItemName = (EditText) dialog.findViewById(R.id.edit_item_name);
        EditText editAmount = (EditText) dialog.findViewById(R.id.edit_amount);
        TextView neededButton = (TextView) dialog.findViewById(R.id.needed_button);

        String itemName = editItemName.getText().toString();
        int amount;
        try {
            amount = Integer.parseInt(editAmount.getText().toString());
        } catch (NumberFormatException e) {
            amount = 1;
        }
        boolean neededNow = neededButton.toString().equals(ac.getString(R.string.button_yes));
        int area = 0;
        int store = ((Store) spinner.getSelectedItem()).getStoreId();
        return new Item(itemName, amount, neededNow, area, store);
    }


}
