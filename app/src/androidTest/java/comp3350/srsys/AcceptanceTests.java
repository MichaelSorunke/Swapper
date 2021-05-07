package comp3350.srsys;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.srsys.presentation.CreateAccountAcceptanceTest;
import comp3350.srsys.presentation.CreateListingAcceptanceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateAccountAcceptanceTest.class,
        CreateListingAcceptanceTest.class
})

public class AcceptanceTests {}
