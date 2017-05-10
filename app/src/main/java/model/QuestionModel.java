package model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

/**
 * Created by Alok on 27-04-2017.
 */
public class QuestionModel extends ExpandableGroup<FaqModel> {

    public QuestionModel(String title, ArrayList<FaqModel> items) {
        super(title, items);
    }


}
