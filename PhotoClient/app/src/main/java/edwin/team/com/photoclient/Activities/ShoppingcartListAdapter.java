package edwin.team.com.photoclient.Activities;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import edwin.team.com.photoclient.Classes.OrderLine;
import edwin.team.com.photoclient.Classes.filter;
import edwin.team.com.photoclient.R;

public class ShoppingcartListAdapter extends ArrayAdapter<OrderLine>{

    private Context context;
    private List<OrderLine> shoppingCart;
    private View.OnClickListener imageClickListener;
    NumberFormat format = NumberFormat.getCurrencyInstance();

    public ShoppingcartListAdapter(Context context, List<OrderLine> shoppingCart){
        super(context, R.layout.shoppingcart_item,shoppingCart);
        this.context = context;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.shoppingcart_item, null);
        }

        if(view != null){
            final OrderLine order = shoppingCart.get(position);
            ImageView iView = (ImageView)view.findViewById(R.id.li_photo);
            ImageView trash = (ImageView)view.findViewById(R.id.trashcan);
            TextView tName = (TextView)view.findViewById(R.id.li_name);
            final EditText amount = (EditText)view.findViewById(R.id.li_amount);
            TextView measure = (TextView)view.findViewById(R.id.li_measure);
            TextView piecePrice = (TextView)view.findViewById(R.id.li_pieceprice);
            TextView totalPrice = (TextView)view.findViewById(R.id.li_totalprice);

            //allows one to see the edited pic or the original by Stefan
            if (order.getNeoBitmap() == null) {
                iView.setImageBitmap(order.getImage());
            } else {
                    iView.setImageBitmap(order.getImage());
            }
            iView.setOnClickListener(this.imageClickListener);
            tName.setText(order.getName());
            amount.setText(String.valueOf(order.getAmount()));
            measure.setText(order.getName());
            piecePrice.setText(format.format(order.getUnitPrice()));
            totalPrice.setText(format.format(order.getTotalPrice()));

            amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                       ((ShoppingCartActivity)context).updateShoppingCart(Integer.parseInt(amount.getText().toString()),order.getPhotoID());
                    }
                    return false;
                }
            });

            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ShoppingCartActivity)context).removeOrder(order.getPhotoID());
                }
            });
        }

        return view;
    }

    public void setOnClickListener(final View.OnClickListener onClickListener){
        this.imageClickListener = onClickListener;
    }
}
