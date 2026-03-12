package hitlist.model.company.role;

import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_NAME_SOFTWARE_ENGINEER;
import static hitlist.logic.commands.CommandTestUtil.VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalRoles.PRODUCT_MANAGER;
import static hitlist.testutil.TypicalRoles.SOFTWARE_ENGINEER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import hitlist.model.company.role.exceptions.DuplicateRoleException;
import hitlist.model.company.role.exceptions.RoleNotFoundException;
import hitlist.testutil.RoleBuilder;

public class UniqueRoleListTest {

    private final UniqueRoleList uniqueRoleList = new UniqueRoleList();

    @Test
    public void contains_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.contains(null));
    }

    @Test
    public void contains_roleNotInList_returnsFalse() {
        assertFalse(uniqueRoleList.contains(SOFTWARE_ENGINEER));
    }

    @Test
    public void contains_roleInList_returnsTrue() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        assertTrue(uniqueRoleList.contains(SOFTWARE_ENGINEER));
    }

    @Test
    public void contains_roleWithSameIdentityFieldsInList_returnsTrue() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        Role editedRole = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
                .build();
        assertTrue(uniqueRoleList.contains(editedRole));
    }

    @Test
    public void add_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.add(null));
    }

    @Test
    public void add_duplicateRole_throwsDuplicateRoleException() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.add(SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_nullTargetRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRole(null, SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_nullEditedRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, null));
    }

    @Test
    public void setRole_targetRoleNotInList_throwsRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER));
    }

    @Test
    public void setRole_editedRoleIsSameRole_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(SOFTWARE_ENGINEER);
        assertEquals(uniqueRoleList, expectedUniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasSameIdentity_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        Role editedRole = new RoleBuilder()
                .withName(VALID_ROLE_NAME_SOFTWARE_ENGINEER)
                .withDescription(VALID_ROLE_DESCRIPTION_PRODUCT_MANAGER)
                .build();
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, editedRole);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(editedRole);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasDifferentIdentity_success() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.setRole(SOFTWARE_ENGINEER, PRODUCT_MANAGER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRole_editedRoleHasNonUniqueIdentity_throwsDuplicateRoleException() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.add(PRODUCT_MANAGER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.setRole(SOFTWARE_ENGINEER, PRODUCT_MANAGER));
    }

    @Test
    public void remove_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.remove(null));
    }

    @Test
    public void remove_roleDoesNotExist_throwsRoleNotFoundException() {
        uniqueRoleList.add(PRODUCT_MANAGER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        uniqueRoleList.remove(SOFTWARE_ENGINEER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void remove_existingRole_removesRole() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        uniqueRoleList.remove(SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

     @Test
    public void setRoles_nullUniqueRoleList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRoles((UniqueRoleList) null));
    }

    @Test
    public void setRoles_uniqueRoleList_replacesOwnListWithProvidedUniqueRoleList() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        uniqueRoleList.setRoles(expectedUniqueRoleList);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRoleList.setRoles((List<Role>) null));
    }

    @Test
    public void setRoles_list_replacesOwnListWithProvidedList() {
        uniqueRoleList.add(SOFTWARE_ENGINEER);
        List<Role> roleList = Collections.singletonList(PRODUCT_MANAGER);
        uniqueRoleList.setRoles(roleList);
        UniqueRoleList expectedUniqueRoleList = new UniqueRoleList();
        expectedUniqueRoleList.add(PRODUCT_MANAGER);
        assertEquals(expectedUniqueRoleList, uniqueRoleList);
    }

    @Test
    public void setRoles_listWithDuplicateRoles_throwsDuplicateRoleException() {
        List<Role> listWithDuplicateRoles = Arrays.asList(SOFTWARE_ENGINEER, SOFTWARE_ENGINEER);
        assertThrows(DuplicateRoleException.class, () -> uniqueRoleList.setRoles(listWithDuplicateRoles));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueRoleList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueRoleList.asUnmodifiableObservableList().toString(), uniqueRoleList.toString());
    }
}
