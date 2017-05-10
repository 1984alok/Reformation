package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reformation.home.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import model.FaqModel;

/**
 * Created by Alok on 27-04-2017.
 */
public class FaqAdapter extends ExpandableRecyclerViewAdapter<QuestionViewHolder, AnswerViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter{

    private Activity activity;
    ArrayList<? extends ExpandableGroup> groups;

    public FaqAdapter(Activity activity, ArrayList<? extends ExpandableGroup> groups) {
        super(groups);
        this.groups = groups;
        this.activity = activity;
    }

    @Override
    public QuestionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.question_item, parent, false);

        return new QuestionViewHolder(view);
    }

    @Override
    public AnswerViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.answer_view, parent, false);

        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(AnswerViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final FaqModel phone = (FaqModel) group.getItems().get(childIndex);
        holder.onBind(phone,group);
    }

    @Override
    public void onBindGroupViewHolder(QuestionViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group);
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= groups.size())
            return null;

        String name = groups.get(pos).getTitle();
        if (name == null || name.length() < 1)
            return null;

        return groups.get(pos).getTitle().substring(0, 1);
    }
}
