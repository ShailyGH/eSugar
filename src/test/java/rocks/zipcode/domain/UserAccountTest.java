package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.UserAccountTestSamples.*;
import static rocks.zipcode.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class UserAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccount.class);
        UserAccount userAccount1 = getUserAccountSample1();
        UserAccount userAccount2 = new UserAccount();
        assertThat(userAccount1).isNotEqualTo(userAccount2);

        userAccount2.setId(userAccount1.getId());
        assertThat(userAccount1).isEqualTo(userAccount2);

        userAccount2 = getUserAccountSample2();
        assertThat(userAccount1).isNotEqualTo(userAccount2);
    }

    @Test
    void userProfileTest() throws Exception {
        UserAccount userAccount = getUserAccountRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userAccount.addUserProfile(userProfileBack);
        assertThat(userAccount.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getUserAccount()).isEqualTo(userAccount);

        userAccount.removeUserProfile(userProfileBack);
        assertThat(userAccount.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getUserAccount()).isNull();

        userAccount.userProfiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userAccount.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getUserAccount()).isEqualTo(userAccount);

        userAccount.setUserProfiles(new HashSet<>());
        assertThat(userAccount.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getUserAccount()).isNull();
    }
}
