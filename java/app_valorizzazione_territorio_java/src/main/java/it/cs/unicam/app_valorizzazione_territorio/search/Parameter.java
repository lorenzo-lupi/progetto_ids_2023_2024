package it.cs.unicam.app_valorizzazione_territorio.search;

import java.util.Map;

public enum Parameter {
    NAME,
    DESCRIPTION,
    POSITION,
    REPRESENTATIVE,
    COMPOUND_POINT_TYPE,
    MUNICIPALITY,
    APPROVAL_STATUS,
    CONTEST_TOPIC,
    CONTEST_STATUS,
    CONTEST_TYPE;

    public static final Map<String, Parameter> stringToParameter = Map.of(
            "name", NAME,
            "description", DESCRIPTION,
            "position", POSITION,
            "representative", REPRESENTATIVE,
            "compoundPointType", COMPOUND_POINT_TYPE,
            "municipality", MUNICIPALITY,
            "approvalStatus", APPROVAL_STATUS,
            "contestTopic", CONTEST_TOPIC,
            "contestStatus", CONTEST_STATUS,
            "contestType", CONTEST_TYPE
    );
}
