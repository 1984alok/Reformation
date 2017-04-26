package adapter;

import android.view.View;
import android.widget.TextView;

import com.reformation.home.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import model.FaqModel;

/**
 * Created by Alok on 27-04-2017.
 */
public class AnswerViewHolder extends ChildViewHolder {

    private TextView phoneName;

    public AnswerViewHolder(View itemView) {
        super(itemView);

        phoneName = (TextView) itemView.findViewById(R.id.phone_name);
    }

    public void onBind(FaqModel faqModel, ExpandableGroup group) {
        phoneName.setText(faqModel.getAns());
           }
}
