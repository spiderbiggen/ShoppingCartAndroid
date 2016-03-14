package spiderbiggen.shoppingcart;

import android.app.Dialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import spiderbiggen.shoppingcart.Adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.Data.Item;
import spiderbiggen.shoppingcart.Data.DataBaseManager;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class OnClickListeners {

    public static OnClickListener openAddDialog(final AppCompatActivity activity) {
        return openAddDialog(-1, activity);
    }

    public static OnClickListener openAddDialog(final int itemId, final AppCompatActivity activity){

        return new OnClickListener() {
            @Override
            public void onClick(View view) {

                final DataBaseManager dataBaseManager = DataBaseManager.getInstance();
                final boolean newItem = itemId < 0;

                final Item item = dataBaseManager.getItem(itemId);

                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.add_item_dialog);
                String dialogTitle = newItem ? activity.getString(R.string.item_dialog_title) : activity.getString(R.string.change_item_dialog);
                dialog.setTitle(dialogTitle);

                final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
                StoreSpinnerAdapter adapter = new StoreSpinnerAdapter(dialog.getContext(), DataBaseManager.getInstance().getKeys());
                spinner.setAdapter(adapter);

                final EditText editText = (EditText) dialog.findViewById(R.id.editText);

                if(!newItem) {
                    for(int i=0; i < adapter.getCount(); i++) {
                        if(dataBaseManager.getStore(item.getStoreId()).equals(adapter.getItem(i))){
                            spinner.setSelection(i);
                            break;
                        }
                    }
                    editText.setText(item.getItemName());

                }

                TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
                saveButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Item oldItem = item.copy();

                        String store = spinner.getSelectedItem().toString();
                        String itemName = editText.getText().toString();

                        item.setStoreId(dataBaseManager.getStoreId(store));
                        item.setItemName(itemName);

                        dataBaseManager.changeItem(itemId, item);

                        final Spinner spin = (Spinner) activity.findViewById(R.id.main_content).findViewById(R.id.spinner);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((String) spin.getSelectedItem()))
                                .commit();

                        Snackbar.make(activity.findViewById(R.id.main_content),
                                activity.getResources().getString(R.string.item_added),
                                Snackbar.LENGTH_LONG)
                                .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (newItem) {
                                            dataBaseManager.removeItem(item.getId());
                                        } else {
                                            dataBaseManager.changeItem(item.getId(), oldItem);
                                        }
                                        activity.getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((String) spin.getSelectedItem()))
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


}
