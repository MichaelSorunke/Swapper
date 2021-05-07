package comp3350.srsys;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.srsys.business.AccessListingsTest;
import comp3350.srsys.business.AccessUsersTest;
import comp3350.srsys.business.AuthenticateUserTest;
import comp3350.srsys.business.EmailValidatorTest;
import comp3350.srsys.business.ListingValidatorTest;
import comp3350.srsys.business.PasswordValidatorTest;
import comp3350.srsys.objects.ListingTest;
import comp3350.srsys.persistence.database.ListingPersistenceFDBTest;
import comp3350.srsys.persistence.database.UserPersistenceFDBTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ListingTest.class,
        ListingPersistenceFDBTest.class,
        UserPersistenceFDBTest.class,
        AccessUsersTest.class,
        AccessListingsTest.class,
        EmailValidatorTest.class,
        PasswordValidatorTest.class,
        ListingValidatorTest.class,
        AuthenticateUserTest.class
})

public class UnitTests {
}
