package hitlist.model.company.role;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import hitlist.model.company.role.exceptions.DuplicateRoleException;
import hitlist.model.company.role.exceptions.RoleNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of roles that enforces uniqueness between its elements and does not allow nulls.
 * A role is considered unique by comparing using {@code Role#isSameRole(Role)}. As such, adding and updating of
 * roles uses Role#isSameRole(Role) for equality so as to ensure that the role being added or updated is
 * unique in terms of identity in the UniqueRoleList. However, the removal of a role uses Role#equals(Object) so
 * as to ensure that the role with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Role#isSameRole(Role)
 */
public class UniqueRoleList implements Iterable<Role> {

    private final ObservableList<Role> internalList = FXCollections.observableArrayList();
    private final ObservableList<Role> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent role as the given argument.
     */
    public boolean contains(Role toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRole);
    }

    /**
     * Adds a role to the list.
     * The role must not already exist in the list.
     */
    public void add(Role toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRoleException("This role " + toAdd.getRoleName() + " already exists");
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the role {@code target} in the list with {@code editedRole}.
     * {@code target} must exist in the list.
     * The role identity of {@code editedRole} must not be the same as another existing role in the list.
     */
    public void setRole(Role target, Role editedRole) {
        requireAllNonNull(target, editedRole);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RoleNotFoundException("The role to be edited does not exist");
        }

        if (!target.equals(editedRole)) {
            throw new DuplicateRoleException("This role already exists");
        }

        internalList.set(index, editedRole);
    }

    /**
     * Removes the equivalent role from the list.
     * The role must exist in the list.
     */
    public void remove(Role toRemove) {
        requireNonNull(toRemove);
        internalList.remove(toRemove);
    }

    /**
     * Replaces the contents of this list with the roles from the given {@code UniqueRoleList}.
     * Since the parameter is already a {@code UniqueRoleList}, it is inherently guaranteed
     * to contain no duplicate roles, so no further validation is needed.
     *
     * @param replacement The {@code UniqueRoleList} containing the replacement roles.
     */
    public void setRoles(UniqueRoleList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with the provided standard list of {@code Role}s.
     * Because a standard {@code List} does not enforce uniqueness, this method explicitly
     * checks that all roles are unique before updating the list.
     *
     * @param roles The list of roles to set.
     * @throws DuplicateRoleException If the provided list contains any duplicate roles.
     */
    public void setRoles(List<Role> roles) {
        requireNonNull(roles);
        if (!rolesAreUnique(roles)) {
            throw new DuplicateRoleException("There is already a role with the same name in the list.");
        }

        internalList.setAll(roles);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Role> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Role> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueRoleList)) {
            return false;
        }

        UniqueRoleList otherUniqueRoleList = (UniqueRoleList) other;
        return internalList.equals(otherUniqueRoleList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code roles} contains only unique roles.
     */
    private boolean rolesAreUnique(List<Role> roles) {
        for (int i = 0; i < roles.size(); i++) {
            for (int j = i + 1; j < roles.size(); j++) {
                if (roles.get(i).isSameRole(roles.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
