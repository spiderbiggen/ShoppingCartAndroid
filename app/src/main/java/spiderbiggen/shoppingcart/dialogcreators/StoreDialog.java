package spiderbiggen.shoppingcart.dialogcreators;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.datamanagement.RealmManager;
import spiderbiggen.shoppingcart.datamanagement.Store;

/**
 * Created by Stefan Breetveld on 16-3-2016.
 * Part of ShoppingCart.
 */
public class StoreDialog {

    private static final String TAG = ItemDialog.class.getSimpleName();
    private static RealmManager realmManager = RealmManager.getInstance();

    public static Dialog openNewStoreDialog(final AppCompatActivity activity) {
        final Dialog dialog = initDialog(activity, R.string.dialog_title_new_store);

        TextView saveButton = (TextView) dialog.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView editStore = (TextView) dialog.findViewById(R.id.edit_store_name);
                Store store = new Store(editStore.getText().toString());
                realmManager.add(store);
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    private static Dialog initDialog(AppCompatActivity activity, int title) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_store);
        dialog.setTitle(title);

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Cancel pressed");
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
