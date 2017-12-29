package com.torfin.mybulletjournal.dataobjects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by torftorf1 on 12/26/17.
 */

@IgnoreExtraProperties
public class Label {

    String label;

    public Label() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("label", label);

        return result;

    }
}
