package adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.reformation.home.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Alok on 27-04-2017.
 */
public class QuestionViewHolder extends GroupViewHolder {
    private TextView QName;

    public QuestionViewHolder(View itemView) {
        super(itemView);

        QName = (TextView) itemView.findViewById(R.id.mobile_os);
    }

    @Override
    public void expand() {
        QName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        Log.i("Adapter", "expand");
    }

    @Override
    public void collapse() {
        Log.i("Adapter", "collapse");
        QName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
    }

    public void setGroupName(ExpandableGroup group) {
        QName.setText(group.getTitle());
    }
}
