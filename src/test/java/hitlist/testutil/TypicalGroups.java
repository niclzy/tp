package hitlist.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hitlist.model.HitList;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

/**
 * A utility class containing a list of {@code Group} objects to be used in tests.
 */
public class TypicalGroups {

    public static final Group STUDENTS = new Group(new GroupName("Students"));
    public static final Group UNEMPLOYED = new Group(new GroupName("Unemployed"));
    public static final Group EXPERIENCED = new Group(new GroupName("Experienced"));

    private TypicalGroups() {} // prevents instantiation

    /**
     * Returns an {@code HitList} with all the typical groups.
     */
    public static HitList getTypicalHitList() {
        HitList ab = new HitList();
        for (Group group : getTypicalGroups()) {
            ab.addGroup(group);
        }
        return ab;
    }

    public static List<Group> getTypicalGroups() {
        return new ArrayList<>(Arrays.asList(STUDENTS, UNEMPLOYED));
    }
}
