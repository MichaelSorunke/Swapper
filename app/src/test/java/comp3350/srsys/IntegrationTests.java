package comp3350.srsys;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.srsys.business.AccessListingsIntegrationTest;
import comp3350.srsys.business.AccessUsersIntegrationTest;
import comp3350.srsys.business.AuthenticateUserIntegrationTest;
import comp3350.srsys.business.ListingValidatorIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessUsersIntegrationTest.class,
        AccessListingsIntegrationTest.class,
        ListingValidatorIntegrationTest.class,
        AuthenticateUserIntegrationTest.class
})

public class IntegrationTests {
}
