package hitlist.testutil;

import hitlist.model.HitList;
import hitlist.model.company.Company;
import hitlist.model.person.Person;

/**
 * A utility class to help with building HitList objects.
 * Example usage: <br>
 *     {@code HitList ab = new HitListBuilder().withPerson("John", "Doe").build();}
 */
public class HitListBuilder {

    private HitList hitList;

    public HitListBuilder() {
        hitList = new HitList();
    }

    public HitListBuilder(HitList hitList) {
        this.hitList = hitList;
    }

    /**
     * Adds a new {@code Person} to the {@code HitList} that we are building.
     */
    public HitListBuilder withPerson(Person person) {
        hitList.addPerson(person);
        return this;
    }

    /**
    * Adds a new {@code Company} to the {@code HitList} that we are building.
    */
    public HitListBuilder withCompany(Company company) {
        hitList.addCompany(company);
        return this;
    }

    public HitList build() {
        return hitList;
    }
}
