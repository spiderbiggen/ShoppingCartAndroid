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

import org.jetbrains.annotations.NotNull;

import spiderbiggen.shoppingcart.Adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.Data.Item;
import spiderbiggen.shoppingcart.Data.StoreHolder;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class OnClickListeners {

    public static OnClickListener openAddDialog(final AppCompatActivity activity) {
        return openAddDialog(new Item(), activity);
    }

    public static OnClickListener openAddDialog(@NotNull final Item item, final AppCompatActivity activity){

        return new OnClickListener() {
            @Override
            public void onClick(View view) {

                final StoreHolder storeHolder = StoreHolder.getInstance();

                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.add_item_dialog);
                String dialogTitle = item.isEmpty() ? activity.getString(R.string.item_dialog_title) : activity.getString(R.string.change_item_dialog);
                dialog.setTitle(dialogTitle);

                final Spinner spinner = (Spinner) dialog.findViewById(R.id.new_item_store_spinner);
                StoreSpinnerAdapter adapter = new StoreSpinnerAdapter(dialog.getContext(), StoreHolder.getInstance().getKeys());
                spinner.setAdapter(adapter);

                final EditText editText = (EditText) dialog.findViewById(R.id.editText);

                if(!item.isEmpty()) {
                    for(int i=0; i < adapter.getCount(); i++) {
                        if(item.getStoreName().equals(adapter.getItem(i))){
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

                        final Item oldItem = new Item(item);

                        String store = spinner.getSelectedItem().toString();
                        String itemName = editText.getText().toString();

                        if (!item.isEmpty()) storeHolder.getItems(item.getStoreName()).remove(item);

                        item.setStoreName(store);
                        item.setItemName(itemName);
                        item.setEmpty(false);

                        storeHolder.getItems(item.getStoreName()).add(item);

                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance((String) spinner.getSelectedItem()))
                                .commit();

                        Snackbar.make(activity.findViewById(R.id.main_content), activity.getResources().getString(R.string.item_added), Snackbar.LENGTH_LONG)
                                .setAction(activity.getResources().getString(R.string.undo), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (oldItem.isEmpty()) {
                                            storeHolder.getItems(item.getStoreName()).remove(item);
                                        } else {
                                            storeHolder.getItems(item.getStoreName()).remove(item);
                                            item.copyFrom(oldItem);
                                            storeHolder.getItems(oldItem.getStoreName()).add(item);
                                        }
                                        final Spinner spin = (Spinner) activity.findViewById(R.id.main_content).findViewById(R.id.spinner);
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
