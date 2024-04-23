import userAccount from 'app/entities/user-account/user-account.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import characteristicData from 'app/entities/characteristic-data/characteristic-data.reducer';
import profileDashboardConfig from 'app/entities/profile-dashboard-config/profile-dashboard-config.reducer';
import bloodGlucose from 'app/entities/blood-glucose/blood-glucose.reducer';
import bodyVitalsLog from 'app/entities/body-vitals-log/body-vitals-log.reducer';
import bodyWeight from 'app/entities/body-weight/body-weight.reducer';
import bodyHeight from 'app/entities/body-height/body-height.reducer';
import dataSource from 'app/entities/data-source/data-source.reducer';
import activity from 'app/entities/activity/activity.reducer';
import userBMR from 'app/entities/user-bmr/user-bmr.reducer';
import activityLog from 'app/entities/activity-log/activity-log.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userAccount,
  userProfile,
  characteristicData,
  profileDashboardConfig,
  bloodGlucose,
  bodyVitalsLog,
  bodyWeight,
  bodyHeight,
  dataSource,
  activity,
  userBMR,
  activityLog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
