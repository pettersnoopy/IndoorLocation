package holder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by mac on 15/5/6.
 */
public abstract class BaseViewHolder {

    protected Context mContext;

    public BaseViewHolder(Context context) {
        mContext = context;
    }

    public abstract void initUi(View view);

    public abstract void setupView(Bundle bundle);
}
